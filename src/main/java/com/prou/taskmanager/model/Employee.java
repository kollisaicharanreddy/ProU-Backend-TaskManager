package com.prou.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    // One Employee can have many Tasks
    // cascade = CascadeType.ALL means if we delete an employee, their tasks are also deleted (optional, depends on requirements, but good for cleanup)
    // orphanRemoval = true means if we remove a task from the list, it's deleted from DB
    // JsonIgnore or similar might be needed to avoid infinite recursion in JSON serialization, but I'll handle that in DTOs or with @JsonIgnoreProperties
    // For now, I will not add the list here to avoid circular dependency issues in simple JSON serialization unless needed.
    // Actually, the requirements say "Link tasks to employees".
    // I'll add it but use @JsonIgnore to prevent infinite recursion if I return entities directly.
    // Better yet, I'll use DTOs. But for simplicity in this assignment, I might return entities.
    
    // I will NOT add the list of tasks in Employee for now to keep it simple and avoid circular references in JSON.
    // The relationship will be managed from the Task side (ManyToOne).
}
