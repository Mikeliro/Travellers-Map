package net.dark_roleplay.travellers_map.mapping;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Mapper extends ForgeRegistryEntry<Mapper> {
	public abstract void mapChunk(World world, IChunk chunk, NativeImage img);

	public abstract int getMappingInterval();
	public abstract int getMaxChunksPerRun();

	public boolean canMapChunk(World world, IChunk chunk){
		return true;
	}
}
