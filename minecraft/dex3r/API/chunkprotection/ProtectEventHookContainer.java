package powertools.chunkprotection;

import java.util.Date;

import powertools.shared.Cc;
import powertools.shared.PowerTools;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ProtectEventHookContainer
{
	@ForgeSubscribe
	public void playerInteractEvent(PlayerInteractEvent event)
	{
		int dimension = event.entityPlayer.worldObj.provider.dimensionId;
		int x = PowerTools.toChunkCoordinate(event.x);
		int z = PowerTools.toChunkCoordinate(event.z);
		String name = event.entityPlayer.getCommandSenderName();
		byte compare = ChunkProtection.chunkCompare(dimension, x, z, name);
		
		// DEBUG REDPOWER !name.equals("")
		if (!name.equals("") && compare==1 && event.isCancelable() )
		{
			event.setCanceled(true);
			PowerTools.date = new Date();			// Display message again after 2 seconds
			long time2 = PowerTools.date.getTime();
			if (time2 - PowerTools.plGetTimer1(name) > 2000)
			{
				event.entityPlayer.sendChatToPlayer(Cc.Yellow + "This land is owned by '" + ChunkProtection.chunkOwner(dimension, x, z) + "'.");
				event.entityPlayer.sendChatToPlayer(Cc.Yellow + "You aren't allowed to build or break here.");
				PowerTools.plSetTimer1(name, time2);
			}
		}
	}
}
