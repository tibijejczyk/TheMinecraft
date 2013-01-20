package dex3r.main.factions;

import cpw.mods.fml.server.FMLServerHandler;
import dex3r.main.DexMain;
import net.minecraft.entity.player.EntityPlayer;

public class FactionMember
{
	public String nickname;
	public FactionMemberRank rank;
	public int xpGiven;
	public boolean onWar;
	private EntityPlayer player;
	
	public FactionMember(String nickname, FactionMemberRank rank)
	{
		this.nickname = nickname;
		this.rank = rank;
		xpGiven = 0;
		onWar = false;
	}
	
	public EntityPlayer getPlayer()
	{
		if(player == null && DexMain.isPlayerOnline(nickname))
		{
			player = FMLServerHandler.instance().getServer().getConfigurationManager().getPlayerForUsername(nickname);
		}
		return player;
	}
}
