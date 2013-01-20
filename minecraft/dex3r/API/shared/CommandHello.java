package powertools.shared;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import powertools.chunkprotection.ChunkProtection;

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
		sender.sendChatToPlayer(Cc.LightRed + "Hello World!");
	}

    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
    	return true;
    }
}
