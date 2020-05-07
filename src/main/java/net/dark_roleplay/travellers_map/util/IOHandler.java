package net.dark_roleplay.travellers_map.util;

import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public class IOHandler {

    public static File MOD_DATA_DIR;

    public static void clientSetup(FMLClientSetupEvent event){
        MOD_DATA_DIR = new File(event.getMinecraftSupplier().get().gameDir, "/mod_data/travellers_map/");
        if(!MOD_DATA_DIR.exists()){
            MOD_DATA_DIR.mkdirs();
        }
    }

    public static File getOrCreateUniqueFolder(UUID uniqueID){
        File worldFile = new File(MOD_DATA_DIR, "/world_" + uniqueID.toString());
        worldFile.mkdirs();
        return worldFile;
    }
}
