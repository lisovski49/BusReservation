package com.ra.busBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ra.busBooking.model.Feedback;
import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback, Long>{
    @Override
    List<Feedback> findAll();
}
