package com.shivam.redis.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shivam.redis.model.Product;
import com.shivam.redis.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @CachePut(value = "products", key = "#result.id")
    public Product save(Product product) {

        System.out.println("Saved to MongoDB");

        return repository.save(product);
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getById(String id) {

        System.out.println("Fetched from MongoDB");

        return repository.findById(id).orElse(null);
    }

    @CacheEvict(value = "products", key = "#id")
    public void delete(String id) {

        repository.deleteById(id);

        System.out.println("Deleted from MongoDB");
    }
}