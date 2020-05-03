package net.dark_roleplay.travellers_map.objects.huds.minimap;

import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MinimapHUD extends AbstractGui {

	public static final MinimapHUD INSTANCE = new MinimapHUD();

	private int width, height;

	private MinimapHUD() {}

	public void render(int mouseX, int mouseY, float delta) {
		World world = Minecraft.getInstance().world;
		PlayerEntity player = Minecraft.getInstance().player;
		BlockPos playerPos = player.getPosition();

		//Draw mapSegment in which the player is.
		MapSegment playerMap = MapManager.getMapSegment(MapSegmentUtil.getSegment(player));
		if(playerMap != null)
			drawMapSegment(playerMap, playerPos, width/2, height/2, 0, 0);

//		for(int x = -1; x < 2; x++){
//			for(int z = -1; z < 2; z++){
//				MapSegment map = MapManager.getMapSegment((playerPos.getX() + x * 256) >> 9, (playerPos.getZ() + z * 256) >> 9);
//				if(map != null && map != playerMap)
//					drawMapSegment(map, playerPos, width/2, height/2, x * 256, z * 256);
//			}
//		}


		drawPlayerMarker((width/2),(height/2));
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private void drawMapSegment(MapSegment map, BlockPos playerPos, int centerX, int centerY, int offsetX, int offsetZ){
		map.getDynTexture().bindTexture();
		map.updadteGPU();
		int relativeX = playerPos.getX() - ((playerPos.getX()+ offsetX) >> 9) * 512 - 256 ;
		int relativeZ = playerPos.getZ() - ((playerPos.getZ()+ offsetZ) >> 9) * 512 - 256;
		blit(centerX - 256 - relativeX, centerY - 256 - relativeZ, 512, 512, 0, 0, 256, 256, 256, 256);
	}

	private void drawPlayerMarker(int centerX, int centerY){
		hLine(centerX -2, centerX +2, centerY -1, 0xFFFFFFFF);
		vLine(centerX, centerY -4, centerY +2, 0xFFFFFFFF);
	}
}
