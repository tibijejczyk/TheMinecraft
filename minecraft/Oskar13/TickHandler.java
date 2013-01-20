package Oskar13;

import java.util.EnumSet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.Side;

public class TickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {/*Not Used*/}

	/**
	 * Does the onTickInGame and onTickInGui stuff.
	 */
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		OskarStart.instance.onTickInGame();
	}

	@Override
	public EnumSet<TickType> ticks() { 
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			return EnumSet.of(TickType.PLAYER);
		} else if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return EnumSet.of(TickType.CLIENT);
		} else if(FMLCommonHandler.instance().getSide() == Side.SERVER) return EnumSet.of(TickType.WORLD);
		return null;
	}

	@Override
	public String getLabel() { return null; }
}