package net.dudko.project.domain.mapper;

import net.dudko.project.domain.entity.Category;
import net.dudko.project.model.dto.CategoryDto;

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
