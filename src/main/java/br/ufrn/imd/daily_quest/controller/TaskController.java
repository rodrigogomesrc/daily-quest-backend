package br.ufrn.imd.daily_quest.controller;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Task;
import br.ufrn.imd.daily_quest.model.dto.TaskDTO;
import br.ufrn.imd.daily_quest.model.enums.PriorityEnum;
import br.ufrn.imd.daily_quest.model.enums.TaskStatusEnum;
import br.ufrn.imd.daily_quest.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO task, @PathVariable Long userId)
            throws BadRequestException, NotFoundException {
        Task createdTask = taskService.save(task, userId);
        return ResponseEntity.ok().body(createdTask);
    }

    @PutMapping("/{id}/{userId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDTO task, @PathVariable Long userId)
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

    @PutMapping("/{taskId}/{taskStatus}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @PathVariable String taskStatus)
            throws NotFoundException {
        Task updatedTask = taskService.updateTaskStatus(taskId, TaskStatusEnum.valueOf(taskStatus));
        return ResponseEntity.ok().body(updatedTask);
    }

    @GetMapping("/valid-task-statuses")
    public ResponseEntity<Set<TaskStatusEnum>> getValidTaskStatuses() {
        return ResponseEntity.ok().body(taskService.getValidTaskStatuses());
    }

    @GetMapping("/valid-task-priorities")
    public ResponseEntity<Set<PriorityEnum>> getValidTaskPriorities() {
        return ResponseEntity.ok().body(taskService.getValidTaskPriorities());
    }
}