package Oskar13.TheCharacters;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

public class Characters {

	 public static HashMap<String,Characters > PLAYERS = new HashMap<String, Characters>();

		
		public EntityPlayer player;
		public  Stats stats;
	 
		
		public  Characters(EntityPlayer player, Stats stats	) {
			
			this.player = player;
			this.stats = stats;
		}
		
		
	 public static void addPlayer(String player, Characters chara) {

		 PLAYERS.put(player, chara);
	 }
	 
	 public static void removePlayer(String player) {

		 PLAYERS.remove(player);
	 }
	 
	 public static Characters getPlayer(String nick) { 
		 
		 return PLAYERS.get(nick);
	 }
	 
	 
	 public  Stats getStats() { 
		 return stats;
		 
	 }
	 
	 
	 
	 
}
