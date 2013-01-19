package Oskar13.TheCharacters.ModelSystem;

public class ModelHumanArmor extends ModelHuman
	implements IModelArmor
{
	public ModelHumanArmor()
    {
        super("/armor/", null);
    }

	public ModelHumanArmor(String texture, String textureUrl)
    {
        super(texture, textureUrl);
    }

	public String texturePath(String armorPrefix, int armorSlot)
	{
		return texture + armorPrefix + "_" + (armorSlot != 2 ? 1 : 2) + ".png";
	}
	
	public float setStrechLayer(int armorSlot)
	{
		return (armorSlot == 2 ? 0.5F : 1F);
	}
	
	public void setVisibleBoxes(int armorSlot)
	{
	    head.showModel = headwear.showModel = (armorSlot == 0);
        body.showModel = (armorSlot == 1 || armorSlot == 2);
        rightArm.showModel = leftArm.showModel = (armorSlot == 1);
        rightLeg.showModel = leftLeg.showModel = (armorSlot == 2 || armorSlot == 3);
	}
}