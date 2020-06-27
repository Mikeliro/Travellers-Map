package net.dark_roleplay.travellers_map2.objects.screens.full_map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.util.*;
import net.dark_roleplay.travellers_map2.objects.screens.SidePanelButton;
import net.dark_roleplay.travellers_map2.objects.screens.minimap.settings.MinimapSettingsScreen;
import net.dark_roleplay.travellers_map2.objects.screens.waypoints.WayPointCreationScreen;
import net.dark_roleplay.travellers_map2.objects.screens.waypoints.WaypointScrollPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

public class FullMapScreen extends Screen {
    public static ResourceLocation FULL_MAP_TEXTURES = new ResourceLocation(TravellersMap.MODID, "textures/guis/full_map.png");

    private float xOffset = 0;
    private float zOffset = 0;
    private int currentZoomLevel = 1;

    private Wrapper<Boolean> isWaypointListOpen = new Wrapper(false);

    private WaypointScrollPanel scrollPanel;

    private float[] zoomLevels = new float[]{2.0F, 1.0F, 0.5F, 0.25F};

    public FullMapScreen(){
        super(new TranslationTextComponent("screen.travellers_map.full_map"));
        BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();
    }

    @Override
    protected void func_231160_c_() {
        scrollPanel = new WaypointScrollPanel(this.field_230706_i_, this, 118, this.field_230709_l_ - 35, 5, 5);
        Button waypointButton = new Button(5, this.field_230709_l_ - 25, 118, 20, new TranslationTextComponent("New Waypoint"), button -> {
            this.field_230706_i_.displayGuiScreen(new WayPointCreationScreen(this, null));
        });

        this.func_230480_a_(new SettingsButton(this.field_230708_k_ - 13, 1, btn -> {
            Minecraft.getInstance().displayGuiScreen(new MinimapSettingsScreen(this));
        }));

        this.func_230480_a_(new SidePanelButton(isWaypointListOpen.get() ? 125 : -2, (this.field_230709_l_ - 23) / 2, isWaypointListOpen, btn -> {
            if(isWaypointListOpen.get()){
                this.field_230705_e_.add(scrollPanel);
                this.func_230480_a_(waypointButton);
                btn.field_230690_l_ = 125;
            }else{
                this.func_231039_at__().remove(scrollPanel);
                btn.field_230690_l_ = -2;
                this.func_231039_at__().remove(waypointButton);
                this.func_231039_at__().remove(waypointButton);
            }
        }));

        if(isWaypointListOpen.get()){
            this.field_230705_e_.add(scrollPanel);
            this.func_230480_a_(waypointButton);
        }
    }

    @Override
    public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        this.func_231165_f_(0);

        RenderSystem.pushMatrix();

        float halfWidth = this.field_230708_k_/2F;
        float halfHeight = this.field_230709_l_/2F;

        RenderSystem.translatef(halfWidth, halfHeight, 0);
        RenderSystem.scalef(zoomLevels[currentZoomLevel], zoomLevels[currentZoomLevel], 1);

        BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();
        MapRenderer renderer = new MapRenderer();

        float zoom = zoomLevels[currentZoomLevel];
        renderer.renderMap(
              playerPos.add(xOffset, 0, zOffset),
              (int)(-halfWidth * zoom),
              (int)(-halfHeight * zoom),
              (int)(halfWidth * zoom),
              (int)(halfHeight * zoom), zoomLevels[currentZoomLevel]);

        Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
        RenderSystem.translatef((int)-xOffset, (int)-zOffset, 0);
        RenderSystem.rotatef(Minecraft.getInstance().player.getYaw(delta) - 180, 0, 0, 1);
        func_238474_b_(matrix, -2, -4, 158, 0, 5, 7);

        RenderSystem.popMatrix();

        if(isWaypointListOpen.get()){
            func_238466_a_(matrix, 0, 0, 128, this.field_230709_l_, 0, 0, 128, 256, 256, 256);
            scrollPanel.func_230430_a_(matrix, mouseX, mouseY, delta);
        }

        super.func_230430_a_(matrix, mouseX, mouseY, delta);
    }


    @Override
    public boolean func_231045_a_(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        boolean success = super.func_231045_a_(mouseX, mouseY, mouseButton, deltaX, deltaY);
        if(success) return success;

        xOffset -= deltaX / zoomLevels[currentZoomLevel];
        zOffset -= deltaY / zoomLevels[currentZoomLevel];
        return true;
    }

    @Override
    public boolean func_231043_a_(double mouseX, double mouseY, double scroll) {
        boolean success = super.func_231043_a_(mouseX, mouseY, scroll);
        if(success) return success;

        if(scroll > 0){
            this.increaseZoom();
        }else if(scroll < 0){
            this.decreaseZoom();
        }
        return true;
    }

    public void increaseZoom(){
        if(this.currentZoomLevel > 0){
            this.currentZoomLevel -= 1;
            //this.xOffset *= 2;
            //this.zOffset *= 2;
        }

    }

    public void decreaseZoom(){
        if(this.currentZoomLevel < this.zoomLevels.length - 1){
            this.currentZoomLevel += 1;
            //this.xOffset /= 2;
           // this.zOffset /= 2;
        }
    }
}
