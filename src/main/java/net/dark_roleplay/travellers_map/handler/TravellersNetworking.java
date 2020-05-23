package net.dark_roleplay.travellers_map.handler;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map2.objects.packets.world_uuid.WorldUUIDPacket;
import net.dark_roleplay.travellers_map2.objects.packets.world_uuid.WorldUUIDPacketHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TravellersNetworking {

    public static SimpleChannel CHANNEL;

    public static void initNetworking() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TravellersMap.MODID, "main_channel"))
                .clientAcceptedVersions("1.0"::equals)
                .serverAcceptedVersions("1.0"::equals)
                .networkProtocolVersion(() -> "1.0")
                .simpleChannel();

        registerPackets();
    }

    private static void registerPackets() {
        CHANNEL.registerMessage(0, WorldUUIDPacket.class, WorldUUIDPacketHandler::encode, WorldUUIDPacketHandler::decode, WorldUUIDPacketHandler::onMessage);
    }
}
