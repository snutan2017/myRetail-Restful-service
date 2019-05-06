package com.retail.myRetail.repository;

import com.retail.myRetail.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends MongoRepository<Price, String> {


    Price findById(Long id);
}

