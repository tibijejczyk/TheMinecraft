package dex3r.API.util;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import java.util.List;

import dex3r.API.Tools;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeDirection;

public class MapLimitTickHandler implements IScheduledTickHandler
{
	public void checkMapLimit(EntityPlayer player)
	{
		int dimension = player.worldObj.provider.dimensionId;
		double posX = player.posX;
		double posY = player.posY;
		double posZ = player.posZ;

		boolean enabled = false;
		if (dimension == -1)
		{
			if (Tools.cfgMapLimitNether.enabled)
				enabled = true;
		} else if (dimension <= Tools.cfgHighestDimension)
		{
			if (Tools.cfgMapLimitDimemsion[dimension].enabled)
				enabled = true;
		}

		if (enabled)
		{
			double maxZ;
			double minX;
			double minZ;
			double maxX;
			if (dimension == -1)
			{
				minX = Tools.cfgMapLimitNether.minX + 0.5D;
				minZ = Tools.cfgMapLimitNether.minZ + 0.5D;
				maxX = Tools.cfgMapLimitNether.maxX - 0.5D;
				maxZ = Tools.cfgMapLimitNether.maxZ - 0.5D;
			} else
			{
				minX = Tools.cfgMapLimitDimemsion[dimension].minX + 0.5D;
				minZ = Tools.cfgMapLimitDimemsion[dimension].minZ + 0.5D;
				maxX = Tools.cfgMapLimitDimemsion[dimension].maxX - 0.5D;
				maxZ = Tools.cfgMapLimitDimemsion[dimension].maxZ - 0.5D;
			}

			double newX = 0.0D;
			double newY = 0.0D;
			double newZ = 0.0D;
			String message = "";
			boolean limitReached = false;
			if (posX < minX)
			{
				newX = minX;
				newY = posY;
				newZ = posZ;
				limitReached = true;
				message = "x<" + (int) minX;
			}
			if (posZ < minZ)
			{
				newX = posX;
				newY = posY;
				newZ = minZ;
				limitReached = true;
				message = "z<" + (int) minZ;
			}
			if (posX > maxX)
			{
				newX = maxX;
				newY = posY;
				newZ = posZ;
				limitReached = true;
				message = "x>" + (int) maxX;
			}
			if (posZ > maxZ)
			{
				newX = posX;
				newY = posY;
				newZ = maxZ;
				limitReached = true;
				message = "z>" + (int) maxZ;
			}
			if (limitReached)
			{
				boolean blockFeet = false;
				boolean blockEyes = false;

				for (; (!blockFeet) || (!blockEyes); newY += 1.0D)
				{
					do
					{
						blockFeet = player.worldObj.isAirBlock((int) newX, (int) newY, (int) newZ);
						blockEyes = player.worldObj.isAirBlock((int) newX, (int) newY + 1, (int) newZ);
					}while ((blockFeet) && (blockEyes));
				}
				player.setPosition(newX, newY, newZ);
				player.sendChatToPlayer("§cYou've reached the limit of this map. " + message);
			}
		}
	}

	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		int numberOfPlayers = Tools.server.getAllUsernames().length;

		for (int i = 0; i < numberOfPlayers; i++)
		{
			EntityPlayer player = (EntityPlayer)Tools.server.getConfigurationManager().playerEntityList.get(i);
			checkMapLimit(player);
		}
	}

	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
	}

	public EnumSet ticks()
	{
		return EnumSet.of(TickType.SERVER);
	}

	public String getLabel()
	{
		return "MapLimitTicks";
	}

	public int nextTickSpacing()
	{
		return 40;
	}
}
