package Oskar13;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import Oskar13.TheCharacters.Stats;
import Oskar13.commands.CommandBonus;
import Oskar13.commands.CommandMessage;

import net.java.games.util.plugins.Plugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.proxyClient.proxyClient;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.proxyServer.proxyServer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "OskarStart", name = "Klasa Startowa Oskara", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class, channels = { "Oskar13" })
public class OskarStart 
{

	@SidedProxy(clientSide = "net.minecraft.client.proxyClient.proxyClient", serverSide = "cpw.mods.fml.common.proxyServer.proxyServer")
	public static proxyServer proxy;


	@Instance("OskarStart")
	public static OskarStart instance;

	// PACKETS
	public PacketSaveData packetSaveData = new PacketSaveData();
    
    
    
	private static boolean debug = true;

	public static void debug(String deg)
	{

		if (debug == true)
		{
			System.out.println(deg);

		}
	}

	

	public static boolean isServer()
	{

		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	public static void sendStats(String nick, Stats stats)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
		DataOutputStream dos = new DataOutputStream(bos);
debug("Packet stats was send");
		try
		{
			dos.writeInt(1);
			dos.writeInt(stats.hp);
			dos.writeInt(stats.mp);
			dos.writeInt(stats.def);
			dos.writeInt(stats.str);
			dos.writeInt(stats.dex);
			dos.writeUTF(stats.modelName);
			dos.writeUTF(nick);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "Oskar13";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			PacketDispatcher.sendPacketToServer(pkt);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager()
					.getPlayerForUsername(nick);
			PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
		}
	}
	
	
	@ServerStarted
	public void onServerStarted(FMLServerStartedEvent event) {
		CommandHandler handler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
		
		handler.registerCommand(new  CommandMessage());
		handler.registerCommand(new  CommandBonus());
	}

	
	public void onTickInGame() {
	
		
		
	//	System.out.println("test");
	   // proxy.onTickInGame();
		
	}
	@Init
	public void load(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerConnectionHandler(packetSaveData);
		proxy.Oskar13();
	}

	



}
