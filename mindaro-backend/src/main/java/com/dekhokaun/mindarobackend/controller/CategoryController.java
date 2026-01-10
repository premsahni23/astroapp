package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.CategoryRequest;
import com.dekhokaun.mindarobackend.payload.response.CategoryResponse;
import com.dekhokaun.mindarobackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category Controller", description = "APIs for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a category", description = "Creates a new category")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all available categories")
    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get category by name", description = "Fetch a category using its name")
    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @Operation(summary = "Update a category", description = "Updates the category details")
    @PutMapping("/{name}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String name,
                                                           @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(name, request));
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its name")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Name of the category to delete", required = true) @PathVariable String name) {
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}
