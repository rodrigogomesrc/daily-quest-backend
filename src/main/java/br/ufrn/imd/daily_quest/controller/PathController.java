package br.ufrn.imd.daily_quest.controller;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Path;
import br.ufrn.imd.daily_quest.model.TaskPath;
import br.ufrn.imd.daily_quest.service.PathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<List<Path>> getAllPaths() {
        List<Path> paths = pathService.findAll();
        return ResponseEntity.ok().body(paths);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Path> getPathById(@PathVariable Long id) throws NotFoundException {
        Path path = pathService.findById(id);
        return ResponseEntity.ok().body(path);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Path> createPath(@RequestBody Path path, @PathVariable Long userId)
            throws BadRequestException, NotFoundException {
        Path createdPath = pathService.save(path, userId);
        return ResponseEntity.ok().body(createdPath);
    }

    @PutMapping("/{id}/{userId}")
    public ResponseEntity<Path> updatePath(@PathVariable Long id, @RequestBody Path path, @PathVariable Long userId)
            throws NotFoundException, BadRequestException {
        Path updatedPath = pathService.updatePath(id, path, userId);
        return ResponseEntity.ok().body(updatedPath);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePath(@PathVariable Long id) throws NotFoundException {
        pathService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{pathId}/{taskId}")
    public ResponseEntity<TaskPath> addTaskToPath(@PathVariable Long pathId, @PathVariable Long taskId)
            throws NotFoundException {
        TaskPath taskPath = pathService.addTaskToPath(pathId, taskId);
        return ResponseEntity.ok().body(taskPath);
    }

    @DeleteMapping("/{pathId}/{taskId}")
    public ResponseEntity<Void> removeTaskFromPath(@PathVariable Long pathId, @PathVariable Long taskId)
            throws NotFoundException {
        pathService.removeTaskFromPath(pathId, taskId);
        return ResponseEntity.noContent().build();
    }
}
