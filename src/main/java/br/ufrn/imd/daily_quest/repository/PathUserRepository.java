package br.ufrn.imd.daily_quest.repository;

import br.ufrn.imd.daily_quest.model.PathUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathUserRepository extends JpaRepository<PathUser, Long> {
}
