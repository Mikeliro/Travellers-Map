package net.dark_roleplay.travellers_map2.objects.screens.full_map;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.api.rendering.MapType;
import net.dark_roleplay.travellers_map.api.util.MapRenderInfo;
import net.dark_roleplay.travellers_map.mapping.tickets.RenderTicket;
import net.dark_roleplay.travellers_map.util.Wrapper;
import net.dark_roleplay.travellers_map2.objects.screens.SidePanelButton;
import net.dark_roleplay.travellers_map2.objects.screens.minimap.settings.MinimapSettingsScreen;
import net.dark_roleplay.travellers_map2.objects.screens.waypoints.WayPointCreationScreen;
import net.dark_roleplay.travellers_map2.objects.screens.waypoints.WaypointScrollPanel;
import net.dark_roleplay.travellers_map2.rendering.MapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.TranslationTextComponent;

public class FullMapScreen extends Screen {
    public static ResourceLocation FULL_MAP_TEXTURES = new ResourceLocation(TravellersMap.MODID, "textures/guis/full_map.png");

    private float xOffset = 0;
    private float zOffset = 0;
    private int currentZoomLevel = 1;

    private MapRenderInfo mapRenderInfo = new MapRenderInfo();

    private Wrapper<Boolean> isWaypointListOpen = new Wrapper(false);

    private WaypointScrollPanel scrollPanel;

    private float[] zoomLevels = new float[]{2.0F, 1.0F, 0.5F, 0.25F};

    public FullMapScreen(){
        super(new TranslationTextComponent("screen.travellers_map.full_map"));
        BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();
    }

    @Override
    protected void init() {
        scrollPanel = new WaypointScrollPanel(this.minecraft, this, 118, this.height - 35, 5, 5);
        Button waypointButton = new Button(5, this.height - 25, 118, 20, new TranslationTextComponent("New Waypoint"), button -> {
            this.minecraft.displayGuiScreen(new WayPointCreationScreen(this, null));
        });

        this.addButton(new SettingsButton(this.width - 13, 1, btn -> {
            Minecraft.getInstance().displayGuiScreen(new MinimapSettingsScreen(this));
        }));

        this.addButton(new SidePanelButton(isWaypointListOpen.get() ? 125 : -2, (this.height - 23) / 2, isWaypointListOpen, btn -> {
            if(isWaypointListOpen.get()){
                this.children.add(scrollPanel);
                this.addButton(waypointButton);
                btn.x = 125;
            }else{
                this.buttons.remove(scrollPanel);
                btn.x = -2;
                this.buttons.remove(waypointButton);
                this.buttons.remove(waypointButton);
            }
        }));

        if(isWaypointListOpen.get()){
            this.children.add(scrollPanel);
            this.addButton(waypointButton);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        this.renderDirtBackground(0);

        //Map
        BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();
        mapRenderInfo.update(this.width, this.height, 1/zoomLevels[currentZoomLevel], playerPos.add(xOffset, 0 , zOffset));

        MapRenderer.renderMap(matrix, mapRenderInfo, MapType.FULL_MAP, false, delta);


        if(isWaypointListOpen.get()){
            blit(matrix, 0, 0, 128, this.height, 0, 0, 128, 256, 256, 256);
            scrollPanel.render(matrix, mouseX, mouseY, delta);
        }

        super.render(matrix, mouseX, mouseY, delta);
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        boolean success = super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
        if(success) return success;

        xOffset -= deltaX / zoomLevels[currentZoomLevel];
        zOffset -= deltaY / zoomLevels[currentZoomLevel];
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        boolean success = super.mouseScrolled(mouseX, mouseY, scroll);
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
        }

    }

    public void decreaseZoom(){
        if(this.currentZoomLevel < this.zoomLevels.length - 1){
            this.currentZoomLevel += 1;
        }
    }
}
