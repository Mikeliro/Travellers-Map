package net.dark_roleplay.travellers_map2.objects.map_layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.api.rendering.IMapLayer;
import net.dark_roleplay.travellers_map.api.rendering.MapType;
import net.dark_roleplay.travellers_map.api.util.MapRenderInfo;
import net.dark_roleplay.travellers_map2.objects.screens.full_map.FullMapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Quaternion;

public class PlayerMarkerLayer extends IMapLayer {

	public PlayerMarkerLayer() {
		super(true);
	}

	@Override
	public void renderLayer(MatrixStack matrix, MapRenderInfo renderInfo, MapType mapType, boolean isRotated, float delta) {
		matrix.push();
		PlayerEntity player = Minecraft.getInstance().player;
		matrix.translate(player.func_233580_cy_().getX() - renderInfo.getCenterX(), (player.func_233580_cy_().getZ() - renderInfo.getCenterZ()), 0);

		float scale = 1/renderInfo.getScale();
		matrix.scale(scale, scale, scale);

		float yaw =(float) Math.toRadians(player.getYaw(delta)- 180) /2F;
		matrix.rotate(new Quaternion(0, 0, (float)Math.sin(yaw), (float)Math.cos(yaw)));

		Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
		blit(matrix, -2, -4, 158, 0, 5, 7);

		matrix.pop();
	}
}
