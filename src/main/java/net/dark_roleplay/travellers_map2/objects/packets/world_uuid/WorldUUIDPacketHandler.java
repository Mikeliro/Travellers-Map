package net.dark_roleplay.travellers_map2.objects.packets.world_uuid;

import net.dark_roleplay.travellers_map.util.MapManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WorldUUIDPacketHandler {
    public static void encode(WorldUUIDPacket packet, PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(packet.getWorldUUID());
    }

    public static WorldUUIDPacket decode(PacketBuffer buffer) {
        WorldUUIDPacket packet = new WorldUUIDPacket();
        return packet.setWorldUUID(buffer.readUniqueId());
    }

    public static void onMessage(WorldUUIDPacket packet, Supplier<NetworkEvent.Context> supplier) {
        MapManager.setWorldUUID(packet.getWorldUUID());
    }
}
