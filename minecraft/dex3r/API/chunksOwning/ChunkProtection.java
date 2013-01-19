package dex3r.API.chunksOwning;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.EventBus;
import dex3r.API.*;
import dex3r.API.util.DimChunkCoordinates;
import dex3r.API.util.PlayerInfo;

@Mod(modid="Tools_chunkprotection", name="Chunk Protection", version="0.1.4", dependencies="after:Tools_shared")
@NetworkMod(clientSideRequired=false, serverSideRequired=false)
public class ChunkProtection
{
  public static ChunkInfo[] chunkInfo = new ChunkInfo[65536];
  public static int numberOfChunks = 0;

  public static PlayerChunkInfo[] playerChunkInfo = new PlayerChunkInfo[8192];
  public static int numberOfPlayers = 0;
  public static boolean cfgDefaultBorderInfo;
  public static boolean cfgAllPlayersCanClaim;
  public static int cfgMaxChunksPerPlayer;
  public static int cfgAutoUnclaimInDays;

  @Mod.Instance("Tools_chunkprotection")
  public static ChunkProtection instance;

  @Mod.PreInit
  public void preInit(FMLPreInitializationEvent event)
  {
    if (event.getSide() == Side.SERVER)
    {
      Configuration config = new Configuration(event.getSuggestedConfigurationFile());
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
    }
  }

  @Mod.Init
  public void init(FMLInitializationEvent event) { if (event.getSide() == Side.SERVER)
    {
      MinecraftForge.EVENT_BUS.register(new ProtectEventHookContainer());

      TickRegistry.registerScheduledTickHandler(new ProtectTickHandler(), Side.SERVER);
      TickRegistry.registerScheduledTickHandler(new ProtectTickHandler2(), Side.SERVER);

      z commandManager = Tools.server.E();
      hi manager = (hi)commandManager;
      manager.a(new CommandChunk());

      readPlayerChunkInfoFromFile();
      readChunkInfoFromFile();
      Tools.chunkProtectionLoaded = true;
    } }

  @Mod.ServerStopping
  public void stopping(FMLServerStoppingEvent event)
  {
    writeChunkInfoToFile();
    writePlayerChunkInfoToFile();
  }

  public static byte chunkCompare(int dimension, int posX, int posZ, String playerName)
  {
    byte compare = 0;

    for (int i = 0; i < numberOfChunks; i++)
    {
      byte c = chunkInfo[i].compare(dimension, posX, posZ, playerName);
      if (c > 0) compare = c;
    }
    return compare;
  }

  public static int chunkGetNr(int dimension, int posX, int posZ)
  {
    int nr = -1;
    for (int i = 0; i < numberOfChunks; i++)
    {
      if ((chunkInfo[i].dimension == dimension) && (chunkInfo[i].x == posX) && (chunkInfo[i].z == posZ))
      {
        nr = i;
      }
    }
    return nr;
  }

  public static String chunkOwner(int dimension, int posX, int posZ)
  {
    String owner = "";
    for (int i = 0; i < numberOfChunks; i++)
    {
      if ((chunkInfo[i].dimension == dimension) && (chunkInfo[i].x == posX) && (chunkInfo[i].z == posZ))
        owner = chunkInfo[i].owner;
    }
    return owner;
  }

  public static byte chunkClaim(int dimension, int posX, int posZ, String playerName)
  {
    byte claimed = 0;
    byte compare = chunkCompare(dimension, posX, posZ, playerName);
    int nr = Tools.getNumberForPlayerName(playerName);

    int maxChunks = getMaxChunksPerPlayer(playerName);
    if (maxChunks == -1) maxChunks = cfgMaxChunksPerPlayer;

    if ((compare == 0) && (Tools.plInfo[nr].chunksOwned < maxChunks))
    {
      chunkInfo[numberOfChunks] = new ChunkInfo(dimension, posX, posZ, playerName);
      numberOfChunks += 1;
      PlayerInfo tmp86_85 = Tools.plInfo[nr]; tmp86_85.chunksOwned = ((byte)(tmp86_85.chunksOwned + 1));
      claimed = -1;
    }
    else if (compare == 0) {
      claimed = 4;
    } else {
      claimed = compare;
    }
    return claimed;
  }

  public static byte chunkUnClaim(int dimension, int posX, int posZ, String playerName)
  {
    byte claimed = 0;
    byte compare = chunkCompare(dimension, posX, posZ, playerName);
    int nr = Tools.getNumberForPlayerName(playerName);
    if (compare == 2)
    {
      int chunkNr = chunkGetNr(dimension, posX, posZ);
      chunkInfo[chunkNr].owner = "";
      PlayerInfo tmp49_48 = Tools.plInfo[nr]; tmp49_48.chunksOwned = ((byte)(tmp49_48.chunksOwned - 1));
      claimed = -1;
    }
    else
    {
      if (compare == 0)
      {
        claimed = 1;
      }
      if ((compare == 1) || (compare == 3))
      {
        claimed = 2;
        if (isPlayerOp(playerName))
        {
          int chunkNr = chunkGetNr(dimension, posX, posZ);
          int ownerNr = Tools.getNumberForPlayerName(chunkInfo[chunkNr].owner);
          chunkInfo[chunkNr].owner = "";
          PlayerInfo tmp134_133 = Tools.plInfo[ownerNr]; tmp134_133.chunksOwned = ((byte)(tmp134_133.chunksOwned - 1));
          claimed = 3;
        }
      }
    }
    return claimed;
  }

  public static void chunkUnClaimAll(String playerName) {
    for (int i = 0; i < numberOfChunks; i++)
    {
      if (chunkInfo[i].owner.equals(playerName))
      {
        chunkInfo[i].owner = "";
        int nr = Tools.getNumberForPlayerName(playerName);
        PlayerInfo tmp44_43 = Tools.plInfo[nr]; tmp44_43.chunksOwned = ((byte)(tmp44_43.chunksOwned - 1));
      }
    }
  }

  public static boolean chunkAddPlayer(int dimension, int posX, int posZ, String playerName, String playerToAdd)
  {
    boolean added = false;
    byte compare = chunkCompare(dimension, posX, posZ, playerName);
    if ((compare == 2) || (isPlayerOp(playerName)))
    {
      int nr = chunkGetNr(dimension, posX, posZ);
      chunkInfo[nr].addPlayer(playerToAdd);
      added = true;
    }
    return added;
  }

  public static boolean chunkRemovePlayer(int dimension, int posX, int posZ, String playerName, String playerToRemove)
  {
    boolean removed = false;
    byte compare = chunkCompare(dimension, posX, posZ, playerName);
    if ((compare == 2) || (isPlayerOp(playerName)))
    {
      int nr = chunkGetNr(dimension, posX, posZ);
      String playerList = chunkInfo[nr].players;
      String newPlayerList = "";

      String tmpName = "";
      for (int i = 0; i < playerList.length(); i++)
      {
        String oneLetter = playerList.substring(i, i + 1);
        if (oneLetter.equals(","))
        {
          if (!tmpName.equals(playerToRemove)) newPlayerList = newPlayerList + tmpName + ",";
          tmpName = "";
        }
        else
        {
          tmpName = tmpName + oneLetter;
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
    if ((compare == 2) || (isPlayerOp(playerName)))
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

  public static boolean chunkGetBorderInfo(String playerName) {
    int nr = Tools.getNumberForPlayerName(playerName);
    return Tools.plInfo[nr].borderInfo;
  }

  public static void chunkSetBorderInfo(String playerName, boolean borderInfo) {
    int nr = Tools.getNumberForPlayerName(playerName);
    Tools.plInfo[nr].borderInfo = borderInfo;
  }

  public static void readChunkInfoFromFile()
  {
    String fileContents = Tools.readFileToString("chunkinfo.dat");

    String inputDimension = "";
    String inputX = "";
    String inputZ = "";
    String inputOwner = "";
    String inputPlayers = "";
    String inputLastVisit = "";
    int storeIn = 1;

    for (int i = 0; i < fileContents.length(); i++)
    {
      String oneLetter = fileContents.substring(i, i + 1);
      if (oneLetter.equals("|")) storeIn++;
      if (oneLetter.equals("\n"))
      {
        storeIn = 1;
        int d = Integer.valueOf(inputDimension).intValue();
        int x = Integer.valueOf(inputX).intValue();
        int z = Integer.valueOf(inputZ).intValue();
        Tools.addPlayer(inputOwner);
        chunkClaim(d, x, z, inputOwner);
        int nr = chunkGetNr(d, x, z);
        if (nr > -1)
        {
          chunkInfo[nr].players = inputPlayers;
          chunkInfo[nr].ownerLastVisitDate = Long.valueOf(inputLastVisit).longValue();
        }
        inputDimension = "";
        inputX = "";
        inputZ = "";
        inputOwner = "";
        inputPlayers = "";
        inputLastVisit = "";
      }
      if ((!oneLetter.equals("|")) && (!oneLetter.equals("\n")))
      {
        if (storeIn == 1) inputDimension = inputDimension + oneLetter;
        if (storeIn == 2) inputX = inputX + oneLetter;
        if (storeIn == 3) inputZ = inputZ + oneLetter;
        if (storeIn == 4) inputOwner = inputOwner + oneLetter;
        if (storeIn == 5) inputPlayers = inputPlayers + oneLetter;
        if (storeIn == 6) inputLastVisit = inputLastVisit + oneLetter;
      }
    }
  }

  public static void writeChunkInfoToFile()
  {
    String fileString = "";

    for (int i = 0; i < numberOfChunks; i++)
    {
      if (!chunkInfo[i].owner.equals(""))
      {
        fileString = fileString + chunkInfo[i].dimension + "|";
        fileString = fileString + chunkInfo[i].x + "|";
        fileString = fileString + chunkInfo[i].z + "|";
        fileString = fileString + chunkInfo[i].owner + "|";
        fileString = fileString + chunkInfo[i].players + "|";
        fileString = fileString + chunkInfo[i].ownerLastVisitDate + "\n";
      }
    }
    Tools.writeFileFromString("chunkinfo.dat", fileString);
  }

  public static void readPlayerChunkInfoFromFile()
  {
    String fileContents = Tools.readFileToString("playerchunkinfo.dat");

    String inputPlayerName = "";
    String inputMaxChunks = "";
    int storeIn = 1;

    for (int i = 0; i < fileContents.length(); i++)
    {
      String oneLetter = fileContents.substring(i, i + 1);
      if (oneLetter.equals("|")) storeIn++;
      if (oneLetter.equals("\n"))
      {
        storeIn = 1;
        addPlayerChunkInfo(inputPlayerName);
        setMaxChunksForPlayer(inputPlayerName, Integer.valueOf(inputMaxChunks).intValue());
        inputPlayerName = "";
        inputMaxChunks = "";
      }
      if ((!oneLetter.equals("|")) && (!oneLetter.equals("\n")))
      {
        if (storeIn == 1) inputPlayerName = inputPlayerName + oneLetter;
        if (storeIn == 2) inputMaxChunks = inputMaxChunks + oneLetter;
      }
    }
  }

  public static void writePlayerChunkInfoToFile()
  {
    String fileString = "";

    for (int i = 0; i < numberOfPlayers; i++)
    {
      fileString = fileString + playerChunkInfo[i].playerName + "|";
      fileString = fileString + playerChunkInfo[i].maxChunks + "\n";
    }
    Tools.writeFileFromString("playerchunkinfo.dat", fileString);
  }

  public static boolean isPlayerOp(String playerName)
  {
    playerName = playerName.toLowerCase();
    return Tools.server.ad().i().contains(playerName);
  }

  public static void addPlayerChunkInfo(String playerName)
  {
    if (getNumberForPlayerName(playerName) == -1)
    {
      playerChunkInfo[numberOfPlayers] = new PlayerChunkInfo(playerName);
      numberOfPlayers += 1;
    }
  }

  public static int getNumberForPlayerName(String playerName)
  {
    int number = -1;
    for (int i = 0; i < numberOfPlayers; i++)
    {
      if (playerChunkInfo[i].playerName.equals(playerName)) number = i;
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

  public static void chunkSetLastVisitNow(int dimension, int posX, int posZ)
  {
    int chunkNr = chunkGetNr(dimension, posX, posZ);
    if (chunkNr > -1) chunkInfo[chunkNr].setLastVisitNow();
  }

  public static int getMaxChunksPerPlayer(String playerName)
  {
    int number = getNumberForPlayerName(playerName);
    if (number > -1) {
      return playerChunkInfo[number].maxChunks;
    }
    return -1;
  }

  public static void setMaxChunksForPlayer(String playerName, int maxChunks)
  {
    int number = getNumberForPlayerName(playerName);
    if (number > -1) playerChunkInfo[number].maxChunks = maxChunks;
  }

  public static int getChunksOwned(String playerName)
  {
    int number = Tools.getNumberForPlayerName(playerName);
    if (number > -1) {
      return Tools.plInfo[number].chunksOwned;
    }
    return -1;
  }
}