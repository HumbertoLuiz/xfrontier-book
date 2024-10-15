package com.nofrontier.book.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import com.nofrontier.book.domain.model.Category;
import com.nofrontier.book.dto.v1.CategoryDto;

public class MockCategory {

    public Category mockEntity() {
        return mockEntity(0);
    }
    
    public CategoryDto mockDto() {
        return mockDto(0);
    }
    
    public List<Category> mockEntityList() {
        List<Category> categorys = new ArrayList<Category>();
        for (int i = 0; i < 14; i++) {
            categorys.add(mockEntity(i));
        }
        return categorys;
    }

    public List<CategoryDto> mockDtoList() {
        List<CategoryDto> categorys = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            categorys.add(mockDto(i));
        }
        return categorys;
    }
    
    public Category mockEntity(Integer number) {
        Category category = new Category();
        category.setId(number.longValue());
        category.setTitle("Some Title" + number);
        category.setName("Some Name" + number);
        category.setDescription("Some Description");

        return category;
    }


    public CategoryDto mockDto(Integer number) {
        CategoryDto category = new CategoryDto();
        category.setKey(number.longValue());
        category.setTitle("Some Title" + number);
        category.setName("Some Name" + number);
        category.setDescription("Some Description");

        return category;
    }
}
