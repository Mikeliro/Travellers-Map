package net.dark_roleplay.travellers_map.features.mappers;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;

public class CaveColorMapper extends Mapper{
	public static Mapper INSTANCE = new CaveColorMapper();

	private static int height = 32;

	@Override
	public void mapChunk(IChunk chunk, NativeImage img) {
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
		int x = Math.floorMod(chunk.getPos().x, 32) * 16, z = Math.floorMod(chunk.getPos().z, 32) * 16;
		for(int x2 = 0; x2 < 16; x2++){
			for(int z2 = 0; z2 < 16; z2++){
				int y = height;
				MaterialColor color = null;
				while(y >= 0 && (color = chunk.getBlockState(pos.setPos(x2, y, z2)).getMaterialColor(Minecraft.getInstance().world, pos)) == MaterialColor.AIR)
					y--;
				if(color != null)
					img.setPixelRGBA(x + x2, z + z2, (color.getMapColor(4)));
			}
		}
		pos.close();
	}
}
