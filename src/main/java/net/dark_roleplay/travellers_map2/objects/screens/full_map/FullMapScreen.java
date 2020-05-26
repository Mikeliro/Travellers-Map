package net.dark_roleplay.travellers_map2.objects.screens.full_map;

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
        BlockPos playerPos = Minecraft.getInstance().player.getPosition();
        this.xOffset = (playerPos.getX()) * zoomLevels[currentZoomLevel];
        this.zOffset = (playerPos.getZ()) * zoomLevels[currentZoomLevel];
    }

    @Override
    protected void init() {
        scrollPanel = new WaypointScrollPanel(this.minecraft, this, 118, this.height - 35, 5, 5);
        Button waypointButton = new Button(5, this.height - 25, 118, 20, "New Waypoint", button -> {
            this.minecraft.displayGuiScreen(new WayPointCreationScreen(this, null));
        });

        this.addButton(new SettingsButton(this.width - 13, 1, btn -> {
            Minecraft.getInstance().displayGuiScreen(new MinimapSettingsScreen(this));
        }));

        this.addButton(new SidePanelButton(-2, (this.height - 15) / 2, isWaypointListOpen, btn -> {
            if(isWaypointListOpen.get()){
                this.children.add(scrollPanel);
                this.addButton(waypointButton);
                btn.x = 125;
            }else{
                this.children.remove(scrollPanel);
                btn.x = -2;
                this.buttons.remove(waypointButton);
                this.children.remove(waypointButton);
            }
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {

        this.renderDirtBackground(0);

        BlockPos playerPos = Minecraft.getInstance().player.getPosition();

        int xSegmentCount = (int) Math.ceil(width / (512F * zoomLevels[currentZoomLevel])) + 1;
        int zSegmentCount = (int) Math.ceil(width / (512F * zoomLevels[currentZoomLevel])) + 1;

        int segX0 = (int)(xOffset - (width/2) * (1/zoomLevels[currentZoomLevel])) >> 9;
        int segZ0 = (int)(zOffset - (width/2) * (1/zoomLevels[currentZoomLevel])) >> 9;
        for(int x = 0; x < xSegmentCount; x++){
            for(int z = 0; z < zSegmentCount; z++) {
                MapSegment map = MapManager.getMapSegment(MapSegmentUtil.toSegment(segX0 + x, segZ0 + z));
                renderSegment(map);
            }
        }

        if(isWaypointListOpen.get()){
            Minecraft.getInstance().getTextureManager().bindTexture(FullMapScreen.FULL_MAP_TEXTURES);
            blit(0, 0, 128, this.height, 0, 0, 128, 256, 256, 256);
            scrollPanel.render(mouseX, mouseY, delta);
        }

        super.render(mouseX, mouseY, delta);
    }

    public void renderSegment(MapSegment map){
        if(map != null ) {
            map.getDynTexture().bindTexture();
            map.updadteGPU();
            float segX = ((int)((map.getIdent() >> 32) & 0xFFFFFFFFL)) * (512 * zoomLevels[currentZoomLevel]);
            float segZ = ((int)(map.getIdent() & 0xFFFFFFFFL)) * (512 * zoomLevels[currentZoomLevel]);
            BlendBlitHelper.blit((width / 2) - xOffset + segX, (height / 2) - zOffset + segZ, (int) (512 * zoomLevels[currentZoomLevel]), (int) (512 * zoomLevels[currentZoomLevel]), 0, 0, 256, 256, 256, 256);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        boolean success = super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
        if(success) return success;

        xOffset -= deltaX;
        zOffset -= deltaY;
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
            this.xOffset *= 2;
            this.zOffset *= 2;
        }

    }

    public void decreaseZoom(){
        if(this.currentZoomLevel < this.zoomLevels.length - 1){
            this.currentZoomLevel += 1;
            this.xOffset /= 2;
            this.zOffset /= 2;
        }
    }
}
