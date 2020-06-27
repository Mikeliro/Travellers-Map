package net.dark_roleplay.travellers_map2.objects.screens.minimap.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.util.Wrapper;
import net.dark_roleplay.travellers_map2.objects.huds.minimap.MinimapHUD;
import net.dark_roleplay.travellers_map2.objects.screens.SidePanelButton;
import net.dark_roleplay.travellers_map2.objects.screens.full_map.FullMapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.gui.ScrollPanel;

public class MinimapSettingsScreen extends Screen {

	private ScrollPanel stylePanel;
	private Screen parentScreen;
	private MinimapMover mover;

	private Wrapper<Boolean> isStlyeListOpen = new Wrapper(false);

	public MinimapSettingsScreen(Screen parentScreen) {
		super(new TranslationTextComponent("screen.travellers_map.minimap_settings"));
		this.parentScreen = parentScreen;
	}

	@Override
	protected void func_231160_c_() {
		if(mover != null) mover.onClose();
		stylePanel = new StylesPanel(this.field_230706_i_, 5, 5, 118, this.field_230709_l_ - 10, MinimapHUD.INSTANCE);
		this.func_230480_a_(mover = new MinimapMover(MinimapHUD.INSTANCE));
		this.func_230480_a_(new SidePanelButton(-2, (this.field_230709_l_ - 23) / 2, isStlyeListOpen, btn -> {
			if(isStlyeListOpen.get()){
				this.field_230705_e_.add(stylePanel);
				btn.field_230690_l_ = 125;
			}else{
				this.func_231039_at__().remove(stylePanel);
				btn.field_230690_l_ = -2;
			}
		}));
	}

	@Override
	public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float delta) {
		//this.renderDirtBackground(0);

		super.func_230430_a_(matrix, mouseX, mouseY, delta);

		if(isStlyeListOpen.get()){
			Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
			func_238466_a_(matrix, 0, 0, 128, this.field_230709_l_, 0, 0, 128, 256, 256, 256);
			stylePanel.func_230430_a_(matrix, mouseX, mouseY, delta);
		}
	}

	@Override
	public void func_231175_as__() {
		if(mover != null) mover.onClose();
		this.field_230706_i_.displayGuiScreen(parentScreen);
	}
}
