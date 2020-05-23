package net.dark_roleplay.travellers_map2.listeners;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map2.configs.ClientConfig;
import net.dark_roleplay.travellers_map2.objects.huds.MinimapHUD;
import net.dark_roleplay.travellers_map.handler.TravellersKeybinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT)
public class MinimapHudListeners {

	@SubscribeEvent
	public static void hudDraw(RenderGameOverlayEvent.Post event){
		if(event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

		RenderSystem.pushMatrix();
		int posX = ClientConfig.MINIMAP.ALIGNMENT.get().getX(event.getWindow().getScaledWidth()) + ClientConfig.MINIMAP.POS_X.get();
		int posY = ClientConfig.MINIMAP.ALIGNMENT.get().getY(event.getWindow().getScaledHeight()) + ClientConfig.MINIMAP.POS_Y.get();
		RenderSystem.translatef(posX, posY, 0);

		MinimapHUD hud = MinimapHUD.INSTANCE;
		hud.setSize(event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
		hud.render(0, 0, event.getPartialTicks());

		RenderSystem.popMatrix();

//		CompassHud compassHud = CompassHud.INSTANCE;
//		compassHud.setWindowSize(event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
//		compassHud.render(0, 0, event.getPartialTicks());
	}

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
