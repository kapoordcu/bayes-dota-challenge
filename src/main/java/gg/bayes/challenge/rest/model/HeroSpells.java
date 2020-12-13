package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "herospells")
@Data
public class HeroSpells {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spellId;

    @JsonIgnore
    private String hero;

    @JsonIgnore
    private String target;

    private String spell;

    private Integer casts;

    @JsonIgnore
    private Long matchId;
}
