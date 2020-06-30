package net.dark_roleplay.travellers_map.api.util;

import net.minecraft.util.math.BlockPos;

public class MapRenderInfo {

	private int width;
	private int height;
	private int offsetX;
	private int offsetZ;
	private Long[][] segments;

	public void update(int width, int height, double scale, BlockPos center) {
		this.width = width;
		this.height = height;
		scale = 1/scale;

		int dX = (int) (width * scale) + 2;
		int dZ = (int) (height * scale) + 2;

		int minSegX = (center.getX() - dX) >> 9;
		int minSegZ = (center.getZ() - dZ) >> 9;
		int maxSegX = (center.getX() + dX) >> 9;
		int maxSegZ = (center.getZ() + dZ) >> 9;

		this.offsetX = -(center.getX() - minSegX * 512);
		this.offsetZ = -(center.getZ() - minSegZ * 512);

		this.segments = new Long[maxSegX - minSegX + 1][maxSegZ - minSegZ + 1];

		for (int x = 0; x <= maxSegX - minSegX; x++) {
			for (int z = 0; z <= maxSegZ - minSegZ; z++) {
				this.segments[x][z] = ((long) (minSegX + x) & 0xFFFFFFFFL) << 32 | ((long) (minSegZ + z) & 0xFFFFFFFFL);
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetZ() {
		return offsetZ;
	}

	public Long[][] getSegments() {
		return segments;
	}
}
