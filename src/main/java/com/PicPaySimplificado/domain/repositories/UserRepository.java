package com.PicPaySimplificado.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.PicPaySimplificado.domain.entities.User;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByDocument(String document);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findById(Long id);

}
