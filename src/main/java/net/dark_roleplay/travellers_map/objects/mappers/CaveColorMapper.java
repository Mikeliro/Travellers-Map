package net.dark_roleplay.travellers_map.objects.mappers;

import net.dark_roleplay.travellers_map.mapping.Mapper;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;

public class CaveColorMapper extends Mapper {
	public static Mapper INSTANCE = new CaveColorMapper();

	private static int height = 32;

	@Override
	public void mapChunk(World world, IChunk chunk, NativeImage img) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		BlockPos.Mutable worker = new BlockPos.Mutable();
		int x = Math.floorMod(chunk.getPos().x, 32) * 16, z = Math.floorMod(chunk.getPos().z, 32) * 16;
		for(int x2 = 0; x2 < 16; x2++){
			for(int z2 = 0; z2 < 16; z2++){
				int y = height;
				MaterialColor color = null;
				while(y >= 0 && (color = chunk.getBlockState(pos.setPos(x2, y, z2)).getMaterialColor(Minecraft.getInstance().world, pos)) == MaterialColor.AIR)
					y--;
				if(color != null && (y < height || (isVisible(chunk, pos, worker))))
					img.setPixelRGBA(x + x2, z + z2,color.getMapColor(4));
				else
					img.setPixelRGBA(x + x2, z + z2,  0xFF000000);

			}
		}
	}

	@Override
	public int getMappingInterval() {
		return 1000;
	}

	@Override
	public int getMaxChunksPerRun() {
		return 10;
	}

	@Override
	public boolean canMapChunk(World world, IChunk chunk){
		return world.getChunk(chunk.getPos().x, chunk.getPos().z - 1, ChunkStatus.FULL, false) != null &&
				world.getChunk(chunk.getPos().x, chunk.getPos().z + 1, ChunkStatus.FULL, false) != null &&
				world.getChunk(chunk.getPos().x - 1, chunk.getPos().z, ChunkStatus.FULL, false) != null &&
				world.getChunk(chunk.getPos().x + 1, chunk.getPos().z, ChunkStatus.FULL, false) != null;
	}

	private boolean isVisible(IChunk chunk, BlockPos center, BlockPos.Mutable worker){
		if(chunk.getBlockState(worker.setPos(center.getX(), center.getY(), center.getZ() - 1)).getMaterialColor(Minecraft.getInstance().world, worker) == MaterialColor.AIR) return true;
		if(chunk.getBlockState(worker.setPos(center.getX() - 1, center.getY(), center.getZ())).getMaterialColor(Minecraft.getInstance().world, worker) == MaterialColor.AIR) return true;
		if(chunk.getBlockState(worker.setPos(center.getX(), center.getY(), center.getZ() + 1)).getMaterialColor(Minecraft.getInstance().world, worker) == MaterialColor.AIR) return true;
		if(chunk.getBlockState(worker.setPos(center.getX() + 1, center.getY(), center.getZ())).getMaterialColor(Minecraft.getInstance().world, worker) == MaterialColor.AIR) return true;
		return false;
	}
}
