package Oskar13.TheCharacters.ModelSystem;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import Oskar13.TheCharacters.Characters;



public class ModelHuman extends ModelPlayerBase
{
	public float yOffset = 0F;
    public ModelRenderer
		head,
		headwear,
		body,
		rightArm,
		leftArm,
		rightLeg,
		leftLeg
	;
	
	public ModelHuman()
    {
        super("/mob/char.png", Characters.skinURL);
    }

	public ModelHuman(String texture, String textureUrl)
    {
        super(texture, textureUrl);
    }
	
	public void init(float strech)
	{
	    armorSlowdown = 0;
        blockState = 0;
        isSneak = false;
        hasBow = false;
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8, strech);
        head.setRotationPoint(0.0F, 0.0F + yOffset, 0.0F);
        headwear = new ModelRenderer(this, 32, 0);
        headwear.addBox(-4F, -8F, -4F, 8, 8, 8, strech + 0.5F);
        headwear.setRotationPoint(0.0F, 0.0F + yOffset, 0.0F);
        body = new ModelRenderer(this, 16, 16);
        body.addBox(-4F, 0.0F, -2F, 8, 12, 4, strech);
        body.setRotationPoint(0.0F, 0.0F + yOffset, 0.0F);
        rightArm = new ModelRenderer(this, 40, 16);
        rightArm.addBox(-3F, -2F, -2F, 4, 12, 4, strech);
        rightArm.setRotationPoint(-5F, 2.0F + yOffset, 0.0F);
        leftArm = new ModelRenderer(this, 40, 16);
        leftArm.mirror = true;
        leftArm.addBox(-1F, -2F, -2F, 4, 12, 4, strech);
        leftArm.setRotationPoint(5F, 2.0F + yOffset, 0.0F);
        rightLeg = new ModelRenderer(this, 0, 16);
        rightLeg.addBox(-2F, 0.0F, -2F, 4, 12, 4, strech);
        rightLeg.setRotationPoint(-2F, 12F + yOffset, 0.0F);
        leftLeg = new ModelRenderer(this, 0, 16);
        leftLeg.mirror = true;
        leftLeg.addBox(-2F, 0.0F, -2F, 4, 12, 4, strech);
        leftLeg.setRotationPoint(2.0F, 12F + yOffset, 0.0F);
	}

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        head.render(f5);
        body.render(f5);
        rightArm.render(f5);
        leftArm.render(f5);
        rightLeg.render(f5);
        leftLeg.render(f5);
        headwear.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        head.rotateAngleY = f3 / 57.29578F;
        head.rotateAngleX = f4 / 57.29578F;
        headwear.rotateAngleY = head.rotateAngleY;
        headwear.rotateAngleX = head.rotateAngleX;
        rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
        leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        rightArm.rotateAngleZ = 0.0F;
        leftArm.rotateAngleZ = 0.0F;
        rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        rightLeg.rotateAngleY = 0.0F;
        leftLeg.rotateAngleY = 0.0F;
        if(isRiding)
        {
            rightArm.rotateAngleX += -0.6283185F;
            leftArm.rotateAngleX += -0.6283185F;
            rightLeg.rotateAngleX = -1.256637F;
            leftLeg.rotateAngleX = -1.256637F;
            rightLeg.rotateAngleY = 0.3141593F;
            leftLeg.rotateAngleY = -0.3141593F;
        }
        if(armorSlowdown != 0)
            leftArm.rotateAngleX = leftArm.rotateAngleX * 0.5F - 0.3141593F * (float)armorSlowdown;
        if(blockState != 0)
            rightArm.rotateAngleX = rightArm.rotateAngleX * 0.5F - 0.3141593F * (float)blockState;
        rightArm.rotateAngleY = 0.0F;
        leftArm.rotateAngleY = 0.0F;
        if(onGround > -9990F)
        {
            float f6 = onGround;
            body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
            rightArm.rotationPointZ = MathHelper.sin(body.rotateAngleY) * 5F;
            rightArm.rotationPointX = -MathHelper.cos(body.rotateAngleY) * 5F;
            leftArm.rotationPointZ = -MathHelper.sin(body.rotateAngleY) * 5F;
            leftArm.rotationPointX = MathHelper.cos(body.rotateAngleY) * 5F;
            rightArm.rotateAngleY += body.rotateAngleY;
            leftArm.rotateAngleY += body.rotateAngleY;
            leftArm.rotateAngleX += body.rotateAngleY;
            f6 = 1.0F - onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0F - f6;
            float f8 = MathHelper.sin(f6 * 3.141593F);
            float f10 = MathHelper.sin(onGround * 3.141593F) * -(head.rotateAngleX - 0.7F) * 0.75F;
            rightArm.rotateAngleX -= (double)f8 * 1.2D + (double)f10;
            rightArm.rotateAngleY += body.rotateAngleY * 2.0F;
            rightArm.rotateAngleZ = MathHelper.sin(onGround * 3.141593F) * -0.4F;
        }
        if(isSneak)
        {
            body.rotateAngleX = 0.5F;
            rightLeg.rotateAngleX -= 0.0F;
            leftLeg.rotateAngleX -= 0.0F;
            rightArm.rotateAngleX += 0.4F;
            leftArm.rotateAngleX += 0.4F;
            rightLeg.rotationPointZ = 4F;
            leftLeg.rotationPointZ = 4F;
            rightLeg.rotationPointY = 9F;
            leftLeg.rotationPointY = 9F;
            head.rotationPointY = 1.0F;
        } else
        {
            body.rotateAngleX = 0.0F;
            rightLeg.rotationPointZ = 0.0F;
            leftLeg.rotationPointZ = 0.0F;
            rightLeg.rotationPointY = 12F;
            leftLeg.rotationPointY = 12F;
            head.rotationPointY = 0.0F;
        }
        rightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        leftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        rightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        leftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
        if(hasBow)
        {
            float f7 = 0.0F;
            float f9 = 0.0F;
            rightArm.rotateAngleZ = 0.0F;
            leftArm.rotateAngleZ = 0.0F;
            rightArm.rotateAngleY = -(0.1F - f7 * 0.6F) + head.rotateAngleY;
            leftArm.rotateAngleY = (0.1F - f7 * 0.6F) + head.rotateAngleY + 0.4F;
            rightArm.rotateAngleX = -1.570796F + head.rotateAngleX;
            leftArm.rotateAngleX = -1.570796F + head.rotateAngleX;
            rightArm.rotateAngleX -= f7 * 1.2F - f9 * 0.4F;
            leftArm.rotateAngleX -= f7 * 1.2F - f9 * 0.4F;
            rightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
            leftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
            rightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
            leftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
        }
    }
	
	public void drawFirstPersonHand()
	{
	   
        
        float var2 = 1.0F;
        GL11.glColor3f(var2, var2, var2);
        this.onGround = 0.0F;
        this.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
       this.rightArm.render(0.0625F);
	}
	
	public void specials(RenderManager renderMan, EntityPlayer player)
	{
		renderDrop(renderMan, player, rightArm, 0.0625F, 1F, -0.0625F, 0.4375F, 0.0625F);
		renderPumpkin(renderMan, player, head, 0.0625F, 0.625F, 0.0F, -0.25F, 0.0F);
	}
}