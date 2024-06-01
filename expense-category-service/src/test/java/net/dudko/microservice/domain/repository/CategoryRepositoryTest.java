package net.dudko.microservice.domain.repository;

import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CategoryRepositoryTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "CategoryRepository: ";

    private final TestEntityManager entityManager;
    private final CategoryRepository repository;

    private Category entity;

    @Autowired
    CategoryRepositoryTest(TestEntityManager entityManager,
                           CategoryRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    @BeforeEach
    public void setup() {
        entity = TestUtil.getValidEntity();
        entity.setId(null);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check exist by name")
    public void givenExistByName_whenCategoryExist_thenReturnTrue() {
        entityManager.persist(entity);
        assertTrue(repository.existsByName(entity.getName()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check not exist by name")
    public void givenExistByName_whenCategoryNotExist_thenReturnFalse() {
        entityManager.persist(entity);
        assertFalse(repository.existsByName("Updated Name"));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find exist Category by name")
    public void givenFindByName_whenCategoryExist_thenReturnCategory() {
        entityManager.persist(entity);
        var inDb = repository.findByName(entity.getName());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find not exist Category by name")
    public void givenFindByName_whenCategoryNotExist_thenReturnNull() {
        var inDb = repository.findByName("Updated Name");
        assertThat(inDb).isNull();
    }

}