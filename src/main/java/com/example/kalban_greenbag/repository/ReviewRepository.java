package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Product;
import com.example.kalban_greenbag.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllByOrderByCreatedDate(Pageable pageable);
    List<Review> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);
    @Query("SELECT r FROM Review r WHERE r.productID.id = :id ORDER BY r.createdDate ASC")
    List<Review> findAllByProductIdOrderByCreatedDate(UUID id, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Review r WHERE r.userID.id = :userId AND r.productID.id = :productId")
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}
