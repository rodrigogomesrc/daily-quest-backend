package br.ufrn.imd.daily_quest.repository;

import br.ufrn.imd.daily_quest.model.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathRepository extends JpaRepository<Path, Long> {
}
