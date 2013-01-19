package Oskar13.TheCharacters.ModelSystem;

public interface IModelArmor
{
	//armorSlot value meaning
	/*
		0 = Helmet
		1 = Chestplate
		2 = Leggings
		3 = Boots
	*/


	public abstract float setStrechLayer(int armorSlot);

	public abstract String texturePath(String armorPrefix, int armorSlot);
	

	public abstract void setVisibleBoxes(int armorSlot);
}