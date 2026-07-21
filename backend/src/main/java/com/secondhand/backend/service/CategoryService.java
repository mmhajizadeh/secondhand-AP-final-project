package com.secondhand.backend.service;

import com.secondhand.backend.entity.Category;
import com.secondhand.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link Category} entities.
 * <p>
 * Encapsulates the logic for saving and fetching advertisement categories
 * used throughout the application.
 * </p>
 *
 * @since 1.0
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Saves a newly created category to the database.
     *
     * @param category The {@link Category} object to be saved.
     * @return The saved {@link Category} entity including its generated ID.
     */
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Retrieves all categories available for advertisements.
     *
     * @return A list of all {@link Category} objects.
     */
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}

