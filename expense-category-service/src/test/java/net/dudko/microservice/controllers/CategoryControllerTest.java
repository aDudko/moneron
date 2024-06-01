package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.CategoryMapper;
import net.dudko.microservice.domain.repository.CategoryRepository;
import net.dudko.microservice.model.dto.CategoryDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "CategoryController: ";
    private static final String BASE_URL = "/category";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository repository;

    private CategoryDto dto;

    @BeforeEach
    public void setup() {
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Category when not exist duplicates")
    public void givenCategoryDto_whenCreateCategory_thenReturnCreatedCategoryDto() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Category when exist duplicates")
    public void givenCategoryDto_whenCreateCategory_thenReturnException() throws Exception {
        repository.save(CategoryMapper.mapToCategory(dto));
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by ID when Category exist")
    public void givenCategoryId_whenGetCategoryById_thenReturnCategoryDto() throws Exception {
        var inDb = repository.save(CategoryMapper.mapToCategory(dto));
        mockMvc.perform(get(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by ID when Category not exist")
    public void givenCategoryId_whenGetCategoryById_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by name when Category exist")
    public void givenCategoryName_whenGetCategoryByName_thenReturnCategoryDto() throws Exception {
        var inDb = repository.save(CategoryMapper.mapToCategory(dto));
        mockMvc.perform(get(BASE_URL.concat("/name/{id}"), inDb.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Category by name when Category not exist")
    public void givenCategoryNAme_whenGetCategoryByName_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/name/{name}"), "not-exist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Categories when Categories exist")
    public void givenCategories_whenGetAllCategories_thenReturnListOfCategoryDto() throws Exception {
        repository.save(CategoryMapper.mapToCategory(dto));
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Categories when Categories not exist")
    public void givenCategories_whenGetAllCategories_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Category when Category exist")
    public void givenCategoryDto_whenUpdateCategory_thenReturnUpdatedCategoryDto() throws Exception {
        var inDb = repository.save(CategoryMapper.mapToCategory(dto));
        dto.setName("Updated Name");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Category when Category not exist")
    public void givenCategoryDto_whenUpdateCategory_thenReturnException() throws Exception {
        dto.setName("Not Exist");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Category when Category exist")
    public void givenCategoryId_whenDeleteCategory_thenReturnNothing() throws Exception {
        var inDb = repository.save(CategoryMapper.mapToCategory(dto));
        mockMvc.perform(delete(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Category deleted successfully");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Category when Category not exist")
    public void givenCategoryId_whenDeleteCategory_thenReturnException() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}