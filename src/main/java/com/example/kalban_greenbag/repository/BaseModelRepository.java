package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.BaseModel;
import com.example.kalban_greenbag.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BaseModelRepository extends JpaRepository<BaseModel, UUID> {
    List<BaseModel> findAllByOrderByCreatedDate(Pageable pageable);
    List<BaseModel> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
}
