package gg.bayes.challenge.rest.repository;

import gg.bayes.challenge.rest.model.HeroItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroItemsRepository extends JpaRepository<HeroItems, Long> {
    @Query("SELECT a FROM HeroItems a  WHERE a.matchId=:matchId AND a.hero=:heroName")
    List<HeroItems> findByMatchIdAAndHero(Long matchId, String heroName);
}
