package com.bt7878.todoapi.service;

import com.bt7878.todoapi.model.Todo;
import com.bt7878.todoapi.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo todo1;
    private Todo todo2;

    @BeforeEach
    void setUp() {
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
    void getAllTodos_ShouldReturnAllTodos() {
        // Arrange
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // Act
        List<Todo> result = todoService.getAllTodos();

        // Assert
        assertEquals(2, result.size());
        assertEquals(todo1.getId(), result.get(0).getId());
        assertEquals(todo2.getId(), result.get(1).getId());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getTodoById_WithExistingId_ShouldReturnTodo() {
        // Arrange
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));

        // Act
        Optional<Todo> result = todoService.getTodoById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Test Todo 1", result.get().getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void getTodoById_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        when(todoRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Todo> result = todoService.getTodoById(3L);

        // Assert
        assertTrue(result.isEmpty());
        verify(todoRepository, times(1)).findById(3L);
    }

    @Test
    void saveTodo_ShouldReturnSavedTodo() {
        // Arrange
        Todo newTodo = new Todo();
        newTodo.setTitle("New Todo");
        newTodo.setCompleted(false);

        Todo savedTodo = new Todo();
        savedTodo.setId(3L);
        savedTodo.setTitle("New Todo");
        savedTodo.setCompleted(false);

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // Act
        Todo result = todoService.saveTodo(newTodo);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Todo", result.getTitle());
        verify(todoRepository, times(1)).save(newTodo);
    }

    @Test
    void deleteTodo_ShouldCallRepositoryDeleteById() {
        // Arrange
        Long id = 1L;
        doNothing().when(todoRepository).deleteById(id);

        // Act
        todoService.deleteTodo(id);

        // Assert
        verify(todoRepository, times(1)).deleteById(id);
    }
}