package net.dark_roleplay.travellers_map2.mapper_logic;

import net.dark_roleplay.travellers_map.mapping.MappingHelper;
import net.dark_roleplay.travellers_map.api.mapping.Mapper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.IChunk;
import org.apache.commons.lang3.tuple.Pair;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MappingTracker {

	private Mapper mapper;

	private AbstractQueue<Long> newChunksQueue = new ConcurrentLinkedQueue<Long>();
	private AbstractQueue<Pair<Long, Long>> repeatedChunksQueue = new ConcurrentLinkedQueue<Pair<Long, Long>>();

	public void trackChunk(IChunk chunk){
		newChunksQueue.add(chunk.getPos().asLong());
	}

	public void trackRepeatedChunk(IChunk chunk){
		repeatedChunksQueue.add(Pair.of(chunk.getPos().asLong(), System.currentTimeMillis() + mapper.getMappingInterval()));
	}

	public void processLoadedChunksQueue(){
		if(Minecraft.getInstance().world == null){
			MappingHelper.killMapper();
			return;
		}
//		enqueToMainThread(() -> {
//			World world = Minecraft.getInstance().world;
//			for(int i = 0; i < mapper.getMaxChunksPerRun() && !newChunksQueue.isEmpty(); i++){
//				ChunkPos pos = newChunksQueue.remove();
//				if(world.chunkExists(pos.x, pos.z)){
//					Chunk chunk = world.getChunk(pos.x, pos.z);
//					if(mapper.canMapChunk(world, chunk)){
//						IMapSegmentTicket ticket = ChunkLoadedTicket.loadChunk(chunk);
//						MapSegment segment = MapManager.getOrCreateMapSegment(chunk, ticket);
//						if(segment == null) return;
//						segment.updateChunk(world, chunk);
//						segment.markDirty();
//					}else{
//						newChunksQueue.add(pos);
//					}
//				}else{
//					i--;
//				}
//			}
//		});
	}

	public boolean killIfInvalid(){
		if(Minecraft.getInstance().world != null) return false;
		else {
			//TODO Kill Tracker
			return true;
		}
	}
}
