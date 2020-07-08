package net.dark_roleplay.travellers_map2.objects.huds.minimap;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.mapping.tickets.RenderTicket;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.dark_roleplay.travellers_map2.configs.ClientConfig;
import net.dark_roleplay.travellers_map2.objects.huds.hud.Hud;
import net.dark_roleplay.travellers_map2.objects.huds.hud.HudStyle;
import net.dark_roleplay.travellers_map2.objects.screens.full_map.FullMapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashSet;
import java.util.Set;

public class MinimapHUD extends Hud {
	public static final MinimapHUD INSTANCE = new MinimapHUD();

	private float[] zoomLevels = new float[]{2.0F, 1.0F, 0.5F, 0.25F};
	private int currentZoomLevel = 1;

	private int width, height;

	private MinimapHUD() {
		super(ClientConfig.MINIMAP, "hud." + TravellersMap.MODID + ".minimap" ,
				new HudStyle("Default", 64, 64, "travellers_map:textures/styles/minimap/default_mask.png", "travellers_map:textures/styles/minimap/default_overlay.png"));
	}

	private final Set<MapSegment> segments = new HashSet<>();

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float delta) {
		renderOverlay(matrix);
		renderMap(matrix);

		HudStyle style = getStyle();
		RenderSystem.pushMatrix();
		RenderSystem.scaled(ClientConfig.MINIMAP.SCALE.get(), ClientConfig.MINIMAP.SCALE.get(), 1);
		RenderSystem.translatef(style.getWidth()/2F, style.getHeight()/2F, 0);
		Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
		float zoom = zoomLevels[currentZoomLevel]/2;
		RenderSystem.scalef(zoom, zoom, zoom);
		if(!ClientConfig.SPIN_MINIMAP.get())
			RenderSystem.rotatef(Minecraft.getInstance().player.getYaw(delta) - 180, 0, 0, 1);
		blit(matrix, -2, -4, 158, 0, 5, 7);
		RenderSystem.popMatrix();
	}

	private void renderOverlay(MatrixStack matrix){
		HudStyle style = getStyle();

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
		fill(matrix, 4680, 2260, -4680, -2260, 0xFFFFFFFF);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.depthFunc(518);
		blit(matrix, 0, 0, style.getWidth(), style.getHeight(), 0, 0, 1, 1, 1, 1);
		RenderSystem.depthFunc(515);
		RenderSystem.colorMask(true, true, true, true);
	}

	private void renderMap(MatrixStack matrix){
		HudStyle style = getStyle();

		RenderSystem.translatef(style.getWidth()/2, style.getHeight()/2, 0);

		PlayerEntity player = Minecraft.getInstance().player;

		RenderSystem.pushMatrix();
		if(ClientConfig.SPIN_MINIMAP.get()){
			RenderSystem.rotatef(-(player.getYaw(0) + 180), 0, 0, 1);
		}

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
		RenderSystem.popMatrix();

		//Reset Minimap Mask
		RenderSystem.depthFunc(518);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(matrix, 4680, 2260, -4680, -2260, -16777216);
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
		MapSegment map = MapManager.getMapSegment(MapSegmentUtil.getSegment(player.func_233580_cy_().add(offsetX, 0, offsetZ)));//Player#getPosition -> BlockPos
		if(map != null && !map.isEmpty() && !segments.contains(map)){
			map.addTicket(ticket);
			segments.add(map);
			drawMapSegment(map, player.getPositionVec(), offsetX, offsetZ);
		}
	}

	private void drawMapSegment(MapSegment map, Vector3d playerPos, int offsetX, int offsetZ){
		map.getDynTexture().bindTexture();
		map.updadteGPU();

		HudStyle style = getStyle();

		//256/64 = 4, needs to be used as size for normal zoom
		int sizeX = (int) (style.getWidth() * 4 * zoomLevels[currentZoomLevel]);
		int sizeZ = (int) (style.getWidth() * 4 * zoomLevels[currentZoomLevel]);

		//+ has a higher priority than bitshifts
		double offsetToPlayerX = (playerPos.x - ((int)playerPos.x + offsetX >> 9) * 512) * (style.getWidth()/128F);
		double offsetToPlayerZ = (playerPos.z - ((int)playerPos.z + offsetZ >> 9) * 512) * (style.getWidth()/128F);

		offsetToPlayerX *= zoomLevels[currentZoomLevel];
		offsetToPlayerZ *= zoomLevels[currentZoomLevel];
		BlendBlitHelper.blit(-offsetToPlayerX, -offsetToPlayerZ, sizeX, sizeZ, 0, 0, 1, 1, 1, 1);
	}

	public static void increaseZoom(){
		INSTANCE.currentZoomLevel = Math.max(0, INSTANCE.currentZoomLevel - 1);
	}

	public static void decreaseZoom(){
		INSTANCE.currentZoomLevel = Math.min(INSTANCE.zoomLevels.length - 1, INSTANCE.currentZoomLevel + 1);
	}
}
