package Oskar13.ItemBonus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import Oskar13.Kolory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemBonus {

public HashMap<Integer, Integer> activeBonus  =  new HashMap<Integer,Integer>();

	







	
	public ItemBonus(ItemStack stack) { 
		
		if(stack != null)  {
		if(stack.isItemBonus()) {

			getBonuses(stack);

		}
		}
		
	}
	

	
 public int getSTR() { 
	 if(activeBonus.containsKey(0)){ 
		 
		 return activeBonus.get(0);
	 }
	 
	 return 0;
 }
	
 public int getHP() { 
	 if(activeBonus.containsKey(1)){ 
		 
		 return activeBonus.get(1);
	 }
	 
	 return 0;
	 
 }
 
 
 public boolean getFREEZE() {
	 
	 if(activeBonus.containsKey(2)){ 
		 
          Random rand = new Random(); 
          
           int szansa = rand.nextInt(100);
		 
           
          if(activeBonus.get(2) <= szansa) { 
        	  
        	  return true;
          } 
		 
	 }
	 
	 return false;
 }
 
 public  int getDEF() {
	 
	 if(activeBonus.containsKey(3)){ 
		 
		 return activeBonus.get(3);
	 }
	 
	 return 0;
 }
 
 public int getDEX() {
	 
	 if(activeBonus.containsKey(4)){ 
		 
		 return activeBonus.get(4);
	 }
	 
	 return 0;
 }
 
 public int getMP() {
	 
	 if(activeBonus.containsKey(5)){ 
		 
		 return activeBonus.get(5);
	 }
	 
	 return 0;
 }
 
	private void getBonuses(ItemStack stack) {
		
		NBTTagList intOfBounus = stack.bonus();
		
	    for (int var7 = 0; var7 <  intOfBounus.tagCount(); ++var7)
        {
          int id = ((NBTTagCompound) intOfBounus.tagAt(var7)).getShort("id");
           int force = ((NBTTagCompound) intOfBounus.tagAt(var7)).getShort("force");
            
            activeBonus.put(id, force);
        }

	}
	

	
	public static void addBonusToItemstack(Map map, ItemStack item) { 

		item.addBonus(map);

	}
	 
   
	

	
	
	public static String  NamesOfBonus(int id, int force) { 
		
		Object color  = null;
		
		if(force < 0) {
			
			color = Kolory.c_czerwony + "- ";
		}else{
			color = Kolory.c_zielony + "+ ";
		}
		switch(id)  {
		case 0: return color  + "Si³a " + force + " pkt"; 
		case 1: return color + "¯ycie " + force + " pkt"; 
		case 2: return color + "Zamro¿enie " + force + " %";
		case 3: return color + "Obrona " + force + " pkt";
		case 4: return color + "Zrêcznoœæ " + force + " pkt"; 
		case 5: return color + "Mana " + force + " pkt";
		default: return "";
		}	
	}
	

}
