package net.dudko.microservice.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.CategoryMapper;
import net.dudko.microservice.domain.repository.CategoryRepository;
import net.dudko.microservice.model.dto.CategoryDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ResourceDuplicatedException(String.format("Category with name: %s already exists!", categoryDto.getName()));
        }
        var category = CategoryMapper.mapToCategory(categoryDto);
        category = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public CategoryDto getById(Long id) {
        var category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id: %d not found", id)));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public CategoryDto getByName(String name) {
        if (!categoryRepository.existsByName(name)) {
            throw new ResourceNotFoundException(String.format("Category with name: %s not found", name));
        }
        var category = categoryRepository.findByName(name);
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAll() {
        var categories = categoryRepository.findAll();
        return categories
                .stream()
                .map(CategoryMapper::mapToCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        var category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id: %d not found", id)));
        category.setName(categoryDto.getName());
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        var category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id: %d not found", id)));
        categoryRepository.delete(category);
    }

}
