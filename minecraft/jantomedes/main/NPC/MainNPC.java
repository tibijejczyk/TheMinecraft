package jantomedes.main.NPC;

import jantomedes.proxy.ServerProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="TMC J MainNPC", name="MainNPC", version="1.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=true)
public class MainNPC{

        @Instance("MainNPC")
        public static MainNPC instance;
       
        @SidedProxy(clientSide="jantomedes.proxy.ClientProxy", serverSide="jantomedes.proxy.ServerProxy")
        public static ServerProxy proxy;
       
        @PreInit
        public void preInit(FMLPreInitializationEvent event) {
        	
        }
       
        @Init
        public void load(FMLInitializationEvent event) {
                proxy.Jantomedes();
        }
       
        @PostInit
        public void postInit(FMLPostInitializationEvent event) {
        	
        }
}