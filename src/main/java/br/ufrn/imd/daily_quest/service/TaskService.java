package br.ufrn.imd.daily_quest.service;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Task;
import br.ufrn.imd.daily_quest.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) throws NotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    public Task save(Task task, Long userId) throws BadRequestException {
        if (userId == null) {
            throw new BadRequestException("User id is required");
        }
        validateTask(task);
        task.setCreatedAt(String.valueOf(LocalDateTime.now()));
        return taskRepository.save(task);
    }

    public void deleteById(Long id) throws NotFoundException {
        Optional<Task> task = getById(id);
        if (task.isEmpty()) {
            throw new NotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    public Task update(Long taskId, Task task, Long userId) throws NotFoundException, BadRequestException {
        validateTask(task);
        Task taskToUpdate = findById(taskId);
        if (!taskToUpdate.getCreator().getId().equals(userId)) {
            throw new BadRequestException("Task does not belong to user");
        }
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setText(task.getText());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setImgLink(task.getImgLink());
        return taskRepository.save(taskToUpdate);
    }

    public void validateTask(Task task) throws BadRequestException {
        if(task.getText() == null || task.getText().isEmpty()){
            throw new BadRequestException("Text is required");
        }
        if(task.getTitle() == null || task.getTitle().isEmpty()){
            throw new BadRequestException("Title is required");
        }
        if(task.getPriority() == null){
            throw new BadRequestException("Priority is required");
        }
        if(task.getDueDate() == null){
            throw new BadRequestException("Due date is required");
        }
    }
}
