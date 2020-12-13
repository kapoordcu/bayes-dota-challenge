package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.rest.aggregate.CombatLogAggregator;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.rest.repository.HeroItemsRepository;
import gg.bayes.challenge.rest.repository.HeroKillsRepository;
import gg.bayes.challenge.rest.repository.HeroSpellsRepository;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {
    private final CombatLogAggregator combatLogAggregator;
    private final HeroKillsRepository killsRepository;
    private final HeroItemsRepository itemsRepository;
    private final HeroSpellsRepository heroSpellsRepository;

    public MatchServiceImpl(CombatLogAggregator combatLogAggregator, HeroKillsRepository killsRepository,
                            HeroItemsRepository itemsRepository, HeroSpellsRepository heroSpellsRepository) {
        this.combatLogAggregator = combatLogAggregator;
        this.killsRepository = killsRepository;
        this.itemsRepository = itemsRepository;
        this.heroSpellsRepository = heroSpellsRepository;
    }

    @Override
    public Long ingestMatch(String payload) {
        Long matchId = System.currentTimeMillis();
        combatLogAggregator.processMatchLog(payload, matchId);
        itemsRepository.saveAll(combatLogAggregator.getItemsList());
        killsRepository.saveAll(combatLogAggregator.getWinnersList());
        heroSpellsRepository.saveAll(combatLogAggregator.getSpellList());
        return matchId;
    }

    @Override
    public List<HeroKills> findByMatchId(Long matchId) {
        return killsRepository.findByMatchId(matchId);
    }

    @Override
    public List<HeroItems> findItemsByMatchIdAAndHero(Long matchId, String heroName) {
        return itemsRepository.findByMatchIdAAndHero(matchId, heroName);
    }

    @Override
    public List<HeroSpells> findSpellsByMatchIdAAndHero(Long matchId, String heroName) {
        return heroSpellsRepository.findByMatchIdAAndHero(matchId, heroName);
    }
}
