package net.dudko.microservice.domain.repository;

import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Task;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TaskRepositoryTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "TaskRepository: ";

    private final TestEntityManager entityManager;
    private final TaskRepository repository;

    private Task entity;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskRepositoryTest(TestEntityManager entityManager,
                       TaskRepository repository) {
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
    public void givenEmployeeEmail_whenTaskExist_thenReturnListOfTasks() {
        entityManager.persist(entity);
        var inDb = taskRepository.findAllByEmployeeEmail(entity.getEmployeeEmail());
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for check not exist by email")
    public void givenEmployeeEmail_whenTaskExist_thenReturnEmptyList() {
        var inDb = taskRepository.findAllByEmployeeEmail(entity.getEmployeeEmail());
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

}