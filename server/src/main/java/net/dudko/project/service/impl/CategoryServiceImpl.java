package net.dudko.project.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.project.domain.mapper.CategoryMapper;
import net.dudko.project.domain.repository.CategoryRepository;
import net.dudko.project.model.dto.CategoryDto;
import net.dudko.project.model.exceprion.ResourceNotFoundException;
import net.dudko.project.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        var category = CategoryMapper.mapToCategory(categoryDto);
        category = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories() {
        var categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapToCategoryDto).toList();
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(categoryDto.name());
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

}
