package com.java.springboot.todoapp.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data               //Automatically write getters and setters
@AllArgsConstructor
@NoArgsConstructor         //Lombok annotations
public class Todo {
    @Id
    @GeneratedValue     //Automatically generate a value
    Long id;
    @NotNull            //Validate the specified request to be given
    @NotBlank
    String title;
    Boolean isCompleted;
}
