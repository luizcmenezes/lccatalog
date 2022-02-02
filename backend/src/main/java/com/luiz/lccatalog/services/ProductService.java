package com.luiz.lccatalog.services;

import com.luiz.lccatalog.dto.ProductDTO;
import com.luiz.lccatalog.entities.Product;
import com.luiz.lccatalog.repositories.ProductRepository;
import com.luiz.lccatalog.services.execeptions.DatabaseException;
import com.luiz.lccatalog.services.execeptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        return repo.findAll(pageRequest).map(prod -> new ProductDTO(prod));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product prod = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(prod, prod.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product obj = new Product();
//        obj.setName(dto.getName());
        obj = repo.save(obj);
        return new ProductDTO(obj);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product obj = repo.getById(id);
//            obj.setName(dto.getName());
            obj = repo.save(obj);
            return new ProductDTO(obj);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            repo.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }
}
