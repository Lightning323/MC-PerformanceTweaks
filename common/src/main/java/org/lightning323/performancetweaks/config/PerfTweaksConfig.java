package org.lightning323.performancetweaks.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerfTweaksConfig {
    @SerializedName("enable_alternate_current_by_default")
    public boolean enableAlternateCurrentByDefault = true;

    /**
     * Despawn config
     */
    @SerializedName("despawning.enable_despawning_optimization")
    public boolean enableLetMeDespawn = true;

    @SerializedName("despawning.mobs_drop_tools")
    public boolean letMeDespawn_dropTools = false;

    @SerializedName("despawning.persistent_mobs")
    private Set<String> persistentMobs = new HashSet<>(List.of("corpse:corpse"));

    public Set<String> persistentMobs() {
        return persistentMobs;
    }

    public void addMobName(String mobName) {
        persistentMobs.add(mobName);
        ConfigManager.saveConfig();
    }

    public void removeMobName(String mobName) {
        persistentMobs.remove(mobName);
        ConfigManager.saveConfig();
    }
}