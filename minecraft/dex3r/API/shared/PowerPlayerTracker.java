package dex3r.API.shared;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;
import dex3r.API.Colors;
import dex3r.API.chunkprotection.ChunkProtection;

public class PowerPlayerTracker implements IPlayerTracker
{

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		String playerName = player.getCommandSenderName();
		PowerTools.addPlayer(playerName);
		int nr = PowerTools.getNumberForPlayerName(playerName);
		PowerTools.plInfo[nr].isConnected = true;

		/*if (PowerTools.powerLoginLoaded)
		{
			if (playerName.equals("Server"))
				player.setDead(); 
			// TODO find method to kick this player
									// instead.
			PowerLogin.plSetLoginPos(playerName, player.posX, player.posY, player.posZ);
			if (PowerLogin.plGetRegisterStatus(playerName))
			{
				player.sendChatToPlayer(Colors.Yellow + "Welcome back, " + playerName + ". Use " + Colors.White
						+ "/login [password]");
			} else
			{
				player.sendChatToPlayer(Colors.Yellow + "You need to be registerd and logged in to play on this server.");
				player.sendChatToPlayer(Colors.Yellow + "Use " + Colors.White + "/register [new password]" + Colors.Yellow + " or "
						+ Colors.White + "/login [password]");
			}
		}*/

		if (PowerTools.chunkProtectionLoaded)
		{
			ChunkProtection.addPlayerChunkInfo(playerName);
			ChunkProtection.chunkSetBorderInfo(playerName, ChunkProtection.cfgDefaultBorderInfo);
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		if (PowerTools.powerLoginLoaded)
		{
			String playerName = player.getCommandSenderName();
			int nr = PowerTools.getNumberForPlayerName(playerName);
			PowerTools.plInfo[nr].isConnected = false;
			/*if (PowerTools.powerLoginLoaded)
			{
				PowerLogin.plLogout(playerName);
			}*/
		}
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

}
