package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.minecraft.world.chunk.ChunkPrimerWrapper;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT)
public class ChunkListener {

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load event){
        IChunk chunk = event.getChunk();
        if(chunk instanceof ChunkPrimerWrapper) return;
        MapSegment segment = MapManager.getOrCreateMapSegment(chunk);
        if(segment == null) return;
        segment.updateChunk(chunk);
        segment.markDirty();
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload event){
    }
}
