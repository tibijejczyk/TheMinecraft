package dex3r.API.chunkprotection;

import dex3r.API.shared.DimChunkCoordinates;

public class PlayerChunkInfo
{
	String playerName;
	String lastOwnerMessage;
	DimChunkCoordinates currentChunkCoordinates;
	
	int maxChunks;
	
	public PlayerChunkInfo(String playerName)
	{
		this.playerName = playerName;
		this.lastOwnerMessage = "";
		this.currentChunkCoordinates = new DimChunkCoordinates();

		this.maxChunks = ChunkProtection.cfgMaxChunksPerPlayer;
	}
}
