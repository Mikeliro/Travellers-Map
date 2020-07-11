package net.dark_roleplay.travellers_map2.objects.map_layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.api.rendering.IMapLayer;
import net.dark_roleplay.travellers_map.api.rendering.MapType;
import net.dark_roleplay.travellers_map.api.util.MapRenderInfo;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Quaternion;

public class WaypointLayer extends IMapLayer {
	public WaypointLayer() {
		super(true);
	}

	@Override
	public void renderLayer(MatrixStack matrix, MapRenderInfo renderInfo, MapType mapType, boolean isRotated, float delta) {
		float posX = renderInfo.getCenterX() - renderInfo.getScaledWidth() / 2 - 10;
		float posX2 = renderInfo.getCenterX() + renderInfo.getScaledWidth() / 2 + 10;
		float posZ = renderInfo.getCenterZ() - renderInfo.getScaledHeight() / 2 - 10;
		float posZ2 = renderInfo.getCenterZ() + renderInfo.getScaledHeight() / 2 + 10;

		MapManager.WAYPOINTS
				.stream()
				.filter(waypoint -> waypoint.isVisible())
				.filter(waypoint ->
						waypoint.getPos().getX() > posX && waypoint.getPos().getX() < posX2 &&
								waypoint.getPos().getZ() > posZ && waypoint.getPos().getZ() < posZ2
				)
				.forEach(waypoint -> {
					double waypointX = waypoint.getPos().getX() - renderInfo.getCenterX();
					double waypointZ = waypoint.getPos().getZ() - renderInfo.getCenterZ();
					boolean isHovered = isHovered(renderInfo, waypointX, waypointZ);

					if (isRotated) {
						matrix.push();
						matrix.translate(waypointX, waypointZ, 0);
						float yaw = (float) Math.toRadians(Minecraft.getInstance().player.getYaw(delta) - 180) / 2F;
						matrix.rotate(new Quaternion(0, 0, (float) Math.sin(yaw), (float) Math.cos(yaw)));
						fill(matrix, -4, -4, 4, 4, isHovered ? 0xFF00FF00 : 0xFFFF00FF);
						matrix.pop();
					} else {
						fill(matrix,
								(int) waypointX - 4,
								(int) waypointZ - 4,
								(int) waypointX + 4,
								(int) waypointZ + 4, isHovered ? 0xFF00FF00 : 0xFFFF00FF);
					}

				});

	}

	private boolean isHovered(MapRenderInfo renderInfo, double waypointX, double waypointZ){
		if(!renderInfo.hasMouse()) return false;
		return waypointX - 4 < renderInfo.getScaledMouseX() && waypointX + 4 > renderInfo.getScaledMouseX()
				&& waypointZ - 4 < renderInfo.getScaledMouseY() && waypointZ + 4 > renderInfo.getScaledMouseY();
	}
}
