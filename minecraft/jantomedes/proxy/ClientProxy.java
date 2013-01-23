package jantomedes.proxy;

import jantomedes.main.NPC.EntityNPC;
import jantomedes.main.NPC.RenderNPC;
import Oskar13.TheCharacters.ModelSystem.ModelHuman;
import Oskar13.TheCharacters.ModelSystem.ModelPlayerBase;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy{

	@Override
	public void Jantomedes(){
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC(new ModelHuman(), 0.3F));
	}
	
}
