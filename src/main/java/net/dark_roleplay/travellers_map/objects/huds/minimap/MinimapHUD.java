package net.dark_roleplay.travellers_map.objects.huds.minimap;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class MinimapHUD extends AbstractGui {
	public static final MinimapHUD INSTANCE = new MinimapHUD();

	private static ResourceLocation MINIMAP_TEXTURES = new ResourceLocation(TravellersMap.MODID, "textures/guis/minimaps.png");

	private float[] zoomLevels = new float[]{2.0F, 1.0F, 0.5F, 0.25F};
	private int currentZoomLevel = 1;

	private int width, height;

	private MinimapHUD() {}

	private final Set<MapSegment> segments = new HashSet<>();

	public void render(int mouseX, int mouseY, float delta) {

		int centerX = width - 33;
		int centerY = 33;
		World world = Minecraft.getInstance().world;
		PlayerEntity player = Minecraft.getInstance().player;
		BlockPos playerPos = player.getPosition();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getInstance().getTextureManager().bindTexture(MINIMAP_TEXTURES);
		blit2(centerX - 32, centerY - 32, 64, 64, 0, 0, 64, 64, 256, 256);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//SetupCutout
		RenderSystem.pushMatrix();
		RenderSystem.enableDepthTest();
		RenderSystem.translatef(0.0F, 0.0F, 950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(4680, 2260, -4680, -2260, 0xFFFFFFFF);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.depthFunc(518);
		blit(centerX - 32, centerY - 32, 64, 64, 64, 0, 64, 64, 256, 256);
		RenderSystem.depthFunc(515);
		RenderSystem.colorMask(true, true, true, true);

		segments.clear();
		getAndDrawMapSegment(player, centerX, centerY, 0, 0);
		getAndDrawMapSegment(player, centerX, centerY, -128, 0);
		getAndDrawMapSegment(player, centerX, centerY, 128, 0);
		getAndDrawMapSegment(player, centerX, centerY, 0, -128);
		getAndDrawMapSegment(player, centerX, centerY, 0, 128);
		getAndDrawMapSegment(player, centerX, centerY, -128, -128);
		getAndDrawMapSegment(player, centerX, centerY, -128, 128);
		getAndDrawMapSegment(player, centerX, centerY, 128, -128);
		getAndDrawMapSegment(player, centerX, centerY, 128, 128);

		//Reset Cutout
		RenderSystem.depthFunc(518);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.translatef(0.0F, 0.0F, 950.0F);
		RenderSystem.depthFunc(515);
		RenderSystem.popMatrix();

		drawPlayerMarker(centerX, centerY);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private void getAndDrawMapSegment(PlayerEntity player, int centerX, int centerY, int offsetX, int offsetZ){
		MapSegment map = MapManager.getMapSegment(MapSegmentUtil.getSegment(player.getPosition().add(offsetX, 0, offsetZ)));
		if(map != null && !segments.contains(map)){
			segments.add(map);
			drawMapSegment(map, player.getPositionVec(), centerX, centerY, offsetX, offsetZ);
		}
	}

	private void drawMapSegment(MapSegment map, Vec3d playerPos, int centerX, int centerY, int offsetX, int offsetZ){
		map.getDynTexture().bindTexture();
		map.updadteGPU();
		int offset = (int)(128 * zoomLevels[currentZoomLevel]);
		int size =  (int)(256 * zoomLevels[currentZoomLevel]);
		double relativeX = ((playerPos.x - (((int)(playerPos.x) + offsetX) >> 9) * 512 - 256) * (0.5F * zoomLevels[currentZoomLevel]));
		double relativeZ = ((playerPos.z - (((int)(playerPos.z) + offsetZ) >> 9) * 512 - 256) * (0.5F * zoomLevels[currentZoomLevel]));
		blit2(centerX - offset - relativeX, centerY - offset - relativeZ, size, size, 0, 0, 256, 256, 256, 256);
	}

	private void drawPlayerMarker(int centerX, int centerY){
		hLine(centerX -3, centerX +1, centerY, 0xFFFFFFFF);
		vLine(centerX - 1, centerY -3, centerY +3, 0xFFFFFFFF);
	}

	public static void blit2(double x0, double y0, int destWidth, int destHeight, float u0, float v0, int srcWidth, int srcHeight, int texWidth, int texHeight) {
		innerBlit2(x0, x0 + destWidth, y0, y0 + destHeight, 0, srcWidth, srcHeight, u0, v0, texWidth, texHeight);
	}

	private static void innerBlit2(double x0, double x1, double y0, double y1, int z, int width, int height, float u0, float v0, int texWidth, int texHeight) {
		innerBlit2(x0, x1, y0, y1, z, (u0 + 0.0F) / (float)texWidth, (u0 + (float)width) / (float)texWidth, (v0 + 0.0F) / (float)texHeight, (v0 + (float)height) / (float)texHeight);
	}

	protected static void innerBlit2(double x0, double x1, double y0, double y1, int z, float u0, float u1, float v0, float v1) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x0, y1, (double) z).tex(u0, v1).endVertex();
		bufferbuilder.pos(x1, y1, (double) z).tex(u1, v1).endVertex();
		bufferbuilder.pos(x1, y0, (double) z).tex(u1, v0).endVertex();
		bufferbuilder.pos(x0, y0, (double) z).tex(u0, v0).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}

	public static void increaseZoom(){
		INSTANCE.currentZoomLevel = Math.max(0, INSTANCE.currentZoomLevel - 1);
	}

	public static void decreaseZoom(){
		INSTANCE.currentZoomLevel = Math.min(3, INSTANCE.currentZoomLevel + 1);
	}
}
