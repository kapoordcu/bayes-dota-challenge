package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.rest.aggregate.DotaAggregator;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.repository.HeroItemsRepository;
import gg.bayes.challenge.rest.repository.HeroKillsRepository;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.service.exception.DataParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    private final DotaAggregator aggregator;
    private final HeroKillsRepository killsRepository;
    private final HeroItemsRepository itemsRepository;

    public MatchServiceImpl(DotaAggregator aggregator, HeroKillsRepository killsRepository,
                            HeroItemsRepository itemsRepository) {
        this.aggregator = aggregator;
        this.killsRepository = killsRepository;
        this.itemsRepository = itemsRepository;
    }

    @Override
    public Long ingestMatch(String payload) {
        aggregator.processMatchLog(payload);
        if(CollectionUtils.isEmpty(aggregator.getWinnersList())) {
            throw new DataParseException("The parsing does not contain any data for killing");
        }
        killsRepository.saveAll(aggregator.getWinnersList());
        itemsRepository.saveAll(aggregator.getItemsList());
        return aggregator.getWinnersList().stream().findAny().get().getMatchId();
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
