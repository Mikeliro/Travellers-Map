package net.dark_roleplay.travellers_map.mapping.tickets;

import net.dark_roleplay.travellers_map.api.mapping.IMapSegmentTicket;
import net.dark_roleplay.travellers_map.util.MapSegmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class RenderTicket implements IMapSegmentTicket {

	public static final int mappingRange = 320;
	public static final int mapSize = 512;

	private static final Map<Long, RenderTicket> TICKETS = new HashMap<>();

	private int posX1, posX2, posZ1, posZ2;

	//func_233580_cy_ == player#getPosition()

	public static RenderTicket getOrCreateTicket(int offsetX, int offsetZ){
		int segX = (Minecraft.getInstance().player.func_233580_cy_().getX() + offsetX) >> 9;
		int segZ = (Minecraft.getInstance().player.func_233580_cy_().getZ() + offsetZ) >> 9;
		long ident = MapSegmentUtil.toSegment(segX, segZ);
		RenderTicket ticket = TICKETS.get(ident);
		if(ticket == null) {
			ticket = new RenderTicket(segX, segZ);
			TICKETS.put(ident, ticket);
		}
		return ticket;
	}

	private RenderTicket(int segX, int segZ){
		posX1 = (segX >> 9) - mappingRange;
		posX2 = (segX >> 9) + mapSize + mappingRange;
		posZ1 = (segZ >> 9) - mappingRange;
		posZ2 = (segZ >> 9) + mapSize + mappingRange;
	}

	@Override
	public boolean isActive() {
		if(Minecraft.getInstance().player == null) return false;
		BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();
		return posX1 < playerPos.getX() && posX2 > playerPos.getX() && posZ1 < playerPos.getZ() && posZ2 > playerPos.getZ();
	}
}
