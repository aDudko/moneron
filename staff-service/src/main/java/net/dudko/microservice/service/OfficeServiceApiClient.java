package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.OfficeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "OFFICE-SERVICE")
public interface OfficeServiceApiClient {

    @GetMapping("office/code/{code}")
    OfficeDto getOfficeByCode(@PathVariable String code);

}
