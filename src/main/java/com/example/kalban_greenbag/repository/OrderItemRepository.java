package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findAllByOrderByCreatedDate(Pageable pageable);
    List<OrderItem> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
}
