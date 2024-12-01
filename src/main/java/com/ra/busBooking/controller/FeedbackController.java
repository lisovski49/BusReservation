package com.ra.busBooking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ra.busBooking.DTO.FeedbackDTO;
import com.ra.busBooking.model.Feedback;
import com.ra.busBooking.model.User;
import com.ra.busBooking.repository.FeedbackRepo;
import com.ra.busBooking.repository.UserRepository;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FeedbackRepo feedbackRepo;

	// Отображение формы обратной связи и списка отзывов
	@GetMapping
	public String feedbackPage(Model model) {
		List<Feedback> feedbackList = feedbackRepo.findAll();
		model.addAttribute("feedbacks", feedbackList);

		FeedbackDTO dto = new FeedbackDTO();
		dto.setEmailId(returnUsername().get("email"));
		dto.setName(returnUsername().get("name"));


		model.addAttribute("userDetails", returnUsername().get("name"));
		model.addAttribute("feedback", dto);

		return "feedback"; // Имя шаблона для формы и списка
	}

	// Сохранение отзыва
	@PostMapping
	public String saveFeedback(@ModelAttribute("feedback") FeedbackDTO feedbackDTO) {
		Map<String, String> map = returnUsername();
		Feedback feedback = new Feedback();
		feedback.setComments(feedbackDTO.getComments());
		feedback.setRating(feedbackDTO.getRating());
		feedback.setName(map.get("name"));
		feedback.setEmailId(map.get("email"));
		feedbackRepo.save(feedback);
		return "redirect:/feedback?success"; // Перенаправление на страницу отзывов
	}

	// Удаление отзыва
	@GetMapping("/delete/{id}") // Удаление отзыва по ID
	public String deleteFeedback(@PathVariable Long id) {
		if (isAdmin()) { // Проверяем, является ли пользователь администратором
			feedbackRepo.deleteById(id);
		}
		return "redirect:/adminFeedback"; // Перенаправление после удаления
	}

	// Получение информации о текущем пользователе
	private Map<String, String> returnUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
		User users = userRepository.findByEmail(user.getUsername());
		Map<String, String> map = new HashMap<>();
		map.put("email", users.getEmail());
		map.put("name", users.getName());
		return map;
	}

	// Метод для проверки, является ли пользователь администратором
	private boolean isAdmin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext.getAuthentication() != null) {
			UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
			User users = userRepository.findByEmail(user.getUsername());
			return users.getRole().equals("ADMIN"); // Предполагается, что роль хранится как строка "ADMIN"
		}
		return false; // Если аутентификация не удалась, возвращаем false
	}

	// Метод для отображения списка отзывов для администратора
	@GetMapping("/admin")
	public String adminFeedbackPage(Model model) {
		if (isAdmin()) {
			List<Feedback> feedbackList = feedbackRepo.findAll();
			model.addAttribute("feedbacks", feedbackList);
			return "adminFeedback"; // Шаблон для отображения отзывов администратором
		}
		return "redirect:/adminScreen"; // Если не администратор, перенаправить на страницу отзывов
	}
}
