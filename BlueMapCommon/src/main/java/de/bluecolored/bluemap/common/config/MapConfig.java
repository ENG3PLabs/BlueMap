package de.bluecolored.bluemap.common.config;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3i;
import de.bluecolored.bluemap.core.debug.DebugDump;
import de.bluecolored.bluemap.core.map.MapSettings;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

import java.nio.file.Path;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@DebugDump
@ConfigSerializable
public class MapConfig implements MapSettings {
    private transient Path configFile = null;

    private String name = null;

    @Required
    private Path world = Path.of("world");

    private Vector2i startPos = null;

    private String skyColor = "#7dabff";

    private float ambientLight = 0;

    private int worldSkyLight = 15;

    private int removeCavesBelowY = 55;

    private boolean caveDetectionUsesBlockLight = false;

    private int minX = Integer.MIN_VALUE;
    private int maxX = Integer.MAX_VALUE;
    private int minZ = Integer.MIN_VALUE;
    private int maxZ = Integer.MAX_VALUE;
    private int minY = Integer.MIN_VALUE;
    private int maxY = Integer.MAX_VALUE;

    private boolean renderEdges = true;

    private String storage = "file";

    private boolean ignoreMissingLightData = false;

    // hidden config fields
    private int hiresTileSize = 32;
    private int lowresPointsPerHiresTile = 4;
    private int lowresPointsPerLowresTile = 50;

    public String getName() {
        return name;
    }

    public Path getWorld() {
        return world;
    }

    public Vector2i getStartPos() {
        return startPos;
    }

    public String getSkyColor() {
        return skyColor;
    }

    public float getAmbientLight() {
        return ambientLight;
    }

    public int getWorldSkyLight() {
        return worldSkyLight;
    }

    public int getRemoveCavesBelowY() {
        return removeCavesBelowY;
    }

    public boolean isCaveDetectionUsesBlockLight() {
        return caveDetectionUsesBlockLight;
    }

    public Vector3i getMinPos() {
        return new Vector3i(minX, minY, minZ);
    }

    public Vector3i getMaxPos() {
        return new Vector3i(maxX, maxY, maxZ);
    }

    public boolean isRenderEdges() {
        return renderEdges;
    }

    public String getStorage() {
        return storage;
    }

    public boolean isIgnoreMissingLightData() {
        return ignoreMissingLightData;
    }

    public int getHiresTileSize() {
        return hiresTileSize;
    }

    public int getLowresPointsPerHiresTile() {
        return lowresPointsPerHiresTile;
    }

    public int getLowresPointsPerLowresTile() {
        return lowresPointsPerLowresTile;
    }

}
