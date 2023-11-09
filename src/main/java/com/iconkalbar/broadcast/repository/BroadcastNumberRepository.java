package com.iconkalbar.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iconkalbar.broadcast.model.BroadcastNumber;
import java.util.List;



public interface BroadcastNumberRepository extends JpaRepository<BroadcastNumber, Integer>, JpaSpecificationExecutor<BroadcastNumber> {
    
    public List<BroadcastNumber> findByUserName(String userName);
    
}
