package net.dark_roleplay.travellers_map.features.mappers;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Mapper extends ForgeRegistryEntry<Mapper> {
	public abstract void mapChunk(IChunk chunk, NativeImage img);
}
