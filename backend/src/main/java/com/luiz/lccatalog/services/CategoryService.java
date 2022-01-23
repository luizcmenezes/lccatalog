package com.luiz.lccatalog.services;

import com.luiz.lccatalog.dto.CategoryDTO;
import com.luiz.lccatalog.entities.Category;
import com.luiz.lccatalog.repositories.CategoryRepository;
import com.luiz.lccatalog.services.execeptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repo.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(repo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entity not found")));
    }
}
