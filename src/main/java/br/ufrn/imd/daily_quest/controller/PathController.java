package br.ufrn.imd.daily_quest.controller;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Path;
import br.ufrn.imd.daily_quest.service.PathService;
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
    public List<Path> getAllPaths() {
        return pathService.findAll();
    }

    @GetMapping("/{id}")
    public Path getPathById(@PathVariable Long id) throws NotFoundException {
        return pathService.findById(id);
    }

    @PostMapping
    public Path createPath(@RequestBody Path path) throws BadRequestException {
        return pathService.save(path);
    }

    @PutMapping("/{id}")
    public Path updatePath(@PathVariable Long id, @RequestBody Path path)
            throws NotFoundException, BadRequestException {
        return pathService.updatePath(id, path);
    }

    @DeleteMapping("/{id}")
    public void deletePath(@PathVariable Long id) throws NotFoundException {
        pathService.deleteById(id);
    }

}
