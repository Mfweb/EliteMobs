package com.magmaguy.elitemobs.mobconstructor.mobdata.aggressivemobs;

import com.magmaguy.elitemobs.config.mobproperties.MobPropertiesConfig;
import org.bukkit.entity.EntityType;

public class EliteIllusioner extends EliteMobProperties {
    public EliteIllusioner() {
        this.name = MobPropertiesConfig.getMobProperties().get(EntityType.ILLUSIONER).getName();
        this.entityType = EntityType.ILLUSIONER;
        this.defaultMaxHealth = 32;
        this.isEnabled = MobPropertiesConfig.getMobProperties().get(EntityType.ILLUSIONER).isEnabled();
        if (this.isEnabled)
            eliteMobData.add(this);
    }
}