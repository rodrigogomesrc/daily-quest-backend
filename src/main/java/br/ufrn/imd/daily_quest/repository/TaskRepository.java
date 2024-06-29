package br.ufrn.imd.daily_quest.repository;


import br.ufrn.imd.daily_quest.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
