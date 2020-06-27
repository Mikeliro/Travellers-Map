package net.dark_roleplay.travellers_map2.objects.screens.minimap.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map2.configs.ClientConfig;
import net.dark_roleplay.travellers_map2.objects.huds.GuiAlignment;
import net.dark_roleplay.travellers_map2.objects.huds.hud.Hud;
import net.dark_roleplay.travellers_map2.objects.huds.hud.HudStyle;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.TranslationTextComponent;

public class MinimapMover extends Widget {

	Hud hud;

	int posX;
	int posY;
	GuiAlignment alignment;

	double initOffsetX = 0;
	double initOffsetY = 0;

	public MinimapMover(Hud hud) {
		super(0, 0, 200, 20, new TranslationTextComponent(hud.getUnlocalizedName() + ".mover"));
		this.hud = hud;

		MainWindow window = Minecraft.getInstance().getMainWindow();
		this.alignment = ClientConfig.MINIMAP.ALIGNMENT.get();
		this.posX = (int)(ClientConfig.MINIMAP.POS_X.get() + alignment.getX(window.getScaledWidth()));
		this.posY = (int)(ClientConfig.MINIMAP.POS_Y.get() + alignment.getY(window.getScaledHeight()));
	}

	@Override
	public void func_230982_a_(double mouseX, double mouseY) {
		MainWindow window = Minecraft.getInstance().getMainWindow();

		initOffsetX = posX - mouseX;
		initOffsetY = posY - mouseY;
	}

	@Override
	public void func_230983_a_(double mouseX, double mouseY, double deltaX, double deltaY) {
		HudStyle style = this.hud.getStyle();
		posX = Math.min(Math.max(0, (int)(mouseX + initOffsetX)), Minecraft.getInstance().getMainWindow().getScaledWidth() - style.getWidth());
		posY = Math.min(Math.max(0, (int)(mouseY + initOffsetY)), Minecraft.getInstance().getMainWindow().getScaledHeight() - style.getHeight());
	}

	@Override
	public void func_230431_b_(MatrixStack matrix, int mouseX, int mouseY, float delta) {
		MainWindow window = Minecraft.getInstance().getMainWindow();
		HudStyle style = this.hud.getStyle();

		this.field_230688_j_ = (int)(style.getWidth() * ClientConfig.MINIMAP.SCALE.get());
		this.field_230689_k_ = (int)(style.getWidth() * ClientConfig.MINIMAP.SCALE.get());

		this.field_230690_l_ = posX;
		this.field_230691_m_ = posY;

		RenderSystem.pushMatrix();
		RenderSystem.scaled(ClientConfig.MINIMAP.SCALE.get(), ClientConfig.MINIMAP.SCALE.get(), 1);

		RenderSystem.translatef(field_230690_l_, field_230691_m_, 0);
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
