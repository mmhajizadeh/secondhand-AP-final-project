package com.secondhand.backend.controller;

import com.secondhand.backend.model.Category;
import com.secondhand.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }
    
    @GetMapping("/all")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }
}
