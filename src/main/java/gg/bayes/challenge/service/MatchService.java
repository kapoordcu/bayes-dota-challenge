package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

import java.util.List;

public interface MatchService {
    Long ingestMatch(String payload);

    List<HeroKills> findByMatchId(Long matchId);

    List<HeroItems> findItemsByMatchIdAAndHero(Long matchId, String heroName);

    List<HeroSpells> findSpellsByMatchIdAAndHero(Long matchId, String heroName);

    List<HeroDamage> findDamageByMatchIdAAndHero(Long matchId, String heroName);
}
