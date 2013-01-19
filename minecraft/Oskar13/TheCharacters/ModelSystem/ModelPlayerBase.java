package Oskar13.TheCharacters.ModelSystem;


import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public abstract class ModelPlayerBase extends ModelBase
{
	//Fields from ModelBase
	/*

		public boolean isRiding = false; 
		public List boxList = new ArrayList();
		public boolean field_40301_k = true;
		public int textureWidth = 64;
		public int textureHeight = 32;
	*/
	
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
	
	public String texture, textureUrl;
    public int armorSlowdown, blockState = 0;
    public boolean isSneak, hasBow;
    public int heldItemLeft;
	public float yOffset = 0F;
    /**
     * 
     * Records whether the model should be rendered holding an item in the right hand, and if that item is a block.
     */
    public int heldItemRight;
  
    /** Records whether the model should be rendered aiming a bow. */
    public boolean aimedBow;

	
    public ModelPlayerBase(float a) {
    	
    	init(a);
    }
    
    
   public ModelPlayerBase(float par1, float par2, float par3, float par4) {
    	
    	init(par1, par2, par3, par4);
    }
    
    
    public ModelPlayerBase(String texture, String textureUrl)

	
	{
		if(texture == null)
			System.out.println("PMAPI - A model has no texture!");
		this.texture = texture;
		this.textureUrl = textureUrl;
		
		
		init(0f);
	}

	public void init(float strech)
	{
		  init(strech, 0.0F, 64, 32);
	}
	
	
	public void init(float par1, float par2, float par3, float par4)
	{
	}
	

	public void setRotationAngles(float move, float swing, float tick, float horz, float vert, float scale)
	{
	}
	

	public void render(Entity entity, float move, float swing, float tick, float horz, float vert, float scale)
	{
	}
	
	
	public void drawFirstPersonHand()
	{
	}
	

	public void specials(RenderManager renderMan, EntityPlayer player)
	{
	}
	

	protected void renderDrop(RenderManager renderMan, EntityPlayer player, ModelRenderer box, float scale, float scaleFactor, float posX, float posY, float posZ)
    {
		ItemStack drop = player.inventory.getCurrentItem();
        if(drop == null)
			return;
		GL11.glPushMatrix();
		if(box != null)
			box.postRender(scale);
		GL11.glTranslatef(posX, posY, posZ);
		if(player.fishEntity != null)
			drop = new ItemStack(Item.stick);
		EnumAction enumaction = null;
		if(player.getItemInUseCount() > 0)
			enumaction = drop.getItemUseAction(); 
		if(drop.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[drop.itemID].getRenderType()))
		{
			float f4 = 0.5F * scaleFactor;
			GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
			f4 *= 0.75F;
			GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f4, -f4, f4);
		} else if(drop.itemID == Item.bow.itemID)		{
			float f5 = 0.625F * scaleFactor;
			GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
			GL11.glRotatef(-20F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f5, -f5, f5);
			GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
		} else if(Item.itemsList[drop.itemID].isFull3D()  )
		{
			float f6 = 0.625F * scaleFactor;
			if(Item.itemsList[drop.itemID].shouldRotateAroundWhenRendering())
			{
				GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(0.0F, -0.125F, 0.0F);
			}
			if(player.getItemInUseCount() > 0 && enumaction == EnumAction.block)
			{
				GL11.glTranslatef(0.05F, 0.0F, -0.1F);
				GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-10F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-60F, 0.0F, 0.0F, 1.0F);
			}
			GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
			GL11.glScalef(f6, -f6, f6);
			GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
		} else
		{
			float f7 = 0.375F * scaleFactor;
			GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
			GL11.glScalef(f7, f7, f7);
			GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
		}
		if(drop.itemID == Item.potion.itemID)
		{
			int j = drop.getItem().getIconFromDamage(drop.getItemDamage());
			float f9 = (float)(j >> 16 & 0xff) / 255F;
			float f10 = (float)(j >> 8 & 0xff) / 255F;
			float f11 = (float)(j & 0xff) / 255F;
			GL11.glColor4f(f9, f10, f11, 1.0F);
			renderMan.itemRenderer.renderItem(player, drop, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			renderMan.itemRenderer.renderItem(player, drop, 1);
		} else
			renderMan.itemRenderer.renderItem(player, drop, 0);
		GL11.glPopMatrix();
	}
	

	protected void renderPumpkin(RenderManager renderMan, EntityPlayer player, ModelRenderer box, float scale, float scaleFactor, float posX, float posY, float posZ)
    {
		ItemStack pumpkin = player.inventory.armorItemInSlot(3);
		if(pumpkin == null || pumpkin.getItem().itemID >= 256)
			return;
		GL11.glPushMatrix();
		if(box != null)
			box.postRender(scale);
		if(RenderBlocks.renderItemIn3d(Block.blocksList[pumpkin.itemID].getRenderType()))
		{
			float f1 = scaleFactor;
			GL11.glTranslatef(posX, posY, posZ);
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f1, -f1, f1);
		}
		renderMan.itemRenderer.renderItem(player, pumpkin, 0);
		GL11.glPopMatrix();
    }
}