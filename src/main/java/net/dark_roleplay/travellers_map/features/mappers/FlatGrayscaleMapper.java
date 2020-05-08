package net.dark_roleplay.travellers_map.features.mappers;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import org.jline.utils.Colors;

public class FlatGrayscaleMapper extends Mapper{
	public static Mapper INSTANCE = new FlatGrayscaleMapper();

	private static int[] GRAYSCALE_COLORS;

	static{
		GRAYSCALE_COLORS = new int[MaterialColor.COLORS.length];

		for(int i = 0; i < MaterialColor.COLORS.length; i++){
			if(MaterialColor.COLORS[i] == null) break;
			int color = MaterialColor.COLORS[i].getMapColor(4);
			float red = color >> 16 & 0xFF;
			float green = color >> 8 & 0xFF;
			float blue = color & 0xFF;

			int grayColor = (int) (0.21 * red + 0.72 * green + 0.07 * blue);
			GRAYSCALE_COLORS[i] = 0xFF000000 | grayColor << 16 | grayColor << 8 | grayColor;
		}
	}

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
					img.setPixelRGBA(x + x2, z + z2, (GRAYSCALE_COLORS[color.colorIndex]));
			}
		}
		pos.close();
	}
}
