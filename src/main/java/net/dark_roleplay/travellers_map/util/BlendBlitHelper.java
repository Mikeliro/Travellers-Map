package net.dark_roleplay.travellers_map.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.TransformationMatrix;

public class BlendBlitHelper {

	public static void blit(double x0, double y0, int destWidth, int destHeight, float u0, float v0, int srcWidth, int srcHeight, int texWidth, int texHeight) {
		innerBlit(x0, x0 + destWidth, y0, y0 + destHeight, 0, srcWidth, srcHeight, u0, v0, texWidth, texHeight);
	}

	private static void innerBlit(double x0, double x1, double y0, double y1, int z, int width, int height, float u0, float v0, int texWidth, int texHeight) {
		innerBlit(x0, x1, y0, y1, z, (u0 + 0.0F) / (float)texWidth, (u0 + (float)width) / (float)texWidth, (v0 + 0.0F) / (float)texHeight, (v0 + (float)height) / (float)texHeight);
	}

	protected static void innerBlit(double x0, double x1, double y0, double y1, int z, float u0, float u1, float v0, float v1) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x0, y1, (double) z).tex(u0, v1).endVertex();
		bufferbuilder.pos(x1, y1, (double) z).tex(u1, v1).endVertex();
		bufferbuilder.pos(x1, y0, (double) z).tex(u1, v0).endVertex();
		bufferbuilder.pos(x0, y0, (double) z).tex(u0, v0).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}

	public static void blitColor(double x0, double y0, int destWidth, int destHeight, float u0, float v0, int srcWidth, int srcHeight, int texWidth, int texHeight, int color) {
		innerBlitColor(x0, x0 + destWidth, y0, y0 + destHeight, 0, srcWidth, srcHeight, u0, v0, texWidth, texHeight, color);
	}

	private static void innerBlitColor(double x0, double x1, double y0, double y1, int z, int width, int height, float u0, float v0, int texWidth, int texHeight, int color) {
		innerBlitColor(x0, x1, y0, y1, z, (u0 + 0.0F) / (float)texWidth, (u0 + (float)width) / (float)texWidth, (v0 + 0.0F) / (float)texHeight, (v0 + (float)height) / (float)texHeight, color);
	}

	protected static void innerBlitColor(double x0, double x1, double y0, double y1, int z, float u0, float u1, float v0, float v1, int color) {
		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;

		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
		bufferbuilder.pos(x0, y1, (double) z).color(f, f1, f2, f3).tex(u0, v1).endVertex();
		bufferbuilder.pos(x1, y1, (double) z).color(f, f1, f2, f3).tex(u1, v1).endVertex();
		bufferbuilder.pos(x1, y0, (double) z).color(f, f1, f2, f3).tex(u1, v0).endVertex();
		bufferbuilder.pos(x0, y0, (double) z).color(f, f1, f2, f3).tex(u0, v0).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}

	public static void vLine(float x, float y1, float y2, int color) {
		fill(x, y1, x + 1, y2, color);
	}

	public static void fill(float x1, float y1, float x2, float y2, int color) {
		fill(TransformationMatrix.identity().getMatrix(), x1, y1, x2, y2, color);
	}

	public static void fill(Matrix4f matrixStack, float x1, float y1, float x2, float y2, int color) {
		if (x1 < x2) {
			float i = x1;
			x1 = x2;
			x2 = i;
		}

		if (y1 < y2) {
			float j = y1;
			y1 = y2;
			y2 = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(matrixStack, x1, y2, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.pos(matrixStack, x2, y2, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.pos(matrixStack, x2, y1, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.pos(matrixStack, x1, y1, 0.0F).color(f, f1, f2, f3).endVertex();
		bufferbuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferbuilder);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}
}
