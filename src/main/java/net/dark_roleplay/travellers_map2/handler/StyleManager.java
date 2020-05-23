package net.dark_roleplay.travellers_map2.handler;

import net.dark_roleplay.travellers_map2.objects.huds.HudStyle;

import java.util.HashMap;
import java.util.Map;

public class StyleManager {

	public static final Map<String, HudStyle> HUD_STYLES = new HashMap<>();

	private static HudStyle SELECTED_MINIMAP_STYLE = null;
	private static HudStyle FALLBACK_MINIMAP_STYLE = null;

	public static void initalizeStyles(){
		HUD_STYLES.clear();
		HudStyle DefaultStyle = new HudStyle("Default", 64, 64, "travellers_map:textures/styles/minimap/default_mask.png", "travellers_map:textures/styles/minimap/default_overlay.png");
		HUD_STYLES.put("Default", DefaultStyle);
		FALLBACK_MINIMAP_STYLE = DefaultStyle;
	}

	public static HudStyle getSelectedMinimapStyle(){
		return SELECTED_MINIMAP_STYLE == null ? FALLBACK_MINIMAP_STYLE : SELECTED_MINIMAP_STYLE;
	}

	public static void selectStyle(HudStyle style){
		SELECTED_MINIMAP_STYLE = style;
	}
}
