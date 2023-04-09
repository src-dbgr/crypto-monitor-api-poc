package com.sam.coin.repository;

import com.sam.coin.model.company.Company;
import com.sam.coin.model.util.CompanyName;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CandleBasedRepository extends MongoRepository<Company, String> {
    Optional<Company> findByCompanyName(CompanyName companyName);
}