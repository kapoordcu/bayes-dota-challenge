package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
@Table(name = "herokills")
@Data
public class HeroKills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long killId;
    private String hero;
    @Column(name = "kills")
    private int kills;
    @JsonIgnore
    @Column(name = "matchId")
    private Long matchId;

    public Long getKillId() {
        return killId;
    }

    public String getHero() {
        return hero;
    }

    public int getKills() {
        return kills;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
}
