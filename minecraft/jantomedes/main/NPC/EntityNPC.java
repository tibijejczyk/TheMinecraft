package jantomedes.main.NPC;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityNPC extends EntityCreature implements NPCQuestListener, NPCTaskListener{

	/**Typ, od kt�rego mo�e zale�e� np. kszta�t modelu, tekstura, zachowanie itp.
	 * Wszystkie typy s� zapisane i b�d� dodawane w klasie NPCTypes w jantomedes.main.NPC
	 */
	public int type;
	
	/**Grupa NPC jest moim pomys�em, kt�ry u�atwi komunikacje eNPeC�w z adminem.
	 * Dzi�ki temu je�li admin chce ustali� jak�� list� quest�w danym eNPeCom to nie musi tego
	 * ustala� dla ka�dego z osobna, tylko wystarczy, �e zrobi to dla danej grupy.
	 */
	public NPCGroup group;
	
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
	
	public EntityNPC(World par1World, int type, NPCGroup group){
		super(par1World);
		this.type = type;
		this.group = group;
		this.texture = NPCTypes.getTextureByType(type);
		this.moveSpeed = NPCTypes.getMoveSpeedByType(type);
		this.taskSet = new TaskSet(TaskSet.emptyTaskSet);
		if(NPCTypes.shouldWatchClosestByType(type)){
			this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F));
		}
		
	}

	@Override
	public int getMaxHealth(){
		return 20;
	}
	
	public boolean interact(EntityPlayer entityPlayer)
    {
       //TODO
    }
	
	public boolean canShowGUI(EntityPlayer entityPlayer){
		return taskSet.canShowGUI(entityPlayer);
	}

}
