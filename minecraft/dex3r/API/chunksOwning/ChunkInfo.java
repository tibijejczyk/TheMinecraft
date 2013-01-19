package dex3r.API.chunksOwning;

import java.util.Date;

import dex3r.API.Tools;

public class ChunkInfo
{
  int dimension;
  int x;
  int z;
  String owner;
  String players;
  long ownerLastVisitDate;

  public ChunkInfo(int dim, int posX, int posZ, String owner)
  {
    this.dimension = dim;
    this.x = posX;
    this.z = posZ;
    this.owner = owner;
    this.players = "";
    this.ownerLastVisitDate = Tools.date.getTime();
  }

  public void addPlayer(String player)
  {
    this.players = (this.players + player + ",");
  }

  public byte compare(int dim, int posX, int posZ, String player)
  {
    byte c = 0;

    if ((this.dimension == dim) && (this.x == posX) && (this.z == posZ) && (!this.owner.equals("")))
    {
      c = 1;
      if (this.owner.equals(player)) c = 2;
      if (Tools.stringlistContains(this.players, player)) c = 3;
    }
    return c;
  }

  public int getDaysSinceLastVisit()
  {
    Tools.date = new Date();
    long now = Tools.date.getTime();
    long compare = now - this.ownerLastVisitDate;
    int devider = 86400000;
    int days = (int)compare / devider;
    return days;
  }

  public void setLastVisitNow()
  {
    Tools.date = new Date();
    this.ownerLastVisitDate = Tools.date.getTime();
  }
}
