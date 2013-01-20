package powertools.chunkprotection;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import powertools.shared.Cc;
import powertools.shared.PowerTools;

public class CommandChunk extends CommandBase
{
	public String getCommandName()
	{
		return "chunk";
	}

	public String getCommandUsage()
	{
		return "/" + getCommandName() + "[claim]|[unclaim]|[add]|[remove]|[clear]|[info]";
	}

    public List getCommandAliases()
    {
        return null;
    }

	public void processCommand(ICommandSender sender, String[] arguments)
	{
		String playerName = sender.getCommandSenderName();
		if (arguments.length > 0)
		{
			if ( arguments[0].equals("claim") &&
				( ChunkProtection.cfgAllPlayersCanClaim || ChunkProtection.isPlayerOp(playerName) ) )
			{
				if (arguments.length > 1)
				{
					if (ChunkProtection.isPlayerOp(playerName))
					{
						PowerTools.addPlayer(arguments[1]);
						chunkClaim(sender, arguments[1]);
					}
				}
				else
				{
					chunkClaim(sender, "");
				}
			}
			if ( arguments[0].equals("unclaim") ) chunkUnClaim(sender);
			if ( arguments[0].equals("unclaimall") ) chunkUnClaimAll(sender, arguments);
			if ( arguments[0].equals("add") ) chunkAddPlayers(sender, arguments);
			if ( arguments[0].equals("remove") ) chunkRemovePlayers(sender, arguments);
			if ( arguments[0].equals("clear") ) chunkClearPlayers(sender);
			if ( arguments[0].equals("info") ) chunkSetBorderInfo(sender, arguments);
			
			if ( arguments[0].equals("playerinfo") ) chunkPlayerInfo(sender, arguments);
			if ( arguments[0].equals("maxchunks") && ChunkProtection.isPlayerOp(playerName) ) chunkSetMaxChunks(sender, arguments);
		}
		else
		{
			EntityPlayerMP player = (EntityPlayerMP) sender;
			int d = player.worldObj.provider.dimensionId;
			int x = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posX);
			int z = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posZ);
			String owner = ChunkProtection.chunkOwner(d, x, z);
			if ( !owner.equals("") )
			{
				sender.sendChatToPlayer(Cc.Yellow + "This chunk is claimed by: " + owner);
				String players = ChunkProtection.chunkGetPlayers(d, x, z);
				if ( !players.equals("") )
					sender.sendChatToPlayer(Cc.Yellow + "Players allowed to build here: " + players);
			}
			else
			{
				sender.sendChatToPlayer(Cc.Yellow + "This chunk is currently not claimed");
			}
			String message = Cc.Yellow + "Use " + Cc.White + "/chunk ";
			if ( ChunkProtection.cfgAllPlayersCanClaim || ChunkProtection.isPlayerOp(playerName) )
				message += "[claim]|";
			message += "[unclaim]";
			sender.sendChatToPlayer(message);
			sender.sendChatToPlayer(Cc.Yellow + "Or " + Cc.White + "/chunk [add]|[remove] [player1] [player2] ..");
			sender.sendChatToPlayer(Cc.Yellow + "Display info at border: " + Cc.White + "/chunk info [on|off]");
			if ( ChunkProtection.isPlayerOp(playerName) )
			{
				sender.sendChatToPlayer(Cc.Yellow + "Display player information: " + Cc.White + "/chunk playerinfo [playername]");
				sender.sendChatToPlayer(Cc.Yellow + "Set maximum chunks to claim for player: " + Cc.White + "/chunk maxchunks [playername] [maxchunks]");
			}
		}
	}

    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
    	return true;
    }

	public void chunkClaim(ICommandSender sender, String claimForPlayer)
	{
		EntityPlayer player = (EntityPlayer) sender;
		int dimension = player.worldObj.provider.dimensionId;
		ChunkCoordinates coordinates = sender.getPlayerCoordinates();
		int x = PowerTools.toChunkCoordinate(coordinates.posX);
		int z = PowerTools.toChunkCoordinate(coordinates.posZ);
		String playerName = sender.getCommandSenderName();
		byte b;
		if (claimForPlayer.equals(""))
		{
			b = ChunkProtection.chunkClaim(dimension, x, z, playerName );
		}
		else
		{
			b = ChunkProtection.chunkClaim(dimension, x, z, claimForPlayer );
		}
		String message = Cc.Yellow + "Dim" + dimension;
		if (dimension == 0) message = Cc.Yellow + "Overworld"; 
		if (dimension == -1) message = Cc.Yellow + "Nether"; 
		message += " x:" + x + " z:" + z;
		if (b == -1)
		{
			ChunkProtection.chunkSetLastVisitNow(dimension, x, z);
			ChunkProtection.writeChunkInfoToFile();
			message += " is successfully claimed.";
			if (claimForPlayer.equals(""))
			{
				ChunkProtection.setChunkOwnerMessage(playerName, playerName);
			}
			else
			{
				ChunkProtection.setChunkOwnerMessage(playerName, claimForPlayer);
			}
		}
		if (b == 1 || b == 3)
		{
			String owner = ChunkProtection.chunkOwner(dimension, x, z);
			message += " was already claimed by " + owner + ".";
		}
		if (b == 2) message += " was already claimed by you.";
		if (b == 4) message += " can't be claimed by you. You may only claim " + ChunkProtection.getMaxChunksPerPlayer(playerName) + " chunks.";
		sender.sendChatToPlayer(message);
	}

	public void chunkUnClaim(ICommandSender sender)
	{
		EntityPlayer player = (EntityPlayer) sender;
		int dimension = player.worldObj.provider.dimensionId;
		ChunkCoordinates coordinates = sender.getPlayerCoordinates();
		int x = PowerTools.toChunkCoordinate(coordinates.posX);
		int z = PowerTools.toChunkCoordinate(coordinates.posZ);

		String name = sender.getCommandSenderName();
		byte b = ChunkProtection.chunkUnClaim(dimension, x, z, name);
		String message = Cc.Yellow + "Dim" + dimension;
		if (dimension == 0) message = Cc.Yellow + "Overworld"; 
		if (dimension == -1) message = Cc.Yellow + "Nether"; 
		message += " x:" + x + " z:" + z;
		if (b == -1)
		{
			ChunkProtection.writeChunkInfoToFile();
			message += " is successfully unclaimed.";
			ChunkProtection.setChunkOwnerMessage(name, "");
		}
		if (b == 1) message += " couln't be unclaimed. It wasn't claimed.";
		if (b == 2)
		{
			String owner = ChunkProtection.chunkOwner(dimension, x, z);
			message += " couln't be unclaimed. It was claimed by " + owner + ".";
		}
		if (b == 3)
		{
			ChunkProtection.writeChunkInfoToFile();
			message += " is " + Cc.LightRed + "successfully unclaimed by op.";
		}
		sender.sendChatToPlayer(message);
	}

	public void chunkUnClaimAll(ICommandSender sender, String[] arguments)
	{
		String playerName = sender.getCommandSenderName();
		if (arguments.length == 2 && ChunkProtection.isPlayerOp(playerName))
			playerName = arguments[1];
		ChunkProtection.chunkUnClaimAll(playerName);
		sender.sendChatToPlayer(Cc.Yellow + "Unclaimed all chunks of " + playerName);
	}	

	public void chunkAddPlayers(ICommandSender sender, String[]arguments)
	{
		EntityPlayerMP player = (EntityPlayerMP) sender;
		int d = player.worldObj.provider.dimensionId;
		int x = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posX);
		int z = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posZ);
		String playerName = sender.getCommandSenderName();
		String owner = ChunkProtection.chunkOwner(d, x, z) ;
		if ( owner.equals(playerName) || ChunkProtection.isPlayerOp(playerName) )
		{
			boolean b;
			String message = "";
			if ( ChunkProtection.isPlayerOp(playerName) ) message += Cc.LightRed + "Op ";
			message += Cc.Yellow + "Added player(s): ";
			for (int i=1; i<arguments.length; i++)
			{
				b = ChunkProtection.chunkAddPlayer(d, x, z, playerName, arguments[i]);
				if (b) message += arguments[i] + ", ";
			}
			sender.sendChatToPlayer(message);
			ChunkProtection.writeChunkInfoToFile();
		}
		else
		{
			sender.sendChatToPlayer(Cc.Yellow + "You can only add players to a chunk you own.");
		}
	}
	public void chunkRemovePlayers(ICommandSender sender, String[]arguments)
	{
		EntityPlayerMP player = (EntityPlayerMP) sender;
		int d = player.worldObj.provider.dimensionId;
		int x = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posX);
		int z = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posZ);
		String playerName = sender.getCommandSenderName();
		String owner = ChunkProtection.chunkOwner(d, x, z);
		if ( owner.equals(playerName) || ChunkProtection.isPlayerOp(playerName) )
		{
			boolean b;
			String message = "";
			if ( ChunkProtection.isPlayerOp(playerName) ) message += Cc.LightRed + "Op ";
			message += Cc.Yellow + "Removed player(s): ";
			for (int i=1; i<arguments.length; i++)
			{
				b = ChunkProtection.chunkRemovePlayer(d, x, z, playerName, arguments[i]);
				if (b) message += arguments[i] + ", ";
			}
			sender.sendChatToPlayer(message);
			ChunkProtection.writeChunkInfoToFile();
		}
		else
		{
			sender.sendChatToPlayer(Cc.Yellow + "You can only remove players from a chunk you own.");
		}
	}
	public void chunkClearPlayers(ICommandSender sender)
	{
		EntityPlayerMP player = (EntityPlayerMP) sender;
		int d = player.worldObj.provider.dimensionId;
		int x = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posX);
		int z = PowerTools.toChunkCoordinate(player.getPlayerCoordinates().posZ);
		String playerName = sender.getCommandSenderName();
		String owner = ChunkProtection.chunkOwner(d, x, z);
		if ( owner.equals(playerName) || ChunkProtection.isPlayerOp(playerName) )
		{
			boolean b = ChunkProtection.chunkClearPlayerList(d, x, z, playerName);
			String message = "";
			if ( ChunkProtection.isPlayerOp(playerName) ) message += Cc.LightRed + "Op ";
			message += Cc.Yellow + "Cleared player list for this chunk.";
			if (b) sender.sendChatToPlayer(message);
		}
		else
		{
			sender.sendChatToPlayer(Cc.Yellow + "You can only remove players from a chunk you own.");
		}
	}
	public void chunkSetBorderInfo(ICommandSender sender, String[]arguments)
	{
		String playerName = sender.getCommandSenderName();
		String message;
		if (arguments.length > 1)
		{
			message = Cc.Yellow + "Border info turned: ";
			if ( arguments[1].equals("on") )
			{
				ChunkProtection.chunkSetBorderInfo(playerName, true);
				message += "on";
			}
			if ( arguments[1].equals("off") )
			{
				ChunkProtection.chunkSetBorderInfo(playerName, false);
				message += "off";
			}
			sender.sendChatToPlayer(message);
		}
		else
		{
			boolean info = ChunkProtection.chunkGetBorderInfo(playerName);
			message = Cc.Yellow + "Border info is currently set: ";
			if (info)
				message += "on";
			else
				message += "off";
			sender.sendChatToPlayer(message);
		}
	}
	
	public void chunkPlayerInfo(ICommandSender sender, String[] arguments)
	{
		String message = "";
		if (arguments.length == 2)
		{
			String playerName = arguments[1];
			int maxChunks = ChunkProtection.getMaxChunksPerPlayer(playerName);
			int chunksOwned = ChunkProtection.getChunksOwned(playerName);
			message = Cc.Yellow + "Info for player '" +  playerName + "', maxChunksAllowed: " + maxChunks
					+ ", numberOfChunksOwned: " + chunksOwned;
		}
		else if (arguments.length == 1)
		{
			String playerName = sender.getCommandSenderName();
			int maxChunks = ChunkProtection.getMaxChunksPerPlayer(playerName);
			int chunksOwned = ChunkProtection.getChunksOwned(playerName);
			message = Cc.Yellow + "Info for player '" +  playerName + "', maxChunksAllowed: " + maxChunks
					+ ", numberOfChunksOwned: " + chunksOwned;
		}
		else
		{
			message = Cc.Yellow + "Use: " + Cc.White + "/chunk playerinfo [playername]";
		}
		sender.sendChatToPlayer(message);
	}

	public void chunkSetMaxChunks(ICommandSender sender, String[] arguments)
	{
		String message = "";
		if (arguments.length == 3)
		{
			String playerName = arguments[1];
			int maxChunks = Integer.valueOf(arguments[2]);
			ChunkProtection.setMaxChunksForPlayer(playerName, maxChunks);
			ChunkProtection.writePlayerChunkInfoToFile();
			message = Cc.Yellow + "Player '" + playerName + "' can now claim " + Cc.White + maxChunks + Cc.Yellow + " chunks.";
		}
		else
		{
			message = Cc.Yellow + "Use: " + Cc.White + "/chunk maxchunks [playername] [maxchunks]";
		}
		sender.sendChatToPlayer(message);
	}
}
