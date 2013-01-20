package Oskar13.commands;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
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


		
		
     	
    	ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			dos.writeInt(2);
			
			dos.writeUTF(var1.getCommandSenderName()+":"+var2[0] + var2[1] + var2[2]);
		
	
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "Oskar13";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
		PacketDispatcher.sendPacketToAllPlayers(pkt);
		
	
		
		
		
		
		
		}
	
}