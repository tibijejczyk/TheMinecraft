package Oskar13.commands;

import java.util.HashMap;
import java.util.Map;

import Oskar13.ItemBonus.ItemBonus;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CommandBonus extends CommandBase{

	

	@Override
	public String getCommandName() {
		return "bonus";
	}
@Override
	  public int getRequiredPermissionLevel()
	    {
	        return 3;
	    }
	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		
		 EntityPlayer var3 = getCommandSenderAsPlayer(var1);
			ItemStack item  = var3.inventory.getCurrentItem();
	
			
			if(item.isItemEnchantable()) {
				if(var2.length == 2){ 
					
					Map map = new HashMap();
					
					map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
					ItemBonus.addBonusToItemstack(map, item);
					var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length - 1) + " bunusow"	);

			}
				
					if(var2.length == 4){ 
						
						Map map = new HashMap();
						
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length - 2) + " bunusow"	);
				}
					
					if(var2.length == 6){ 
						
						Map map = new HashMap();
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						map.put(Integer.valueOf(var2[4]), Integer.valueOf(var2[5]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length  / 2) + " bunusow"	);
				}
					
					if(var2.length == 8){ 
						
						Map map = new HashMap();
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						map.put(Integer.valueOf(var2[4]), Integer.valueOf(var2[5]));
						map.put(Integer.valueOf(var2[6]), Integer.valueOf(var2[7]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length / 2) + " bunusow"	);
				}
					
					if(var2.length == 10){ 
						
						Map map = new HashMap();
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						map.put(Integer.valueOf(var2[4]), Integer.valueOf(var2[5]));
						map.put(Integer.valueOf(var2[6]), Integer.valueOf(var2[7]));
						map.put(Integer.valueOf(var2[8]), Integer.valueOf(var2[9]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length /2 ) + " bunusow"	);

				}
					
					if(var2.length == 12){ 
						
						Map map = new HashMap();
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						map.put(Integer.valueOf(var2[4]), Integer.valueOf(var2[5]));
						map.put(Integer.valueOf(var2[6]), Integer.valueOf(var2[7]));
						map.put(Integer.valueOf(var2[8]), Integer.valueOf(var2[9]));
						map.put(Integer.valueOf(var2[10]), Integer.valueOf(var2[11]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length /2) + " bunusow"	);

				}
					
					if(var2.length == 14){ 
						
						Map map = new HashMap();
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						map.put(Integer.valueOf(var2[4]), Integer.valueOf(var2[5]));
						map.put(Integer.valueOf(var2[6]), Integer.valueOf(var2[7]));
						map.put(Integer.valueOf(var2[8]), Integer.valueOf(var2[9]));
						map.put(Integer.valueOf(var2[10]), Integer.valueOf(var2[11]));
						map.put(Integer.valueOf(var2[12]), Integer.valueOf(var2[13]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length /2 ) + " bunusow"	);

				}
					
					if(var2.length == 16){ 
						
						Map map = new HashMap();
						map.put(Integer.valueOf(var2[0]), Integer.valueOf(var2[1]));
						map.put(Integer.valueOf(var2[2]), Integer.valueOf(var2[3]));
						map.put(Integer.valueOf(var2[4]), Integer.valueOf(var2[5]));
						map.put(Integer.valueOf(var2[6]), Integer.valueOf(var2[7]));
						map.put(Integer.valueOf(var2[8]), Integer.valueOf(var2[9]));
						map.put(Integer.valueOf(var2[10]), Integer.valueOf(var2[11]));
						map.put(Integer.valueOf(var2[12]), Integer.valueOf(var2[13]));
						map.put(Integer.valueOf(var2[14]), Integer.valueOf(var2[15]));
						ItemBonus.addBonusToItemstack(map, item);
						var1.sendChatToPlayer("Pomyslnie dodano " + (var2.length / 2) + " bunusow"	);

				}
					
				
				
				
				
				
			}
		
	}


		

}
