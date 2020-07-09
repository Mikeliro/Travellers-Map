package net.dark_roleplay.travellers_map.mapping.tickets;

import net.dark_roleplay.travellers_map.api.mapping.IMapSegmentTicket;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class RenderTicket implements IMapSegmentTicket {

	private static final Map<Long, RenderTicket> TICKETS = new HashMap<>();

	private int unloadTicker = 3;

	public static RenderTicket getOrCreateTicket(int x, int z){
		int segX = x >> 9;
		int segZ = z >> 9;
		long ident = MapSegmentUtil.toSegment(segX, segZ);
		RenderTicket ticket = TICKETS.get(ident);
		if(ticket == null) {
			ticket = new RenderTicket();
			TICKETS.put(ident, ticket);
		}else{
			ticket.unloadTicker = 3;
		}
		return ticket;
	}

	private RenderTicket(){}

	@Override
	public boolean isActive() {
		return unloadTicker-- > 0;
	}
}
