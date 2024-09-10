package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByOrderByCreatedDate(Pageable pageable);
    List<Product> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);
}
