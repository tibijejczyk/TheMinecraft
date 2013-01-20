package Oskar13.commands;


import Oskar13.APIs.ModChat;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandMessage extends CommandBase{

	@Override
	public String getCommandName() {
		return "message";
	}
@Override
	  public int getRequiredPermissionLevel()
	    {
	        return 3;
	    }
	@Override
	public void processCommand(ICommandSender var1, String[] var2) {

		String o = null;
		for(int i = 0; i > var2.length; i++) {
			
			o += var2[i];
		}

		ModChat.addNote(var1.getCommandSenderName(), o, 0);
var1.sendChatToPlayer("Test");
		}
	
}