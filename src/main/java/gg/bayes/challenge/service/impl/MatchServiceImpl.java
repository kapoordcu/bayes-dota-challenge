package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.rest.aggregate.LogAggregator;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.repository.HeroItemsRepository;
import gg.bayes.challenge.rest.repository.HeroKillsRepository;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {
    private final LogAggregator logAggregator;
    private final HeroKillsRepository killsRepository;
    private final HeroItemsRepository itemsRepository;

    public MatchServiceImpl(LogAggregator logAggregator, HeroKillsRepository killsRepository,
                            HeroItemsRepository itemsRepository) {
        this.logAggregator = logAggregator;
        this.killsRepository = killsRepository;
        this.itemsRepository = itemsRepository;
    }

    @Override
    public Long ingestMatch(String payload) {
        Long matchId = System.currentTimeMillis();
        logAggregator.processMatchLog(payload, matchId);
        itemsRepository.saveAll(logAggregator.getItemsList());
        killsRepository.saveAll(logAggregator.getWinnersList());
        return matchId;
    }

    @Override
    public List<HeroKills> findByMatchId(Long matchId) {
        return killsRepository.findByMatchId(matchId);
    }

    @Override
    public List<HeroItems> findByMatchIdAAndHero(Long matchId, String heroName) {
        return itemsRepository.findByMatchIdAAndHero(matchId, heroName);
    }
}
