package dex3r.API.util;

import java.util.Date;

import dex3r.API.Tools;

public class PlayerInfo
{
  public String name;
  public String password;
  public boolean isConnected;
  public boolean isRegistered;
  public boolean isLoggedIn;
  public double loginPosX;
  public double loginPosY;
  public double loginPosZ;
  public static DimChunkCoordinates currentChunkCoordinates;
  public static String lastOwnerMessage;
  public byte chunksOwned;
  public boolean borderInfo;
  public long time1;
  public long time2;
  public byte chatMode;
  public String chatToPlayers;

  public PlayerInfo(String plName)
  {
    this.name = plName;
    this.password = "";
    this.isConnected = false;
    this.isRegistered = false;
    this.isLoggedIn = false;
    this.loginPosX = 0.0D;
    this.loginPosY = 0.0D;
    this.loginPosZ = 0.0D;

    currentChunkCoordinates = new DimChunkCoordinates(0, 0, 0);
    lastOwnerMessage = "";
    this.chunksOwned = 0;
    this.borderInfo = true;

    this.time1 = Tools.date.getTime();
    this.time2 = this.time1;

    this.chatMode = 0;
    this.chatToPlayers = "";
  }
}
