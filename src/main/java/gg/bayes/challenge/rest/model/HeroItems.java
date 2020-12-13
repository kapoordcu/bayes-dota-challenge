package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "heroitems")
@Data
public class HeroItems {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "item")
    private String item;
    @Column(name = "timestamp")
    private Long timestamp;

    @JsonIgnore
    @Column(name = "matchId")
    private Long matchId;
    @JsonIgnore
    private String hero;
    public void setHero(String hero) {
        this.hero = hero;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getHero() {
        return hero;
    }

    public String getItem() {
        return item;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getMatchId() {
        return matchId;
    }
}
