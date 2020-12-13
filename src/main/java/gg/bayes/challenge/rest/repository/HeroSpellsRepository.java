package gg.bayes.challenge.rest.repository;

import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroSpellsRepository extends JpaRepository<HeroSpells, Long> {
    @Query("SELECT a FROM HeroSpells a  WHERE a.matchId=:matchId AND a.hero=:heroName")
    List<HeroSpells> findByMatchIdAAndHero(Long matchId, String heroName);
}
