package net.dark_roleplay.travellers_map.features.mappers;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class FlatColorMapper extends Mapper{
	public static Mapper INSTANCE = new FlatColorMapper();

	@Override
	public void mapChunk(IChunk chunk, NativeImage img) {
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
		int x = Math.floorMod(chunk.getPos().x, 32) * 16, z = Math.floorMod(chunk.getPos().z, 32) * 16;
		for(int x2 = 0; x2 < 16; x2++){
			for(int z2 = 0; z2 < 16; z2++){
				int y = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, x2, z2);
				BlockState state = chunk.getBlockState(pos.setPos(x2, y, z2));
				MaterialColor color = state.getMaterialColor(Minecraft.getInstance().world, pos);
				if(color != null)
					img.setPixelRGBA(x + x2, z + z2, (color.getMapColor(4)));
			}
		}
		pos.close();
	}
}
