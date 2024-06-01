package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Category;
import net.dudko.microservice.domain.mapper.CategoryMapper;
import net.dudko.microservice.model.dto.CategoryDto;

public class TestUtil {

    public static final String MS_NAME = "EXPENSE-CATEGORY-MICROSERVICE: ";

    public static Category getValidEntity() {
        return Category.builder()
                .id(1L)
                .name("Default Category Name")
                .build();
    }

    public static CategoryDto getValidDto() {
        return CategoryMapper.mapToCategoryDto(getValidEntity());
    }

}
