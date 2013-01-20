package powertools.chunkprotection;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayerMP;

import powertools.shared.Cc;
import powertools.shared.DimChunkCoordinates;
import powertools.shared.PowerTools;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ProtectTickHandler implements IScheduledTickHandler
{
	public void checkPlayerChunk(EntityPlayerMP player)
	{
		String playerName = player.getCommandSenderName();
		DimChunkCoordinates oldChunkCoordinates = ChunkProtection.getChunkCoordinates(playerName);
		int oldD = oldChunkCoordinates.d;
		int oldX = oldChunkCoordinates.x;
		int oldZ = oldChunkCoordinates.z;

		int newD = player.worldObj.provider.dimensionId;
		int newX = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posX);
		int newZ = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posZ);
		DimChunkCoordinates newChunkCoordinates = new DimChunkCoordinates(newD, newX, newZ);

		if ( oldD!=newD || oldX!=newX || oldZ!=newZ )
		{
			String owner = "";
			byte c = ChunkProtection.chunkCompare(newD, newX, newZ, playerName);
			if (c == 0) owner = "";
			if (c == 1 || c == 3) owner = ChunkProtection.chunkOwner(newD, newX, newZ);
			if (c == 2)
			{
				owner = playerName;
				ChunkProtection.chunkSetLastVisitNow(newD, newX, newZ);
			}
			if (!owner.equals(ChunkProtection.getChunkOwnerMessage(playerName) ) )
			{
				if ( ChunkProtection.chunkGetBorderInfo(playerName) )
				{
					if (c == 0) player.sendChatToPlayer(Cc.Yellow + "Unclaimed territory");
					if (c == 1 || c==3) player.sendChatToPlayer(Cc.Yellow + "This land is owned by '" + owner + "'");
					if (c == 2) player.sendChatToPlayer(Cc.Yellow + "You own this land");
				}
			}
			ChunkProtection.setChunkOwnerMessage(playerName, owner);
			ChunkProtection.setChunkCoordinates(playerName, newChunkCoordinates);
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		int numberOfPlayers = PowerTools.server.getAllUsernames().length;
		EntityPlayerMP player;
		for (int i=0; i<numberOfPlayers; i++)
		{
			player = ((EntityPlayerMP)PowerTools.server.getConfigurationManager().playerEntityList.get(i));
			checkPlayerChunk(player);
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
		return "ChunkProtectionTicks";
	}
	@Override
	public int nextTickSpacing()
	{
		return 10;
	}

}
