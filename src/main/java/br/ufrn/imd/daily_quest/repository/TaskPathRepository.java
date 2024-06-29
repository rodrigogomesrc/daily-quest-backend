package br.ufrn.imd.daily_quest.repository;

import br.ufrn.imd.daily_quest.model.TaskPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskPathRepository extends JpaRepository<TaskPath, Long> {

    List<TaskPath> findByPathId(Long pathId);

    Optional<TaskPath> findByPathIdAndTaskId(Long pathId, Long taskId);
}
