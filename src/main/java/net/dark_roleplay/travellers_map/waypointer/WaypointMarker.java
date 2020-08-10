package net.dark_roleplay.travellers_map.waypointer;

import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class WaypointMarker {

    private String internalName;
    private String displayName;

    private ResourceLocation compassIcon;
    private ResourceLocation compassIconTint;

    private ResourceLocation mapIcon;
    private ResourceLocation mapIconColored;

    public WaypointMarker(JsonObject json){
        internalName = JSONUtils.getString(json, "name", "");
        displayName = JSONUtils.getString(json, "displayName", internalName);
        compassIcon = locOrNull(JSONUtils.getString(json, "compassIcon", null), null);
        compassIconTint = locOrNull(JSONUtils.getString(json, "compassIconTint", null), null);
        mapIcon = locOrNull(JSONUtils.getString(json, "compassIcon", null), compassIcon);
        mapIconColored = locOrNull(JSONUtils.getString(json, "compassIconTint", null), compassIconTint);
    }

    private ResourceLocation locOrNull(String loc, ResourceLocation fallback){
        return loc == null ? fallback != null ? fallback : null : new ResourceLocation(loc);
    }

    public ResourceLocation getCompassIcon(){
        return compassIcon;
    }

    public ResourceLocation getCompassIconTint(){
        return compassIconTint;
    }

    public ResourceLocation getMapIcon(){
        return mapIcon;
    }

    public ResourceLocation getMapIconTint(){
        return mapIconColored;
    }

    public void gatherTextures(Set<ResourceLocation> textures){
        if(compassIcon != null) textures.add(compassIcon);
        if(compassIconTint != null) textures.add(compassIconTint);
        if(mapIcon != null) textures.add(mapIcon);
        if(mapIconColored != null) textures.add(mapIconColored);
    }
}
