package net.dark_roleplay.travellers_map.objects.screens.full_map;

import net.dark_roleplay.travellers_map.util.MapManager;
import net.dark_roleplay.travellers_map.util.MapSegment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class FullMapScreen extends Screen {

    public FullMapScreen() {
        super(new TranslationTextComponent("screen.travellers_map.full_map"));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        World world = Minecraft.getInstance().world;
        PlayerEntity player = Minecraft.getInstance().player;

        BlockPos playerPos = player.getPosition();
        int relativeX = playerPos.getX() - (playerPos.getX() & 0b00000000) - 256;
        int relativeZ = playerPos.getZ() - (playerPos.getZ() & 0b00000000) - 256;

        MapSegment map = MapManager.getOrCreateMapSegment(world.getChunk(playerPos.getX() >> 4, playerPos.getZ() >> 4));
        map.getMap().bindTexture();
        map.updadteGPU();


        blit((width/2) - 256 + relativeX, (height/2) - 256 + relativeZ, 512, 512, 0, 0, 256, 256, 256, 256);

        hLine((width/2) -2, (width/2) +2, (height/2) -1, 0xFFFFFFFF);
        vLine((width/2), (height/2) -4, (height/2) +2, 0xFFFFFFFF);

        super.render(mouseX, mouseY, delta);
    }
}
