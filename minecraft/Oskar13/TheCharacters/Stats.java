package Oskar13.TheCharacters;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import Oskar13.OskarStart;

public class Stats {

	
	
	public int hp;
	public int mp;
	public int str;
	public int dex;
	public int  def;
	public String modelName;
	
	public Stats()  { 
   OskarStart.debug("Init Stats");
	hp = 50000;
	mp = 0;
	str = 0;
	dex = 0;
	def = 0;
	modelName = "Ghast";
		

	}
	

	public void onTick(EntityPlayer player, EntityPlayerMP playerMP) {
		
if(hp == 50000) {
	
	player.addChatMessage("XD");
}
		
	}
}

