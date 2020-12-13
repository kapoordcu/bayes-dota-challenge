package gg.bayes.challenge.rest.aggregate;

import gg.bayes.challenge.config.PropertyConfig;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.rest.model.MatchEnum;
import gg.bayes.challenge.service.exception.DataParseException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Data
public class CombatLogAggregator {
    private static final Logger LOG = LoggerFactory.getLogger(CombatLogAggregator.class);
    private final PropertyConfig config;

    public CombatLogAggregator(PropertyConfig config) {
        this.config = config;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // Data list exposed to API
    private Map<String, Integer> winnersByKillCount = new HashMap<>();
    private List<HeroItems> itemsList = new ArrayList<>();
    private List<HeroKills> winnersList = new ArrayList<>();
    private List<HeroSpells> spellList = new ArrayList<>();

    private Pattern regexSqBrackets = Pattern.compile("\\[(.*?)\\]");

    private Matcher bracketMatcher;
    private SimpleDateFormat dateFormat;

    public void processMatchLog(String payload, Long matchId) {
        if(StringUtils.isEmpty(payload)) {
            throw new DataParseException("Payload is empty");
        }
        String[] events = payload.split(System.getProperty("line.separator"));
        Arrays.stream(events).forEach(event -> processEvent(event, matchId));
        winnersList = winnersByKillCount.entrySet().stream()
                .map(entry -> {
                    HeroKills killRecord = new HeroKills();
                    killRecord.setHero(entry.getKey());
                    killRecord.setKills(entry.getValue());
                    killRecord.setMatchId(matchId);
                    return killRecord;
                })
                .collect(Collectors.toList());
    }

    public void processEvent(String event, Long matchId) {
        MatchEnum eventType = MatchEnum.findEventType(event);
        if(EnumSet.of(MatchEnum.BUYS).contains(eventType)) {
            aggItemsBeingPurchased(event, matchId);
        } else if(EnumSet.of(MatchEnum.KILLED).contains(eventType)) {
            aggregateWinnersByHeroes(event, matchId);
        }  else if(EnumSet.of(MatchEnum.CASTS).contains(eventType)) {
            aggregateCastSpell(event, matchId);
        }  else if(EnumSet.of(MatchEnum.HITS).contains(eventType)) {
            aggregateDamageDone(event, matchId);
        }
    }

    private void aggregateDamageDone(String event, Long matchId) {
        //[00:20:08.093] npc_dota_hero_rubick hits npc_dota_hero_dragon_knight with dota_unknown for 23 damage (1699->1676)
        // Not implemented because of time limit of 2 hours
    }

    private void aggregateCastSpell(String event, Long matchId) {
        //[00:19:19.038] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown
        String[] recordKeys = event.split("\\s+");
        if(recordKeys.length > 4) {
            String target = recordKeys[recordKeys.length-1];
            String heroName = retrieveHeroNameFromPrefix(recordKeys[1]);
            String spellAbility = recordKeys[4];
            HeroSpells spell = new HeroSpells();
            spell.setTarget(target);
            spell.setHero(heroName);
            spell.setMatchId(matchId);
            spell.setSpell(spellAbility);
            spell.setCasts(extractLevelOfCast(event));
            spellList.add(spell);
        }
    }

    private Integer extractLevelOfCast(String event) {
        try {
            String castLevel = event.substring(event.indexOf("(") + 1, event.indexOf(")"));
            String[] level = castLevel.split("\\s+");
            return Integer.valueOf(level[level.length-1]);
        } catch (Exception ex) {
            LOG.warn(event + ": could not be parsed to Long value");
        }
        return 0;
    }

    /**
     * Items being purchased
     */
    private void aggItemsBeingPurchased(String event, Long matchId) {
        String[] recordKeys = event.split("\\s+");
        if(recordKeys.length > 1) {
            String itemName = recordKeys[recordKeys.length-1].replace(config.getItemPrefix(), "");;
            String heroName = retrieveHeroNameFromPrefix(recordKeys[1]);
            String timeStamp = recordKeys[0];
            HeroItems item = new HeroItems();
            item.setItem(itemName);
            item.setHero(heroName);
            item.setMatchId(matchId);
            item.setTimestamp(extractTimeStampIntoLong(timeStamp));
            itemsList.add(item);
        }
    }

    /**
     * Heroes killing each other
     */
    private void aggregateWinnersByHeroes(String record, Long matchId) {
        String[] recordKeys = record.split("\\s+");
        if(recordKeys.length > 0) {
            String heroName = retrieveHeroNameFromPrefix(recordKeys[recordKeys.length-1]);
            winnersByKillCount.merge(heroName, 1, Integer::sum);
        }
    }

    private String retrieveHeroNameFromPrefix(String heroWithPrefix) {
        for (String prefix: config.getHeroPrefix()) {
            if(heroWithPrefix.contains(prefix)) {
                return heroWithPrefix.replace(prefix, "");
            }
        }
        return heroWithPrefix;
    }

    private Long extractTimeStampIntoLong(String timeStamp) {
        try {
            bracketMatcher = regexSqBrackets.matcher(timeStamp);
            if(bracketMatcher.matches()) {
                String timeStampMMHHSS = bracketMatcher.group(1).trim();
                return dateFormat.parse("1970-01-01 " + timeStampMMHHSS).getTime();
            }
        } catch (ParseException ex) {
            LOG.warn(timeStamp + ": The timestamp could not be parsed to Long value");
        }
        return System.currentTimeMillis();
    }

    public List<HeroItems> getItemsList() {
        return itemsList;
    }

    public List<HeroKills> getWinnersList() {
        return winnersList;
    }

    public List<HeroSpells> getSpellList() {
        return spellList;
    }
}

