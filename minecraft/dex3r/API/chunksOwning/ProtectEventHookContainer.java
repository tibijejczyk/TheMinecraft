package dex3r.API.chunksOwning;

import java.util.Date;

import dex3r.API.Tools;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ProtectEventHookContainer
{
  @ForgeSubscribe
  public void playerInteractEvent(PlayerInteractEvent event)
  {
    int dimension = event.entityPlayer.p.u.h;
    int x = Tools.toChunkCoordinate(event.x);
    int z = Tools.toChunkCoordinate(event.z);
    String name = event.entityPlayer.c_();
    byte compare = ChunkProtection.chunkCompare(dimension, x, z, name);

    if ((!name.equals("")) && (compare == 1) && (event.isCancelable()))
    {
      event.setCanceled(true);
      Tools.date = new Date();
      long time2 = Tools.date.getTime();
      if (time2 - Tools.plGetTimer1(name) > 2000L)
      {
        event.entityPlayer.a("§eThis land is owned by '" + ChunkProtection.chunkOwner(dimension, x, z) + "'.");
        event.entityPlayer.a("§eYou aren't allowed to build or break here.");
        Tools.plSetTimer1(name, time2);
      }
    }
  }
}