package com.ra.busBooking;

import com.ra.busBooking.DTO.ReservationDTO;
import com.ra.busBooking.DTO.UserRegisteredDTO;
import com.ra.busBooking.model.BusData;
import com.ra.busBooking.repository.BusDataRepository;
import com.ra.busBooking.repository.UserRepository;
import com.ra.busBooking.service.DefaultUserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
class BusBookingSystemApplicationTests {

	@Autowired
	private DefaultUserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BusDataRepository busDataRepository;

	@Test
	public void registerAndLoginAdminAccount() {
		UserRegisteredDTO userRegisteredDTO = new UserRegisteredDTO();
		userRegisteredDTO.setName("Admin");
		userRegisteredDTO.setEmail_id("admin@gmail.com");
		userRegisteredDTO.setPassword("12345");
		userRegisteredDTO.setRole("ADMIN");

		userService.save(userRegisteredDTO);
		Assert.notNull(userRepo.findByEmail("admin@gmail.com"), "Admin registered successfully");

		UserDetails user = userService.loadUserByUsername("admin@gmail.com");
		Assert.notNull(user, "Admin login successful");
	}

	@Test
	public void saveAndFetchBusData() {
		BusData busData = new BusData();
		busData.setBusName("TestBus");
		busData.setFromDestination("Minsk");
		busData.setToDestination("Borisov");
		busData.setFilterDate("15.11.2024");
		busData.setTime("11:25");
		busData.setPrice(40.0);

		BusData savedBusData = busDataRepository.save(busData);
		Assert.notNull(savedBusData, "Bus data saved successfully");


		ReservationDTO rs = new ReservationDTO();
		rs.setFrom("Minsk");
		rs.setTo("Borisov");
		rs.setFilterDate("15.11.2024");

		List<BusData> foundBuses = busDataRepository.findByToFromAndDate(rs.getTo(), rs.getFrom(), rs.getFilterDate());
		Assert.notEmpty(foundBuses, "Bus data available with the given filters");
	}
}
