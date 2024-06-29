package br.ufrn.imd.daily_quest.controller;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Task;
import br.ufrn.imd.daily_quest.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id)
            throws NotFoundException {
        Task task = taskService.findById(id);
        return ResponseEntity.ok().body(task);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @PathVariable Long userId)
            throws BadRequestException {
        Task createdTask = taskService.save(task, userId);
        return ResponseEntity.ok().body(createdTask);
    }

    @PutMapping("/{id}/{userId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, @PathVariable Long userId)
            throws NotFoundException, BadRequestException {
        Task updatedTask = taskService.update(id, task, userId);
        return ResponseEntity.ok().body(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id)
            throws NotFoundException {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}