package net.dark_roleplay.travellers_map.listeners;

import net.dark_roleplay.travellers_map.TravellersMap;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FileUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.fml.packs.ResourcePackLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Mod.EventBusSubscriber(modid = TravellersMap.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DataPackTest {

	@SubscribeEvent
	public static void serverStarting(FMLServerStarted event) {
		((ResourcePackList<?>)
				ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, event.getServer(), "field_195577_ad"))
				.addPackFinder(new ResourcePackLoader.LambdaFriendlyPackFinder(packFi));
	}

	@SubscribeEvent
	public static void reloadListener(AddReloadListenerEvent event) {
		event.getDataPackRegistries().
		ModFile modFile = ModList.get().getModFileById("travellers_map").getFile();
		ModFileResourcePack pack = new ModFileResourcePack(modFile.getLocator());

		event.addListener(new
//		ModList.get().getModFiles().stream().
//				map(mf -> new ModFileResourcePack(mf.getFile())).
//				collect(Collectors.toMap(ModFileResourcePack::getModFile, Function.identity(), (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); },  LinkedHashMap::new));
//		resourcePacks.addPackFinder(new ResourcePackLoader.LambdaFriendlyPackFinder(packFinder.apply(modResourcePacks, ModFileResourcePack::setPackInfo)));
//
//		event.addListener()
	}

	private static class SubModFileResourcePack extends ModFileResourcePack {

		private String subFolder = "conditionals/";
		private ModFile modFile;

		public SubModFileResourcePack(ModFile modFile, String subFolder) {
			super(modFile);
			this.subFolder += subFolder + "/";
			this.modFile = modFile;
		}

		@Override
		public InputStream getInputStream(String name) throws IOException
		{
			final Path path = modFile.getLocator().findPath(modFile, subFolder + name);
			return Files.newInputStream(path, StandardOpenOption.READ);
		}

		@Override
		public boolean resourceExists(String name)
		{
			return Files.exists(modFile.getLocator().findPath(modFile, subFolder + name));
		}
	}
}
