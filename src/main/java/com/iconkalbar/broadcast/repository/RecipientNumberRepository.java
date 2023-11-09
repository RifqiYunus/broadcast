package com.iconkalbar.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iconkalbar.broadcast.model.RecipientNumber;

public interface RecipientNumberRepository extends JpaRepository<RecipientNumber, Integer>, JpaSpecificationExecutor<RecipientNumber> {

    
}
