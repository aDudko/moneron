package net.dudko.microservice.domain.repository;

import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Employee;
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
class StaffRepositoryTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "StaffRepository: ";

    private final TestEntityManager entityManager;
    private final StaffRepository repository;

    private Employee entity;

    @Autowired
    StaffRepositoryTest(TestEntityManager entityManager,
                        StaffRepository repository) {
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
    @DisplayName(testNamePrefix + "Test for check exist by email")
    public void givenExistByCode_whenStaffExist_thenReturnTrue() {
        entityManager.persist(entity);
        assertTrue(repository.existsByEmail(entity.getEmail()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check not exist by email")
    public void givenExistByCode_whenStaffNotExist_thenReturnFalse() {
        entityManager.persist(entity);
        assertFalse(repository.existsByEmail("not-exist@email.com"));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find exist staff by email")
    public void givenFindByCode_whenStaffExist_thenReturnStaff() {
        entityManager.persist(entity);
        var inDb = repository.findByEmail(entity.getEmail());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(inDb.getLastName()).isEqualTo(entity.getLastName());
        assertThat(inDb.getEmail()).isEqualTo(entity.getEmail());
        assertThat(inDb.getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getOfficeCode()).isEqualTo(entity.getOfficeCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find not exist staff by email")
    public void givenFindByCode_whenStaffNotExist_thenReturnNull() {
        var inDb = repository.findByEmail("not-exist@email.com");
        assertThat(inDb).isNull();
    }

}