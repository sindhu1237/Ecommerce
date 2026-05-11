package com.app.ecomapplication.repository;

import com.app.ecomapplication.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);
    List<Cart> findByDateBetween(LocalDate startDate, LocalDate endDate);
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
}