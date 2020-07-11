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
	private boolean hasMouse;
	private double mouseX;
	private double mouseY;
	private double scaledMouseX;
	private double scaledMouseY;


	public void update(int width, int height, double scale, BlockPos center, double mouseX, double mouseY) {
		this.update(width, height, scale, center);
		this.hasMouse = true;
		this.mouseX = mouseX - width / 2F;
		this.mouseY = mouseY - height / 2F;
		this.scaledMouseX = this.mouseX / this.scale;
		this.scaledMouseY = this.mouseY / this.scale;
	}

	public void update(int width, int height, double scale, BlockPos center) {
		this.hasMouse = false;
		this.width = width;
		this.height = height;
		this.scale = (float) scale;

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

	public boolean hasMouse() {
		return hasMouse;
	}

	public double getMouseX() {
		return mouseX;
	}

	public double getMouseY() {
		return mouseY;
	}

	public double getScaledMouseX() {
		return scaledMouseX;
	}

	public double getScaledMouseY() {
		return scaledMouseY;
	}
}
