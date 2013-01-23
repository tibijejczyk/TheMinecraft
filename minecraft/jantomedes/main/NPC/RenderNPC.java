package jantomedes.main.NPC;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import Oskar13.TheCharacters.ModelSystem.ModelHuman;


public class RenderNPC extends RenderLiving{

	protected ModelHuman model;
	
	public RenderNPC(ModelHuman par1ModelHuman, float par2){
		super(par1ModelHuman, par2);
		model = ((ModelHuman)mainModel);
	}

	public void renderNPC(EntityNPC entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(entity, par2, par4, par6, par8, par9);
    }

public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        renderNPC((EntityNPC)par1EntityLiving, par2, par4, par6, par8, par9);
    }

public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderNPC((EntityNPC)par1Entity, par2, par4, par6, par8, par9);
    }
}
