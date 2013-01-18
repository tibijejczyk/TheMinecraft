package Oskar13;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler  implements IPacketHandler{
public static boolean polaczono = false;
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
	
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int id = dat.readInt();
		Object[] extradata = { (EntityPlayer)player };
		switch(FMLCommonHandler.instance().getEffectiveSide()) {
			case SERVER: {
				switch(id) {
				//case 1:

				}
				break;
			}
			case CLIENT: {
				switch(id) {
				//	case 1: 
			
				}
				break;
			}
		default:
			break;
		}


		
	}
	
	public void readClient(int id, ByteArrayDataInput data, Object[] extradata) {}
	public void readServer(int id, ByteArrayDataInput data, Object[] extradata) {}


}
