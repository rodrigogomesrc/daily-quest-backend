package br.ufrn.imd.daily_quest.service;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Task;
import br.ufrn.imd.daily_quest.model.User;
import br.ufrn.imd.daily_quest.model.dto.TaskDTO;
import br.ufrn.imd.daily_quest.model.enums.PriorityEnum;
import br.ufrn.imd.daily_quest.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
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

    public Task save(TaskDTO taskDTO, Long userId) throws BadRequestException, NotFoundException {
        if (userId == null) {
            throw new BadRequestException("User id is required");
        }
        Task task = createTaskFromDTO(taskDTO);
        User creator = userService.findById(userId);
        task.setCreatedAt(String.valueOf(LocalDateTime.now()));
        task.setCreator(creator);
        return taskRepository.save(task);
    }

    public void deleteById(Long id) throws NotFoundException {
        Optional<Task> task = getById(id);
        if (task.isEmpty()) {
            throw new NotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    public Task update(Long taskId, TaskDTO taskDTO, Long userId) throws NotFoundException, BadRequestException {
        Task task = createTaskFromDTO(taskDTO);
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

    private Task createTaskFromDTO(TaskDTO taskDTO) throws BadRequestException {
        Task task = new Task();
        task.setTitle(taskDTO.text());
        task.setText(taskDTO.text());
        task.setPriority(taskDTO.priority());
        task.setDueDate(taskDTO.dueDate());
        task.setImgLink(taskDTO.imgLink());
        task.setReward(taskDTO.reward());
        validateTask(task);
        return task;
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

        try {
            LocalDateTime.parse(task.getDueDate());
        } catch (Exception e) {
            throw new BadRequestException("Invalid due date format");
        }

        try {
            PriorityEnum.valueOf(task.getPriority().toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority");
        }
    }
}
