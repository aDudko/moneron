package net.dudko.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.StaffDto;
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
            summary = "CREATE staffDto REST API",
            description = "Create Staff REST API to save Employee"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<StaffDto> createEmployee(@RequestBody StaffDto staffDto) {
        var staff = staffService.create(staffDto);
        return new ResponseEntity<>(staff, HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Staff REST API",
            description = "Get Staff REST API to get Employee by ID with Department"
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
            description = "Get Staff REST API to get Staff"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping
    public ResponseEntity<List<StaffDto>> getStaff() {
        var staff = staffService.getAll();
        return ResponseEntity.ok(staff);
    }

    @Operation(
            summary = "UPDATE Staff REST API",
            description = "Update Staff REST API to update Employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @PutMapping("{id}")
    public ResponseEntity<StaffDto> updateEmployee(@PathVariable Long id,
                                                   @RequestBody StaffDto staffDto) {
        var staff = staffService.update(id, staffDto);
        return ResponseEntity.ok(staff);
    }

    @Operation(
            summary = "DELETE Staff REST API",
            description = "Delete Staff REST API to update Employee"
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
