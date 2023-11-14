package com.iconkalbar.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iconkalbar.broadcast.model.SitePOP;
import java.util.List;


public interface SitePOPRepository extends JpaRepository<SitePOP, Integer>, JpaSpecificationExecutor<SitePOP> {

    List<SitePOP> findByPopId(String popId);
    
}
