package net.dark_roleplay.travellers_map.features.mappers;

import net.dark_roleplay.travellers_map.features.color_palettes.ColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.DefaultColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.MonoColorPalette;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class LightingColorMapper extends Mapper{
	public static Mapper INSTANCE = new LightingColorMapper(new DefaultColorPalette());
	public static Mapper INSTANCE_GRAYSCALE = new LightingColorMapper(new MonoColorPalette());

	private ColorPalette palette;

	public LightingColorMapper(ColorPalette palette){
		this.palette = palette;
	}

	@Override
	public void mapChunk(IChunk chunk, NativeImage img) {
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
		BlockPos.PooledMutable pos2 = BlockPos.PooledMutable.retain();
		int x = Math.floorMod(chunk.getPos().x, 32) * 16, z = Math.floorMod(chunk.getPos().z, 32) * 16;
		for(int x2 = 0; x2 < 16; x2++){
			for(int z2 = 0; z2 < 16; z2++){
				int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x2, z2);
				BlockState state = chunk.getBlockState(pos.setPos(x2, y, z2));
				MaterialColor color = state.getMaterialColor(Minecraft.getInstance().world, pos);
				if(color != null){
					int brightness = 1;
					if(chunk.getBlockState(pos2.setPos(x2, y + 1, z2 - 1)).getMaterialColor(Minecraft.getInstance().world, pos2) != MaterialColor.AIR)
						brightness--;
					else if(chunk.getBlockState(pos2.setPos(x2, y, z2 - 1)).getMaterialColor(Minecraft.getInstance().world, pos2) == MaterialColor.AIR)
						brightness++;

					img.setPixelRGBA(x + x2, z + z2, (palette.getRGBA(color.colorIndex, brightness)));
				}
			}
		}
		pos.close();
	}
}
