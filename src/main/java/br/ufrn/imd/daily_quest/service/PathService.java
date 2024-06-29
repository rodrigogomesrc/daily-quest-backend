package br.ufrn.imd.daily_quest.service;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.*;
import br.ufrn.imd.daily_quest.model.enums.TaskStatusEnum;
import br.ufrn.imd.daily_quest.repository.PathRepository;
import br.ufrn.imd.daily_quest.repository.PathUserRepository;
import br.ufrn.imd.daily_quest.repository.TaskPathRepository;
import br.ufrn.imd.daily_quest.repository.UserTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final PathRepository pathRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final PathUserRepository pathUserRepository;
    private final TaskPathRepository taskPathRepository;
    private final UserTaskRepository userTaskRepository;

    public PathService(
            PathRepository pathRepository,
            UserService userService,
            TaskService taskService,
            PathUserRepository pathUserRepository,
            TaskPathRepository taskPathRepository,
            UserTaskRepository userTaskRepository) {
        this.pathRepository = pathRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.pathUserRepository = pathUserRepository;
        this.taskPathRepository = taskPathRepository;
        this.userTaskRepository = userTaskRepository;
    }

    public Path save(Path path, Long userId) throws BadRequestException, NotFoundException {
        validatePath(path);
        if (userId == null) {
            throw new BadRequestException("User id is required");
        }
        User creator = userService.findById(userId);
        path.setCreator(creator);
        return pathRepository.save(path);
    }

    public List<Path> findAll() {
        return pathRepository.findAll();
    }

    public Path findById(Long id) throws NotFoundException {
        return pathRepository.findById(id).orElseThrow(() -> new NotFoundException("Path not found"));
    }

    public Path updatePath(Long id, Path path, Long userId) throws NotFoundException, BadRequestException {
        Path pathToUpdate = findById(id);
        if (!pathToUpdate.getCreator().getId().equals(userId)) {
            throw new BadRequestException("Path does not belong to user");
        }
        validatePath(path);
        pathToUpdate.setName(path.getName());
        pathToUpdate.setReward(path.getReward());
        return pathRepository.save(pathToUpdate);
    }

    public void deleteById(Long id) throws NotFoundException {
        findById(id);
        pathRepository.deleteById(id);
    }

    @Transactional
    public PathUser addUserToPath(Long pathId, Long taskId) throws NotFoundException {
        Path path = findById(pathId);
        User user = userService.findById(taskId);
        PathUser pathUser = new PathUser();
        pathUser.setPath(path);
        pathUser.setUser(user);

        List<TaskPath> taskPaths = taskPathRepository.findByPathId(pathId);
        for (TaskPath taskPath : taskPaths) {
            UserTask userTask = new UserTask();
            userTask.setTask(taskPath.getTask());
            userTask.setUser(user);
            userTask.setStatus(TaskStatusEnum.NOT_INITIATED);
            userTaskRepository.save(userTask);
        }

        return pathUserRepository.save(pathUser);
    }

    public TaskPath addTaskToPath(Long pathId, Long taskId) throws NotFoundException {
        Path path = findById(pathId);
        Task task = taskService.findById(taskId);
        TaskPath taskPath = new TaskPath();
        taskPath.setPath(path);
        taskPath.setTask(task);
        return taskPathRepository.save(taskPath);
    }

    public void removeTaskFromPath(Long pathId, Long taskId) throws NotFoundException {
        TaskPath taskPath = taskPathRepository.findByPathIdAndTaskId(pathId, taskId)
                .orElseThrow(() -> new NotFoundException("Task not found in path"));
        taskPathRepository.delete(taskPath);
    }

    private void validatePath(Path path) throws BadRequestException {
        if(path.getName() == null || path.getName().isEmpty()){
            throw new BadRequestException("Path name is required");
        }
        if(path.getReward() < 0){
            throw new BadRequestException("Path reward must be greater than or equal to 0");
        }

    }

}
