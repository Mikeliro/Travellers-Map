package net.dark_roleplay.travellers_map.objects.data;

public class RenderTicket implements IMapSegmentTicket {

	private boolean isActive = false;

	public RenderTicket(){
		this.isActive = true;
	}

	public void invalidate(){
		this.isActive = false;
	}

	@Override
	public boolean isActive() {
		return false;
	}
}
