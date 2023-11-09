package com.iconkalbar.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.iconkalbar.broadcast.model.BroadcastHistory;

public interface BroadcastHistoryRepository extends JpaRepository<BroadcastHistory, Integer>, JpaSpecificationExecutor<BroadcastHistory> {

    
}
