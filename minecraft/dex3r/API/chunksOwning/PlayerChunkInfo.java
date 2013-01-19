package dex3r.API.chunksOwning;

import dex3r.API.util.DimChunkCoordinates;


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