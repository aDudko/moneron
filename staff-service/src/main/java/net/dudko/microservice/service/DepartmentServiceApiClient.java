package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentServiceApiClient {

    @GetMapping("department/code/{code}")
    DepartmentDto getDepartmentByCode(@PathVariable String code);

}
