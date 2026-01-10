package com.dekhokaun.mindarobackend.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ApiException {
    public CategoryNotFoundException(String categoryName) {
        super("Category not found: " + categoryName, HttpStatus.NOT_FOUND);
    }
}
