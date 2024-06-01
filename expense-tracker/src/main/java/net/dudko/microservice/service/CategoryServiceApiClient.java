package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "EXPENSE-CATEGORY-SERVICE")
public interface CategoryServiceApiClient {

    @GetMapping("category/name/{name}")
    CategoryDto getCategoryByName(@PathVariable String name);

}
