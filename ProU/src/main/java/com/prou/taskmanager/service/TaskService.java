package com.prou.taskmanager.service;

import com.prou.taskmanager.exception.ResourceNotFoundException;
import com.prou.taskmanager.model.Employee;
import com.prou.taskmanager.model.Task;
import com.prou.taskmanager.model.TaskStatus;
import com.prou.taskmanager.repository.EmployeeRepository;
import com.prou.taskmanager.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Task createTask(Task task) {
        log.info("Creating new task: {}", task.getTitle());
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        
        // Handle employee assignment if provided
        if (task.getEmployee() != null && task.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(task.getEmployee().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + task.getEmployee().getId()));
            task.setEmployee(employee);
        }
        
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(TaskStatus status, Long employeeId) {
        log.info("Fetching tasks with status: {} and employeeId: {}", status, employeeId);
        if (status != null && employeeId != null) {
            return taskRepository.findByStatusAndEmployeeId(status, employeeId);
        } else if (status != null) {
            return taskRepository.findByStatus(status);
        } else if (employeeId != null) {
            return taskRepository.findByEmployeeId(employeeId);
        } else {
            return taskRepository.findAll();
        }
    }

    public Task getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", id);
                    return new ResourceNotFoundException("Task not found with id: " + id);
                });
    }

    public Task updateTask(Long id, Task taskDetails) {
        log.info("Updating task with id: {}", id);
        Task task = getTaskById(id);
        
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        
        
        if (taskDetails.getEmployee() != null) {
             if (taskDetails.getEmployee().getId() != null) {
                 Employee employee = employeeRepository.findById(taskDetails.getEmployee().getId())
                         .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
                 task.setEmployee(employee);
             } else {
                 task.setEmployee(null); // Unassign if empty employee object passed? Or keep existing?
                 // For now, let's assume if employee object is passed with ID, we update.
             }
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
    
    public Task assignTaskToEmployee(Long taskId, Long employeeId) {
        log.info("Assigning task {} to employee {}", taskId, employeeId);
        Task task = getTaskById(taskId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        task.setEmployee(employee);
        return taskRepository.save(task);
    }
}
