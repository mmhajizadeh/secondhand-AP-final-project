package com.secondhand.backend.controller;

import com.secondhand.backend.model.Category;
import com.secondhand.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing category-related HTTP requests.
 * <p>
 * Provides endpoints for client applications to fetch available categories
 * or create new ones. Base path is "/api/categories".
 * </p>
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new advertisement category.
     *
     * @param category The category details sent in the request body.
     * @return The saved {@link Category} object.
     * Endpoint: POST /api/categories
     */
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    /**
     * Fetches all categories for use in filters or dropdowns.
     *
     * @return A JSON array containing all {@link Category} objects.
     * Endpoint: GET /api/categories/all
     */
    @GetMapping("/all")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }
}
