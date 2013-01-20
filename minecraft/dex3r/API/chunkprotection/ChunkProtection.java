package powertools.chunkprotection;

import java.util.Date;

import powertools.shared.DimChunkCoordinates;
import powertools.shared.PowerTools;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "powertools_chunkprotection", name = "Chunk Protection", version = "0.1.4", dependencies = "after:powertools_shared")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class ChunkProtection
{
	public static ChunkInfo[] chunkInfo = new ChunkInfo[65536];
	public static int numberOfChunks = 0;
	
	public static PlayerChunkInfo[] playerChunkInfo = new PlayerChunkInfo[8192];
	public static int numberOfPlayers = 0;
	
	public static boolean cfgDefaultBorderInfo, cfgAllPlayersCanClaim;
	public static int cfgMaxChunksPerPlayer, cfgAutoUnclaimInDays;

	@Instance("powertools_chunkprotection")
	public static ChunkProtection instance;

	@PreInit
    public void preInit(FMLPreInitializationEvent event)
	{if (event.getSide() == Side.SERVER)
	{
		Configuration config = new Configuration( event.getSuggestedConfigurationFile() );
		config.load();

		Property proAllPlayersCanClaim = config.get("Player settings", "AllPlayersCanClaim", true);
		proAllPlayersCanClaim.comment = "true: All players are allowed to claim chunks. / false: Only op's are allowed to claim.";
		cfgAllPlayersCanClaim = proAllPlayersCanClaim.getBoolean(true);

		Property proAutoUnclaimInDays = config.get("Player settings", "AutoUnclaimInDays", 7);
		proAutoUnclaimInDays.comment = "Chunks will automaticly be unclaimed if owner doesn't visit them for X days. 0=never unclaim";
		cfgAutoUnclaimInDays = proAutoUnclaimInDays.getInt();

		Property proDefaultBorderInfo = config.get("Player settings", "DefaultBorderInfo", true);
		proDefaultBorderInfo.comment = "This sets the default of displaying messages when crossing chunk borders. true / false";
		cfgDefaultBorderInfo = proDefaultBorderInfo.getBoolean(true);

		Property proMaxChunksPerPlayer = config.get("Player settings", "MaxChunksPerPlayer", 4);
		proMaxChunksPerPlayer.comment = "This defines how many chunks each player is allowed to claim.";
		cfgMaxChunksPerPlayer = proMaxChunksPerPlayer.getInt();

		config.save();
	}}
	
	@Init
	public void init(FMLInitializationEvent event)
	{if (event.getSide() == Side.SERVER)
	{
		MinecraftForge.EVENT_BUS.register(new ProtectEventHookContainer());

		TickRegistry.registerScheduledTickHandler(new ProtectTickHandler(), Side.SERVER);
		TickRegistry.registerScheduledTickHandler(new ProtectTickHandler2(), Side.SERVER);

		ICommandManager commandManager = PowerTools.server.getCommandManager();
		ServerCommandManager manager = ((ServerCommandManager) commandManager); 
		manager.registerCommand(new CommandChunk());

		readPlayerChunkInfoFromFile();
		readChunkInfoFromFile();
		PowerTools.chunkProtectionLoaded = true;
	}}
	
	@ServerStopping
	public void stopping(FMLServerStoppingEvent event)
	{
		writeChunkInfoToFile();
		writePlayerChunkInfoToFile();
	}

	public static byte chunkCompare(int dimension, int posX, int posZ, String playerName)
	{
		byte compare = 0;
		byte c;
		for (int i=0; i<numberOfChunks; i++)
		{
			c = chunkInfo[i].compare(dimension, posX, posZ, playerName);
			if (c > 0) compare = c;
		}
		return compare;
	}

	public static int chunkGetNr(int dimension, int posX, int posZ)
	{
		int nr = -1;
		for (int i=0; i<numberOfChunks; i++)
		{
			if (chunkInfo[i].dimension == dimension &&
				chunkInfo[i].x == posX && chunkInfo[i].z == posZ)
			{
				nr = i;
			}
		}
		return nr;
	}

	public static String chunkOwner(int dimension, int posX, int posZ)
	{
		String owner = "";
		for (int i=0; i<numberOfChunks; i++)
		{
			if (chunkInfo[i].dimension == dimension && chunkInfo[i].x == posX && chunkInfo[i].z == posZ)
				owner = chunkInfo[i].owner;
		}
		return owner;
	}

	public static byte chunkClaim(int dimension, int posX, int posZ, String playerName)
	{
		byte claimed = 0; // -1=claimed, 1=already claimed, 2 & 3=already owned, 4=max allowed
		byte compare = chunkCompare(dimension, posX, posZ, playerName);
		int nr = PowerTools.getNumberForPlayerName(playerName);
		
		int maxChunks = getMaxChunksPerPlayer(playerName);
		if (maxChunks == -1) maxChunks = cfgMaxChunksPerPlayer;
		
		if (compare == 0 && PowerTools.plInfo[nr].chunksOwned < maxChunks)
		{
			chunkInfo[numberOfChunks] = new ChunkInfo(dimension, posX, posZ, playerName);
			numberOfChunks++;
			PowerTools.plInfo[nr].chunksOwned++;
			claimed = -1;
		}
		else
		{
			if (compare == 0)
				claimed = 4;
			else
				claimed = compare;
		}
		return claimed;
	}

	public static byte chunkUnClaim(int dimension, int posX, int posZ, String playerName)
	{
		byte claimed = 0; // -1=unclaimed, 1=wasn't claimed, 2=claimed by someone else, 3=unclaimed by op
		byte compare = chunkCompare(dimension, posX, posZ, playerName);
		int nr = PowerTools.getNumberForPlayerName(playerName);
		if (compare == 2)
		{
			int chunkNr = chunkGetNr(dimension, posX, posZ);
			chunkInfo[chunkNr].owner = "";
			PowerTools.plInfo[nr].chunksOwned--;
			claimed = -1;
		}
		else
		{
			if (compare == 0)
			{
				claimed = 1;
			}
			if (compare == 1 || compare == 3)
			{
				claimed = 2;
				if (isPlayerOp(playerName) )
				{
					int chunkNr = chunkGetNr(dimension, posX, posZ);
					int ownerNr = PowerTools.getNumberForPlayerName(chunkInfo[chunkNr].owner);
					chunkInfo[chunkNr].owner = "";
					PowerTools.plInfo[ownerNr].chunksOwned--;
					claimed = 3;
				}
			}
		}
		return claimed;
	}
	public static void chunkUnClaimAll(String playerName)
	{
		for (int i=0; i<numberOfChunks; i++)
		{
			if ( chunkInfo[i].owner.equals(playerName) )
			{
				chunkInfo[i].owner = "";
				int nr = PowerTools.getNumberForPlayerName(playerName);
				PowerTools.plInfo[nr].chunksOwned--;
			}
		}
	}

	public static boolean chunkAddPlayer(int dimension, int posX, int posZ,
										String playerName, String playerToAdd)
	{
		boolean added = false;
		byte compare = chunkCompare(dimension, posX, posZ, playerName);
		if (compare == 2 || isPlayerOp(playerName) )
		{
			int nr = chunkGetNr(dimension, posX, posZ);
			chunkInfo[nr].addPlayer(playerToAdd);
			added = true;
		}
		return added;
	}

	public static boolean chunkRemovePlayer(int dimension, int posX, int posZ,
										String playerName, String playerToRemove)
	{
		boolean removed = false;
		byte compare = chunkCompare(dimension, posX, posZ, playerName);
		if (compare == 2 || isPlayerOp(playerName) )
		{
			int nr = chunkGetNr(dimension, posX, posZ);
			String playerList = chunkInfo[nr].players;
			String newPlayerList = "";
			String oneLetter;
			String tmpName = "";
			for (int i=0; i<playerList.length(); i++)
			{
				oneLetter = playerList.substring(i, i+1);
				if (oneLetter.equals(","))
				{
					if ( !tmpName.equals(playerToRemove) ) newPlayerList += tmpName + ",";
					tmpName = "";
				}
				else
				{
					tmpName += oneLetter;
				}
			}
			chunkInfo[nr].players = newPlayerList;
			removed = true;
		}
		return removed;
	}

	public static boolean chunkClearPlayerList(int dimension, int posX, int posZ, String playerName)
	{
		boolean cleared = false;
		byte compare = chunkCompare(dimension, posX, posZ, playerName);
		if (compare == 2 || isPlayerOp(playerName) )
		{
			int nr = chunkGetNr(dimension, posX, posZ);
			chunkInfo[nr].players = "";
			cleared = true;
		}
		return cleared;
	}

	public static String chunkGetPlayers(int dimension, int posX, int posZ)
	{
		int nr = chunkGetNr(dimension, posX, posZ);
		return chunkInfo[nr].players;
	}
	public static boolean chunkGetBorderInfo(String playerName)
	{
		int nr = PowerTools.getNumberForPlayerName(playerName);
		return PowerTools.plInfo[nr].borderInfo;
	}
	public static void chunkSetBorderInfo(String playerName, boolean borderInfo)
	{
		int nr = PowerTools.getNumberForPlayerName(playerName);
		PowerTools.plInfo[nr].borderInfo = borderInfo;
	}

	public static void readChunkInfoFromFile()
	{
		String fileContents = PowerTools.readFileToString("chunkinfo.dat");
		String oneLetter;
		String inputDimension = "";
		String inputX = "";
		String inputZ = "";
		String inputOwner = "";
		String inputPlayers = "";
		String inputLastVisit = "";
		int storeIn=1; // 1=dimension, 2=x, 3=z, 4=owner, 5=players, 6=lastVisit
		int i, d, x, z, nr;
		for (i=0; i<fileContents.length(); i++)
		{
			oneLetter = fileContents.substring(i, i+1);
			if (oneLetter.equals("|")) storeIn+=1;
			if (oneLetter.equals("\n"))
			{
				storeIn=1;
				d = Integer.valueOf(inputDimension);
				x = Integer.valueOf(inputX);
				z = Integer.valueOf(inputZ);
				PowerTools.addPlayer(inputOwner);
				chunkClaim(d, x, z, inputOwner);
				nr = chunkGetNr(d, x, z);
				if (nr > -1)
				{
					chunkInfo[nr].players = inputPlayers;
					chunkInfo[nr].ownerLastVisitDate = Long.valueOf(inputLastVisit);
				}
				inputDimension = "";
				inputX = "";
				inputZ = "";
				inputOwner = "";
				inputPlayers = "";
				inputLastVisit = "";
			}
			if (!oneLetter.equals("|") && !oneLetter.equals("\n"))
			{
				if (storeIn == 1) inputDimension += oneLetter; 
				if (storeIn == 2) inputX += oneLetter; 
				if (storeIn == 3) inputZ += oneLetter;
				if (storeIn == 4) inputOwner += oneLetter;
				if (storeIn == 5) inputPlayers += oneLetter;
				if (storeIn == 6) inputLastVisit += oneLetter;
			}
		}
	}

	public static void writeChunkInfoToFile()
	{
		String fileString = "";
		int i;
		for (i=0; i<numberOfChunks; i++)
		{
			if ( !chunkInfo[i].owner.equals("") )
			{
				fileString += chunkInfo[i].dimension + "|";
				fileString += chunkInfo[i].x + "|";
				fileString += chunkInfo[i].z + "|";
				fileString += chunkInfo[i].owner + "|";
				fileString += chunkInfo[i].players + "|";
				fileString += chunkInfo[i].ownerLastVisitDate + "\n";
			}
		}
		PowerTools.writeFileFromString("chunkinfo.dat", fileString);
	}

	public static void readPlayerChunkInfoFromFile()
	{
		String fileContents = PowerTools.readFileToString("playerchunkinfo.dat");
		String oneLetter;
		String inputPlayerName = "";
		String inputMaxChunks = "";
		int storeIn=1; // 1=playerName, 2=maxChunks
		int i;
		for (i=0; i<fileContents.length(); i++)
		{
			oneLetter = fileContents.substring(i, i+1);
			if (oneLetter.equals("|")) storeIn+=1;
			if (oneLetter.equals("\n"))
			{
				storeIn=1;
				addPlayerChunkInfo(inputPlayerName);
				setMaxChunksForPlayer( inputPlayerName, Integer.valueOf(inputMaxChunks) );
				inputPlayerName = "";
				inputMaxChunks = "";
			}
			if (!oneLetter.equals("|") && !oneLetter.equals("\n"))
			{
				if (storeIn == 1) inputPlayerName += oneLetter; 
				if (storeIn == 2) inputMaxChunks += oneLetter; 
			}
		}
	}
	
	public static void writePlayerChunkInfoToFile()
	{
		String fileString = "";
		int i;
		for (i=0; i<numberOfPlayers; i++)
		{
			fileString += playerChunkInfo[i].playerName + "|";
			fileString += playerChunkInfo[i].maxChunks + "\n";
		}
		PowerTools.writeFileFromString("playerchunkinfo.dat", fileString);
	}

	// -------
	/*
	public static DimChunkCoordinates getCurrentChunkCoordinates(String name)
	{
		int nr = PowerTools.getNumberForPlayerName(name);
		return PowerTools.plInfo[nr].currentChunkCoordinates;
	}

	public static void setCurrentChunkCoordinates(String name, DimChunkCoordinates coordinates)
	{
		int nr = PowerTools.getNumberForPlayerName(name);
		PowerTools.plInfo[nr].currentChunkCoordinates = coordinates;
	}

	public static String getLastOwnerMessage(String name)
	{
		int nr = PowerTools.getNumberForPlayerName(name);
		return PowerTools.plInfo[nr].lastOwnerMessage;
	}

	public static void setLastOwnerMessage(String name, String owner)
	{
		int nr = PowerTools.getNumberForPlayerName(name);
		PowerTools.plInfo[nr].lastOwnerMessage = owner;
	}
	*/

	// -------
	public static boolean isPlayerOp(String playerName)
	{
		playerName = playerName.toLowerCase();
		return PowerTools.server.getConfigurationManager().getOps().contains(playerName);
	}
	
	// ------- Player Chunk Info -----------
	
	public static void addPlayerChunkInfo(String playerName)
	{
		if (getNumberForPlayerName(playerName) == -1)
		{
			playerChunkInfo[numberOfPlayers] = new PlayerChunkInfo(playerName);
			numberOfPlayers++;
		}
	}
	
	public static int getNumberForPlayerName(String playerName)
	{
		int number = -1;
		for (int i=0; i< numberOfPlayers; i++)
		{
			if ( playerChunkInfo[i].playerName.equals(playerName) ) number = i;
		}
		return number;
	}
	
	public static String getChunkOwnerMessage(String playerName)
	{
		String message = "";
		int number = getNumberForPlayerName(playerName);
		if (number > -1) message = playerChunkInfo[number].lastOwnerMessage;
		return message;
	}

	public static void setChunkOwnerMessage(String playerName, String message)
	{
		int number = getNumberForPlayerName(playerName);
		if (number > -1) playerChunkInfo[number].lastOwnerMessage = message;
	}
	
	public static DimChunkCoordinates getChunkCoordinates(String playerName)
	{
		DimChunkCoordinates coordinates = new DimChunkCoordinates();
		int number = getNumberForPlayerName(playerName);
		if (number > -1) coordinates = playerChunkInfo[number].currentChunkCoordinates;
		return coordinates;
	}

	public static void setChunkCoordinates(String playerName, DimChunkCoordinates coordinates)
	{
		int number = getNumberForPlayerName(playerName);
		if (number > -1) playerChunkInfo[number].currentChunkCoordinates = coordinates;
	}

	// ------- Chunk Info Date -----------
	public static void chunkSetLastVisitNow(int dimension, int posX, int posZ)
	{
		int chunkNr = chunkGetNr(dimension, posX, posZ);
		if (chunkNr > -1) chunkInfo[chunkNr].setLastVisitNow();
	}

	// ------- Chunk Info Player -----------
	public static int getMaxChunksPerPlayer(String playerName)
	{
		int number = getNumberForPlayerName(playerName);
		if (number > -1)
			return playerChunkInfo[number].maxChunks;
		else
			return -1;
	}
	
	public static void setMaxChunksForPlayer(String playerName, int maxChunks)
	{
		int number = getNumberForPlayerName(playerName);
		if (number > -1) playerChunkInfo[number].maxChunks = maxChunks;
	}

	public static int getChunksOwned(String playerName)
	{
		int number = PowerTools.getNumberForPlayerName(playerName);
		if (number > -1)
			return PowerTools.plInfo[number].chunksOwned;
		else
			return -1;
	}

}
