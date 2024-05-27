package net.dudko.microservice.domain.repository;

import net.dudko.microservice.domain.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Boolean existsByEmail(String email);

    Staff findByEmail(String email);

}
