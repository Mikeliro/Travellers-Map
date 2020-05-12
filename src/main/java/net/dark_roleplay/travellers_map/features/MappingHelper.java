package net.dark_roleplay.travellers_map.features;

import net.dark_roleplay.travellers_map.features.mappers.LightingColorMapper;
import net.minecraft.util.math.ChunkPos;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MappingHelper {

	private static final Set<MapperQueue> activeMappers = new HashSet<>();

	private static Timer chunkMapper;

	public static void initMapper(){
		activeMappers.add(new MapperQueue(LightingColorMapper.INSTANCE));

		chunkMapper = new Timer("TravellersMap - Chunk Mapper", true);
		chunkMapper.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for(MapperQueue mapper : activeMappers)
					mapper.processLoadedChunksQueue();
			}
		}, 250, 250);

		for(MapperQueue mapper : activeMappers)
			mapper.initTimer(chunkMapper);
	}

	public static void killMapper(){
		chunkMapper.cancel();
		for(MapperQueue mapper : activeMappers)
			mapper.stopMapper();
	}

	public static void scheduleLoadedChunk(ChunkPos pos){
		for(MapperQueue mapper : activeMappers)
			mapper.scheduleLoadedChunk(pos);
	}

	public static void schedulePeriodicalChunk(ChunkPos pos){
		for(MapperQueue mapper : activeMappers)
			mapper.schedulePeriodicalChunk(pos);
	}
}
