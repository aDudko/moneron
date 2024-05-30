package net.dudko.microservice.domain.repository;

import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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
class DepartmentRepositoryTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = "DEPARTMENT-MICROSERVICE: DEPARTMENT-REPOSITORY: ";

    private final TestEntityManager entityManager;
    private final DepartmentRepository repository;

    private Department entity;

    @Autowired
    DepartmentRepositoryTest(TestEntityManager entityManager,
                             DepartmentRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    @BeforeEach
    public void setup() {
        entity = TestUtil.getValidDepartment();
        entity.setId(null);
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check exist by code")
    public void givenExistByCode_whenDepartmentExist_thenReturnTrue() {
        entityManager.persist(entity);
        assertTrue(repository.existsByCode(entity.getCode()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check not exist by code")
    public void givenExistByCode_whenDepartmentNotExist_thenReturnFalse() {
        entityManager.persist(entity);
        assertFalse(repository.existsByCode("DDT"));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find exist department by code")
    public void givenFindByCode_whenDepartmentExist_thenReturnDepartment() {
        entityManager.persist(entity);
        var inDb = repository.findDepartmentByCode(entity.getCode());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
        assertThat(inDb.getDescription()).isEqualTo(entity.getDescription());
        assertThat(inDb.getCode()).isEqualTo(entity.getCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find nat exist department by code")
    public void givenFindByCode_whenDepartmentNotExist_thenReturnNull() {
        entityManager.persist(entity);
        var inDb = repository.findDepartmentByCode("DDT");
        assertThat(inDb).isNull();
    }

}