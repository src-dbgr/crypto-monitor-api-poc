package com.sam.coin.repository;

import com.sam.coin.model.DynamicEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DynamicCandleRepository extends MongoRepository<DynamicEntity, String> {

}