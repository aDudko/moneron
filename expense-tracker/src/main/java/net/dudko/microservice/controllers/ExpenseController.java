package net.dudko.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.ExpenseDto;
import net.dudko.microservice.service.ExpenseService;
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
        name = "CRUD REST API for Expense Resource"
)
@AllArgsConstructor
@RestController
@RequestMapping("expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(
            summary = "CREATE Expense REST API",
            description = "Create Expense REST API to save Expense"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseDto) {
        var expense = expenseService.create(expenseDto);
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Expense REST API",
            description = "Get Expense REST API to get Expense by ID with Category"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDto> getExpense(@PathVariable Long id) {
        var response = expenseService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "GET all Expenses REST API",
            description = "Get all Expenses REST API to get all Expenses"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getExpenses() {
        var expenses = expenseService.getAll();
        return ResponseEntity.ok(expenses);
    }

    @Operation(
            summary = "UPDATE Expense REST API",
            description = "Update Expense REST API to update Expense"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @PutMapping("{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id,
                                                    @RequestBody ExpenseDto expenseDto) {
        var expense = expenseService.update(id, expenseDto);
        return ResponseEntity.ok(expense);
    }

    @Operation(
            summary = "DELETE Expense REST API",
            description = "Delete Expense REST API to delete Expense"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS 200 OK"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.ok("Expense was deleted successfully");
    }

}
