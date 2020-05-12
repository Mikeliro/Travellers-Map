package net.dark_roleplay.travellers_map.listeners;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.objects.huds.minimap.MinimapHUD;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT)
public class HudListener {

	@SubscribeEvent
	public static void hudPreDraw(RenderGameOverlayEvent.Pre event){
		if(event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS){
			RenderSystem.pushMatrix();
			RenderSystem.translatef(-65, 0, 0);
		}
	}

	@SubscribeEvent
	public static void hudDraw(RenderGameOverlayEvent.Post event){
		if(event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS){
			RenderSystem.popMatrix();
		}
		if(event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

		MinimapHUD hud = MinimapHUD.INSTANCE;
		hud.setSize(event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
		hud.render(0, 0, event.getPartialTicks());
	}
}
