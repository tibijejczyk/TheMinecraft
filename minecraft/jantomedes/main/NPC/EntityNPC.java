package jantomedes.main.NPC;

import jantomedes.main.NPC.Tasks.TaskSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPC extends EntityCreature implements INPCQuestListener, INPCTaskListener{

	/**Typ, od kt�rego mo�e zale�e� np. kszta�t modelu, tekstura, zachowanie itp.
	 * Wszystkie typy s� zapisane i b�d� dodawane w klasie EnumNPCTypes w jantomedes.main.NPC
	 */
	public byte type;
	
	/**Grupa NPC jest moim pomys�em, kt�ry u�atwi komunikacje eNPeC�w z adminem.
	 * Dzi�ki temu je�li admin chce ustali� jak�� list� quest�w danym eNPeCom to nie musi tego
	 * ustala� dla ka�dego z osobna, tylko wystarczy, �e zrobi to dla danej grupy.
	 * Mo�e te� rozkaza� jaki� task danej grupie eNPeC�w.
	 */
	public String group;
	
	/**Unikalne id nadawane ka�demu eNPeCowi
	 * Szczerze m�wi�c, nie wiem czy ma to jaki� specjalny sens, no c�, Oskar mnie o to
	 * prosi�, wi�c...
	 * 
	 * PS. Z racji, �e id nie maj� jak narazie wbudowanego zastosowania to zostawiam je nieu�yte
	 */
	public int NPCid;
	
	/**Tasksety przechowuj� zachowanie jakie eNPeCek ma wykonywa�.
	 * Jest to np. zaatakowanie danego gracza b�d� pod��anie gdzie�.
	 * Tasksety nie s� zestawami quest�w i maj� wi�kszy priorytet ni� one.
	 */
	public TaskSet taskSet;
	
	public EntityNPC(World par1World, EnumNPCTypes typeArg, String group){
		super(par1World);
		this.type = EnumNPCTypes.getTypeIdByType(typeArg);
		this.group = group;
		this.texture = EnumNPCTypes.getTexturePath(typeArg);
		this.moveSpeed = EnumNPCTypes.getMoveSpeed(typeArg);
		this.taskSet = new TaskSet();
		addAITasks(typeArg);
		
	}

	private void addAITasks(EnumNPCTypes typeArg){
		if(EnumNPCTypes.shouldWatchClosest(typeArg)){
			this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F));
		}
	}

	@Override
	public int getMaxHealth(){
		EnumNPCTypes.getMaxHealth(EnumNPCTypes.getTypeById(type));
	}
	
	public boolean interact(EntityPlayer entityPlayer)
    {
       if(canShowGUI(entityPlayer)){
    	   entityPlayer.addChatMessage("GUI si� pokazuje");
       }
       else{
    	   entityPlayer.addChatMessage("GUI si� nie pokazuje");
       }
    }
	
	public boolean canShowGUI(EntityPlayer entityPlayer){
		if(taskSet.canShowGUIToPlayer(entityPlayer) && EnumNPCTypes.canShowGUI(EnumNPCTypes.getTypeById(type))){
			return true;
		}
		return false;
	}
	
	public int getAttackStrength(Entity par1Entity){
		return EnumNPCTypes.getAttackStrength(EnumNPCTypes.getTypeById(type));
    }
	
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound){
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Type of NPC", this.type);
	}
	
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound){
		super.readEntityFromNBT(par1NBTTagCompound);
		this.type = (byte)par1NBTTagCompound.getInteger("Type of NPC");
	}
	
	protected boolean isAIEnabled()
    {
        return true;
    }

}