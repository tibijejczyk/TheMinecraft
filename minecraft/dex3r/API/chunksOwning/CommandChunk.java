package dex3r.API.chunksOwning;

import java.util.List;
import dex3r.API.*;

public class CommandChunk extends w
{
  public String c()
  {
    return "chunk";
  }

  public String getCommandUsage()
  {
    return "/" + c() + "[claim]|[unclaim]|[add]|[remove]|[clear]|[info]";
  }

  public List b()
  {
    return null;
  }

  public void b(aa sender, String[] arguments)
  {
    String playerName = sender.c_();
    if (arguments.length > 0)
    {
      if ((arguments[0].equals("claim")) && ((ChunkProtection.cfgAllPlayersCanClaim) || (ChunkProtection.isPlayerOp(playerName))))
      {
        if (arguments.length > 1)
        {
          if (ChunkProtection.isPlayerOp(playerName))
          {
            Tools.addPlayer(arguments[1]);
            chunkClaim(sender, arguments[1]);
          }
        }
        else
        {
          chunkClaim(sender, "");
        }
      }
      if (arguments[0].equals("unclaim")) chunkUnClaim(sender);
      if (arguments[0].equals("unclaimall")) chunkUnClaimAll(sender, arguments);
      if (arguments[0].equals("add")) chunkAddPlayers(sender, arguments);
      if (arguments[0].equals("remove")) chunkRemovePlayers(sender, arguments);
      if (arguments[0].equals("clear")) chunkClearPlayers(sender);
      if (arguments[0].equals("info")) chunkSetBorderInfo(sender, arguments);

      if (arguments[0].equals("playerinfo")) chunkPlayerInfo(sender, arguments);
      if ((arguments[0].equals("maxchunks")) && (ChunkProtection.isPlayerOp(playerName))) chunkSetMaxChunks(sender, arguments);
    }
    else
    {
      iq player = (iq)sender;
      int d = player.p.u.h;
      int x = Tools.toChunkCoordinate(player.b().a);
      int z = Tools.toChunkCoordinate(player.b().c);
      String owner = ChunkProtection.chunkOwner(d, x, z);
      if (!owner.equals(""))
      {
        sender.a("§eThis chunk is claimed by: " + owner);
        String players = ChunkProtection.chunkGetPlayers(d, x, z);
        if (!players.equals(""))
          sender.a("§ePlayers allowed to build here: " + players);
      }
      else
      {
        sender.a("§eThis chunk is currently not claimed");
      }
      String message = "§eUse §f/chunk ";
      if ((ChunkProtection.cfgAllPlayersCanClaim) || (ChunkProtection.isPlayerOp(playerName)))
        message = message + "[claim]|";
      message = message + "[unclaim]";
      sender.a(message);
      sender.a("§eOr §f/chunk [add]|[remove] [player1] [player2] ..");
      sender.a("§eDisplay info at border: §f/chunk info [on|off]");
      if (ChunkProtection.isPlayerOp(playerName))
      {
        sender.a("§eDisplay player information: §f/chunk playerinfo [playername]");
        sender.a("§eSet maximum chunks to claim for player: §f/chunk maxchunks [playername] [maxchunks]");
      }
    }
  }

  public boolean b(aa sender)
  {
    return true;
  }

  public void chunkClaim(aa sender, String claimForPlayer)
  {
    qx player = (qx)sender;
    int dimension = player.p.u.h;
    s coordinates = sender.b();
    int x = Tools.toChunkCoordinate(coordinates.a);
    int z = Tools.toChunkCoordinate(coordinates.c);
    String playerName = sender.c_();
    byte b;
    byte b;
    if (claimForPlayer.equals(""))
    {
      b = ChunkProtection.chunkClaim(dimension, x, z, playerName);
    }
    else
    {
      b = ChunkProtection.chunkClaim(dimension, x, z, claimForPlayer);
    }
    String message = "§eDim" + dimension;
    if (dimension == 0) message = "§eOverworld";
    if (dimension == -1) message = "§eNether";
    message = message + " x:" + x + " z:" + z;
    if (b == -1)
    {
      ChunkProtection.chunkSetLastVisitNow(dimension, x, z);
      ChunkProtection.writeChunkInfoToFile();
      message = message + " is successfully claimed.";
      if (claimForPlayer.equals(""))
      {
        ChunkProtection.setChunkOwnerMessage(playerName, playerName);
      }
      else
      {
        ChunkProtection.setChunkOwnerMessage(playerName, claimForPlayer);
      }
    }
    if ((b == 1) || (b == 3))
    {
      String owner = ChunkProtection.chunkOwner(dimension, x, z);
      message = message + " was already claimed by " + owner + ".";
    }
    if (b == 2) message = message + " was already claimed by you.";
    if (b == 4) message = message + " can't be claimed by you. You may only claim " + ChunkProtection.getMaxChunksPerPlayer(playerName) + " chunks.";
    sender.a(message);
  }

  public void chunkUnClaim(aa sender)
  {
    qx player = (qx)sender;
    int dimension = player.p.u.h;
    s coordinates = sender.b();
    int x = Tools.toChunkCoordinate(coordinates.a);
    int z = Tools.toChunkCoordinate(coordinates.c);

    String name = sender.c_();
    byte b = ChunkProtection.chunkUnClaim(dimension, x, z, name);
    String message = "§eDim" + dimension;
    if (dimension == 0) message = "§eOverworld";
    if (dimension == -1) message = "§eNether";
    message = message + " x:" + x + " z:" + z;
    if (b == -1)
    {
      ChunkProtection.writeChunkInfoToFile();
      message = message + " is successfully unclaimed.";
      ChunkProtection.setChunkOwnerMessage(name, "");
    }
    if (b == 1) message = message + " couln't be unclaimed. It wasn't claimed.";
    if (b == 2)
    {
      String owner = ChunkProtection.chunkOwner(dimension, x, z);
      message = message + " couln't be unclaimed. It was claimed by " + owner + ".";
    }
    if (b == 3)
    {
      ChunkProtection.writeChunkInfoToFile();
      message = message + " is §csuccessfully unclaimed by op.";
    }
    sender.a(message);
  }

  public void chunkUnClaimAll(aa sender, String[] arguments)
  {
    String playerName = sender.c_();
    if ((arguments.length == 2) && (ChunkProtection.isPlayerOp(playerName)))
      playerName = arguments[1];
    ChunkProtection.chunkUnClaimAll(playerName);
    sender.a("§eUnclaimed all chunks of " + playerName);
  }

  public void chunkAddPlayers(aa sender, String[] arguments)
  {
    iq player = (iq)sender;
    int d = player.p.u.h;
    int x = Tools.toChunkCoordinate(player.b().a);
    int z = Tools.toChunkCoordinate(player.b().c);
    String playerName = sender.c_();
    String owner = ChunkProtection.chunkOwner(d, x, z);
    if ((owner.equals(playerName)) || (ChunkProtection.isPlayerOp(playerName)))
    {
      String message = "";
      if (ChunkProtection.isPlayerOp(playerName)) message = message + "§cOp ";
      message = message + "§eAdded player(s): ";
      for (int i = 1; i < arguments.length; i++)
      {
        boolean b = ChunkProtection.chunkAddPlayer(d, x, z, playerName, arguments[i]);
        if (b) message = message + arguments[i] + ", ";
      }
      sender.a(message);
      ChunkProtection.writeChunkInfoToFile();
    }
    else
    {
      sender.a("§eYou can only add players to a chunk you own.");
    }
  }

  public void chunkRemovePlayers(aa sender, String[] arguments) {
    iq player = (iq)sender;
    int d = player.p.u.h;
    int x = Tools.toChunkCoordinate(player.b().a);
    int z = Tools.toChunkCoordinate(player.b().c);
    String playerName = sender.c_();
    String owner = ChunkProtection.chunkOwner(d, x, z);
    if ((owner.equals(playerName)) || (ChunkProtection.isPlayerOp(playerName)))
    {
      String message = "";
      if (ChunkProtection.isPlayerOp(playerName)) message = message + "§cOp ";
      message = message + "§eRemoved player(s): ";
      for (int i = 1; i < arguments.length; i++)
      {
        boolean b = ChunkProtection.chunkRemovePlayer(d, x, z, playerName, arguments[i]);
        if (b) message = message + arguments[i] + ", ";
      }
      sender.a(message);
      ChunkProtection.writeChunkInfoToFile();
    }
    else
    {
      sender.a("§eYou can only remove players from a chunk you own.");
    }
  }

  public void chunkClearPlayers(aa sender) {
    iq player = (iq)sender;
    int d = player.p.u.h;
    int x = Tools.toChunkCoordinate(player.b().a);
    int z = Tools.toChunkCoordinate(player.b().c);
    String playerName = sender.c_();
    String owner = ChunkProtection.chunkOwner(d, x, z);
    if ((owner.equals(playerName)) || (ChunkProtection.isPlayerOp(playerName)))
    {
      boolean b = ChunkProtection.chunkClearPlayerList(d, x, z, playerName);
      String message = "";
      if (ChunkProtection.isPlayerOp(playerName)) message = message + "§cOp ";
      message = message + "§eCleared player list for this chunk.";
      if (b) sender.a(message);
    }
    else
    {
      sender.a("§eYou can only remove players from a chunk you own.");
    }
  }

  public void chunkSetBorderInfo(aa sender, String[] arguments) {
    String playerName = sender.c_();

    if (arguments.length > 1)
    {
      String message = "§eBorder info turned: ";
      if (arguments[1].equals("on"))
      {
        ChunkProtection.chunkSetBorderInfo(playerName, true);
        message = message + "on";
      }
      if (arguments[1].equals("off"))
      {
        ChunkProtection.chunkSetBorderInfo(playerName, false);
        message = message + "off";
      }
      sender.a(message);
    }
    else
    {
      boolean info = ChunkProtection.chunkGetBorderInfo(playerName);
      String message = "§eBorder info is currently set: ";
      if (info)
        message = message + "on";
      else
        message = message + "off";
      sender.a(message);
    }
  }

  public void chunkPlayerInfo(aa sender, String[] arguments)
  {
    String message = "";
    if (arguments.length == 2)
    {
      String playerName = arguments[1];
      int maxChunks = ChunkProtection.getMaxChunksPerPlayer(playerName);
      int chunksOwned = ChunkProtection.getChunksOwned(playerName);
      message = "§eInfo for player '" + playerName + "', maxChunksAllowed: " + maxChunks + ", numberOfChunksOwned: " + chunksOwned;
    }
    else if (arguments.length == 1)
    {
      String playerName = sender.c_();
      int maxChunks = ChunkProtection.getMaxChunksPerPlayer(playerName);
      int chunksOwned = ChunkProtection.getChunksOwned(playerName);
      message = "§eInfo for player '" + playerName + "', maxChunksAllowed: " + maxChunks + ", numberOfChunksOwned: " + chunksOwned;
    }
    else
    {
      message = "§eUse: §f/chunk playerinfo [playername]";
    }
    sender.a(message);
  }

  public void chunkSetMaxChunks(aa sender, String[] arguments)
  {
    String message = "";
    if (arguments.length == 3)
    {
      String playerName = arguments[1];
      int maxChunks = Integer.valueOf(arguments[2]).intValue();
      ChunkProtection.setMaxChunksForPlayer(playerName, maxChunks);
      ChunkProtection.writePlayerChunkInfoToFile();
      message = "§ePlayer '" + playerName + "' can now claim " + "§f" + maxChunks + "§e" + " chunks.";
    }
    else
    {
      message = "§eUse: §f/chunk maxchunks [playername] [maxchunks]";
    }
    sender.a(message);
  }
}
