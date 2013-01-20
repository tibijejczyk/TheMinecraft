package powertools.shared;

import java.util.Date;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PowerEventHookContainer
{
	@ForgeSubscribe
	public void chatEvent(ServerChatEvent event)
	{
		String playerName = event.username;
		byte chatMode = PowerTools.getChatMode(playerName);
		if (chatMode == 1) // Chat to selected players only
		{
			event.setCanceled(true);
			String chatMessage = Cc.LightBlue + "<" + playerName + ">"
								+ Cc.White + " " + event.message;
			String chatList = PowerTools.getChatList(playerName);
			event.player.sendChatToPlayer(chatMessage);

			String oneLetter;
			String tmpName = "";
			for (int i=0; i<chatList.length(); i++)
			{
				oneLetter = chatList.substring(i, i+1);
				if (oneLetter.equals("|"))
				{
					PowerTools.sendChatToPlayerName(tmpName, chatMessage);
					tmpName = "";
				}
				else
				{
					tmpName += oneLetter;
				}
			}
		}
	}
}