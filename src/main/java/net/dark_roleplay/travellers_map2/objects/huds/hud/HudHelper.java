package net.dark_roleplay.travellers_map2.objects.huds.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dark_roleplay.travellers_map2.configs.client.HudConfig;
import net.dark_roleplay.travellers_map2.objects.huds.minimap.MinimapHUD;

import java.util.function.Predicate;

public class HudHelper {

    private Hud hud;
    private HudConfig config;
    private Predicate<Void>[] predicates;

    public HudHelper(Hud hud, Predicate<Void>... predicates){
        this.hud = hud;
        this.config = hud.config;
        this.predicates = predicates;
    }

    public void render(int width, int height, float partialTicks){
        if(!shouldRender()) return;

        RenderSystem.pushMatrix();
        int posX = config.ALIGNMENT.get().getX(width) + config.POS_X.get();
        int posY = config.ALIGNMENT.get().getY(height) + config.POS_Y.get();
        RenderSystem.translatef(posX, posY, 0);
        hud.setWindowSize(width, height);
        hud.render(0, 0, partialTicks);

        RenderSystem.popMatrix();
    }

    private boolean shouldRender(){
        if(!config.VISIBLE.get()) return false;

        for(Predicate<Void> predicate : predicates)
            if(!predicate.test(null)) return false;

        return true;
    }
}
