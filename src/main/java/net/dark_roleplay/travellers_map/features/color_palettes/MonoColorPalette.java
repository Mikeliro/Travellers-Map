package net.dark_roleplay.travellers_map.features.color_palettes;

import net.minecraft.block.material.MaterialColor;

public class MonoColorPalette extends ColorPalette {

	private int[][] colors;

	public MonoColorPalette() {
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
					colors[i][j] = 0xFF000000 | grayColor << 16 | grayColor << 8 | grayColor;
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
