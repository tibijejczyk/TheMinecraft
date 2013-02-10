package jantomedes.main.NPC.Tasks;

import java.util.List;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;


public class TaskSet{

	//TaskSety nie s¹ aktualnie u¿ywane
	
	/**
	 * Mo¿liwe jest przydzielenie dziesiêciu konkretnych zadañ ka¿demu eNPeCowi.
	 * Oczywiœcie TaskSet przechowuje wiêcej danych ni¿ tylko dziesiêæ celi.
	 * Tym wy¿szy numer, tym wy¿szy priorytet.
	 */
	public Task[] taskList = new Task[10];
	
	public List<String> shouldBeAttackedPlayerNicks;
	public List<EntityMob> shouldBeAttackedMobTypes;
	public List<String> playersWhoCantSeeTheGUI;
	
	public boolean canShowGUI = true;
	
	//Narazie tyle. Jakieœ pomys³y?
	
	public void writeToNBT(NBTTagCompound nbt){
		/*NBTTagCompound mainTasks = new NBTTagCompound();
		for(int i = 0; i < taskList.length; i++){
			NBTTagCompound thatTask = new NBTTagCompound();
			thatTask.setInteger("Task type id", taskList[i].id);
			thatTask.setInteger("Jakies dane1", taskList[i].dane1);
			thatTask.setInteger("Jakies dane2", taskList[i].dane2);
			mainTasks.setCompoundTag("Task nr"+taskList[i], thatTask);
		}
		nbt.setCompoundTag("Main Tasks", mainTasks);
		
		NBTTagCompound playersToAttackList = new NBTTagCompound();
		playersToAttackList.setInteger("Size", shouldBeAttackedPlayerNicks.size());
		
		for(int i = 0; i < shouldBeAttackedPlayerNicks.size(); i++){
			playersToAttackList.setString("Nick"+i, );
		}
		nbt.setCompoundTag("Players To Attack", playersToAttackList);
		*/
	}
	public void readFromNBT(NBTTagCompound nbt){
		
	}
	
	public boolean canShowGUIToPlayer(EntityPlayer player){
		if(this.canShowGUI && !playersWhoCantSeeTheGUI.contains(player.username)){
			return true;
		}
		return false;
	}
	
	
	
	
}
