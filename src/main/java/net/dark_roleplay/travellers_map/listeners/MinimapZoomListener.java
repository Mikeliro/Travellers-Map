package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.handler.TravellersKeybinds;
import net.dark_roleplay.travellers_map.objects.huds.minimap.MinimapHUD;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT)
public class MinimapZoomListener {

	@SubscribeEvent
	public static void mouseScroll(InputEvent.MouseScrollEvent event){
		if(TravellersKeybinds.ZOOM.isKeyDown()){
			double scroll = event.getScrollDelta();
			if(scroll > 0){
				MinimapHUD.increaseZoom();
			}else if(scroll < 0){
				MinimapHUD.decreaseZoom();
			}
			event.setCanceled(true);
		}
	}
}
