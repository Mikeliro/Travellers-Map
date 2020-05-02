package net.dark_roleplay.travellers_map.util;

import net.minecraft.world.chunk.IChunk;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager {

    private static UUID WORLD_UUID = null;
    private static File WORLD_FOLDER = null;

    private static Map<String, MapSegment> MAPS = new ConcurrentHashMap<>();

    public static void setWorldUUID(UUID uuid){
        WORLD_UUID = uuid;
        WORLD_FOLDER = IOHandler.getOrCreateUniqueFolder(uuid);
        MAPS.clear();
    }

    public static MapSegment getOrCreateMapSegment(IChunk chunk){
        if(WORLD_FOLDER == null) return null;

        String name = "m_" + (chunk.getPos().x >> 5) + "_" +  (chunk.getPos().z >> 5);
        if(MAPS.containsKey(name)){
            MapSegment segment = MAPS.get(name);
            segment.addListeningChunk(chunk);
            return segment;
        }

        File mapFile = new File(WORLD_FOLDER, name + ".png");
        MapSegment segment = new MapSegment(name, mapFile);
        MAPS.put(name, segment);
        segment.addListeningChunk(chunk);

        return segment;
    }

    public static void performIO(){
        for(MapSegment segment : MAPS.values()){
            segment.writeToFile();
        }
    }
}
