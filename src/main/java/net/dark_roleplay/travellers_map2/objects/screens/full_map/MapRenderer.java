package net.dark_roleplay.travellers_map2.objects.screens.full_map;

import net.dark_roleplay.travellers_map.api.util.MapRenderUtil;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.util.math.BlockPos;

public class MapRenderer {

	public void renderMap(BlockPos center, int width, int height, float zoom){
		Long[][] maps = MapRenderUtil.getVisibleSegments(width, height, zoom, center);

		int topLeftX = (int) (-(width/2F) * zoom);
		int topLeftZ = (int) (-(height/2F) * zoom);

		for(int x = 0; x < maps.length; x++){
			for(int z = 0; z < maps[x].length; z++){
				MapSegment map = MapManager.getMapSegment(maps[x][z]);
				drawSegment(map, topLeftX + (x * 512), topLeftZ + (z * 512));
			}
		}
	}

	private void drawSegment(MapSegment map, int offsetX, int offsetZ){
		if(map != null ) {
			map.getDynTexture().bindTexture();
			BlendBlitHelper.blit(offsetX, offsetZ, 512, 512, 0, 0, 1, 1, 1, 1);
		}
	}
}
