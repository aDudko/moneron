package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Category;
import net.dudko.microservice.domain.mapper.CategoryMapper;
import net.dudko.microservice.domain.repository.CategoryRepository;
import net.dudko.microservice.model.dto.CategoryDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoryServiceTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "CategoryService: ";

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl service;

    private Category entity;
    private CategoryDto dto;

    @BeforeEach
    public void setup() {
        entity = TestUtil.getValidEntity();
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Category when not exist duplicates")
    public void givenCategoryDto_whenCreateCategory_thenReturnCreatedCategoryDto() {
        given(repository.existsByName(entity.getName())).willReturn(Boolean.FALSE);
        given(repository.save(entity)).willReturn(entity);
        var inDb = service.create(dto);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Category when exist duplicates")
    public void givenCategoryDto_whenCreateCategory_thenReturnException() {
        given(repository.existsByName(entity.getName())).willReturn(Boolean.TRUE);
        var message = assertThrows(ResourceDuplicatedException.class, () -> {
            service.create(dto);
        }).getMessage();
        assertThat(message).isEqualTo("Category with name: %s already exists!", dto.getName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by ID when Category exist")
    public void givenCategoryId_whenGetCategoryById_thenReturnCategoryDto() {
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        var inDb = service.getById(dto.getId());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(dto.getName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by ID when Category not exist")
    public void givenCategoryId_whenGetCategoryById_thenReturnException() {
        given(repository.findById(entity.getId())).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(dto.getId());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Category with id: %d not found", dto.getId()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by name when Category exist")
    public void givenCategoryName_whenGetCategoryByName_thenReturnCategoryDto() {
        given(repository.existsByName(entity.getName())).willReturn(Boolean.TRUE);
        given(repository.findByName(entity.getName())).willReturn(entity);
        var inDb = service.getByName(dto.getName());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(dto.getName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by name when Category not exist")
    public void givenCategoryName_whenGetCategoryByName_thenReturnException() {
        given(repository.existsByName(entity.getName())).willReturn(Boolean.FALSE);
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getByName(dto.getName());
        }).getMessage();
        assertThat(message).isEqualTo(String.format(String.format("Category with name: %s not found", dto.getName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Categories when Categories exist")
    public void givenCategories_whenGetAllCategories_thenReturnListOfCategoryDto() {
        given(repository.findAll()).willReturn(List.of(entity));
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Categories when Categories not exist")
    public void givenCategories_whenGetAllCategories_thenReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Category when Category exist")
    public void givenCategoryDto_whenUpdateCategory_thenReturnUpdatedCategoryDto() {
        given(repository.save(entity)).willReturn(entity);
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        entity.setName("Updated Name");
        var inDb = service.update(dto.getId(), CategoryMapper.mapToCategoryDto(entity));
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Category when Category not exist")
    public void givenCategoryDto_whenUpdateCategory_thenReturnException() {
        given(repository.findById(entity.getId())).willReturn(Optional.empty());
        entity.setName("Not Exist");
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto.getId(), CategoryMapper.mapToCategoryDto(entity));
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Category with id: %d not found", dto.getId()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Category when Category exist")
    public void givenCategoryId_whenDeleteCategory_thenReturnNothing() {
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        service.delete(entity.getId());
        verify(repository, times(0)).deleteById(entity.getId());
    }

}