package Oskar13.APIs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class modChat {

    //Name to nazwa moda który wysy³a wiadomoœæ
	//Message wiadomoœæ moda
	//Nick potrzebny do wys³ania pakiety do konkretnego gracza **(jest go ³atwiej uzyskaæ)**
	
	public static void sendPacket(String name, String message, String nick) {

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
				EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(nick);
				PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
			}
			
			
			
		}
	}

