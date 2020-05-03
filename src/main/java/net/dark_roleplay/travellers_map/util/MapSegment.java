package net.dark_roleplay.travellers_map.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map.objects.data.IMapSegmentTicket;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class MapSegment {

    //IO Parts
    private File mapFile;
    private long identifier;


    private DynamicTexture dynTexture;
    private NativeImage mapImage;
    private ResourceLocation mapLocation;
    private Set<IMapSegmentTicket> tickets = new HashSet<>();
    boolean dirtyIO = false;
    boolean dirtyGPU = false;
    private String segmentName;

    public MapSegment(String segmentName, File mapFile, long identifier, IMapSegmentTicket... tickets){
        this.segmentName = segmentName;
        this.mapFile = mapFile;
        this.identifier = identifier;
        try {
            if(mapFile.exists()) this.mapImage = NativeImage.read(new FileInputStream(mapFile));
            else this.mapImage = new NativeImage(NativeImage.PixelFormat.RGBA, 512, 512, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(IMapSegmentTicket ticket : tickets)
            this.tickets.add(ticket);
    }

    public void addTicket(IMapSegmentTicket ticket) {
        this.tickets.add(ticket);
    }

    public DynamicTexture getDynTexture() {
        if(this.dynTexture == null){
            this.dynTexture = new DynamicTexture(this.mapImage);
            this.mapLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(this.segmentName, this.dynTexture);
        }
        return dynTexture;
    }

    public void updateChunk(IChunk chunk){
        BlockPos.PooledMutable pos = BlockPos.PooledMutable.retain();
        int x = Math.floorMod(chunk.getPos().x, 32) * 16, z = Math.floorMod(chunk.getPos().z, 32) * 16;
        for(int x2 = 0; x2 < 16; x2++){
            for(int z2 = 0; z2 < 16; z2++){
                int y = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, x2, z2);
                BlockState state = chunk.getBlockState(pos.setPos(x2, y, z2));
                MaterialColor color = state.getMaterialColor(Minecraft.getInstance().world, pos);
                if(color != null)
                    mapImage.setPixelRGBA(x + x2, z + z2, (color.getMapColor(4)));
            }
        }
        pos.close();
    }

    public void markDirty(){
        this.dirtyIO = true;
        this.dirtyGPU = true;
    }

    public void updadteGPU() {
        if(this.dirtyGPU){
            this.dynTexture.updateDynamicTexture();
            this.dirtyGPU = false;
        }
    }

    public void update(){
        if(this.dirtyIO){
            try {
                this.mapImage.write(this.mapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.dirtyIO = false;
        }

        Iterator<IMapSegmentTicket> iter = tickets.iterator();
        while(iter.hasNext())
            if(!iter.next().isActive()) iter.remove();

        if(tickets.isEmpty()){
            this.free();
        }
    }

    public void free(){
        RenderSystem.recordRenderCall(() -> Minecraft.getInstance().getTextureManager().deleteTexture(this.mapLocation));
        MapManager.freeMapSegment(this.identifier);
    }
}
