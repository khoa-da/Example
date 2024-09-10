package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Order;
import com.example.kalban_greenbag.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Order o WHERE o.userID.id = :userId AND o.status = :status")
    boolean existsByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") String status);
}
