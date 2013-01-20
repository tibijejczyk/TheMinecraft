package dex3r.API.chunkprotection;

import java.util.Date;

import dex3r.API.shared.PowerTools;

public class ChunkInfo
{
	int dimension, x, z;
	String owner, players;
	long ownerLastVisitDate;
	
	public ChunkInfo(int dim, int posX, int posZ, String owner)
	{
		this.dimension = dim;
		this.x = posX;
		this.z = posZ;
		this.owner = owner;
		this.players = "";
		this.ownerLastVisitDate = PowerTools.date.getTime();
	}

	public void addPlayer(String player)
	{
		this.players += player + ",";
	}

	public byte compare(int dim, int posX, int posZ, String player)
	{
		byte c = 0;	// c=0, chunk is not claimed
					// c=1, chunk is claimed but not owned
					// c=2, chunk is owned by player
					// c=3, chunk claimed, but player is allowed
		if ( this.dimension==dim && this.x==posX && this.z==posZ && !this.owner.equals("") )
		{
			c = 1;
			if ( this.owner.equals(player) ) c = 2;
			if ( PowerTools.stringlistContains(this.players, player) ) c = 3;
		}
		return c;
	}

	public int getDaysSinceLastVisit()
	{
		PowerTools.date = new Date();
		long now = PowerTools.date.getTime();
		long compare = now - this.ownerLastVisitDate;
		int devider = 1000 * 60 * 60 * 24;
		int days = (int) compare / devider;
		return days;
	}

	public void setLastVisitNow()
	{
		PowerTools.date = new Date();
		this.ownerLastVisitDate = PowerTools.date.getTime();
	}
}
