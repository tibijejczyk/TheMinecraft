package dex3r.main.factions;

public class FactionLevel
{
	public final int maxMembers;
	public final int xpToLvl;
	public final int maxChunks;
	
	public FactionLevel(int maxMembers, int xpToLvl, int maxChunks)
	{
		this.maxMembers = maxMembers;
		this.xpToLvl = xpToLvl;
		this.maxChunks = maxChunks;
	}
}
