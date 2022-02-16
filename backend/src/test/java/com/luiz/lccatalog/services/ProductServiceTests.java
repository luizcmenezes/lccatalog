package com.luiz.lccatalog.services;

import com.luiz.lccatalog.dto.CategoryDTO;
import com.luiz.lccatalog.dto.ProductDTO;
import com.luiz.lccatalog.entities.Category;
import com.luiz.lccatalog.entities.Product;
import com.luiz.lccatalog.repositories.CategoryRepository;
import com.luiz.lccatalog.repositories.ProductRepository;
import com.luiz.lccatalog.services.execptions.DatabaseException;
import com.luiz.lccatalog.services.execptions.ResourceNotFoundException;
import com.luiz.lccatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDto;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDto = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.doNothing().when(repo).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repo).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repo).deleteById(dependentId);

        Mockito.when(repo.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repo.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repo.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repo.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repo.getById(existingId)).thenReturn(product);
        Mockito.when(repo.getById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDto);
        });
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists(){
        ProductDTO result = service.update(existingId, productDto);
        assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){
        ProductDTO result = service.findById(existingId);
        assertNotNull(result);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repo, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldResourceNotFoundExceptionWhenIdDoesNotExists(){
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

        Mockito.verify(repo, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDatabaseExceptionWhenDependentId(){
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
        Mockito.verify(repo, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        assertNotNull(result);
        Mockito.verify(repo, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldDoNothingWhenIdExists(){
        assertDoesNotThrow(() -> {
            service.findById(existingId);
        });
        Mockito.verify(repo, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldResourceNotFoundExceptionWhenIdDoesNotExists(){
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
        Mockito.verify(repo, Mockito.times(1)).findById(nonExistingId);
    }
}
