package net.dark_roleplay.travellers_map.objects.color_palettes;

public abstract class ColorPalette {

	public final int getRGBA(int colorIndex){
		return getRGBA(colorIndex, 1);
	}

	public abstract int getRGBA(int colorIndex, int shading);

}
