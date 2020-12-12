package gg.bayes.challenge.rest.aggregate;

import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.MatchEnum;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.service.exception.DataParseException;
import lombok.Data;
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
public class DotaAggregator {
    private List<HeroKills> winnersList = new ArrayList<>();
    private List<HeroItems> itemsList = new ArrayList<>();

    private Pattern regex = Pattern.compile("\\[(.*?)\\]");
    private Matcher bracketMatcher;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.S");

    public void processMatchLog(String payload) {
        if(StringUtils.isEmpty(payload)) {
            throw new DataParseException("Payload is empty");
        }
        processMatchRecordByMove(payload, System.currentTimeMillis());
    }

    private void processMatchRecordByMove(String payload, Long matchId) {
        Map<String, Integer> winningHeroesMap = new HashMap<>();
        String[] records = payload.split(System.getProperty("line.separator"));
        for (String record:   records) {
            MatchEnum recordType = MatchEnum.findEventType(record);
            if(EnumSet.of(MatchEnum.KILLED).contains(recordType)) {
               aggregateWinnersByHeroes(record, winningHeroesMap);
            } else if(EnumSet.of(MatchEnum.BUYS).contains(recordType)) {
                aggregateItemsBought(record, matchId);
            }
        }
        winnersList = winningHeroesMap.entrySet().stream()
                .map(entry -> {
                    HeroKills killRecord = new HeroKills();
                    killRecord.setHero(entry.getKey());
                    killRecord.setKills(entry.getValue());
                    killRecord.setMatchId(matchId);
                    return killRecord;
                })
                .collect(Collectors.toList());
    }

    private void aggregateItemsBought(String record, Long matchId) {
        String[] recordKeys = record.split("\\s+");
        if(recordKeys.length > 1) {
            String itemName = recordKeys[recordKeys.length-1];
            String heroName = recordKeys[1];
            String timeStamp = recordKeys[0];
            HeroItems item = new HeroItems();
            item.setItem(itemName);
            item.setHero(heroName);
            item.setMatchId(matchId);
            item.setTimestamp(extractTimeStampIntoLong(timeStamp));
            itemsList.add(item);
        }
    }

    private Long extractTimeStampIntoLong(String timeStamp) {
        try {
            bracketMatcher = regex.matcher(timeStamp);
            if(bracketMatcher.matches()) {
                String timeStampMMHHSS = bracketMatcher.group(1);
                return dateFormat.parse(timeStampMMHHSS).getTime();
            }
        } catch (ParseException ex) {

        }
        return null;
    }

    private void aggregateWinnersByHeroes(String record, Map<String, Integer> winningHeroesMap) {
        String[] recordKeys = record.split("\\s+");
        if(recordKeys.length > 0) {
            String heroName = recordKeys[recordKeys.length-1];
            winningHeroesMap.merge(heroName, 1, Integer::sum);
        }
    }

    public List<HeroKills> getWinnersList() {
        return winnersList;
    }

    public List<HeroItems> getItemsList() {
        return itemsList;
    }
}

