package net.dudko.microservice.domain.repository;

import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Office;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class OfficeRepositoryTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "OfficeRepository: ";

    private final TestEntityManager entityManager;
    private final OfficeRepository repository;

    private Office entity;

    @Autowired
    OfficeRepositoryTest(TestEntityManager entityManager,
                         OfficeRepository repository) {
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
    @DisplayName(testNamePrefix + "Test for check exist by code")
    public void givenExistByCode_whenOfficeExist_thenReturnTrue() {
        entityManager.persist(entity);
        assertTrue(repository.existsByCode(entity.getCode()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check not exist by code")
    public void givenExistByCode_whenOfficeNotExist_thenReturnFalse() {
        entityManager.persist(entity);
        assertFalse(repository.existsByCode("Not Exist"));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find exist Office by code")
    public void givenFindByCode_whenOfficeExist_thenReturnOffice() {
        entityManager.persist(entity);
        var inDb = repository.findOfficeByCode(entity.getCode());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
        assertThat(inDb.getDescription()).isEqualTo(entity.getDescription());
        assertThat(inDb.getCode()).isEqualTo(entity.getCode());
        assertThat(inDb.getCreated()).isEqualTo(entity.getCreated());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for find not exist Office by code")
    public void givenFindByCode_whenOfficeNotExist_thenReturnNull() {
        var inDb = repository.findOfficeByCode("Not Exist");
        assertThat(inDb).isNull();
    }

}