package net.minecraft.client.proxyClient;

import Oskar13.TickHandler;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.proxyServer.proxyServer;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class proxyClient extends proxyServer {
	
	// Ka¿dy z Was ma w³asn¹ metode do wywo³ywania rejestacji z poziomu moda.
	// ew. mo¿na dorobiæ jeszcze jakieœ nie bêdzie syfu!:(
	
	@Override
	public void Oskar13()  {
	TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
	TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
	}
	
	@Override
	public void Dexter() {}
	
	@Override
	public void Omlet() {}


	public static EntityPlayer getPlayer() {
		return FMLClientHandler.instance().getClient().thePlayer;
	}

	
	

}
