package br.ufrn.imd.daily_quest.service;

import br.ufrn.imd.daily_quest.exception.BadRequestException;
import br.ufrn.imd.daily_quest.exception.NotFoundException;
import br.ufrn.imd.daily_quest.model.Path;
import br.ufrn.imd.daily_quest.repository.PathRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final PathRepository pathRepository;

    public PathService(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    public Path save(Path path) throws BadRequestException {
        validatePath(path);
        return pathRepository.save(path);
    }

    public List<Path> findAll() {
        return pathRepository.findAll();
    }

    public Path findById(Long id) throws NotFoundException {
        return pathRepository.findById(id).orElseThrow(() -> new NotFoundException("Path not found"));
    }

    public Path updatePath(Long id, Path path) throws NotFoundException, BadRequestException {
        Path pathToUpdate = findById(id);
        validatePath(path);
        pathToUpdate.setName(path.getName());
        pathToUpdate.setReward(path.getReward());
        return pathRepository.save(pathToUpdate);
    }

    public void deleteById(Long id) throws NotFoundException {
        findById(id);
        pathRepository.deleteById(id);
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
