package net.dark_roleplay.travellers_map2.objects.screens.minimap.settings;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map2.configs.ClientConfig;
import net.dark_roleplay.travellers_map2.handler.StyleManager;
import net.dark_roleplay.travellers_map2.objects.huds.GuiAlignment;
import net.dark_roleplay.travellers_map2.objects.huds.HudStyle;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;

public class MinimapMover extends Widget {

	int posX;
	int posY;
	GuiAlignment alignment;

	double initOffsetX = 0;
	double initOffsetY = 0;

	public MinimapMover(String name) {
		super(0, 0, name);

		MainWindow window = Minecraft.getInstance().getMainWindow();
		this.alignment = ClientConfig.MINIMAP.ALIGNMENT.get();
		this.posX = (int)(ClientConfig.MINIMAP.POS_X.get() + alignment.getX(window.getScaledWidth()));
		this.posY = (int)(ClientConfig.MINIMAP.POS_Y.get() + alignment.getY(window.getScaledHeight()));
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		MainWindow window = Minecraft.getInstance().getMainWindow();

		initOffsetX = posX - mouseX;
		initOffsetY = posY - mouseY;
	}

	@Override
	public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
		posX = (int)(mouseX + initOffsetX);
		posY = (int)(mouseY + initOffsetY);
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float delta) {
		MainWindow window = Minecraft.getInstance().getMainWindow();
		HudStyle style = StyleManager.getSelectedMinimapStyle();

		this.width = (int)(style.getWidth() * ClientConfig.MINIMAP.SCALE.get());
		this.height = (int)(style.getWidth() * ClientConfig.MINIMAP.SCALE.get());

		this.x = posX;
		this.y = posY;

		RenderSystem.pushMatrix();
		RenderSystem.scaled(ClientConfig.MINIMAP.SCALE.get(), ClientConfig.MINIMAP.SCALE.get(), 1);

		RenderSystem.translatef(x, y, 0);
		Minecraft.getInstance().getTextureManager().bindTexture(style.getOverlay());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		BlendBlitHelper.blit(0, 0, style.getWidth(), style.getHeight(), 0, 0, 1, 1, 1, 1);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.popMatrix();
	}

	public void onClose() {MainWindow window = Minecraft.getInstance().getMainWindow();
		int windowWidth = window.getScaledWidth();
		int windowHeight = window.getScaledHeight();
		GuiAlignment alignment = ClientConfig.MINIMAP.ALIGNMENT.get();

		if(posX < windowWidth/3)
			alignment = posY < windowHeight/3 ?
					GuiAlignment.TOP_LEFT : posY < (windowHeight/3) * 2 ?
					GuiAlignment.LEFT : GuiAlignment.BOTTOM_LEFT;
		else if(posX < (windowWidth/3)*2)
			alignment = posY < windowHeight/3 ?
					GuiAlignment.TOP : posY < (windowHeight/3) * 2 ?
					GuiAlignment.CENTER : GuiAlignment.BOTTOM;
		else
			alignment = posY < windowHeight/3 ?
					GuiAlignment.TOP_RIGHT : posY < (windowHeight/3) * 2 ?
					GuiAlignment.RIGHT : GuiAlignment.BOTTOM_RIGHT;


		int newX = posX - alignment.getX(window.getScaledWidth());
		int newY = posY - alignment.getY(window.getScaledHeight());

		ClientConfig.MINIMAP.ALIGNMENT.set(alignment);
		ClientConfig.MINIMAP.POS_X.set(newX);
		ClientConfig.MINIMAP.POS_Y.set(newY);
	}
}
