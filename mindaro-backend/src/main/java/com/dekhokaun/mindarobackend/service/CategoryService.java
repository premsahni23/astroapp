package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.Category;
import com.dekhokaun.mindarobackend.payload.request.CategoryRequest;
import com.dekhokaun.mindarobackend.payload.response.CategoryResponse;
import com.dekhokaun.mindarobackend.repository.CategoryRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        
        // Set fields from request
        category.setName(request.getName());
        
        // Set required fields with default values
        category.setFbid(""); // Default empty string
        category.setCode(0); // Default code
        category.setPrede(0); // Default predecessor
        category.setIcon(""); // Default empty icon
        category.setImage(""); // Default empty image
        category.setText(request.getDescription() != null ? request.getDescription() : ""); // Use description or empty
        category.setLanguage("en"); // Default language
        category.setAction(""); // Default empty action
        category.setLink(""); // Default empty link
        category.setOrderx(0); // Default order
        category.setStatus(1); // Default active status
        
        categoryRepository.save(category);
        return ObjectMapperUtils.map(category, CategoryResponse.class);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> ObjectMapperUtils.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new InvalidRequestException("Category not found"));
        return ObjectMapperUtils.map(category, CategoryResponse.class);
    }

    public CategoryResponse updateCategory(String name, CategoryRequest request) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new InvalidRequestException("Category not found"));

        category.setName(request.getName());
        categoryRepository.save(category);
        return ObjectMapperUtils.map(category, CategoryResponse.class);
    }

    public void deleteCategory(String name) {
        categoryRepository.deleteByName(name);
    }
}
