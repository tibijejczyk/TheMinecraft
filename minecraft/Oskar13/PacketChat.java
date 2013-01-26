package Oskar13;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketChat extends PacketHandler{


	
	public void readClient(int id, ByteArrayDataInput dat, Object[] extradata) {
		
		GuiIngame.addNote(dat.readUTF(), dat.readUTF(),0);
		
	}
	
	
	
	
	public void readServer(int id, ByteArrayDataInput dat, Object[] extradata) {
		

	}
	
	
	public static void sendPacket(String name, String message, String nick)	 {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			dos.writeInt(2);
			dos.writeUTF(name);
			dos.writeUTF(message);
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "Oskar13";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
	
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager()
					.getPlayerForUsername(nick);
			PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
		}
		
		
		
	}
}
