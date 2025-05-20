package com.bt7878.todoapi.controller;

import com.bt7878.todoapi.model.Todo;
import com.bt7878.todoapi.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo todo) {
        Optional<Todo> existingTodo = todoService.getTodoById(id);
        if (existingTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        return ResponseEntity.ok(todoService.saveTodo(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        Optional<Todo> existingTodo = todoService.getTodoById(id);
        if (existingTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

}
