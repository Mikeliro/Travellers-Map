package net.dark_roleplay.travellers_map2.objects.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.util.Wrapper;
import net.dark_roleplay.travellers_map2.objects.screens.full_map.FullMapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class SidePanelButton extends Button {

	private Wrapper<Boolean> value;
	private IPressable additionalCallback;

	public SidePanelButton(int posX, int posY, Wrapper<Boolean> value, IPressable additionalCallback) {
		super(posX, posY, 15, 23, new TranslationTextComponent("gui.button.travellers_map.sidepanel_hider"), SidePanelButton::toggleActive);
		this.value = value;
		this.additionalCallback = additionalCallback;
	}

	private static void toggleActive(Button btn){
		if(btn instanceof SidePanelButton)
			((SidePanelButton)btn).value.set(!((SidePanelButton)btn).value.get());
		((SidePanelButton)btn).additionalCallback.onPress(btn);
	}

	@Override
	public void func_230431_b_(MatrixStack matrix, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.field_230695_q_);
		int x = this.func_230449_g_() ? 15 : 0;
		int y = this.value.get() ? 23 : 0;
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		this.func_238474_b_(matrix, this.field_230690_l_, this.field_230691_m_, 128 + x, y, this.field_230688_j_, this.field_230689_k_);
		this.func_230441_a_(matrix, minecraft, p_renderButton_1_, p_renderButton_2_);
	}
}
