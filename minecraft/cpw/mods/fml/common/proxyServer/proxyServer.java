package cpw.mods.fml.common.proxyServer;

import net.minecraft.entity.player.EntityPlayerMP;
import Oskar13.TickHandler;
import Oskar13.TheCharacters.Characters;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class proxyServer {
	// Ka�dy z Was ma w�asn� metode do wywo�ywania rejestacji z poziomu moda.
	// ew. mo�na dorobi� jeszcze jakie� nie b�dzie syfu!:(
	
	public void Oskar13() {
		
		TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
	}
	
	public void Dexter()  { } 
	
	public void Omlet() {  }

	public void onTickInGame() {
		
		String[] usernames = FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames();
		for(int i = 0; i < usernames.length; i++) {
			EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(usernames[i]);
			if(Characters.getPlayer(player.username) != null) {
				Characters.getPlayer(usernames[i]).getStats().onTick(player, player);
			}
		}
	}
		
	
	
	
	
}
