package dex3r.API.shared;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import dex3r.API.Colors;

public class CommandChat extends CommandBase
{
	public String getCommandName()
	{
		return "chat";
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
		String playerName = sender.getCommandSenderName();
		byte chatMode = PowerTools.getChatMode(playerName);
		if (arguments.length > 0) // handle arguments: change chat mode or edit
									// list
		{
			if (arguments[0].equals("normal"))
			{
				PowerTools.setChatMode(playerName, (byte) 0);
				sender.sendChatToPlayer(Colors.Yellow + "Chat mode changed to 'normal'");
			}
			if (arguments[0].equals("player"))
			{
				PowerTools.setChatMode(playerName, (byte) 1);
				sender.sendChatToPlayer(Colors.Yellow + "Chat mode changed to 'player'");
				for (int i = 1; i < arguments.length; i++)
				{
					PowerTools.addNameToChatList(playerName, arguments[i]);
				}
			}
			if (arguments[0].equals("whisper"))
			{
				PowerTools.setChatMode(playerName, (byte) 2);
				sender.sendChatToPlayer(Colors.Yellow + "Chat mode changed to 'whisper'");
			}
			if (arguments[0].equals("clear") && chatMode == 1)
			{
				PowerTools.clearChatList(playerName);
				sender.sendChatToPlayer(Colors.Yellow + "Cleared player list for chat");
			}
			if (arguments[0].equals("remove") && chatMode == 1)
			{
				if (arguments.length > 1)
				{
					for (int i = 1; i < arguments.length; i++)
					{
						PowerTools.removePlayerFromChatList(playerName, arguments[i]);
					}
					sender.sendChatToPlayer(Colors.Yellow + "Players removed from chat list");
				} else
				{
					sender.sendChatToPlayer(Colors.Yellow + "Use " + Colors.White + "/chat remove [player1] [player2] ..");
				}
			}
		} else
		// display current chat mode
		{
			String chatMessage = Colors.Yellow + "Current chat mode: ";
			if (chatMode == 0)
				chatMessage += "normal, default chat";
			if (chatMode == 1)
			{
				chatMessage += "player, to selected players only\n" + Colors.LightBlue + PowerTools.getChatList(playerName);
			}
			if (chatMode == 2)
				chatMessage += "whisper, only to players within range";
			sender.sendChatToPlayer(chatMessage);
			sender.sendChatToPlayer(Colors.Yellow + "Use: " + Colors.White + "/chat normal");
			sender.sendChatToPlayer(Colors.Yellow + "or " + Colors.White + "/chat player [player1] [player2] ..");
			if (chatMode == 1)
			{
				sender.sendChatToPlayer(Colors.Yellow + "Clear all players from list: " + Colors.White + "/chat clear");
				sender.sendChatToPlayer(Colors.Yellow + "Remove player from list: " + Colors.White
						+ "/chat remove [player1] [player2] ..");
			}
		}
	}

	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}
}