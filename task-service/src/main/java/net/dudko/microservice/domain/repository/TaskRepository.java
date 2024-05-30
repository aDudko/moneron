package net.dudko.microservice.domain.repository;

import net.dudko.microservice.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByEmployeeEmail(String email);

}
