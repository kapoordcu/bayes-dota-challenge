package gg.bayes.challenge.rest.aggregate;

import gg.bayes.challenge.config.PropertyConfig;
import gg.bayes.challenge.rest.model.*;
import gg.bayes.challenge.service.exception.DataParseException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;
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
        dateFormat = new SimpleDateFormat(config.getDateFormat());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // Data list exposed to API
    private Map<String, Integer> winnersByKillCount = new HashMap<>();
    private List<HeroItems> itemsList = new ArrayList<>();
    private List<HeroKills> winnersList = new ArrayList<>();
    private List<HeroSpells> spellList = new ArrayList<>();
    private List<HeroDamage> damageList = new ArrayList<>();

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
            aggregateWinnersByHeroes(event);
        }  else if(EnumSet.of(MatchEnum.CASTS).contains(eventType)) {
            aggregateCastSpell(event, matchId);
        }  else if(EnumSet.of(MatchEnum.HITS).contains(eventType)) {
            aggregateDamageDone(event, matchId);
        } else if(EnumSet.of(MatchEnum.UNKNOWN).contains(eventType)) {
            LOG.warn("This event type is supported in future modifications");
        }
    }

    private void aggregateDamageDone(String event, Long matchId) {
        try {
            String[] recordKeys = event.split("\\s+");
            if(recordKeys.length > 4) {
                String heroName = retrieveHeroNameFromPrefix(recordKeys[1]);
                String target = retrieveHeroNameFromPrefix(recordKeys[3]);
                Matcher matcher = Pattern.compile("(?<=for\\s).*(?=\\sdamage)").matcher(event);
                HeroDamage damage = new HeroDamage();
                damage.setTarget(target);
                damage.setHero(heroName);
                damage.setMatchId(matchId);
                damage.setDamageInstances(1);
                if(matcher.find()) {
                    damage.setTotalDamage(Integer.valueOf(matcher.group().trim()));
                }
                damageList.add(damage);
            }
        } catch (Exception exception) {
            LOG.debug("Cast Spelling event is not in expected format: " + event);
        }
    }

    /**
     * Spells being cast
     *
     * @param event
     * @param matchId
     */
    private void aggregateCastSpell(String event, Long matchId) {
        try {
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
        } catch (Exception exception) {
            LOG.debug("Cast Spelling event is not in expected format: " + event);
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
     *
     * @param event
     * @param matchId
     */
    private void aggItemsBeingPurchased(String event, Long matchId) {
        try {
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
        } catch (Exception exception) {
            LOG.debug("Purchase event is not in expected format: " + event);
        }
    }

    /**
     * Heroes killing each other
     *
     * @param event
     */
    private void aggregateWinnersByHeroes(String event) {
        try {
            String[] recordKeys = event.split("\\s+");
            if(recordKeys.length > 0) {
                String heroName = retrieveHeroNameFromPrefix(recordKeys[recordKeys.length-1]);
                winnersByKillCount.merge(heroName, 1, Integer::sum);
            }
        } catch (Exception exception) {
            LOG.debug("Killing event is not in expected format: " + event);
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
}

