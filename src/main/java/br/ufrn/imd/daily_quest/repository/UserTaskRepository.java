package br.ufrn.imd.daily_quest.repository;

import br.ufrn.imd.daily_quest.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {

}
