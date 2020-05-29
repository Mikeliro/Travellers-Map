package net.dark_roleplay.travellers_map2.objects.screens.full_map;

import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.util.math.BlockPos;

public class MapRenderer {

	public void renderMap(BlockPos center, int minX, int minZ, int maxX, int maxZ, float zoom){
		MapSegment[][] maps = collectMapParts(center.getX() + minX, center.getZ() + minZ, center.getX() + maxX,  center.getZ() + maxZ);

		BlockPos topLeft = center.add(minX, 0, minZ);

		int minX2 = ((int)Math.floor(topLeft.getX()/512F) * 512) - topLeft.getX() + minX;
		int minZ2 = ((int)Math.floor(topLeft.getZ()/512F) * 512) - topLeft.getZ() + minZ;

		for(int x = 0; x < maps.length; x++){
			MapSegment[] zMaps = maps[x];
			for(int z = 0; z < zMaps.length; z++){
				drawSegment(zMaps[z], minX2 + (x * 512), minZ2 + (z * 512));
			}
		}
	}

	private MapSegment[][] collectMapParts(int minX, int minZ, int maxX, int maxZ){

		int xSegments = (int) Math.ceil(Math.abs(maxX - minX)/512F) + 1;
		int zSegments = (int) Math.ceil(Math.abs(maxZ - minZ)/512F) + 1;

		MapSegment[][] segments = new MapSegment[xSegments][zSegments];

		for(int x = 0; x < xSegments; x++){
			segments[x] = new MapSegment[zSegments];
			for(int z = 0; z < zSegments; z++){
				segments[x][z] = MapManager.getMapSegment(MapSegmentUtil.getSegment(new BlockPos(minX + (x * 512), 0, minZ + (z * 512))));
			}
		}

		return segments;
	}

	private void drawSegment(MapSegment map, int offsetX, int offsetZ){
		if(map != null ) {
			map.getDynTexture().bindTexture();
			BlendBlitHelper.blit(offsetX, offsetZ, 512, 512, 0, 0, 1, 1, 1, 1);
		}
	}
}
