package net.dark_roleplay.travellers_map.api.util;

import net.minecraft.util.math.BlockPos;

public class MapRenderInfo {

	private int width;
	private int height;
	private int scaledWidth;
	private int scaledHeight;
	private int offsetX;
	private int offsetZ;
	private float scale;
	private float centerX;
	private float centerZ;
	private Long[][] segments;

	public void update(int width, int height, double scale, BlockPos center) {
		this.width = width;
		this.height = height;
		this.scale = (float) scale;

		System.out.println(scale);

		scaledWidth = (int) (width / this.scale) + 2;
		scaledHeight = (int) (height / this.scale) + 2;

		this.centerX = center.getX();
		this.centerZ = center.getZ();

		int minSegX = (center.getX() - scaledWidth) >> 9;
		int minSegZ = (center.getZ() - scaledHeight) >> 9;
		int maxSegX = (center.getX() + scaledWidth) >> 9;
		int maxSegZ = (center.getZ() + scaledHeight) >> 9;

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

	public float getScale() {
		return scale;
	}

	public Long[][] getSegments() {
		return segments;
	}

	public float getCenterX() {
		return centerX;
	}

	public float getCenterZ() {
		return centerZ;
	}

	public int getScaledWidth() {
		return scaledWidth;
	}

	public int getScaledHeight() {
		return scaledHeight;
	}
}
