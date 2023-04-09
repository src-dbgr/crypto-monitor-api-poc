package com.sam.coin.repository;

import com.sam.coin.model.company.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CandleBasedRepository extends MongoRepository<Company, String> {
    Optional<Company> findByCompanyName(String companyName);
}