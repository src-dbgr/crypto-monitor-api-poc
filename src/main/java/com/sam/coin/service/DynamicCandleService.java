package com.sam.coin.service;

import com.sam.coin.model.DynamicEntity;
import com.sam.coin.repository.DynamicCandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DynamicCandleService {

    private final DynamicCandleRepository dynamicCandleRepository;

    @Autowired
    public DynamicCandleService(DynamicCandleRepository dynamicCandleRepository) {
        this.dynamicCandleRepository = dynamicCandleRepository;
    }

    public DynamicEntity save(DynamicEntity entity) {
        return dynamicCandleRepository.save(entity);
    }

    public Optional<DynamicEntity> findById(String id) {
        return dynamicCandleRepository.findById(id);
    }

}
