package net.dudko.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.dudko.microservice.model.dto.OfficeDto;
import net.dudko.microservice.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "CRUD REST API for Office Resource"
)
@AllArgsConstructor
@RestController
@RequestMapping("office")
public class OfficeController {

    private final OfficeService officeService;

    @Operation(
            summary = "CREATE Office REST API",
            description = "Create Office REST API to save Office"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<OfficeDto> createOffice(@RequestBody OfficeDto officeDto) {
        var office = officeService.create(officeDto);
        return new ResponseEntity<>(office, HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Office REST API",
            description = "Get Office REST API to get Office by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("{id}")
    public ResponseEntity<OfficeDto> getOfficeById(@PathVariable Long id) {
        var office = officeService.getById(id);
        return ResponseEntity.ok(office);
    }

    @Operation(
            summary = "GET Office REST API",
            description = "Get Office REST API to get Office by code"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("code/{code}")
    public ResponseEntity<OfficeDto> getOfficeByCode(@PathVariable String code) {
        var office = officeService.getByCode(code);
        return new ResponseEntity<>(office, HttpStatus.OK);
    }

}
