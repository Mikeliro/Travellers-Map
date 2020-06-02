package net.dark_roleplay.travellers_map2.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import net.dark_roleplay.travellers_map2.objects.huds.hud.HudStyle;
import net.dark_roleplay.travellers_map2.objects.huds.minimap.MinimapHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceReloadListener {

	private static final Gson GSON;

	static{
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		GSON = builder.create();
	}

	public static void run() {
		if (Minecraft.getInstance() == null) return; //Nop out, running DataGen

		if (Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(ResourceReloadListener::reload);
		}
	}

	private static CompletableFuture<Void> reload(IFutureReloadListener.IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor){
		Set<HudStyle> styles = new HashSet<>();
		return CompletableFuture.<Void>supplyAsync(() -> {
			Collection<ResourceLocation> minimapStyles = resourceManager.getAllResourceLocations("travellers_map/styles/minimap", val -> true);

			for(ResourceLocation styleLoc : minimapStyles){
				try {
					IResource resource = resourceManager.getResource(styleLoc);
					try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(resource.getInputStream())))) {
						HudStyle style = GSON.fromJson(reader, HudStyle.class);
						styles.add(style);
					}catch (JsonIOException | JsonSyntaxException e){
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			MinimapHUD.INSTANCE.refreshStyles(styles);
			return null;
		}).thenCompose(stage::markCompleteAwaitingOthers);
	}
}
