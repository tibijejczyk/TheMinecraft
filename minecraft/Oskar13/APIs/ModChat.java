package Oskar13.APIs;

import net.minecraft.client.gui.GuiIngame;

public class ModChat {

	
	
	public ModChat(String name, String message) {
		
		
		GuiIngame.addNote(name, message, 0);
	}

}
