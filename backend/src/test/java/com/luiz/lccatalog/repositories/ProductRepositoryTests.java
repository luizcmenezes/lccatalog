package com.luiz.lccatalog.repositories;

import com.luiz.lccatalog.entities.Product;
import com.luiz.lccatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    private long exitingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        exitingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repo.save(product);

        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {
        Optional<Product> result = repo.findById(exitingId);
        assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldNotReturnObjectWhenIdNotExists() {
        Optional<Product> result = repo.findById(nonExistingId);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repo.deleteById(exitingId);
        Optional<Product> result = repo.findById(exitingId);
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repo.deleteById(nonExistingId);
        });
    }
}
