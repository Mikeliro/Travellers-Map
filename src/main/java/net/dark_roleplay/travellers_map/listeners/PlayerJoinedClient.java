package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerJoinedClient {

	@SubscribeEvent
	public void playerJoinWorld(EntityJoinWorldEvent event) {
		//MapManager.setUpWorldUUIDForRemote();
		System.out.println();
	}
}
