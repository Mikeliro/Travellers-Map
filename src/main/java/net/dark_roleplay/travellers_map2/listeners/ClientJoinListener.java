package net.dark_roleplay.travellers_map2.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.handler.TravellersNetworking;
import net.dark_roleplay.travellers_map2.objects.packets.world_uuid.WorldUUIDPacket;
import net.dark_roleplay.travellers_map2.objects.world_data.WorldIdentifierData;
import net.dark_roleplay.travellers_map2.util.MapFileHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

import java.net.InetSocketAddress;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientJoinListener {

	@SubscribeEvent
	public static void joinServerListener(ClientPlayerNetworkEvent.LoggedInEvent event){
		//SocketAddress socket = event.getNetworkManager().getNetHandler().

		if(event.getNetworkManager().getRemoteAddress() instanceof InetSocketAddress){
			InetSocketAddress socket = (InetSocketAddress) event.getNetworkManager().getRemoteAddress();

			MapFileHelper.joinServer(socket);
		}
	}

	@SubscribeEvent
	public static void leaveServerListener(ClientPlayerNetworkEvent.LoggedOutEvent event){
		MapFileHelper.leaveServer();
	}

	@SubscribeEvent
	public static void joinWorldListener(EntityJoinWorldEvent event){
		if(!event.getWorld().isRemote()) return;
		if(event.getEntity() instanceof PlayerEntity){
			RegistryKey<DimensionType> key = event.getWorld().func_234922_V_();
			MapFileHelper.joinDimension(key.func_240901_a_());
		}
	}
}
