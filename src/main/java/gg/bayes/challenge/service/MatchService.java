package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;

import java.util.List;

public interface MatchService {
    Long ingestMatch(String payload);

    List<HeroKills> findByMatchId(Long matchId);

    List<HeroItems> findByMatchIdAAndHero(Long matchId, String heroName);
}
