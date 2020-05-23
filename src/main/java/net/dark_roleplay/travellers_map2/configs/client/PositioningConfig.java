package net.dark_roleplay.travellers_map2.configs.client;

import net.dark_roleplay.travellers_map2.objects.huds.GuiAlignment;
import net.minecraftforge.common.ForgeConfigSpec;

public class PositioningConfig {

	public ForgeConfigSpec.EnumValue<GuiAlignment> ALIGNMENT;
	public ForgeConfigSpec.IntValue POS_X;
	public ForgeConfigSpec.IntValue POS_Y;
	public ForgeConfigSpec.DoubleValue SCALE;

	public PositioningConfig(ForgeConfigSpec.Builder builder, String name, int posX, int posY, GuiAlignment alignemnt){
		ALIGNMENT = builder.comment("Alignment of the " + name)
				.defineEnum("alignment", alignemnt);
		POS_X = builder.comment("X Position of the " + name)
				.defineInRange("posX", posX, -500, 500);
		POS_Y = builder.comment("Y Position of the " + name)
				.defineInRange("posY", posY, -500, 500);
		SCALE = builder.comment("Scale of the " + name)
				.defineInRange("Scale", 1.0, -0.5, 2.0);
	}
}
