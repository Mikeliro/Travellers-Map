package net.dark_roleplay.travellers_map.objects.data;

import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.world.chunk.IChunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChunkLoadedTicket implements IMapSegmentTicket{

	private static final Map<Long, ChunkLoadedTicket> TICKETS = new HashMap<>();

	public static ChunkLoadedTicket loadChunk(IChunk chunk){
		long ident = MapSegmentUtil.getSegment(chunk);
		ChunkLoadedTicket ticket = TICKETS.get(ident);
		if(ticket == null) {
			ticket = new ChunkLoadedTicket();
			TICKETS.put(ident, ticket);
		}
		ticket.addChunk(chunk);
		return ticket;
	}

	private final Set<IChunk> loadedChunks = new HashSet<>();

	public static void unloadChunk(IChunk chunk){
		ChunkLoadedTicket ticket = TICKETS.get(MapSegmentUtil.getSegment(chunk));
		if(ticket != null) ticket.removeChunk(chunk);
	}

	protected void addChunk(IChunk chunk){
		this.loadedChunks.add(chunk);
	}

	protected void removeChunk(IChunk chunk){
		this.loadedChunks.remove(chunk);
	}

	@Override
	public boolean isActive() {
		return !loadedChunks.isEmpty();
	}
}
