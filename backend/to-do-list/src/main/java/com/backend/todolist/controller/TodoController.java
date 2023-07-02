package com.backend.todolist.controller;

import java.security.Principal;
import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.todolist.errorhandler.CustomException;
import com.backend.todolist.model.Todo;
import com.backend.todolist.service.TodoService;


@RestController
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@ApiResponses(value = {
        @ApiResponse(responseCode="400", description = "Bad Request"),
        @ApiResponse(responseCode="401", description = "Unauthorized"),
        @ApiResponse(responseCode="403", description = "Forbidden"),
        @ApiResponse(responseCode="404", description = "Not Found")
 })
public class TodoController {
	private final TodoService todoService;

	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(value = "/api/todo")
	public ResponseEntity<Todo> create(@Valid @RequestBody TodoCreateRequest todoCreateRequest, Principal principal) {
		return new ResponseEntity<>(todoService.create(todoCreateRequest, principal.getName()), HttpStatus.CREATED);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping(value = "/api/todo")
	public ResponseEntity<List<Todo>> readAll(Principal principal, @RequestParam(required = false) String isCompleted){
		if(isCompleted != null) {
			return new ResponseEntity<>(todoService.readAllByIsCompleted(principal.getName(), isCompleted), HttpStatus.OK);
		}
		return new ResponseEntity<>(todoService.readAll(principal.getName()), HttpStatus.OK);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping(value = "/api/todo/count")
	public ResponseEntity<CountResponse> countAll(Principal principal, @RequestParam(required = false) String isCompleted){
		if(isCompleted != null) {
			return new ResponseEntity<>(todoService.countAllByIsCompleted(principal.getName(), isCompleted), HttpStatus.OK);
		}
		return new ResponseEntity<>(todoService.countAll(principal.getName()), HttpStatus.OK);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping(value = "/api/todo/{pageNumber}/{pageSize}")
	public ResponseEntity<List<Todo>> readAllPageable(Principal principal, @PathVariable String pageNumber, @PathVariable String pageSize, @RequestParam(required = false) String isCompleted){
		if(isCompleted != null) {
			return new ResponseEntity<>(todoService.readAllByIsCompletedPageable(principal.getName(), isCompleted, pageNumber, pageSize), HttpStatus.OK);
		}
		return new ResponseEntity<>(todoService.readAllPageable(principal.getName(), pageNumber, pageSize), HttpStatus.OK);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping(value = "/api/todo/{id}")
	public ResponseEntity<Todo> read(@PathVariable long id, Principal principal) {
		return new ResponseEntity<>(todoService.readById(id, principal.getName()), HttpStatus.OK);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PutMapping(value = "/api/todo/{id}/markcomplete")
	public ResponseEntity<Todo> markComplete(@PathVariable long id, Principal principal) {
		return new ResponseEntity<>(todoService.markCompleteById(id, principal.getName()), HttpStatus.OK);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PutMapping(value = "/api/todo/{id}")
	public ResponseEntity<Todo> update(@PathVariable long id, @Valid @RequestBody TodoUpdateRequest todoUpdateRequest, Principal principal) {
		return new ResponseEntity<>(todoService.updateById(id, todoUpdateRequest, principal.getName()), HttpStatus.OK);
	}
	
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/api/todo/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id, Principal principal) {
		todoService.deleteById(id, principal.getName());
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
