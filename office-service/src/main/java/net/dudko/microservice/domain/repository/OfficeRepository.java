package net.dudko.microservice.domain.repository;

import net.dudko.microservice.domain.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {

    Boolean existsByCode(String code);

    Office findOfficeByCode(String code);

}
