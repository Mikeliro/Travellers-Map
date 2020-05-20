package net.dark_roleplay.travellers_map.util;

import net.dark_roleplay.travellers_map.features.MappingHelper;
import net.dark_roleplay.travellers_map.features.waypoints.Waypoint;
import net.dark_roleplay.travellers_map.objects.data.IMapSegmentTicket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.chunk.IChunk;

import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapManager {

    private static UUID WORLD_UUID = null;
    private static File WORLD_FOLDER = null;
    private static File DIMENSION_FOLDER = null;
    private static File WAYPOINT_WORLD_FOLDER = null;

    public static final List<Waypoint> WAYPOINTS = new ArrayList<>();
    private static Map<Long, MapSegment> MAPS = new ConcurrentHashMap<>();

    public static void setupMappingFolders(){
        if(Minecraft.getInstance().isIntegratedServerRunning()){
            String name = Minecraft.getInstance().getIntegratedServer().getWorldName();
            System.out.println("Test");
        }else{
            SocketAddress adress = Minecraft.getInstance().player.connection.getNetworkManager().getRemoteAddress();
            String name = adress.toString();
            System.out.println("Test");
        }
    }

    public static void setWorldUUID(UUID uuid){

        setupMappingFolders();
        WORLD_UUID = uuid;
        WORLD_FOLDER = IOHandler.getOrCreateUniqueFolder(uuid);
        WAYPOINT_WORLD_FOLDER = new File(WORLD_FOLDER, "waypoints");
        WAYPOINT_WORLD_FOLDER.mkdirs();
        MAPS.clear();
        WAYPOINTS.clear();

        File[] waypointFiles = WAYPOINT_WORLD_FOLDER.listFiles((dir, name) -> name.endsWith(".waypoint"));
        for(File file : waypointFiles){
            try {
                CompoundNBT nbt = CompressedStreamTools.read(file);
                Waypoint waypoint = new Waypoint(UUID.fromString(file.getName().replace(".waypoint", "")));
                waypoint.deserializeNBT(nbt);
                WAYPOINTS.add(waypoint);
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Unable to read Waypoint File '" + file.getName() + "' skipping it");
            }
        }

        MappingHelper.initMapper();
    }

    public static void saveWaypoint(Waypoint waypoint, boolean isNew){
        File waypointFile = new File(WAYPOINT_WORLD_FOLDER, waypoint.uuid.toString() + ".waypoint");
        try {
            CompressedStreamTools.write(waypoint.serializeNBT(), waypointFile);
            if(isNew) WAYPOINTS.add(waypoint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWaypoint(Waypoint waypoint){
        File waypointFile = new File(WAYPOINT_WORLD_FOLDER, waypoint.uuid.toString() + ".waypoint");
        waypointFile.delete();
        WAYPOINTS.remove(waypoint);
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
