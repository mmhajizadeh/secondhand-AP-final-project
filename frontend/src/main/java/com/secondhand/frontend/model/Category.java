package com.secondhand.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Data model representing a Category.
 * Used to classify advertisements into specific groups (e.g., Electronics, Vehicles).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
