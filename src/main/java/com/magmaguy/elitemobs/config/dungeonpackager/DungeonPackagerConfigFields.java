package com.magmaguy.elitemobs.config.dungeonpackager;

import com.magmaguy.elitemobs.ChatColorConverter;
import com.magmaguy.elitemobs.config.ConfigurationEngine;
import com.magmaguy.elitemobs.utils.ConfigurationLocation;
import com.magmaguy.elitemobs.utils.WarningMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class DungeonPackagerConfigFields {

    public enum DungeonLocationType {
        WORLD,
        SCHEMATIC
    }

    public enum DungeonSizeCategory {
        LAIR,
        MINIDUNGEON,
        DUNGEON,
        RAID
    }

    private final String fileName;
    private boolean isEnabled;
    private final String name;
    private FileConfiguration fileConfiguration;
    private DungeonLocationType dungeonLocationType;
    private final List<String> customInfo;
    private final List<String> relativeBossLocations;
    private final List<String> relativeTreasureChestLocations;
    private final String downloadLink;
    private DungeonSizeCategory dungeonSizeCategory;
    private String worldName;
    private final String schematicName;
    private World.Environment environment;
    private final Boolean protect;
    private File file;
    private Location anchorPoint;
    private double rotation;

    public DungeonPackagerConfigFields(String fileName,
                                       boolean isEnabled,
                                       String name,
                                       DungeonLocationType dungeonLocationType,
                                       List<String> customInfo,
                                       List<String> relativeBossLocations,
                                       List<String> relativeTreasureChestLocations,
                                       String downloadLink,
                                       DungeonSizeCategory dungeonSizeCategory,
                                       String worldName,
                                       String schematicName,
                                       World.Environment environment,
                                       Boolean protect) {
        this.fileName = fileName + ".yml";
        this.isEnabled = isEnabled;
        this.name = name;
        this.dungeonLocationType = dungeonLocationType;
        this.customInfo = customInfo;
        this.relativeBossLocations = relativeBossLocations;
        this.relativeTreasureChestLocations = relativeTreasureChestLocations;
        this.downloadLink = downloadLink;
        this.dungeonSizeCategory = dungeonSizeCategory;
        this.worldName = worldName;
        this.schematicName = schematicName;
        this.worldName = worldName;
        this.environment = environment;
        this.protect = protect;
    }

    public void generateConfigDefaults(FileConfiguration fileConfiguration) {
        fileConfiguration.addDefault("isEnabled", isEnabled);
        fileConfiguration.addDefault("name", name);
        fileConfiguration.addDefault("dungeonLocationType", dungeonLocationType.toString());
        fileConfiguration.addDefault("customInfo", customInfo);
        fileConfiguration.addDefault("relativeBossLocations", relativeBossLocations);
        fileConfiguration.addDefault("relativeTreasureLocations", relativeTreasureChestLocations);
        fileConfiguration.addDefault("downloadLink", downloadLink);
        fileConfiguration.addDefault("dungeonSizeCategory", dungeonSizeCategory.toString());
        fileConfiguration.addDefault("worldName", worldName);
        fileConfiguration.addDefault("schematicName", schematicName);
        if (environment != null)
            fileConfiguration.addDefault("environment", environment.toString());
        if (protect != null)
            fileConfiguration.addDefault("protect", protect);
        fileConfiguration.addDefault("rotation", 0);
    }

    public DungeonPackagerConfigFields(FileConfiguration fileConfiguration, File file) {
        this.file = file;
        this.fileName = file.getName();
        this.fileConfiguration = fileConfiguration;
        this.isEnabled = fileConfiguration.getBoolean("isEnabled");
        this.name = fileConfiguration.getString("name");
        try {
            this.dungeonLocationType = DungeonLocationType.valueOf(fileConfiguration.getString("dungeonLocationType"));
        } catch (Exception exception) {
            new WarningMessage("File " + fileName + " does not have a valid dungeonLocationType!");
        }
        this.customInfo = fileConfiguration.getStringList("customInfo");
        this.relativeBossLocations = fileConfiguration.getStringList("relativeBossLocations");
        this.relativeTreasureChestLocations = fileConfiguration.getStringList("relativeTreasureChestLocations");
        this.downloadLink = fileConfiguration.getString("downloadLink");
        try {
            this.dungeonSizeCategory = DungeonSizeCategory.valueOf(fileConfiguration.getString("dungeonSizeCategory"));
        } catch (Exception exception) {
            new WarningMessage("File " + fileName + " does not have a valid dungeonSizeCategory!");
        }
        this.worldName = fileConfiguration.getString("worldName");
        this.schematicName = fileConfiguration.getString("schematicName");
        if (fileConfiguration.contains("environment"))
            this.environment = World.Environment.valueOf(fileConfiguration.getString("environment"));
        this.protect = fileConfiguration.getBoolean("protect");
        if (fileConfiguration.contains("anchorPoint"))
            this.anchorPoint = ConfigurationLocation.deserialize(fileConfiguration.getString("anchorPoint"));
        if (fileConfiguration.contains("rotation"))
            this.rotation = fileConfiguration.getDouble("rotation");
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        ConfigurationEngine.writeValue(isEnabled, file, fileConfiguration, "isEnabled");
    }

    public void setEnabled(boolean isEnabled, Location anchorPoint) {
        setEnabled(isEnabled);
        if (isEnabled)
            setAnchorPoint(anchorPoint);
        else
            removeAnchorPoint();
    }

    public String getName() {
        return ChatColorConverter.convert(name);
    }

    public DungeonLocationType getDungeonLocationType() {
        return this.dungeonLocationType;
    }

    public List<String> getCustomInfo() {
        return ChatColorConverter.convert(customInfo);
    }

    public List<String> getRelativeBossLocations() {
        return relativeBossLocations;
    }

    public List<String> getRelativeTreasureChestLocations() {
        return relativeTreasureChestLocations;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public DungeonSizeCategory getDungeonSizeCategory() {
        return dungeonSizeCategory;
    }

    public String getWorldName() {
        return ChatColorConverter.convert(worldName);
    }

    public String getSchematicName() {
        return schematicName;
    }

    public World.Environment getEnvironment() {
        return this.environment;
    }

    public boolean getProtect() {
        return this.protect != null && this.protect;
    }

    public Location getAnchorPoint() {
        return this.anchorPoint;
    }

    private void setAnchorPoint(Location location) {
        this.anchorPoint = location;
        ConfigurationEngine.writeValue(ConfigurationLocation.serialize(location), file, fileConfiguration, "anchorPoint");
    }

    private void removeAnchorPoint() {
        ConfigurationEngine.removeValue(file, fileConfiguration, "anchorPoint");
        this.anchorPoint = null;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        ConfigurationEngine.writeValue(rotation, file, fileConfiguration, "rotation");
    }

}
