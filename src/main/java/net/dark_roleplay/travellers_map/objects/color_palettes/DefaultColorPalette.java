package net.dark_roleplay.travellers_map.objects.color_palettes;

import net.minecraft.block.material.MaterialColor;

public class DefaultColorPalette extends ColorPalette {

	private int[][] colors;

	public DefaultColorPalette() {
		colors = new int[MaterialColor.COLORS.length][];

		for (int i = 0; i < MaterialColor.COLORS.length; i++) {
			colors[i] = new int[4];

			for (int j = 0; j < 4; j++) {
				if (MaterialColor.COLORS[i] != null)
					colors[i][j] = MaterialColor.COLORS[i].getMapColor(j);
			}
		}
	}

	@Override
	public int getRGBA(int colorIndex, int shading) {
		if(colorIndex >= colors.length || shading > 3) return 0;
		return colors[colorIndex][shading];
	}
}
