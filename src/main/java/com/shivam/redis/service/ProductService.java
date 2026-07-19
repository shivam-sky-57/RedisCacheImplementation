package com.shivam.redis.service;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shivam.redis.model.Product;
import com.shivam.redis.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProductService(ProductRepository repository,
                          RedisTemplate<String, Object> redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    // CREATE
    public Product save(Product product) {
        Product saved = repository.save(product);

        // store in redis
        redisTemplate.opsForValue()
                .set("product:" + saved.getId(), saved, Duration.ofMinutes(10));

        return saved;
    }

    // READ ALL
    public List<Product> getAll() {
        return repository.findAll();
    }

    // READ BY ID (with Redis cache)
    public Product getById(String id) {

        String key = "product:" + id;

        // 1. Check Redis
        Product cached = (Product) redisTemplate.opsForValue().get(key);

        if (cached != null) {
            System.out.println("Fetched from REDIS");
            return cached;
        }

        // 2. If not found, fetch from MongoDB
        Product product = repository.findById(id).orElse(null);

        if (product != null) {
            System.out.println("Fetched from MONGODB");

            // 3. Store in Redis
            redisTemplate.opsForValue()
                    .set(key, product, Duration.ofMinutes(10));
        }

        return product;
    }

    // DELETE
    public void delete(String id) {

        repository.deleteById(id);

        // remove from redis
        redisTemplate.delete("product:" + id);
    }
}