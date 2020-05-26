package net.dark_roleplay.travellers_map2.objects.screens.minimap.settings;

import net.dark_roleplay.travellers_map.util.Wrapper;
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
	protected void init() {
		if(mover != null) mover.onClose();
		stylePanel = new StylesPanel(this.minecraft, 5, 5, 118, this.height - 10);
		this.addButton(mover = new MinimapMover("screen.travellers_map.minimap_positioning"));
		this.addButton(new SidePanelButton(-2, (this.height - 15) / 2, isStlyeListOpen, btn -> {
			if(isStlyeListOpen.get()){
				this.children.add(stylePanel);
				btn.x = 125;
			}else{
				this.children.remove(stylePanel);
				btn.x = -2;
			}
		}));
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		//this.renderDirtBackground(0);

		super.render(mouseX, mouseY, delta);

		if(isStlyeListOpen.get()){
			Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
			blit(0, 0, 128, this.height, 0, 0, 128, 256, 256, 256);
			stylePanel.render(mouseX, mouseY, delta);
		}
	}

	@Override
	public void onClose() {
		if(mover != null) mover.onClose();
		this.minecraft.displayGuiScreen(parentScreen);
	}
}
