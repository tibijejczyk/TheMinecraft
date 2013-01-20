package powertools.shared;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayerMP;

import powertools.shared.PowerTools;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class MapLimitTickHandler  implements IScheduledTickHandler
{
	public void checkMapLimit(EntityPlayerMP player)
	{
		int dimension = player.worldObj.provider.dimensionId;
		double posX = player.posX;
		double posY = player.posY;
		double posZ = player.posZ;

		boolean enabled = false; // for this dimension
		if (dimension == -1)
		{
			if (PowerTools.cfgMapLimitNether.enabled) enabled = true;
		}
		else if (dimension <= PowerTools.cfgHighestDimension)
		{
			if (PowerTools.cfgMapLimitDimemsion[dimension].enabled) enabled = true;
		}

		if (enabled)
		{
			double minX;
			double minZ;
			double maxX;
			double maxZ;
			if (dimension == -1)
			{
				minX = PowerTools.cfgMapLimitNether.minX + 0.5;
				minZ = PowerTools.cfgMapLimitNether.minZ + 0.5;
				maxX = PowerTools.cfgMapLimitNether.maxX - 0.5;
				maxZ = PowerTools.cfgMapLimitNether.maxZ - 0.5;
			}
			else
			{
				minX = PowerTools.cfgMapLimitDimemsion[dimension].minX + 0.5;
				minZ = PowerTools.cfgMapLimitDimemsion[dimension].minZ + 0.5;
				maxX = PowerTools.cfgMapLimitDimemsion[dimension].maxX - 0.5;
				maxZ = PowerTools.cfgMapLimitDimemsion[dimension].maxZ - 0.5;
			}

			double newX = 0, newY = 0, newZ = 0;
			String message = "";
			boolean limitReached = false;
			if (posX < minX)
			{
				newX = minX;
				newY = posY;
				newZ = posZ;
				limitReached = true;
				message = "x<" + (int)minX;
			}
			if (posZ < minZ)
			{
				newX = posX;
				newY = posY;
				newZ = minZ;
				limitReached = true;
				message = "z<" + (int)minZ;
			}
			if (posX > maxX)
			{
				newX = maxX;
				newY = posY;
				newZ = posZ;
				limitReached = true;
				message = "x>" + (int)maxX;
			}
			if (posZ > maxZ)
			{
				newX = posX;
				newY = posY;
				newZ = maxZ;
				limitReached = true;
				message = "z>" + (int)maxZ;
			}
			if (limitReached)
			{
				boolean blockFeet = false;
				boolean blockEyes = false;
				while (!blockFeet || !blockEyes)
				{
					blockFeet = player.worldObj.isAirBlock((int)newX, (int)newY, (int)newZ);
					blockEyes = player.worldObj.isAirBlock((int)newX, (int)newY+1, (int)newZ);
					if (!blockFeet || !blockEyes) newY=newY+1;
				}
				player.setPositionAndUpdate(newX, newY, newZ);
				player.sendChatToPlayer(Cc.LightRed + "You've reached the limit of this map. " + message);
			}
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
			checkMapLimit(player);
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
		return "MapLimitTicks";
	}
	@Override
	public int nextTickSpacing()
	{
		return 40;
	}
}
