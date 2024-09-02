package com.example.kalban_greenbag.repository;

import com.example.kalban_greenbag.entity.Role;
import com.example.kalban_greenbag.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String name);
}
