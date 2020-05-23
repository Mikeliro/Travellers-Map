package net.dark_roleplay.travellers_map.mapping.waypoints;

import net.dark_roleplay.travellers_map.util.MapManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class Waypoint implements INBTSerializable<CompoundNBT> {

	public final UUID uuid;
	private String name;
	private BlockPos pos;
	private int color;
	private boolean isVisible;

	public Waypoint(UUID uuid){
		this.uuid = uuid;
	}

	public Waypoint(UUID uuid, String name, BlockPos pos, int color, boolean isVisible) {
		this(uuid);
		this.name = name;
		this.pos = pos;
		this.color = color;
		this.isVisible = true;
	}

	public void update(String newName, BlockPos newPos, int newColor){
		this.name = newName;
		this.pos = newPos;
		this.color = newColor;
	}

	public BlockPos getPos() {
		return pos;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public boolean isVisible(){
		return this.isVisible;
	}

	public void toggleVisible(){
		this.isVisible = !this.isVisible;
		MapManager.saveWaypoint(this, false);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("name", this.name);
		nbt.put("pos", NBTUtil.writeBlockPos(this.pos));
		nbt.putBoolean("enabled", this.isVisible);
		nbt.putInt("color", this.color);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.name = nbt.getString("name");
		this.pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
		this.isVisible = nbt.getBoolean("enabled");
		this.color = nbt.getInt("color");
	}

	/** ----- Helper Methods for Stream Based Rendering ----- **/

	public static int widestNameWidth = 0;
	private float lastRenderedYaw;
	private float lastRenderedOffset;
	private int nameWidth;

	public Waypoint setLastRenderedData(float lastRenderedYaw, float lastRenderedOffset) {
		this.lastRenderedYaw = lastRenderedYaw;
		this.lastRenderedOffset = lastRenderedOffset;
		return this;
	}

	public Waypoint initNameWidth(FontRenderer renderer){
		this.nameWidth = renderer.getStringWidth(this.name);
		if(this.nameWidth > widestNameWidth)
			widestNameWidth = this.nameWidth;
		return this;
	}

	public float getLastRenderedYaw() {
		return lastRenderedYaw;
	}

	public float getLastRenderedOffset() {
		return lastRenderedOffset;
	}

	public int getNameWidth() {
		return nameWidth;
	}
}
