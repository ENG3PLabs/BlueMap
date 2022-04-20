/*
 * This file is part of BlueMap, licensed under the MIT License (MIT).
 *
 * Copyright (c) Blue (Lukas Rieger) <https://bluecolored.de>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.bluecolored.bluemap.common.web;

import com.flowpowered.math.vector.Vector2i;
import de.bluecolored.bluemap.common.config.MapConfig;
import de.bluecolored.bluemap.core.map.BmMap;
import de.bluecolored.bluemap.core.util.ConfigUtils;
import de.bluecolored.bluemap.core.util.math.Color;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

public class WebSettings {

    private final ConfigurationLoader<? extends ConfigurationNode> configLoader;
    private ConfigurationNode rootNode;

    public WebSettings(Path settingsFile) throws IOException {
        if (!Files.exists(settingsFile)) Files.createFile(settingsFile);

        configLoader = GsonConfigurationLoader.builder()
                .path(settingsFile)
                .build();

        load();
    }

    public void load() throws IOException {
        rootNode = configLoader.load();
    }

    public void save() throws IOException {
        configLoader.save(rootNode);
    }

    public void set(Object value, Object... path) throws SerializationException {
        rootNode.node(path).set(value);
    }

    public Object get(Object... path) {
        return rootNode.node(path).raw();
    }

    public String getString(Object... path) {
        return rootNode.node(path).getString();
    }

    public int getInt(Object... path) {
        return rootNode.node(path).getInt();
    }

    public long getLong(Object... path) {
        return rootNode.node(path).getLong();
    }

    public float getFloat(Object... path) {
        return rootNode.node(path).getFloat();
    }

    public double getDouble(Object... path) {
        return rootNode.node(path).getDouble();
    }

    public Collection<String> getMapIds() {
        return rootNode.node("maps").childrenMap().keySet().stream().map(Object::toString).collect(Collectors.toSet());
    }

    public void setAllMapsEnabled(boolean enabled) throws SerializationException {
        for (ConfigurationNode mapNode : rootNode.node("maps").childrenMap().values()) {
            mapNode.node("enabled").set(enabled);
        }
    }

    public void setMapEnabled(boolean enabled, String mapId) throws SerializationException {
        set(enabled, "maps", mapId, "enabled");
    }

    public void setFrom(BmMap map) throws SerializationException {
        Vector2i hiresTileSize = map.getHiresModelManager().getTileGrid().getGridSize();
        Vector2i gridOrigin = map.getHiresModelManager().getTileGrid().getOffset();
        Vector2i lowresTileSize = map.getLowresModelManager().getTileSize();
        Vector2i lowresPointsPerHiresTile = map.getLowresModelManager().getPointsPerHiresTile();

        set(hiresTileSize.getX(), "maps", map.getId(), "hires", "tileSize", "x");
        set(hiresTileSize.getY(), "maps", map.getId(), "hires", "tileSize", "z");
        set(1, "maps", map.getId(), "hires", "scale", "x");
        set(1, "maps", map.getId(), "hires", "scale", "z");
        set(gridOrigin.getX(), "maps", map.getId(), "hires", "translate", "x");
        set(gridOrigin.getY(), "maps", map.getId(), "hires", "translate", "z");

        Vector2i pointSize = hiresTileSize.div(lowresPointsPerHiresTile);
        Vector2i tileSize = pointSize.mul(lowresTileSize);

        set(tileSize.getX(), "maps", map.getId(), "lowres", "tileSize", "x");
        set(tileSize.getY(), "maps", map.getId(), "lowres", "tileSize", "z");
        set(pointSize.getX(), "maps", map.getId(), "lowres", "scale", "x");
        set(pointSize.getY(), "maps", map.getId(), "lowres", "scale", "z");
        set(pointSize.getX() / 2, "maps", map.getId(), "lowres", "translate", "x");
        set(pointSize.getY() / 2, "maps", map.getId(), "lowres", "translate", "z");

        set(map.getWorld().getSpawnPoint().getX(), "maps", map.getId(), "startPos", "x");
        set(map.getWorld().getSpawnPoint().getZ(), "maps", map.getId(), "startPos", "z");
        set(map.getWorldId(), "maps", map.getId(), "world");
    }

    public void setFrom(MapConfig mapConfig, String mapId) throws SerializationException {
        Vector2i startPos = mapConfig.getStartPos();
        if (startPos != null) {
            set(startPos.getX(), "maps", mapId, "startPos", "x");
            set(startPos.getY(), "maps", mapId, "startPos", "z");
        }

        Color skyColor = new Color().set(ConfigUtils.parseColorFromString(mapConfig.getSkyColor()));
        set(skyColor.r, "maps", mapId, "skyColor", "r");
        set(skyColor.g, "maps", mapId, "skyColor", "g");
        set(skyColor.b, "maps", mapId, "skyColor", "b");

        set(mapConfig.getAmbientLight(), "maps", mapId, "ambientLight");

        setName(mapConfig.getName(), mapId);
    }

    public void setOrdinal(int ordinal, String mapId) throws SerializationException {
        set(ordinal, "maps", mapId, "ordinal");
    }

    public int getOrdinal(String mapId) {
        return getInt("maps", mapId, "ordinal");
    }

    public void setName(String name, String mapId) throws SerializationException {
        set(name, "maps", mapId, "name");
    }

    public String getName(String mapId) {
        return getString("maps", mapId, "name");
    }

}
