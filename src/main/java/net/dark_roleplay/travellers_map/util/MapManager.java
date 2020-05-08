package net.dark_roleplay.travellers_map.util;

import net.dark_roleplay.travellers_map.objects.data.IMapSegmentTicket;
import net.minecraft.world.chunk.IChunk;

import java.io.File;
import java.net.SocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager {

    private static UUID WORLD_UUID = null;
    private static File WORLD_FOLDER = null;

    private static Map<Long, MapSegment> MAPS = new ConcurrentHashMap<>();

    public static void setUpWorldUUIDForRemote(SocketAddress remote){
        //remote.
    }

    public static void setWorldUUID(UUID uuid){
        WORLD_UUID = uuid;
        WORLD_FOLDER = IOHandler.getOrCreateUniqueFolder(uuid);
        MAPS.clear();
    }

    public static MapSegment getMapSegment(long ident){
        return MAPS.get(ident);
    }

    public static MapSegment getOrCreateMapSegment(IChunk chunk, IMapSegmentTicket ticket){
        if(WORLD_FOLDER == null) return null;
        int segmentX = (chunk.getPos().x >> 5);
        int segmentZ = (chunk.getPos().z >> 5);

        long segmentIdentifier = MapSegmentUtil.getSegment(chunk);

        MapSegment segment = MAPS.get(segmentIdentifier);
        if(segment == null){
            String name = "m_" + segmentX + "_" +  segmentZ;
            File mapFile = new File(WORLD_FOLDER, name + ".png");
            segment = new MapSegment(name, mapFile, segmentIdentifier);
            MAPS.put(segmentIdentifier, segment);
        }

        segment.addTicket(ticket);
        return segment;
    }

    public static void performUpdates(){
        for(MapSegment segment : MAPS.values()){
            segment.update();
        }
    }

    public static void freeMapSegment(long ident){
        if(MAPS.containsKey(ident))
            MAPS.remove(ident);
    }
}
