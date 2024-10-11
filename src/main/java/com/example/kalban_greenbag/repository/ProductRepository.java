package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByOrderByCreatedDateDesc(Pageable pageable);
    List<Product> findAllByStatusOrderByCreatedDateDesc(String status, Pageable pageable);
    int countByStatus(String status);
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :stock WHERE p.id = :productId AND p.stock >= :stock")
    int reduceProductStockById(UUID productId, Integer stock);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:productName% AND p.status = 'active'")
    List<Product> findAllByProductNameContaining(@Param("productName") String productName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.finalPrice BETWEEN :minPrice AND :maxPrice AND p.status = 'active'")
    List<Product> findAllByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

}
