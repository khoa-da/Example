package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByOrderByCreatedDate(Pageable pageable);
    List<Order> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
    Order findByOrderCode(long orderCode);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Order o WHERE o.userID.id = :userId AND o.status = :status")
    boolean existsByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.totalAmount = :totalAmount WHERE o.id = :orderId")
    void updateTotalAmount(@Param("orderId") UUID orderId, @Param("totalAmount") BigDecimal totalAmount);
}
