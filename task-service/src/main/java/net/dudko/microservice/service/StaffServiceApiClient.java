package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.StaffDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "STAFF-SERVICE")
public interface StaffServiceApiClient {

    @GetMapping("staff/email/{email}")
    StaffDto getEmployeeByEmail(@PathVariable String email);

}
