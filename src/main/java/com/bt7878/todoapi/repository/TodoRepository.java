package com.bt7878.todoapi.repository;

import com.bt7878.todoapi.model.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {
}
