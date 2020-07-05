package net.dark_roleplay.travellers_map2.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.SaveFormat;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class MapFileHelper {

	private static File SP_FOLDER;
	private static File MP_FOLDER;

	private static File ACTIVE_FOLDER;
	private static File DIM_FOLDER;

	private static File WAYPOINT_FOLDER;

	static{
		File baseFolder = new File("./mod_data/travellers_map/");
		SP_FOLDER = new File(baseFolder, "singleplayer");
		MP_FOLDER = new File(baseFolder, "multiplayer");

		SP_FOLDER.mkdirs();
		MP_FOLDER.mkdirs();
	}

	public static void setupBaseMapFolder(ResourceLocation dimensionLoc){
		if(Minecraft.getInstance().isIntegratedServerRunning()){
			SaveFormat.LevelSave saveFile = Minecraft.getInstance().getIntegratedServer().anvilConverterForAnvilFile;
			ACTIVE_FOLDER = new File(SP_FOLDER, saveFile.func_237282_a_());
		}else{
			SocketAddress socket = Minecraft.getInstance().getConnection().getNetworkManager().getRemoteAddress();
			if(socket instanceof InetSocketAddress){
				InetSocketAddress inet = (InetSocketAddress) socket;
				ACTIVE_FOLDER = new File(MP_FOLDER, inet.getHostName() + "_" +  inet.getPort());
			}
		}

		WAYPOINT_FOLDER = new File(ACTIVE_FOLDER, "waypoints");
		WAYPOINT_FOLDER.mkdirs();

		DIM_FOLDER = new File(ACTIVE_FOLDER, dimensionLoc.getNamespace() + "_" + dimensionLoc.getPath() + "/default_mapper");
		DIM_FOLDER.mkdirs();
	}

	public static File getDimFolder(){
		return DIM_FOLDER;
	}

	public static File getWaypointFolder(){
		return WAYPOINT_FOLDER;
	}
}
