package com.shivam.redis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shivam.redis.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

}
