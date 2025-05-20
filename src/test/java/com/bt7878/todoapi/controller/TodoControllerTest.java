package com.bt7878.todoapi.controller;

import com.bt7878.todoapi.model.Todo;
import com.bt7878.todoapi.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import(TodoControllerTest.TestConfig.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoService todoService;
    private Todo todo1;
    private Todo todo2;

    @BeforeEach
    void setUp() {
        // Reset mock before each test
        Mockito.reset(todoService);

        // Create test data
        todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Test Todo 1");
        todo1.setCompleted(false);

        todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Test Todo 2");
        todo2.setCompleted(true);
    }

    @Test
    void getAllTodos_ShouldReturnAllTodos() throws Exception {
        // Arrange
        List<Todo> todos = Arrays.asList(todo1, todo2);
        when(todoService.getAllTodos()).thenReturn(todos);

        // Act & Assert
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Todo 1")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test Todo 2")))
                .andExpect(jsonPath("$[1].completed", is(true)));

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void getTodoById_WithExistingId_ShouldReturnTodo() throws Exception {
        // Arrange
        when(todoService.getTodoById(1L)).thenReturn(todo1);

        // Act & Assert
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Todo 1")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(todoService, times(1)).getTodoById(1L);
    }

    @Test
    void getTodoById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(todoService.getTodoById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/todos/99"))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).getTodoById(99L);
    }

    @Test
    void createTodo_WithValidData_ShouldReturnCreatedTodo() throws Exception {
        // Arrange
        Todo newTodo = new Todo();
        newTodo.setTitle("New Todo");
        newTodo.setCompleted(false);

        Todo savedTodo = new Todo();
        savedTodo.setId(3L);
        savedTodo.setTitle("New Todo");
        savedTodo.setCompleted(false);

        when(todoService.saveTodo(any(Todo.class))).thenReturn(savedTodo);

        // Act & Assert
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("New Todo")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(todoService, times(1)).saveTodo(any(Todo.class));
    }

    @Test
    void createTodo_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Todo invalidTodo = new Todo();
        invalidTodo.setTitle(""); // Empty title should fail validation
        invalidTodo.setCompleted(false);

        // Act & Assert
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());

        verify(todoService, never()).saveTodo(any(Todo.class));
    }

    @Test
    void updateTodo_WithExistingIdAndValidData_ShouldReturnUpdatedTodo() throws Exception {
        // Arrange
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Todo");
        updatedTodo.setCompleted(true);

        Todo savedTodo = new Todo();
        savedTodo.setId(1L);
        savedTodo.setTitle("Updated Todo");
        savedTodo.setCompleted(true);

        when(todoService.getTodoById(1L)).thenReturn(todo1);
        when(todoService.saveTodo(any(Todo.class))).thenReturn(savedTodo);

        // Act & Assert
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Todo")))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(todoService, times(1)).getTodoById(1L);
        verify(todoService, times(1)).saveTodo(any(Todo.class));
    }

    @Test
    void updateTodo_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Todo");
        updatedTodo.setCompleted(true);

        when(todoService.getTodoById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/todos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).getTodoById(99L);
        verify(todoService, never()).saveTodo(any(Todo.class));
    }

    @Test
    void deleteTodo_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(todoService.getTodoById(1L)).thenReturn(todo1);
        doNothing().when(todoService).deleteTodo(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).getTodoById(1L);
        verify(todoService, times(1)).deleteTodo(1L);
    }

    @Test
    void deleteTodo_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(todoService.getTodoById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/todos/99"))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).getTodoById(99L);
        verify(todoService, never()).deleteTodo(anyLong());
    }

    static class TestConfig {
        @Bean
        public TodoService todoService() {
            return Mockito.mock(TodoService.class);
        }
    }

}
