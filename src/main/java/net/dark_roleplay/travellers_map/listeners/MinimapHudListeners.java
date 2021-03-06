package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.objects.huds.compass.CompassHud;
import net.dark_roleplay.travellers_map.objects.huds.hud.HudHelper;
import net.dark_roleplay.travellers_map.objects.huds.minimap.MinimapHUD;
import net.dark_roleplay.travellers_map.handler.TravellersKeybinds;
import net.dark_roleplay.travellers_map.objects.screens.minimap.settings.MinimapSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT)
public class MinimapHudListeners {

	private static HudHelper[] HUDS = new HudHelper[]{
			new HudHelper(
					MinimapHUD.INSTANCE,
					MinimapHudListeners::hideWhenDebug,
					v -> !(Minecraft.getInstance().currentScreen instanceof MinimapSettingsScreen)
			),
			new HudHelper(
					CompassHud.INSTANCE,
					MinimapHudListeners::hideWhenDebug,
					v -> !Minecraft.getInstance().ingameGUI.getTabList().visible
			)
	};

	@SubscribeEvent
	public static void hudDraw(RenderGameOverlayEvent.Post event){
		if(event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
		if (Minecraft.getInstance().gameSettings.hideGUI) return;

		int width = event.getWindow().getScaledWidth();
		int height = event.getWindow().getScaledHeight();
		float partialTicks = event.getPartialTicks();

		for(HudHelper helper : HUDS)
			helper.render(event.getMatrixStack(), width, height, partialTicks);
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

	private static boolean hideWhenDebug(Void v){
		return !Minecraft.getInstance().gameSettings.showDebugInfo;
	}
}
