package com.luiz.lccatalog.repositories;

import com.luiz.lccatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
