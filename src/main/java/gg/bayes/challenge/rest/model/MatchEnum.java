package gg.bayes.challenge.rest.model;

import lombok.Data;

import java.util.Arrays;
import java.util.Optional;

public enum MatchEnum {
    UNKNOWN(-1, "UNKNOWN"),
    CASTS(1000, "casts"),
    BUYS(1001, "buys"),
    USES(1002, "uses"),
    HITS(1003, "hits"),
    HEALS(1004, "heals"),
    KILLED(1010, "is killed by");

    private final int id;
    private final String keyword;

    MatchEnum(int id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public static MatchEnum findEventType(String record) {
        Optional<MatchEnum> matchingType = Arrays.stream(MatchEnum.values())
                .filter(type -> record.contains(type.getKeyword()))
                .findFirst();
        return matchingType.orElse(MatchEnum.UNKNOWN);
    }

    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }
}
