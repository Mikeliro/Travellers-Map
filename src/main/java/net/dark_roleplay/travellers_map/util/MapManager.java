package net.dark_roleplay.travellers_map.util;

import net.dark_roleplay.travellers_map.api.mapping.IMapSegmentTicket;
import net.dark_roleplay.travellers_map.mapping.waypoints.Waypoint;
import net.dark_roleplay.travellers_map2.util.MapFileHelper;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.chunk.IChunk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager {

    public static final List<Waypoint> WAYPOINTS = new ArrayList<>();
    private static Map<Long, MapSegment> MAPS = new ConcurrentHashMap<>();

    public static void saveWaypoint(Waypoint waypoint, boolean isNew){
        File waypointFile = new File(MapFileHelper.getWaypointFolder(), waypoint.uuid.toString() + ".waypoint");
        try {
            CompressedStreamTools.write(waypoint.serializeNBT(), waypointFile);
            if(isNew) WAYPOINTS.add(waypoint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWaypoint(Waypoint waypoint){
        File waypointFile = new File(MapFileHelper.getWaypointFolder(), waypoint.uuid.toString() + ".waypoint");
        waypointFile.delete();
        WAYPOINTS.remove(waypoint);
    }

    public static MapSegment getMapSegment(long ident){
        return MAPS.get(ident);
    }

    public static MapSegment getMapOrTryLoad(long ident){
        MapSegment map = MAPS.get(ident);

        if(map == null){
            int segmentX = (int) (ident >> 32 & 0xFFFFFFFF);
            int segmentZ = (int) (ident & 0xFFFFFFFF);
            String name = "m_" + segmentX + "_" +  segmentZ;
            File mapFile = new File(MapFileHelper.getDimFolder(), name + ".png");
            if(mapFile.exists()) {
                map = new MapSegment(name, mapFile, ident);
                MAPS.put(ident, map);
            }else{
                map = MapSegment.EMPTY;
                MAPS.put(ident, MapSegment.EMPTY);
            }
        }

        return map;
    }

    public static MapSegment getOrCreateMapSegment(IChunk chunk, IMapSegmentTicket ticket){
        if(MapFileHelper.getDimFolder() == null) return null;
        int segmentX = (chunk.getPos().x >> 5);
        int segmentZ = (chunk.getPos().z >> 5);

        long segmentIdentifier = MapSegmentUtil.getSegment(chunk);

        MapSegment segment = MAPS.get(segmentIdentifier);
        if(segment == null || segment.isEmpty()){
            String name = "m_" + segmentX + "_" +  segmentZ;
            File mapFile = new File(MapFileHelper.getDimFolder(), name + ".png");
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
