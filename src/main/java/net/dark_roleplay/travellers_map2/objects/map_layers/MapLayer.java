package net.dark_roleplay.travellers_map2.objects.map_layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.api.mapping.IMapSegmentTicket;
import net.dark_roleplay.travellers_map.api.rendering.IMapLayer;
import net.dark_roleplay.travellers_map.api.rendering.MapType;
import net.dark_roleplay.travellers_map.api.util.MapRenderInfo;
import net.dark_roleplay.travellers_map.mapping.tickets.RenderTicket;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.minecraft.client.gui.AbstractGui;

public class MapLayer extends IMapLayer {

	public MapLayer() {
		super(false);
	}

	@Override
	public void renderLayer(MatrixStack matrix, MapRenderInfo renderInfo, MapType mapType, boolean isRotated, float delta) {
		Long[][] maps = renderInfo.getSegments();

		for(int x = 0; x < maps.length; x++){
			for(int z = 0; z < maps[x].length; z++){
				MapSegment map = MapManager.getMapOrTryLoad(maps[x][z]);

				IMapSegmentTicket ticket = RenderTicket.getOrCreateTicket(map.getSegX(), map.getSegZ());
				map.addTicket(ticket);

				drawSegment(matrix, map, renderInfo.getOffsetX() + (x * 512), renderInfo.getOffsetZ() + (z * 512));
			}
		}
	}

	private static void drawSegment(MatrixStack matrix, MapSegment map, int offsetX, int offsetZ){
		if(map != null && !map.isEmpty()) {
			map.getDynTexture().bindTexture();
			AbstractGui.blit(matrix, offsetX, offsetZ, 512, 512, 0, 0, 1, 1, 1, 1);
		}
	}
}
