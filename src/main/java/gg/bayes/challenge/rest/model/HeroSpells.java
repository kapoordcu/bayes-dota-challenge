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
@Table(name = "herospells")
@Data
public class HeroSpells {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spellId;

    @JsonIgnore
    @Column(name = "hero")
    private String hero;

    @JsonIgnore
    @Column(name = "target")
    private String target;

    @Column(name = "spell")
    private String spell;

    @Column(name = "casts")
    private Integer casts;

    @JsonIgnore
    @Column(name = "matchId")
    private Long matchId;

    public void setHero(String hero) {
        this.hero = hero;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public void setCasts(Integer casts) {
        this.casts = casts;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
}
