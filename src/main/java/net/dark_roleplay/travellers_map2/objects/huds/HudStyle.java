package net.dark_roleplay.travellers_map2.objects.huds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

public class HudStyle {
	@Expose
	@SerializedName("StyleName")
	private String styleName;
	@Expose
	@SerializedName("Mask")
	private String maskLocation;
	@Expose
	@SerializedName("Overlay")
	private String overlayLocation;
	@Expose
	@SerializedName("Width")
	private int width;
	@Expose
	@SerializedName("Height")
	private int height;

	private ResourceLocation mask;
	private ResourceLocation overlay;

	public HudStyle(){}

	public HudStyle(String styleName, int width, int height, String mask, String overlay){
		this.styleName = styleName;
		this.maskLocation = mask;
		this.overlayLocation = overlay;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getStyleName(){
		return this.styleName;
	}

	public ResourceLocation getMask() {
		if(mask == null)
			mask = new ResourceLocation(maskLocation);
		return mask;
	}

	public ResourceLocation getOverlay() {
		if(overlay == null)
			overlay = new ResourceLocation(overlayLocation);
		return overlay;
	}
}
