package net.dark_roleplay.travellers_map2.objects.screens.minimap.settings;

import net.dark_roleplay.travellers_map.util.BlendBlitHelper;
import net.dark_roleplay.travellers_map2.handler.StyleManager;
import net.dark_roleplay.travellers_map2.objects.huds.HudStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.client.gui.ScrollPanel;

public class StylesPanel extends ScrollPanel {

	private int spacing = 5;

	public StylesPanel(Minecraft client, int x, int y, int width, int height) {
		super(client, width, height, x, y);
	}

	@Override
	protected int getContentHeight() {
		int height = 0;
		for(HudStyle style : StyleManager.HUD_STYLES.values())
			height += style.getHeight() + spacing;
		return height;
	}

	@Override
	protected void drawPanel(int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
		int offset = 5;
		for(HudStyle style : StyleManager.HUD_STYLES.values()){
			int elemTop = (int)(this.top + offset - this.scrollDistance);
			offset += style.getHeight() + spacing;
			if(elemTop + style.getHeight() < this.top || elemTop > this.top + this.height) continue;

			Minecraft.getInstance().getTextureManager().bindTexture(style.getOverlay());

			if(mouseX >= this.left && mouseX <= this.right  - 6 && mouseY >= elemTop - 2 && mouseY <= elemTop + style.getHeight() + 2){
				fill(this.left, elemTop - 2, this.right, elemTop + style.getHeight() + 2, 0xFF202020);
			}

			BlendBlitHelper.blit(this.left + (this.width - style.getWidth()) / 2, elemTop, style.getWidth(), style.getHeight(), 0, 0, 1, 1, 1, 1);
		}
	}


	@Override
	protected boolean clickPanel(double mouseX, double mouseY, int button) {
		int offset = 5;
		for(HudStyle style : StyleManager.HUD_STYLES.values()) {
			int elemTop = (int) (this.top + offset - this.scrollDistance);
			offset += style.getHeight() + spacing;
			if (elemTop + style.getHeight() < this.top || elemTop > this.top + this.height) continue;

			if(mouseX >= this.left && mouseX <= this.right  - 6 && mouseY >= elemTop - 2 && mouseY <= elemTop + style.getHeight() + 2){
				StyleManager.selectStyle(style);
				Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				System.out.println(style.getStyleName());
				return true;
			}
		}
		return false;
	}
}
