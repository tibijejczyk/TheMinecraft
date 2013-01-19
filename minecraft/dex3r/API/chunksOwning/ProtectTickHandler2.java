package dex3r.API.chunksOwning;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;

public class ProtectTickHandler2
  implements IScheduledTickHandler
{
  public void tickStart(EnumSet type, Object...tickData)
  {
    if (ChunkProtection.cfgAutoUnclaimInDays > 0)
    {
      for (int i = 0; i < ChunkProtection.numberOfChunks; i++)
      {
        if (ChunkProtection.chunkInfo[i].owner != "")
        {
          int days = ChunkProtection.chunkInfo[i].getDaysSinceLastVisit();
          if (days >= ChunkProtection.cfgAutoUnclaimInDays)
          {
            ChunkProtection.chunkInfo[i].owner = "";
          }
        }
      }
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
    return "ChunkProtectionTicks2";
  }

  public int nextTickSpacing()
  {
    return 6000;
  }
}
