package com.bt7878.todoapi.repository;

import com.bt7878.todoapi.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void findById_WithExistingId_ShouldReturnTodo() {
        // Arrange
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setCompleted(false);

        // Save the todo and get the generated ID
        Todo savedTodo = entityManager.persistAndFlush(todo);

        // Act
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());

        // Assert
        assertTrue(foundTodo.isPresent());
        assertEquals("Test Todo", foundTodo.get().getTitle());
        assertFalse(foundTodo.get().isCompleted());
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Act
        Optional<Todo> foundTodo = todoRepository.findById(999L);

        // Assert
        assertFalse(foundTodo.isPresent());
    }

    @Test
    void save_ShouldPersistTodo() {
        // Arrange
        Todo todo = new Todo();
        todo.setTitle("New Todo");
        todo.setCompleted(true);

        // Act
        Todo savedTodo = todoRepository.save(todo);

        // Assert
        assertNotNull(savedTodo.getId());

        // Verify it's in the database
        Todo foundTodo = entityManager.find(Todo.class, savedTodo.getId());
        assertNotNull(foundTodo);
        assertEquals("New Todo", foundTodo.getTitle());
        assertTrue(foundTodo.isCompleted());
    }

    @Test
    void deleteById_WithExistingId_ShouldRemoveTodo() {
        // Arrange
        Todo todo = new Todo();
        todo.setTitle("Todo to Delete");
        todo.setCompleted(false);

        Todo savedTodo = entityManager.persistAndFlush(todo);

        // Act
        todoRepository.deleteById(savedTodo.getId());

        // Assert
        Todo foundTodo = entityManager.find(Todo.class, savedTodo.getId());
        assertNull(foundTodo);
    }

    @Test
    void findAll_ShouldReturnAllTodos() {
        // Arrange
        Todo todo1 = new Todo();
        todo1.setTitle("Todo 1");
        todo1.setCompleted(false);

        Todo todo2 = new Todo();
        todo2.setTitle("Todo 2");
        todo2.setCompleted(true);

        entityManager.persist(todo1);
        entityManager.persist(todo2);
        entityManager.flush();

        // Act
        Iterable<Todo> todos = todoRepository.findAll();

        // Assert
        int count = 0;
        for (Todo ignored : todos) {
            count++;
        }
        assertEquals(2, count);
    }
}