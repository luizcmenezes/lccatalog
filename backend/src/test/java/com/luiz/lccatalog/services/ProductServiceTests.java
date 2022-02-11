package com.luiz.lccatalog.services;

import com.luiz.lccatalog.repositories.CategoryRepository;
import com.luiz.lccatalog.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {


    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repo;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        Mockito.doNothing().when(repo).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repo).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repo, Mockito.times(1)).deleteById(existingId);
    }

}
