package Oskar13.TheCharacters;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import Oskar13.OskarStart;

public class Stats {
//Max stats = 20;
	
	public int hp;
	public int mp;
	public int str;
	public int dex;
	public int  def;
	public String modelName;
	
	public Stats()  { 
   OskarStart.debug("Init Stats");
	hp = 20;
	mp = 500;
	str = 20;
	dex = 20;
	def = 20;
	modelName = "Ghast";
		

	}
	
	
	public float getSpeedOnGround() {
		
		
		return (float)dex / 100; 
		
		
	}
	
	
	public float getSpeedInAir() {
		

		return (float)dex / 100; 
	}

		
	}


