package net.dark_roleplay.travellers_map.configs.client;

import net.dark_roleplay.travellers_map.util.GuiAlignment;
import net.minecraftforge.common.ForgeConfigSpec;

public class PositioningConfig {

	public ForgeConfigSpec.EnumValue<GuiAlignment> ALIGNMENT;
	public ForgeConfigSpec.IntValue POS_X;
	public ForgeConfigSpec.IntValue POS_Y;
	public ForgeConfigSpec.IntValue WIDTH;
	public ForgeConfigSpec.IntValue HEIGHT;

	public PositioningConfig(ForgeConfigSpec.Builder builder, String name, int posX, int posY, int width, int height, GuiAlignment alignemnt){
		ALIGNMENT = builder.comment("Alignment of the " + name)
				.defineEnum("alignment", alignemnt);
		POS_X = builder.comment("X Position of the " + name)
				.defineInRange("posX", posX, -500, 500);
		POS_Y = builder.comment("Y Position of the " + name)
				.defineInRange("posY", posY, -500, 500);
		WIDTH = builder.comment("Width of the " + name)
				.defineInRange("width", width, 16, 256);
		HEIGHT = builder.comment("Height of the " + name)
				.defineInRange("Height", height, 16, 256);
	}
}
