package com.luiz.lccatalog.repositories;

import com.luiz.lccatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        long exitingId = 1L;

        repo.deleteById(exitingId);

        Optional<Product> result = repo.findById(exitingId);

        Assertions.assertFalse(result.isPresent());

    }
}
