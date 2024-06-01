package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto getById(Long id);

    CategoryDto getByName(String name);

    List<CategoryDto> getAll();

    CategoryDto update(Long id, CategoryDto categoryDto);

    void delete(Long id);

}
