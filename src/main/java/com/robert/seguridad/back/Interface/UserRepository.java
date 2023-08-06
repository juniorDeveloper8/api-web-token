package com.robert.seguridad.back.Interface;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.robert.seguridad.back.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String username);
}
