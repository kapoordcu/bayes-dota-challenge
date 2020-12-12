package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "herokills")
@Data
public class HeroKills {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String hero;
    @Column(name = "kills")
    private int kills;
    @JsonIgnore
    @Column(name = "matchId")
    private Long matchId;

    public Long getId() {
        return id;
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
