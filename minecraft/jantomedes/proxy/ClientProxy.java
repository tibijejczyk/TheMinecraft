package jantomedes.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import jantomedes.main.NPC.EntityNPC;
import jantomedes.main.NPC.ModelNPC;
import jantomedes.main.NPC.RenderNPC;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy{

	@Override
	public void Jantomedes(){
		MinecraftForgeClient.preloadTexture("/textures/jantomedes/przykladowa-tekstura.png");
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC(new ModelNPC(), 0.3F));
	}
	
}
