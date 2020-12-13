package gg.bayes.challenge.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PropertyConfig {

    @Value("#{'${spring.props.hero-prefix}'.trim().split(',')}: npc_dota_hero_, npc_dota_")
    List<String> heroPrefix;

    @Value("${spring.props.item-prefix: item_}")
    private String itemPrefix;

    public List<String> getHeroPrefix() {
        return heroPrefix;
    }

    public String getItemPrefix() {
        return itemPrefix;
    }
}
