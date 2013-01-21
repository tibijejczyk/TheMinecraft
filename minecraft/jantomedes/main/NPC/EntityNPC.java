package jantomedes.main.NPC;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityNPC extends EntityCreature implements NPCQuestListener, NPCTaskListener{

	/**Typ, od którego mo¿e zale¿eæ np. kszta³t modelu, tekstura, zachowanie itp.
	 * Wszystkie typy s¹ zapisane i bêd¹ dodawane w klasie NPCTypes w jantomedes.main.NPC
	 */
	public int type;
	
	/**Grupa NPC jest moim pomys³em, który u³atwi komunikacje eNPeCów z adminem.
	 * Dziêki temu jeœli admin chce ustaliæ jak¹œ listê questów danym eNPeCom to nie musi tego
	 * ustalaæ dla ka¿dego z osobna, tylko wystarczy, ¿e zrobi to dla danej grupy.
	 */
	public NPCGroup group;
	
	/**Unikalne id nadawane ka¿demu eNPeCowi
	 * Szczerze mówi¹c, nie wiem czy ma to jakiœ specjalny sens, no có¿, Oskar mnie o to
	 * prosi³, wiêc...
	 * 
	 * PS. Z racji, ¿e id nie maj¹ jak narazie wbudowanego zastosowania to zostawiam je nieu¿yte
	 */
	public int NPCid;
	
	/**Tasksety przechowuj¹ zachowanie jakie eNPeCek ma wykonywaæ.
	 * Jest to np. zaatakowanie danego gracza b¹dŸ pod¹¿anie gdzieœ.
	 * Tasksety nie s¹ zestawami questów i maj¹ wiêkszy priorytet ni¿ one.
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
