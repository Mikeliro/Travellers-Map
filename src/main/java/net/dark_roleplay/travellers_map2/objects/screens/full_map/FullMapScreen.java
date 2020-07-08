package net.dark_roleplay.travellers_map2.objects.screens.full_map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.api.util.MapRenderInfo;
import net.dark_roleplay.travellers_map.util.Wrapper;
import net.dark_roleplay.travellers_map2.objects.screens.SidePanelButton;
import net.dark_roleplay.travellers_map2.objects.screens.minimap.settings.MinimapSettingsScreen;
import net.dark_roleplay.travellers_map2.objects.screens.waypoints.WayPointCreationScreen;
import net.dark_roleplay.travellers_map2.objects.screens.waypoints.WaypointScrollPanel;
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

        float halfWidth = this.width/2F;
        float halfHeight = this.height/2F;

        float scale = zoomLevels[currentZoomLevel];


        matrix.push();
        matrix.translate(halfWidth, halfHeight, 0);
        matrix.scale(scale, scale, scale);

        //Map
        BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();
        MapRenderer renderer = new MapRenderer();

        mapRenderInfo.update(this.width, height, scale, playerPos.add(xOffset, 0 , zOffset));

        renderer.renderMap(matrix, mapRenderInfo);

        //Player Marker
        Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);

        matrix.translate(-xOffset, -zOffset, 0);

        //TODO Get Player Marker rotation working plz
        float yaw = (float) Math.toRadians(Minecraft.getInstance().player.getYaw(delta) - 180) /2F;
        //matrix.rotate(new Quaternion(0, 0, (float)Math.sin(yaw), (float)Math.cos(yaw)));
        matrix.rotate(new Quaternion(0, 0, (float)Math.sin(yaw), (float)Math.cos(yaw)));
        blit(matrix, -2, -4, 158, 0, 5, 7);

//
//        q.w = (cz*1*1));
//        q.x = 0;
//        q.y = 0;
//        q.z = (sz*1*1);

        matrix.pop();




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
