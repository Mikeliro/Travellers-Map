package net.dark_roleplay.travellers_map.util;

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
import java.util.ArrayList;
import java.util.List;

public class MapSegment {

    //IO Parts
    private File mapFile;

    private DynamicTexture map;
    private NativeImage mapImage;
    private ResourceLocation mapLocation;
    private List<IChunk> chunks = new ArrayList<>();
    boolean dirtyIO = false;
    boolean dirtyGPU = false;
    private String segmentName = "";

    public MapSegment(String segmentName, File mapFile){
        this.mapFile = mapFile;
        this.segmentName = segmentName;
        try {
            if(mapFile.exists()) this.mapImage = NativeImage.read(new FileInputStream(mapFile));
            else this.mapImage = new NativeImage(512, 512, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addListeningChunk(IChunk chunk){
        this.chunks.add(chunk);
    }

    public void markDirty(){
        this.dirtyIO = true;
        this.dirtyGPU = true;
    }

    public void writeToFile(){
        if(this.dirtyIO){
            try {
                this.mapImage.write(this.mapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.dirtyIO = false;
        }
    }

    public void updadteGPU() {
        if(this.dirtyGPU){
            this.map.updateDynamicTexture();
            this.dirtyGPU = false;
        }
    }

    public DynamicTexture getMap() {
        if(this.map == null){
            this.map = new DynamicTexture(this.mapImage);
            this.mapLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(this.segmentName, this.map);
        }
        return map;
    }

    public File getMapFile() {
        return mapFile;
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
}
