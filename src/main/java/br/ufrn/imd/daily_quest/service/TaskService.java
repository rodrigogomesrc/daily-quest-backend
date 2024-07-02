package br.ufrn.imd.daily_quest.service;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Path;
import br.ufrn.imd.daily_quest.model.Task;
import br.ufrn.imd.daily_quest.model.User;
import br.ufrn.imd.daily_quest.model.dto.TaskDTO;
import br.ufrn.imd.daily_quest.model.enums.PriorityEnum;
import br.ufrn.imd.daily_quest.model.enums.TaskStatusEnum;
import br.ufrn.imd.daily_quest.repository.TaskRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(
            TaskRepository taskRepository,
            UserService userService) {
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
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setImgLink(task.getImgLink());
        return taskRepository.save(taskToUpdate);
    }

    private Task createTaskFromDTO(TaskDTO taskDTO) throws BadRequestException {
        Task task = new Task();
        task.setTitle(taskDTO.title());
        task.setText(taskDTO.text());
        task.setPriority(taskDTO.priority());
        task.setStatus(taskDTO.status());
        task.setDueDate(taskDTO.dueDate());
        task.setImgLink(taskDTO.imgLink());
        task.setReward(taskDTO.reward());
        //validateTask(task);
        return task;
    }

    public Task updateTaskStatus(Long taskId, TaskStatusEnum newStatus) throws NotFoundException {
        Task task = findById(taskId);

        if (task.getStatus().equals(newStatus)) {
            return task;
        }

        if(task.getStatus().equals(TaskStatusEnum.COMPLETED) && !newStatus.equals(TaskStatusEnum.COMPLETED)){
            task.getCreator().setRewardsGained(task.getCreator().getRewardsGained() - task.getReward());
            task.setStatus(newStatus);
            return taskRepository.save(task);
        }

        if (newStatus.equals(TaskStatusEnum.COMPLETED)) {
            task.getCreator().setRewardsGained(task.getCreator().getRewardsGained() + task.getReward());
            task.setStatus(newStatus);
            return taskRepository.save(task);
        }
        return task;
    }

    public Set<TaskStatusEnum> getValidTaskStatuses(){
        return EnumSet.of(TaskStatusEnum.COMPLETED, TaskStatusEnum.IN_PROGRESS, TaskStatusEnum.NOT_INITIATED);
    }

    public Set<PriorityEnum> getValidTaskPriorities(){
        return EnumSet.of(PriorityEnum.HIGH, PriorityEnum.LOW, PriorityEnum.MEDIUM);
    }

    private void validateTask(Task task) throws BadRequestException {
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
            TaskStatusEnum.valueOf(task.getStatus().toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority");
        }

        try {
            PriorityEnum.valueOf(task.getPriority().toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority");
        }
    }
}
