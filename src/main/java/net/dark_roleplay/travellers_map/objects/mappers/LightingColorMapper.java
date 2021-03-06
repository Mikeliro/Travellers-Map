package net.dark_roleplay.travellers_map.objects.mappers;

import net.dark_roleplay.travellers_map.mapping.Mapper;
import net.dark_roleplay.travellers_map.objects.color_palettes.ColorPalette;
import net.dark_roleplay.travellers_map.objects.color_palettes.DefaultColorPalette;
import net.dark_roleplay.travellers_map.objects.color_palettes.MonoColorPalette;
import net.dark_roleplay.travellers_map.util.MapperUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

public class LightingColorMapper extends Mapper {
	public static Mapper INSTANCE = new LightingColorMapper(new DefaultColorPalette());
	public static Mapper INSTANCE_GRAYSCALE = new LightingColorMapper(new MonoColorPalette(0xFFFFFFFF));

	private ColorPalette palette;

	public LightingColorMapper(ColorPalette palette){
		this.palette = palette;
	}

	@Override
	public void mapChunk(World world, IChunk chunk, NativeImage img) {
		ChunkPos chunkPos = chunk.getPos();
		BlockPos.Mutable pos = new BlockPos.Mutable();
		BlockPos.Mutable fluidPos = new BlockPos.Mutable();
		int x = chunkPos.x * 16, z = chunkPos.z * 16;
		for(int i = 0, x2 = x; i < 16; i++, x2++){
			for(int j = 0, z2 = z; j < 16; j++, z2++){
				int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x2, z2);
				BlockState firstBlock = MapperUtil.getFirstMappableBlock(world, pos.setPos(x2, y, z2), y, 0);

				if(firstBlock == null) continue;

				MaterialColor color = firstBlock.getMaterialColor(world, pos);
				if(color != null){

					int brightness;
					if(!firstBlock.getFluidState().isEmpty() && firstBlock.getFluidState().getFluid() == Fluids.WATER){
						brightness = 2;
						boolean isPrimary = (i + j) % 2 == 1;

						int depth = MapperUtil.getFluidDepth(world, fluidPos.setPos(pos));
						if(depth >= 10 || (depth >= 7 && isPrimary))
							brightness = 0;
						else if(depth >= 5 || depth >= 3 && isPrimary)
							brightness = 1;
					}else{
						brightness = 1;
						if(world.getBlockState(pos.add(0, 1, -1)).getMaterialColor(world, pos) != MaterialColor.AIR)
							brightness--;
						if(world.getBlockState(pos.add(0, 0, -1)).getMaterialColor(world, pos) == MaterialColor.AIR)
							brightness++;
					}
					img.setPixelRGBA(Math.floorMod(x2, 512), Math.floorMod(z2, 512), (palette.getRGBA(color.colorIndex, brightness)));
				}
			}
		}
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
		return world.getChunk(chunk.getPos().x, chunk.getPos().z - 1, ChunkStatus.FULL, false) != null;
	}
}
