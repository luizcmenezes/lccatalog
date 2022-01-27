package com.luiz.lccatalog.services;

import com.luiz.lccatalog.dto.CategoryDTO;
import com.luiz.lccatalog.entities.Category;
import com.luiz.lccatalog.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        return repo.findAll(pageRequest).map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Entity not found")));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category obj = new Category();
        obj.setName(dto.getName());
        obj = repo.save(obj);
        return new CategoryDTO(obj);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category obj = repo.getById(id);
            obj.setName(dto.getName());
            obj = repo.save(obj);
            return new CategoryDTO(obj);
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
