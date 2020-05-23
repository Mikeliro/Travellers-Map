package net.dark_roleplay.travellers_map2.configs;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map2.configs.client.PositioningConfig;
import net.dark_roleplay.travellers_map2.objects.huds.GuiAlignment;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {

	public static ForgeConfigSpec CLIENT_SPECS;

	public static PositioningConfig MINIMAP;
	public static PositioningConfig COMPASS;

	static{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.comment("Settings for the Minimap").push("minimap");
		MINIMAP = new PositioningConfig(builder, "Minimap", -65, 1, GuiAlignment.TOP_RIGHT);
		builder.pop();

		builder.comment("Settings for the Compass (Unused right now)").push("compass");
		COMPASS = new PositioningConfig(builder, "Compass", -128, 1, GuiAlignment.TOP);
		builder.pop();

		CLIENT_SPECS = builder.build();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {

	}

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {
	}
}
