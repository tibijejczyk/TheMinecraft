package dex3r.API.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.IPlayerTracker;
import dex3r.API.Tools;
//import dex3r.API.chunksOwning.ChunkProtection;

public class PlayerTracker implements IPlayerTracker
{
  public void onPlayerLogin(EntityPlayer player)
  {
    String playerName = player.username;
    Tools.addPlayer(playerName);
    int nr = Tools.getNumberForPlayerName(playerName);
    Tools.plInfo[nr].isConnected = true;

    //TODO: uncomment
//    if (Tools.chunkProtectionLoaded)
//    {
//      ChunkProtection.addPlayerChunkInfo(playerName);
//      ChunkProtection.chunkSetBorderInfo(playerName, ChunkProtection.cfgDefaultBorderInfo);
//    }
  }

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		
	}
	
	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
	
	}
	
	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
		
	}
}
