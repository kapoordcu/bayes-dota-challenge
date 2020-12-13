package gg.bayes.challenge.rest.repository;

import gg.bayes.challenge.rest.model.HeroKills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroKillsRepository extends JpaRepository<HeroKills, Long> {
    List<HeroKills> findByMatchId(Long matchId);
}
