package com.iconkalbar.broadcast.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iconkalbar.broadcast.model.SitePOP;
import com.iconkalbar.broadcast.repository.SitePOPRepository;

@Service
public class SitePoPService {
    
    @Autowired
    private SitePOPRepository sitePOPRepository;


    public SitePOP saveOrUpdateSitePOP(SitePOP sitePOP) {
        return sitePOPRepository.save(sitePOP);
    }

    public List<SitePOP> fetchByPopId(String popId) {
        return sitePOPRepository.findByPopId(popId);
    }
}
