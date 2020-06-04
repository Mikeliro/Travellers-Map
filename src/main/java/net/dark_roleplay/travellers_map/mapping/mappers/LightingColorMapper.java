package net.dark_roleplay.travellers_map.mapping.mappers;

import net.dark_roleplay.travellers_map.features.color_palettes.ColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.DefaultColorPalette;
import net.dark_roleplay.travellers_map.features.color_palettes.MonoColorPalette;
import net.dark_roleplay.travellers_map2.mapper_logic.MapperUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import org.apache.commons.lang3.tuple.Pair;

public class LightingColorMapper extends Mapper{
	public static Mapper INSTANCE = new LightingColorMapper(new DefaultColorPalette());
	public static Mapper INSTANCE_GRAYSCALE = new LightingColorMapper(new MonoColorPalette(0xFFFFFFFF));

	private ColorPalette palette;

	public LightingColorMapper(ColorPalette palette){
		this.palette = palette;
	}

	@Override
	public void mapChunk(World world, IChunk chunk, NativeImage img) {
		ChunkPos chunkPos = chunk.getPos();
		BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
		int x = chunkPos.x * 16, z = chunkPos.z * 16;
		for(int x2 = x; x2 < x + 16; x2++){
			for(int z2 = z; z2 < z + 16; z2++){
				int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x2, z2);
				BlockState firstBlock = MapperUtil.getFirstMappableBlock(world, pos.setPos(x2, y, z2), y, 0);

				if(firstBlock == null) continue;

				MaterialColor color = firstBlock.getMaterialColor(world, pos);
				if(color != null){
					int brightness = 1;
					if(world.getBlockState(pos.add(0, 1, -1)).getMaterialColor(world, pos) != MaterialColor.AIR)
						brightness--;
					if(world.getBlockState(pos.add(0, 0, -1)).getMaterialColor(world, pos) == MaterialColor.AIR)
						brightness++;

					img.setPixelRGBA(Math.floorMod(x2, 512), Math.floorMod(z2, 512), (palette.getRGBA(color.colorIndex, brightness)));
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
