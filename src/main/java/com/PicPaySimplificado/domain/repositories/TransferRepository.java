package com.PicPaySimplificado.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PicPaySimplificado.domain.entities.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, String> {

}
