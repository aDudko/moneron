package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Category;
import net.dudko.microservice.model.dto.CategoryDto;

public class CategoryMapper {

    public static Category mapToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.id(),
                categoryDto.name());
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName()
        );
    }

}
