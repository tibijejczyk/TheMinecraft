package powertools.chunkprotection;

import java.util.EnumSet;

import powertools.shared.Cc;
import powertools.shared.PowerTools;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ProtectTickHandler2  implements IScheduledTickHandler
{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (ChunkProtection.cfgAutoUnclaimInDays > 0)
		{
			int days;
			for (int i=0; i<ChunkProtection.numberOfChunks; i++)
			{
				if (ChunkProtection.chunkInfo[i].owner != "")
				{
					days = ChunkProtection.chunkInfo[i].getDaysSinceLastVisit();
					if (days >= ChunkProtection.cfgAutoUnclaimInDays)
					// last visit more days ago than...
					{
						/* TODO Send this message to all players/server console
						PowerTools.sendChatToPlayerName("PowerBeat", Cc.LightRed +
							"Auto unlcaimed chunk owned by " + ChunkProtection.chunkInfo[i].owner); */

						// Unclaim chunk
						ChunkProtection.chunkInfo[i].owner = "";
					}
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public EnumSet<TickType> ticks()
	{
		 return EnumSet.of(TickType.SERVER);
	}
	@Override
	public String getLabel()
	{
		return "ChunkProtectionTicks2";
	}
	@Override
	public int nextTickSpacing()
	{
		return 6000; // 5 minutes
	}

}
