package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Category;
import net.dudko.microservice.model.dto.CategoryDto;

public class CategoryMapper {

    public static Category mapToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
