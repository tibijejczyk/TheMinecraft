package Oskar13.APIs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public abstract class ModChat
{
	/**
	 * Wysy�a wiadomo�� do gracza o podanym nicku.
	 * @param name Nazwa moda
	 * @param message Tre�� wiadomo�ci
	 * @param nick Nick gracza
	 */
	public static void sendModMessage(String name, String message, String nick)
	{
		if (FMLCommonHandler.instance().getSide() == Side.SERVER)
		{
			 sendModMessage(name, message, FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(nick));
		}
		else
		{
			
		}
	}
	
	/**
	 * Wysy�a wiadomo�� wszystkich graczy.
	 * @param name Nazwa moda
	 * @param message Wiadmo��
	 */
	public static void sendModMessageToAll(String name, String message)
	{
		
	}
	
	/**
	 * Wysy�a wiadomo�� do wszystkich OP�w.
	 * @param name Nazwa moda
	 * @param message Wiadmo��
	 */
	public static void sendModMessageToOps(String name, String message)
	{
		
	}
	
	/**
	 * Wysy�a wiadomo�� do podanego gracza.
	 * @param name Nazwa moda
	 * @param message Wiadmo��
	 * @param player Gracz, do kt�rego zostanie wys�ana wiadomo��
	 */
	public static void sendModMessage(String name, String message, EntityPlayerMP player)
	{
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
		
		if (FMLCommonHandler.instance().getSide() == Side.SERVER)
		{
			PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
		}
	}
}
