package net.dudko.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.TaskDto;
import net.dudko.microservice.service.TaskService;
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
        name = "CRUD REST API for Task Resource"
)
@AllArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "CREATE Task REST API",
            description = "Create Task REST API to save Task"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        var task = taskService.create(taskDto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Task REST API",
            description = "Get Task REST API to get Task by ID with Department, Office and Employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDto> getTaskById(@PathVariable Long id) {
        var response = taskService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "GET Tasks REST API",
            description = "Get all Tasks REST API to get Tasks by email of Employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("email/{email}")
    public ResponseEntity<List<TaskDto>> getAllByEmployeeEmail(@PathVariable String email) {
        var response = taskService.getAllByEmployeeEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "UPDATE Task REST API",
            description = "Update Task REST API to update Task"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @PutMapping("{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id,
                                              @RequestBody TaskDto taskDto) {
        var task = taskService.update(id, taskDto);
        return ResponseEntity.ok(task);
    }

    @Operation(
            summary = "DELETE Task REST API",
            description = "Delete Task REST API to delete Task"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

}
