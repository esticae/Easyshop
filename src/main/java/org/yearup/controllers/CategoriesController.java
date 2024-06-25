package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:8080")
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // add the appropriate annotation for a get action
    public List<Category> getAll()
    {
        List<Category> categories = categoryDao.getAllCategories();
        // find and return all categories
        return categories;
    }

    // add the appropriate annotation for a get action
    public Category getById(@PathVariable int id)
    {
        Category category = categoryDao.getById(id);
        // get the category by id
        return category;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("/categories/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        // Example logic to get a list of products by categoryId
        List<Product> products = productDao.listByCategoryId(categoryId); // Assuming productDao has a method to find products by categoryId
        return products;
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        Category savedCategory = categoryDao.create(category);
        return savedCategory;
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/categories/{categoryId}")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        category.setCategoryId(id);
        categoryDao.update(id, category);
        // update the category by id
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/categories/{categoryId}")
    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
        // delete the category by id
    }
}
