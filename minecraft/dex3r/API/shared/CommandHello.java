package dex3r.API.shared;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import dex3r.API.Colors;

public class CommandHello extends CommandBase
{
	public String getCommandName()
	{
		return "hello";
	}

	public String getCommandUsage()
	{
		return "/" + getCommandName() + " <arguments>";
	}

	public List getCommandAliases()
	{
		return null;
	}

	public void processCommand(ICommandSender sender, String[] arguments)
	{
		sender.sendChatToPlayer(Colors.LightRed + "Hello World!");
	}

	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}
}
