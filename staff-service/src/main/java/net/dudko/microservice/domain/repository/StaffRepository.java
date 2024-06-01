package net.dudko.microservice.domain.repository;

import net.dudko.microservice.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmail(String email);

    Employee findByEmail(String email);

}
