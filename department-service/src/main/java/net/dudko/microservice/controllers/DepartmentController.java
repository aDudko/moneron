package net.dudko.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.dudko.microservice.model.dto.DepartmentDto;
import net.dudko.microservice.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "CRUD REST API for Department Resource"
)
@AllArgsConstructor
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(
            summary = "CREATE Department REST API",
            description = "Create Department REST API to save Department"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        var department = departmentService.create(departmentDto);
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Department REST API",
            description = "Get Department REST API to get Department by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
        var department = departmentService.getById(id);
        return ResponseEntity.ok(department);
    }

    @Operation(
            summary = "GET all Departments REST API",
            description = "Get all Departments REST API to get all Departments"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getDepartments() {
        var departments = departmentService.getAll();
        return ResponseEntity.ok(departments);
    }

    @Operation(
            summary = "UPDATE Department REST API",
            description = "Update Department REST API to update Department"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id,
                                                          @RequestBody DepartmentDto departmentDto) {
        var department = departmentService.update(id, departmentDto);
        return ResponseEntity.ok(department);
    }

}
