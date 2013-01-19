package Oskar13;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.proxyClient.proxyClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;


import Oskar13.TheCharacters.Characters;
import Oskar13.TheCharacters.NBTRead;
import Oskar13.TheCharacters.NBTSave;
import Oskar13.TheCharacters.Stats;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketSaveData extends PacketHandler implements IConnectionHandler{

	public void readClient(int id, ByteArrayDataInput dat, Object[] extradata) {
		
		

		int hp = dat.readInt();
	    int mp = dat.readInt();
	    int def = dat.readInt();
	    int str = dat.readInt();
	    int dex = dat.readInt();
	    String playerModel = dat.readUTF();
String nick = dat.readUTF();
	    
		
		Characters.addPlayer(nick,  new Characters((EntityPlayer) extradata[0], new Stats()));
		
		if(proxyClient.getPlayer().username.equals(nick)) {
			Characters.getPlayer(nick).getStats().hp = hp;
			Characters.getPlayer(nick).getStats().mp = mp;
			Characters.getPlayer(nick).getStats().def = def;
			Characters.getPlayer(nick).getStats().str = str;
			Characters.getPlayer(nick).getStats().dex = dex;
			Characters.getPlayer(nick).getStats().modelName =  playerModel;
		}
		
	}
	
	
	
	
	public void readServer(int id, ByteArrayDataInput dat, Object[] extradata) {
		
		

EntityPlayer player = (EntityPlayer)extradata[0];
	    
	    
	    writeData(player.username);

	Stats stats =  Characters.getPlayer(player.username).getStats();
		stats.hp = dat.readInt();
		stats.mp  = dat.readInt();
		
		stats.def	 = dat.readInt();
		stats.str = dat.readInt();
		stats.dex =  dat.readInt();
		stats.modelName  = dat.readUTF();
	
		
	}
	
	
	public void writeData( String nick) {
		
new NBTSave(nick);
	}
	
	
	public void readData(String s) {
		
new NBTRead(s);
		
		
	}
	
	
	

	
	
	
	
	@Override
	public void playerLoggedIn(Player other, NetHandler netHandler,
			INetworkManager manager) {
		FMLCommonHandler.instance().getFMLLogger().warning("Dzia³a PlayerLogin");
		
		EntityPlayer player = (EntityPlayer)other;
		Characters.addPlayer(player.username, new Characters(player, new Stats()));
		
		
		
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			new NBTRead(player.username);
			
		}
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {

		
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {

		
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
	
		
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			INetworkManager manager, Packet1Login login) {


	}




}
