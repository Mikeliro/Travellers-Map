package net.dark_roleplay.travellers_map2.objects.huds.compass;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.mapping.waypoints.Waypoint;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map2.configs.ClientConfig;
import net.dark_roleplay.travellers_map2.objects.huds.hud.Hud;
import net.dark_roleplay.travellers_map2.objects.huds.hud.HudStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CompassHud extends Hud {
	public static final CompassHud INSTANCE = new CompassHud();

	private static ResourceLocation WAYPOINT_ICONS = new ResourceLocation(TravellersMap.MODID, "textures/guis/waypoint_icons.png");

	private static final int HALF_WIDTH = 128;

	private CompassHud() {
		super(ClientConfig.COMPASS, "hud." + TravellersMap.MODID + ".compass" ,
				new HudStyle("Default", 256, 16, "travellers_map:textures/styles/compass/default_mask.png", "travellers_map:textures/styles/compass/default_overlay.png"));
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		HudStyle style = getStyle();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();

		Minecraft.getInstance().getTextureManager().bindTexture(style.getOverlay());
		BlendBlitHelper.blit(0, 0, 256, 16, 0, 0, 1, 1, 1, 1);

		resetMarkerRendering(delta);

		drawDirectionMarker(0, "S");
		drawDirectionMarker(90, "W");
		drawDirectionMarker(180, "N");
		drawDirectionMarker(270, "E");
		RenderSystem.disableBlend();

		FontRenderer renderer = Minecraft.getInstance().fontRenderer;

		for(Waypoint waypoint : MapManager.WAYPOINTS){
			if(!waypoint.isVisible()) continue;
			float waypointYaw = getYawForMarker(waypoint.getPos());
			float waypointOffset = ((waypointYaw - playerYaw)/90) * 128;
			waypoint.setLastRenderedData(waypointYaw, waypointOffset);
			if(waypoint.getLastRenderedOffset() < -128 || waypoint.getLastRenderedOffset() > 128) continue;
			drawWaypointMarker(waypoint);
			if(waypoint.getLastRenderedYaw() - 3 < playerYaw && waypoint.getLastRenderedYaw() + 3 > playerYaw)
				waypoint.initNameWidth(renderer);
		}

		for(Waypoint waypoint : MapManager.WAYPOINTS)
			if(waypoint.isVisible() && waypoint.getLastRenderedYaw() - 3 < playerYaw && waypoint.getLastRenderedYaw() + 3 > playerYaw)
				drawWaypointName(renderer, waypoint);
	}

	private float playerYaw;
	private Vec3d playerPos;
	private int renderedNames = 0;

	private void resetMarkerRendering(float delta){
		this.playerYaw = Math.floorMod((int) Minecraft.getInstance().player.getYaw(delta), 360);
		this.playerPos = Minecraft.getInstance().player.getPositionVec();
		this.renderedNames = 0;
		Waypoint.widestNameWidth = 0;
	}

	private Waypoint drawWaypointMarker(Waypoint waypoint){
		//Rudimentary culling;
		RenderSystem.enableAlphaTest();
		Minecraft.getInstance().getTextureManager().bindTexture(WAYPOINT_ICONS);
		float pos = HALF_WIDTH + waypoint.getLastRenderedOffset() - 3.5F;
		BlendBlitHelper.blitColor(pos, 2, 7, 12, 0, 0, 7, 12, 140, 120, waypoint.getColor());

		return waypoint;
	}

	private void drawWaypointName(FontRenderer renderer, Waypoint waypoint){
		fill(HALF_WIDTH - Waypoint.widestNameWidth/2 - 2, 16 + (renderedNames * 10), HALF_WIDTH + Waypoint.widestNameWidth/2 + 2, 26 + (renderedNames * 10), 0xA0333333);
		renderer.drawString(waypoint.getName(), HALF_WIDTH - (renderer.getStringWidth(waypoint.getName()) / 2), 17 + (renderedNames * 10), waypoint.getColor());
		renderedNames ++;
	}

	private void drawDirectionMarker(float markerYaw, String markerName){
		FontRenderer renderer = Minecraft.getInstance().fontRenderer;

		if(markerYaw > 270 && playerYaw < 180) markerYaw -= 360;
		else if(markerYaw < 90 && playerYaw > 180) markerYaw += 360;

		float offset = ((markerYaw - playerYaw)/90) * 128;
		if(offset > -128 && offset < 128){
			float pos = HALF_WIDTH + offset;
			BlendBlitHelper.vLine(pos - 1F, 2, 3.5F, 0xFF888888);
			BlendBlitHelper.vLine(pos - 1F, 12.5F, 14, 0xFF888888);
			renderer.drawString(markerName, pos - (renderer.getStringWidth(markerName) / 2), 4.5F, 0xFF888888);
		}
	}

	private float getYawForMarker(BlockPos pos){
		Vec3d pos2 = new Vec3d(pos.getX() - playerPos.getX(), 0, pos.getZ() - playerPos.getZ());

		float angle = (float) Math.toDegrees(Math.atan2(Math.abs(pos2.getX()), -pos2.getZ()));
		if(pos2.getX() < 0) angle = 360-angle;
		angle =  (angle + 180) % 360;

		//Handle 360° conversion
		if(angle > 270 && playerYaw < 180) angle -= 360;
		else if(angle < 90 && playerYaw > 180) angle += 360;

		return angle;
	}
}