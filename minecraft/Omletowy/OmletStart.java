package Omletowy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.proxyServer.proxyServer;

@Mod(modid = "OmletStart", name = "Omlet TheMinecraft Mod", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)

public class OmletStart {
	
	@SidedProxy(clientSide = "net.minecraft.client.proxyClient.proxyClient", serverSide = "cpw.mods.fml.common.proxyServer.proxyServer")
	public static proxyServer proxy;
	
	@Instance("OmletStart")
	public static OmletStart instance;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@Init
	public void init(FMLInitializationEvent event) {
		
	}

	@PostInit
	public static void postInit(FMLPostInitializationEvent event) {
		
	}
}