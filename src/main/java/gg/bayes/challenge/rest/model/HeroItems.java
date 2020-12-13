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
    @Column(name = "hero")
    private String hero;
}
