package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Category;
import com.example.kalban_greenbag.entity.CustomizationOption;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomizationOptionRepository extends JpaRepository<CustomizationOption, UUID> {
    List<CustomizationOption> findAllByOrderByCreatedDate(Pageable pageable);
    List<CustomizationOption> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
}
