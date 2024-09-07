package com.example.kalban_greenbag.converter;

import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.dto.response.role.RoleResponse;
import com.example.kalban_greenbag.entity.Category;
import com.example.kalban_greenbag.entity.Role;

public class CategoryConverter {
    public static CategoryResponse entityToResponse(Category category) {

            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(category.getId());
            categoryResponse.setCategoryName(category.getCategoryName());
            categoryResponse.setDescription(category.getDescription());
            categoryResponse.setStatus(category.getStatus());
            categoryResponse.setCreatedDate(category.getCreatedDate());
            categoryResponse.setModifiedDate(category.getModifiedDate());
            categoryResponse.setModifiedBy(category.getModifiedBy());
            categoryResponse.setCreatedBy(category.getCreatedBy());
            return categoryResponse;
    }

    public static Category responseToEntity(CategoryResponse categoryResponse) {

            Category category = new Category();
            category.setId(categoryResponse.getId());
            category.setCategoryName(categoryResponse.getCategoryName());
            category.setDescription(categoryResponse.getDescription());
            category.setStatus(categoryResponse.getStatus());
            category.setCreatedDate(categoryResponse.getCreatedDate());
            category.setModifiedDate(categoryResponse.getModifiedDate());
            category.setModifiedBy(categoryResponse.getModifiedBy());
            category.setCreatedBy(categoryResponse.getCreatedBy());
            return category;
    }
}
