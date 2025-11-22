package com.java.springboot.todoapp.repository;


import com.java.springboot.todoapp.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
