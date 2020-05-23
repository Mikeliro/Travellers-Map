package net.dark_roleplay.travellers_map2.objects.huds;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map2.configs.ClientConfig;
import net.dark_roleplay.travellers_map.mapping.tickets.RenderTicket;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.dark_roleplay.travellers_map2.handler.StyleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class MinimapHUD extends AbstractGui {
	public static final MinimapHUD INSTANCE = new MinimapHUD();

	private float[] zoomLevels = new float[]{2.0F, 1.0F, 0.5F, 0.25F};
	private int currentZoomLevel = 1;

	private int width, height;

	private MinimapHUD() {}

	private final Set<MapSegment> segments = new HashSet<>();

	public void render(int mouseX, int mouseY, float delta) {
		StyleManager.selectStyle(StyleManager.HUD_STYLES.get("Daylight"));
		renderOverlay();
		renderMap();
	}

	private void renderOverlay(){
		HudStyle style = StyleManager.getSelectedMinimapStyle();

		RenderSystem.pushMatrix();
		RenderSystem.scaled(ClientConfig.MINIMAP.SCALE.get(), ClientConfig.MINIMAP.SCALE.get(), 1);

		Minecraft.getInstance().getTextureManager().bindTexture(style.getOverlay());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		BlendBlitHelper.blit(0, 0, style.getWidth(), style.getHeight(), 0, 0, 1, 1, 1, 1);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		Minecraft.getInstance().getTextureManager().bindTexture(style.getMask());
		RenderSystem.enableDepthTest();
		RenderSystem.translatef(0.0F, 0.0F, 950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(4680, 2260, -4680, -2260, 0xFFFFFFFF);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.depthFunc(518);
		blit(0, 0, style.getWidth(), style.getHeight(), 0, 0, 1, 1, 1, 1);
		RenderSystem.depthFunc(515);
		RenderSystem.colorMask(true, true, true, true);

		RenderSystem.popMatrix();
	}

	private void renderMap(){
		HudStyle style = StyleManager.getSelectedMinimapStyle();

		RenderSystem.translatef(style.getWidth()/2, style.getHeight()/2, 0);

		PlayerEntity player = Minecraft.getInstance().player;

		segments.clear();
		getAndDrawMapSegment(player, 0, 0);
		getAndDrawMapSegment(player, -256, 0);
		getAndDrawMapSegment(player, 256, 0);
		getAndDrawMapSegment(player, 0, -256);
		getAndDrawMapSegment(player, 0, 256);
		getAndDrawMapSegment(player, -256, -256);
		getAndDrawMapSegment(player, -256, 256);
		getAndDrawMapSegment(player, 256, -256);
		getAndDrawMapSegment(player, 256, 256);

		//Reset Minimap Mask
		RenderSystem.depthFunc(518);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.translatef(0.0F, 0.0F, 950.0F);
		RenderSystem.depthFunc(515);
		RenderSystem.popMatrix();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private void getAndDrawMapSegment(PlayerEntity player, int offsetX, int offsetZ){
		RenderTicket ticket = RenderTicket.getOrCreateTicket(offsetX, offsetZ);
		MapSegment map = MapManager.getMapSegment(MapSegmentUtil.getSegment(player.getPosition().add(offsetX, 0, offsetZ)));
		if(map != null && !segments.contains(map)){
			map.addTicket(ticket);
			segments.add(map);
			drawMapSegment(map, player.getPositionVec(), offsetX, offsetZ);
		}
	}

	private void drawMapSegment(MapSegment map, Vec3d playerPos, int offsetX, int offsetZ){
		map.getDynTexture().bindTexture();
		map.updadteGPU();

		HudStyle style = StyleManager.getSelectedMinimapStyle();
		double scaledWidth = style.getWidth() * ClientConfig.MINIMAP.SCALE.get();
		double scaledHeight = style.getWidth() * ClientConfig.MINIMAP.SCALE.get();

		//256/64 = 4, needs to be used as size for normal zoom
		int sizeX = (int) (scaledWidth * 4 * zoomLevels[currentZoomLevel]);
		int sizeZ = (int) (scaledHeight * 4 * zoomLevels[currentZoomLevel]);

		//+ has a higher priority than bitshifts
		double offsetToPlayerX = (playerPos.x - ((int)playerPos.x + offsetX >> 9) * 512) * (scaledWidth/128F);
		double offsetToPlayerZ = (playerPos.z - ((int)playerPos.z + offsetZ >> 9) * 512) * (scaledHeight/128F);

		offsetToPlayerX *= zoomLevels[currentZoomLevel];
		offsetToPlayerZ *= zoomLevels[currentZoomLevel];
		BlendBlitHelper.blit(-offsetToPlayerX, -offsetToPlayerZ, sizeX, sizeZ, 0, 0, 1, 1, 1, 1);
	}

	private void drawPlayerMarker(int centerX, int centerY){
		hLine(centerX -3, centerX +1, centerY, 0xFFFFFFFF);
		vLine(centerX - 1, centerY -3, centerY +3, 0xFFFFFFFF);
	}


	public static void increaseZoom(){
		INSTANCE.currentZoomLevel = Math.max(0, INSTANCE.currentZoomLevel - 1);
	}

	public static void decreaseZoom(){
		INSTANCE.currentZoomLevel = Math.min(INSTANCE.zoomLevels.length - 1, INSTANCE.currentZoomLevel + 1);
	}
}
