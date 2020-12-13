package gg.bayes.challenge.rest.repository;

import gg.bayes.challenge.rest.model.HeroDamage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroDamageRepository extends JpaRepository<HeroDamage, Long> {
    @Query("select target, SUM(damageInstances), SUM(totalDamage) from HeroDamage " +
            "where matchId=:matchId AND hero=:heroName GROUP by target")
    List<HeroDamage> findByMatchIdAAndHero(Long matchId, String heroName);
}
