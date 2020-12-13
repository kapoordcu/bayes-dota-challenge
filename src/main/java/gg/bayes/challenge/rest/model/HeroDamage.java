package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "herodamageentity")
@Data
public class HeroDamage {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long damageId;

    private String target;

    @JsonProperty("damage_instances")
    private Integer damageInstances;

    @JsonProperty("total_damage")
    private Integer totalDamage;

    @JsonIgnore
    @Column(name = "matchId")
    private Long matchId;

    @JsonIgnore
    @Column(name = "hero")
    private String hero;
}
