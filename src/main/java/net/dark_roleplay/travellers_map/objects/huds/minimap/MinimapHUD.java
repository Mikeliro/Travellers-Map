package net.dark_roleplay.travellers_map.objects.huds.minimap;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.objects.data.RenderTicket;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
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
		BlendBlitHelper.blit(centerX - 32, centerY - 32, 64, 64, 0, 0, 64, 64, 256, 256);
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
		getAndDrawMapSegment(player, centerX, centerY, -256, 0);
		getAndDrawMapSegment(player, centerX, centerY, 256, 0);
		getAndDrawMapSegment(player, centerX, centerY, 0, -256);
		getAndDrawMapSegment(player, centerX, centerY, 0, 256);
		getAndDrawMapSegment(player, centerX, centerY, -256, -256);
		getAndDrawMapSegment(player, centerX, centerY, -256, 256);
		getAndDrawMapSegment(player, centerX, centerY, 256, -256);
		getAndDrawMapSegment(player, centerX, centerY, 256, 256);

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
		RenderTicket ticket = RenderTicket.getOrCreateTicket(offsetX, offsetZ);
		MapSegment map = MapManager.getMapSegment(MapSegmentUtil.getSegment(player.getPosition().add(offsetX, 0, offsetZ)));
		if(map != null && !segments.contains(map)){
			map.addTicket(ticket);
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
		BlendBlitHelper.blit(centerX - offset - relativeX, centerY - offset - relativeZ, size, size, 0, 0, 256, 256, 256, 256);
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
