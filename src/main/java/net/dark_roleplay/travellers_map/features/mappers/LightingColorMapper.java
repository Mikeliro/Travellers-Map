package net.dark_roleplay.travellers_map.features.mappers;

import net.dark_roleplay.travellers_map.features.color_palettes.ColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.DefaultColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.MonoColorPalette;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
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
	public void mapChunk(World world, IChunk chunk, NativeImage img) {
		ChunkPos chunkPos = chunk.getPos();
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
		int x = Math.floorMod(chunkPos.x, 32) * 16, z = Math.floorMod(chunkPos.z, 32) * 16;
		for(int x2 = 0; x2 < 16; x2++){
			for(int z2 = 0; z2 < 16; z2++){
				int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x2, z2);
				BlockState state = chunk.getBlockState(pos.setPos((chunkPos.x << 4) | x2, y, (chunkPos.z << 4) | z2));
				MaterialColor color = state.getMaterialColor(Minecraft.getInstance().world, pos);
				if(color != null){
					int brightness = 1;
					if(world.getBlockState(pos.add(0, 1, -1)).getMaterialColor(world, pos) != MaterialColor.AIR)
						brightness--;
					else if(world.getBlockState(pos.add(0, -1, 0)).getMaterialColor(world, pos) == MaterialColor.AIR)
						brightness++;

					img.setPixelRGBA(x + x2, z + z2, (palette.getRGBA(color.colorIndex, brightness)));
				}
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
		return 20;
	}

	@Override
	public boolean canMapChunk(World world, IChunk chunk){
		return world.chunkExists(chunk.getPos().x, chunk.getPos().z - 1);
	}
}
