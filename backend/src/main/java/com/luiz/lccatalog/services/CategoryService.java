package com.luiz.lccatalog.services;

import com.luiz.lccatalog.entities.Category;
import com.luiz.lccatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public List<Category> findAll(){
        return repo.findAll();
    }
}
