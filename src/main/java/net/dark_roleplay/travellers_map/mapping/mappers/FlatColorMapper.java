package net.dark_roleplay.travellers_map.mapping.mappers;

import net.dark_roleplay.travellers_map.features.color_palettes.ColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.DefaultColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.MonoColorPalette;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class FlatColorMapper extends   Mapper{
	public static Mapper INSTANCE_COLOR = new FlatColorMapper(new DefaultColorPalette());
	public static Mapper INSTANCE_GRAYSCALE = new FlatColorMapper(new MonoColorPalette(0xFFFFFFFF));

	private ColorPalette palette;

	public FlatColorMapper(ColorPalette palette){
		this.palette = palette;
	}

	@Override
	public void mapChunk(World world, IChunk chunk, NativeImage img) {
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
		int x = Math.floorMod(chunk.getPos().x, 32) * 16, z = Math.floorMod(chunk.getPos().z, 32) * 16;
		for(int x2 = 0; x2 < 16; x2++){
			for(int z2 = 0; z2 < 16; z2++){
				int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x2, z2);
				BlockState state = chunk.getBlockState(pos.setPos(x2, y, z2));
				MaterialColor color = state.getMaterialColor(Minecraft.getInstance().world, pos);
				if(color != null)
					img.setPixelRGBA(x + x2, z + z2, (palette.getRGBA(color.colorIndex)));
			}
		}
		pos.close();
	}

	@Override
	public int getMappingInterval() {
		return 1000;
	}

	@Override
	public int getMaxChunksPerRun() {
		return 40;
	}
}
