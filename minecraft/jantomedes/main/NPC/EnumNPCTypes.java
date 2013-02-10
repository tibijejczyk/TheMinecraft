package jantomedes.main.NPC;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public enum EnumNPCTypes {

	TALKER((byte)0, true, 0F, 12, "/textures/jantomedes/przykladowa-tekstura.png", 2),
	GUARDIAN((byte)1, false, 0F, 20, "/textures/jantomedes/przykladowa-tekstura.png", 5, new ItemStack(Item.swordSteel), false);
	//Oczywiœcie bêdzie i mo¿e byæ wiêcej typów, a one same mog¹ mieæ wiêcej opcji
	
	/**Id typu enPeCeka. S³u¿y tylko do tego by ³atwiej by³o zapisaæ typ eNPeCeka
	 * w tagach NBT.
	 */
	private final byte id;
	private final boolean canShowGUI;
	private final float moveSpeed;
	private final int maxHealth;
	//private final Model model;
	private final String texturePath;
	private final int attackStrength;
	private final ItemStack holdItem;
	private final boolean shouldWatchClosest;
	
	private EnumNPCTypes(byte par1, boolean par2, float par3, int par4, String par5, int par6, ItemStack par7, boolean par8){
		this.id = par1;
		this.canShowGUI = par2;
		this.moveSpeed = par3;
		this.maxHealth = par4;
		//this.model = [...]
		this.texturePath = par5;
		this.attackStrength = par6;
		this.holdItem = par7;
		this.shouldWatchClosest = par8;
	}
	
	private EnumNPCTypes(byte par1, boolean par2, float par3, int par4, String par5, int par6){
		this.id = par1;
		this.canShowGUI = par2;
		this.moveSpeed = par3;
		this.maxHealth = par4;
		//this.model = [...]
		this.texturePath = par5;
		this.attackStrength = par6;
		this.holdItem = null;
		this.shouldWatchClosest = true;
	}
	
	public static byte getTypeIdByType(EnumNPCTypes type){
		return type.id;
	}
	
	public static EnumNPCTypes getTypeById(int type){
		switch(type){
			case 0: return EnumNPCTypes.TALKER;
			case 1: return EnumNPCTypes.GUARDIAN;
			default: return EnumNPCTypes.TALKER;
		}
			
	}
	
	public static boolean canShowGUI(EnumNPCTypes type){
		return type.canShowGUI;
	}
	
	public static float getMoveSpeed(EnumNPCTypes type){
		return type.moveSpeed;
	}
	
	public static int getMaxHealth(EnumNPCTypes type){
		return type.maxHealth;
	}
	
	public static int getAttackStrength(EnumNPCTypes type){
		return type.attackStrength;
	}
	
	public ItemStack getHoldItem(EnumNPCTypes type){
		return type.holdItem;
	}
	
	public static boolean shouldWatchClosest(EnumNPCTypes type){
		return type.shouldWatchClosest;
	}
	
	public static String getTexturePath(EnumNPCTypes type){
		return type.texturePath;
	}
	
}