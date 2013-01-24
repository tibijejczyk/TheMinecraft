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
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
                EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, this, 80, 3, true);
                LanguageRegistry.instance().addStringLocalization("entity.MainNPC.NPC.name", "Non-player character");
        }
       
        @PostInit
        public void postInit(FMLPostInitializationEvent event) {
        	
        }
        
}