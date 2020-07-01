package net.dark_roleplay.travellers_map2.util;

import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.net.InetSocketAddress;

public class MapFileHelper {

	private static boolean IS_MULTIPLAYER = false;

	private static File SP_FOLDER;
	private static File MP_FOLDER;

	private static File ACTIVE_FOLDER;
	private static File DIM_FOLDER;

	static{
		File baseFolder = new File("./mod_data/travellers_map/");
		SP_FOLDER = new File(baseFolder, "singleplayer");
		MP_FOLDER = new File(baseFolder, "multiplayer");

		SP_FOLDER.mkdirs();
		MP_FOLDER.mkdirs();
	}

	public static void joinServer(InetSocketAddress socket){
		IS_MULTIPLAYER = true;

		ACTIVE_FOLDER = new File(MP_FOLDER, socket.getHostName() + "_" +  socket.getPort());
		ACTIVE_FOLDER.mkdirs();
	}

	public static void leaveServer(){
		IS_MULTIPLAYER = false;
		ACTIVE_FOLDER = null;
	}

	public static void joinDimension(ResourceLocation loc){
		if(ACTIVE_FOLDER == null){
			ACTIVE_FOLDER = new File(SP_FOLDER, "temp");
			ACTIVE_FOLDER.mkdirs();
			//TODO Implement client side folders
		}

		DIM_FOLDER = new File(ACTIVE_FOLDER, loc.getNamespace() + "_" + loc.getPath());
		DIM_FOLDER.mkdirs();
	}
}
