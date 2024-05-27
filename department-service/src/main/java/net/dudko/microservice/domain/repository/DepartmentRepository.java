package net.dudko.microservice.domain.repository;

import net.dudko.microservice.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Boolean existsByCode(String code);

    Department findDepartmentByCode(String code);

}
