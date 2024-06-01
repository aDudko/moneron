package net.dudko.microservice.domain.repository;

import net.dudko.microservice.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

    Category findByName(String name);

}
