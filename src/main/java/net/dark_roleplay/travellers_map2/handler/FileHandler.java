package net.dark_roleplay.travellers_map2.handler;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.File;

public class FileHandler {
    public static File MOD_DATA_DIR;

    public static void clientSetup(FMLClientSetupEvent event){
        MOD_DATA_DIR = new File(event.getMinecraftSupplier().get().gameDir, "/mod_data/travellers_map/");
        if(!MOD_DATA_DIR.exists()){
            MOD_DATA_DIR.mkdirs();
        }
    }
}
