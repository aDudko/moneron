package net.dudko.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.EmployeeDto;
import net.dudko.microservice.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "CRUD REST API for Staff Resource"
)
@AllArgsConstructor
@RestController
@RequestMapping("staff")
public class StaffController {


    private final StaffService staffService;

    @Operation(
            summary = "CREATE Staff REST API",
            description = "Create Employee REST API to save Employee"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        var staff = staffService.create(employeeDto);
        return new ResponseEntity<>(staff, HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Staff REST API",
            description = "Get Employee REST API to get Employee by ID with codes of Department and Office"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDto> getEmployeeById(@PathVariable Long id) {
        var response = staffService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "GET Staff REST API",
            description = "Get Employee REST API to get Employee by email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("email/{email}")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@PathVariable String email) {
        var response = staffService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "GET Staff REST API",
            description = "Get Staff REST API to get Staff"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getStaff() {
        var staff = staffService.getStaff();
        return ResponseEntity.ok(staff);
    }

    @Operation(
            summary = "UPDATE Staff REST API",
            description = "Update Employee REST API to update Employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestBody EmployeeDto employeeDto) {
        var staff = staffService.update(id, employeeDto);
        return ResponseEntity.ok(staff);
    }

    @Operation(
            summary = "DELETE Staff REST API",
            description = "Delete Employee REST API to update Employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

}
