package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByOrderByCreatedDate(Pageable pageable);
    List<Category> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
}
