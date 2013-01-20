package powertools.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import powertools.login.LoginTickHandler;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "powertools_shared", name = "Power Tools", version = "0.1.4")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)

public class PowerTools
{
	public static MinecraftServer server;
	public static PlayerInfo[] plInfo = new PlayerInfo[8192];
	public static int numberOfPlayers = 0;
	public static Date date = new Date();
	
	public static boolean powerLoginLoaded = false;
	public static boolean chunkProtectionLoaded = false;
	
	public static String testString = "";

	public static int cfgHighestDimension;
	public static MapLimit cfgMapLimitNether = new MapLimit();
	public static MapLimit[] cfgMapLimitDimemsion = new MapLimit[63];
	public static boolean cfgAnyWorldMapLimit;

	@Instance("powertools_shared")
	public static PowerTools instance;

	@PreInit
    public void preInit(FMLPreInitializationEvent event)
	{if (event.getSide() == Side.SERVER)
	{
		Configuration config = new Configuration( event.getSuggestedConfigurationFile() );
		config.load();

		Property proHighestDimension = config.get("Map limiter", "Highest_dimension", 2);
		proHighestDimension.comment = "Higest dimension specifies what the highest dimension is for which Map Limiter can be enabled. Default=2. Increase when using extra dimensions created by mods.";
		cfgHighestDimension = proHighestDimension.getInt();
		if (cfgHighestDimension < 1) cfgHighestDimension = 1; // Must be at least 1 to include 'The End'.
		
		Property proMapLimitNether = config.get("Map limiter", "Nether_enabled", false);
		proMapLimitNether.comment = "Setting this to true will stop players from moving beyond the specified coordinates on the map in the Nether.";
		cfgMapLimitNether.enabled = proMapLimitNether.getBoolean(false);
		cfgMapLimitNether.minX = config.get("Map limiter", "Nether_min_X", -500).getInt();
		cfgMapLimitNether.minZ = config.get("Map limiter", "Nether_min_Z", -500).getInt();
		cfgMapLimitNether.maxX = config.get("Map limiter", "Nether_max_X", 500).getInt();
		cfgMapLimitNether.maxZ = config.get("Map limiter", "Nether_max_Z", 500).getInt();

		Property proMapLimitOverworld = config.get("Map limiter", "Overworld_enabled", false);
		proMapLimitOverworld.comment = "Setting this to true will stop players from moving beyond the specified coordinates on the map in the Overworld.";
		cfgMapLimitDimemsion[0] = new MapLimit();
		cfgMapLimitDimemsion[0].enabled = proMapLimitOverworld.getBoolean(false);
		cfgMapLimitDimemsion[0].minX = config.get("Map limiter", "Overworld_min_X", -500).getInt();
		cfgMapLimitDimemsion[0].minZ = config.get("Map limiter", "Overworld_min_Z", -500).getInt();
		cfgMapLimitDimemsion[0].maxX = config.get("Map limiter", "Overworld_max_X", 500).getInt();
		cfgMapLimitDimemsion[0].maxZ = config.get("Map limiter", "Overworld_max_Z", 500).getInt();

		Property proMapLimitTheEnd = config.get("Map limiter", "TheEnd_enabled", false);
		proMapLimitTheEnd.comment = "Setting this to true will stop players from moving beyond the specified coordinates on the map in the End.";
		cfgMapLimitDimemsion[1] = new MapLimit();
		cfgMapLimitDimemsion[1].enabled = proMapLimitTheEnd.getBoolean(false);
		cfgMapLimitDimemsion[1].minX = config.get("Map limiter", "TheEnd_min_X", -500).getInt();
		cfgMapLimitDimemsion[1].minZ = config.get("Map limiter", "TheEnd_min_Z", -500).getInt();
		cfgMapLimitDimemsion[1].maxX = config.get("Map limiter", "TheEnd_max_X", 500).getInt();
		cfgMapLimitDimemsion[1].maxZ = config.get("Map limiter", "TheEnd_max_Z", 500).getInt();
		
		String worldNumber, zero;
		for (int i=2; i<=cfgHighestDimension; i++)
		{
			if (i < 10) zero = "0";
			else zero = "";
			worldNumber = "World" + zero + i;
			Property proMapLimitWorld = config.get("Map limiter", worldNumber + "_enabled", false);
			proMapLimitWorld.comment = "Setting this to true will stop players from moving beyond the specified coordinates on the map in " + worldNumber;
			cfgMapLimitDimemsion[i] = new MapLimit();
			cfgMapLimitDimemsion[i].enabled = proMapLimitWorld.getBoolean(false);
			cfgMapLimitDimemsion[i].minX = config.get("Map limiter", worldNumber + "_min_X", -500).getInt();
			cfgMapLimitDimemsion[i].minZ = config.get("Map limiter", worldNumber + "_min_Z", -500).getInt();
			cfgMapLimitDimemsion[i].maxX = config.get("Map limiter", worldNumber + "_max_X", 500).getInt();
			cfgMapLimitDimemsion[i].maxZ = config.get("Map limiter", worldNumber + "_max_Z", 500).getInt();
		}
		config.save();

		cfgAnyWorldMapLimit = false;
		if (cfgMapLimitNether.enabled) cfgAnyWorldMapLimit = true;
		for (int i=0; i<=cfgHighestDimension; i++)
		{
			if (cfgMapLimitDimemsion[i].enabled) cfgAnyWorldMapLimit = true;
		}
	}}

	@Init
	public void init(FMLInitializationEvent event)
	{if (event.getSide() == Side.SERVER)
	{
		MinecraftForge.EVENT_BUS.register(new PowerEventHookContainer());

		if (cfgAnyWorldMapLimit)
		{
			TickRegistry.registerScheduledTickHandler(new MapLimitTickHandler(), Side.SERVER);
		}
		
		GameRegistry.registerPlayerTracker(new PowerPlayerTracker());
		server = ModLoader.getMinecraftServerInstance();
		ICommandManager commandManager = server.getCommandManager();
		ServerCommandManager manager = ((ServerCommandManager) commandManager); 
		manager.registerCommand(new CommandChat());
		manager.registerCommand(new CommandHello());
	}}

	// --- Player Info ---
	public static void addPlayer(String plName)
	{
		int nr = getNumberForPlayerName(plName);
		if (nr == -1) // Add new player
		{
			plInfo[numberOfPlayers] = new PlayerInfo(plName);
			numberOfPlayers += 1;
		}
	}
	public static int getNumberForPlayerName(String plName)
	{
		int nr = -1;
		int i;
		for (i=0; i<numberOfPlayers; i++)
		{
			if (plInfo[i].name.equals(plName) ) nr = i;
		}
		return nr;
	}
	public static long plGetTimer1(String plName)
	{
		return plInfo[getNumberForPlayerName(plName)].time1;
	}
	public static long plGetTimer2(String plName)
	{
		return plInfo[getNumberForPlayerName(plName)].time2;
	}
	public static void plSetTimer1(String plName, long time)
	{
		plInfo[getNumberForPlayerName(plName)].time1 = time;
	}
	public static void plSetTimer2(String plName, long time)
	{
		plInfo[getNumberForPlayerName(plName)].time1 = time;
	}

	// --- Chat ---
	public static void sendChatToPlayerName(String playerName, String chatMessage)
	{
		EntityPlayerMP player = server.getConfigurationManager().getPlayerForUsername(playerName);
		if (player != null) player.sendChatToPlayer(chatMessage);
	}
	public static byte getChatMode(String playerName)
	{
		byte chatMode = -1;
		int nr = getNumberForPlayerName(playerName);
		if (nr > -1) chatMode = plInfo[nr].chatMode;
		return chatMode;
	}
	public static void setChatMode(String playerName, byte chatMode)
	{
		int nr = getNumberForPlayerName(playerName);
		if (nr > -1) plInfo[nr].chatMode = chatMode;
	}
	public static void addNameToChatList(String playerName, String chatName)
	{
		int nr = getNumberForPlayerName(playerName);
		if (nr > -1) plInfo[nr].chatToPlayers += chatName + "|";
	}
	public static String getChatList(String playerName)
	{
		String chatList = "";
		int nr = getNumberForPlayerName(playerName);
		if (nr > -1)
		{
			chatList = plInfo[nr].chatToPlayers;
		}
		return chatList;
	}
	public static void clearChatList(String playerName)
	{
		int nr = getNumberForPlayerName(playerName);
		if (nr > -1) plInfo[nr].chatToPlayers = "";
	}
	public static void removePlayerFromChatList(String playerName, String chatName)
	{
		int nr = getNumberForPlayerName(playerName);
		if (nr > -1)
		{
			String chatList = plInfo[nr].chatToPlayers;
			String newChatList = "";
			String oneLetter;
			String tmpName = "";
			for (int i=0; i<chatList.length(); i++)
			{
				oneLetter = chatList.substring(i, i+1);
				if (oneLetter.equals("|"))
				{
					if ( !tmpName.equals(chatName) ) newChatList += tmpName + "|";
					tmpName = "";
				}
				else
				{
					tmpName += oneLetter;
				}
			}
			plInfo[nr].chatToPlayers = newChatList;
		}
	}

	// ---
	public static int toChunkCoordinate(int coordinate)
	{
		int returnCoordinate;
		if (coordinate < 0)
			returnCoordinate = ( (coordinate+1) / 16) - 1;
		else
			returnCoordinate = coordinate / 16;
		return returnCoordinate;
	}

	// --- File IO ---
	public static String readFileToString(String fileName)
	{
		String folderName = "." + File.separator + "powertools";
		String fileContents = "";
		char inputChar;
		try
		{
			File file = new File(folderName + File.separator + fileName);
			if ( file.exists() && file.canRead() )
			{
				FileInputStream fileIn = new FileInputStream(file);
				for (;;)
				{
					int buffer = fileIn.read();
					if (buffer < 0) break;
					inputChar = (char)buffer;
					fileContents += inputChar;
				}
				fileIn.close();
			}
		}
		catch (IOException ioe)
		{
			//
		}
		return fileContents;
	}
	public static void writeFileFromString(String fileName, String fileString)
	{
		try
		{
			String folderName = "." + File.separator + "powertools";
			File checkFile = new File(folderName);
			if ( !checkFile.exists() ) checkFile.mkdir();

			FileWriter file = new FileWriter(folderName + File.separator + fileName);
			file.write(fileString);
			file.close();
		}
		catch (IOException ioe)
		{
			//
		}
	}

	// ------------------ Encrypt
	public static String encrypt(String stringIn, String codeIn)
	{
		String stringOut = "";
		String newCode = "";
		char oneLetter, codeLetter, newLetter;
		int tmp;
		int i;
		int j=0;
		codeIn = codeIn.toLowerCase();
		for (i=0; i<codeIn.length(); i++)
		{
			oneLetter = codeIn.charAt(i); // codeLetter: 1..37
			codeLetter = 1;
			if (oneLetter > 47 && oneLetter < 58) codeLetter = (char) (oneLetter - 47);
			if (oneLetter == 95) codeLetter = 11;
			if (oneLetter > 96 && oneLetter < 123) codeLetter = (char) (oneLetter - 85);
			newCode += codeLetter;
		}
		for (i=0; i<stringIn.length(); i++)
		{
			oneLetter = stringIn.charAt(i);
			codeLetter = newCode.charAt(j);
			tmp = oneLetter + codeLetter;
			if (tmp > 122) tmp -= 75;
			newLetter = (char) tmp;
			stringOut += newLetter;
			j++; if (j == newCode.length() ) j=0;
		}
		return stringOut;
	}
	public static String decrypt(String stringIn, String codeIn)
	{
		String stringOut = "";
		String newCode = "";
		char oneLetter, codeLetter, newLetter;
		int tmp;
		int i;
		int j=0;
		codeIn = codeIn.toLowerCase();
		for (i=0; i<codeIn.length(); i++)
		{
			oneLetter = codeIn.charAt(i); // codeLetter: 1..37
			codeLetter = 1;
			if (oneLetter > 47 && oneLetter < 58) codeLetter = (char) (oneLetter - 47);
			if (oneLetter == 95) codeLetter = 11;
			if (oneLetter > 96 && oneLetter < 123) codeLetter = (char) (oneLetter - 85);
			newCode += codeLetter;
		}
		for (i=0; i<stringIn.length(); i++)
		{
			oneLetter = stringIn.charAt(i);
			codeLetter = newCode.charAt(j);
			tmp = oneLetter - codeLetter;
			if (tmp < 48) tmp += 75;
			newLetter = (char) tmp;
			stringOut += newLetter;
			j++; if (j == newCode.length() ) j=0;
		}
		return stringOut;
	}
	public static boolean checkForValidCharacters(String inString)
	{
		boolean valid = true;
		int i;
		char oneLetter;
		for (i=0; i<inString.length(); i++)
		{
			oneLetter=inString.charAt(i);
			if (oneLetter < 48) valid = false;
			if (oneLetter > 57 && oneLetter < 65) valid = false;
			if (oneLetter > 90 && oneLetter < 95) valid = false;
			if (oneLetter == 96) valid = false;
			if (oneLetter > 122) valid = false;
		}
		return valid;
	}

	// ------------------
	public static boolean stringlistContains(String stringList, String checkString)
	{
		boolean check = false;
		String oneLetter;
		String tmpString = "";
		for (int i=0; i<stringList.length(); i++)
		{
			oneLetter = stringList.substring(i, i+1);
			if (oneLetter.equals(","))
			{
				if ( tmpString.equals(checkString) ) check = true;
				tmpString = "";
			}
			else
			{
				tmpString += oneLetter;
			}
		}
		return check;
	}
}



