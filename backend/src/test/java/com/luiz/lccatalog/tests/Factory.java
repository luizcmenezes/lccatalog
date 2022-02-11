package com.luiz.lccatalog.tests;

import com.luiz.lccatalog.dto.ProductDTO;
import com.luiz.lccatalog.entities.Category;
import com.luiz.lccatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://upload.wikimedia.org/wikipedia/commons/3/34/Nokia_3310_3G_%2820180116%29.jpg", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
