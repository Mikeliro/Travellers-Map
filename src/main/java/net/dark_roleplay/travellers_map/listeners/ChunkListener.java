package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.mapping.MappingHelper;
import net.dark_roleplay.travellers_map.objects.tickets.ChunkLoadedTicket;
import net.minecraft.world.chunk.ChunkPrimer;
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
        if(event.getWorld() == null || !event.getWorld().isRemote())return;
        if(chunk instanceof ChunkPrimerWrapper || chunk instanceof ChunkPrimer) return;

        MappingHelper.scheduleLoadedChunk(chunk.getPos());
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload event){
        IChunk chunk = event.getChunk();
        ChunkLoadedTicket.unloadChunk(chunk);
    }
}
