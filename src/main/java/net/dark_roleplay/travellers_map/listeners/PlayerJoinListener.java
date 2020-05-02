package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.handler.TravellersNetworking;
import net.dark_roleplay.travellers_map.networking.world_uuid.WorldUUIDPacket;
import net.dark_roleplay.travellers_map.objects.data.WorldIdentifierData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerJoinListener {

    @SubscribeEvent
    public static void playerJoinWorld(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof ServerPlayerEntity && event.getWorld() instanceof ServerWorld){
            WorldIdentifierData data = WorldIdentifierData.getWorldIdentifier((ServerWorld) event.getWorld());
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            TravellersNetworking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new WorldUUIDPacket().setWorldUUID(data.getWorldUUID()));
        }
    }
}
