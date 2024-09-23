package com.BMS.repository;

import com.BMS.model.ShoppingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingSessionRepository extends JpaRepository<ShoppingSession, Long> {
    Optional<ShoppingSession> findBySessionId(String sessionId);

}
