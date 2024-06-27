package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:8080")
public class CategoriesController {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryDao.getAllCategories();
    }

    @RequestMapping(path = "/{id}" , method = RequestMethod.GET)
    public Category getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nothing" + id);
        }
        return category;
    }

    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        List<Product> products = productDao.listByCategoryId(categoryId);
        if (products == null || products.isEmpty()) {
            throw new RuntimeException(HttpStatus.NOT_FOUND + " No products found for categoryId: " + categoryId);
        }
        return products;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{categoryId}")
    public void updateCategory(@PathVariable int categoryId, @RequestBody Category category) {
        category.setCategoryId(categoryId);
        categoryDao.update(categoryId, category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int categoryId) {
        Category existingCategory = categoryDao.getById(categoryId);
        if (existingCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found" + categoryId);
        }

        categoryDao.delete(categoryId);
    }

}
