package net.dark_roleplay.travellers_map.objects.huds.hud;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.configs.client.HudConfig;
import net.dark_roleplay.travellers_map.objects.style.HudStyle;
import net.dark_roleplay.travellers_map.objects.style.HudStyleProvider;
import net.minecraft.client.gui.AbstractGui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Hud extends AbstractGui {

    protected int wWidth;
    protected int wHeight;
    protected HudStyleProvider styleProvider;

    protected HudConfig config;
    private String unlocalizedName;

    protected Hud(HudConfig config, String unlocalizedName, HudStyleProvider styleProvider){
        this.config = config;
        this.unlocalizedName = unlocalizedName;
        this.styleProvider = styleProvider;
    }

    public HudStyleProvider getStyleProvider(){
        return this.styleProvider;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public void setWindowSize(int width, int height) {
        this.wWidth = width;
        this.wHeight = height;
    }

    public abstract void render(MatrixStack matrix, int mouseX, int mouseY, float delta);
}

