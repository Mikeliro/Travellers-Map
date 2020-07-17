package net.dark_roleplay.travellers_map.objects.color_palettes;

import net.minecraft.block.material.MaterialColor;

public class MonoColorPalette extends ColorPalette {

	private int[][] colors;

	public MonoColorPalette(int colorMult) {
		int multR = colorMult >> 16 & 0xFF;
		int multG = colorMult >> 8 & 0xFF;
		int multB = colorMult & 0xFF;
		colors = new int[MaterialColor.COLORS.length][];

		for (int i = 0; i < MaterialColor.COLORS.length; i++) {
			colors[i] = new int[4];

			for (int j = 0; j < 4; j++) {
				if (MaterialColor.COLORS[i] != null){
					int color = MaterialColor.COLORS[i].getMapColor(j);
					float red = color >> 16 & 0xFF;
					float green = color >> 8 & 0xFF;
					float blue = color & 0xFF;

					int grayColor = (int) (0.21 * red + 0.72 * green + 0.07 * blue);
					int redChannel = (grayColor * (multR/256)) << 16 ;
					int greenChannel = (grayColor * (multG/256))  << 8;
					int blueChannel = grayColor * (multB/256);
					colors[i][j] = 0xFF000000 | redChannel | greenChannel | blueChannel;
				}
			}
		}
	}

	@Override
	public int getRGBA(int colorIndex, int shading) {
		if(colorIndex >= colors.length || shading > 3) return 0;
		return colors[colorIndex][shading];
	}
}
