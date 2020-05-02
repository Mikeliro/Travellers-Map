package net.dark_roleplay.travellers_map.handler;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.dark_roleplay.travellers_map.objects.screens.full_map.FullMapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, value = Dist.CLIENT)
public class TravellersKeybinds {

    public static KeyBinding OPEN_MAP = new KeyBinding("travellers_map.map.open", GLFW.GLFW_KEY_M, "key.categories.misc");

    @SubscribeEvent
    public static void keyListeners(InputEvent.KeyInputEvent event){
        if(OPEN_MAP.isPressed()){
            Minecraft.getInstance().displayGuiScreen(new FullMapScreen());
        }
    }

    public static void registerKeybinds(FMLClientSetupEvent event){
        ClientRegistry.registerKeyBinding(OPEN_MAP);
    }
}
