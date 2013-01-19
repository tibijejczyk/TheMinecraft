package dex3r.API.chunksOwning;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import dex3r.API.Tools;
import dex3r.API.util.*;

public class ProtectTickHandler
  implements IScheduledTickHandler
{
  public void checkPlayerChunk(EntityPlayerMP player)
  {
    String playerName = player.username;
    DimChunkCoordinates oldChunkCoordinates = ChunkProtection.getChunkCoordinates(playerName);
    int oldD = DimChunkCoordinates.d;
    int oldX = DimChunkCoordinates.x;
    int oldZ = DimChunkCoordinates.z;

    int newD = player.p.u.h;
    int newX = Tools.toChunkCoordinate(player.b().a);
    int newZ = Tools.toChunkCoordinate(player.b().c);
    DimChunkCoordinates newChunkCoordinates = new DimChunkCoordinates(newD, newX, newZ);

    if ((oldD != newD) || (oldX != newX) || (oldZ != newZ))
    {
      String owner = "";
      byte c = ChunkProtection.chunkCompare(newD, newX, newZ, playerName);
      if (c == 0) owner = "";
      if ((c == 1) || (c == 3)) owner = ChunkProtection.chunkOwner(newD, newX, newZ);
      if (c == 2)
      {
        owner = playerName;
        ChunkProtection.chunkSetLastVisitNow(newD, newX, newZ);
      }
      if (!owner.equals(ChunkProtection.getChunkOwnerMessage(playerName)))
      {
        if (ChunkProtection.chunkGetBorderInfo(playerName))
        {
          if (c == 0) player.a("§eUnclaimed territory");
          if ((c == 1) || (c == 3)) player.a("§eThis land is owned by '" + owner + "'");
          if (c == 2) player.a("§eYou own this land");
        }
      }
      ChunkProtection.setChunkOwnerMessage(playerName, owner);
      ChunkProtection.setChunkCoordinates(playerName, newChunkCoordinates);
    }
  }

  public void tickStart(EnumSet type, Object...tickData)
  {
    int numberOfPlayers = Tools.server.A().length;

    for (int i = 0; i < numberOfPlayers; i++)
    {
      iq player = (iq)Tools.server.ad().b.get(i);
      checkPlayerChunk(player);
    }
  }

  public void tickEnd(EnumSet type, Object...tickData)
  {
  }

  public EnumSet ticks()
  {
    return EnumSet.of(TickType.SERVER);
  }

  public String getLabel()
  {
    return "ChunkProtectionTicks";
  }

  public int nextTickSpacing()
  {
    return 10;
  }
}