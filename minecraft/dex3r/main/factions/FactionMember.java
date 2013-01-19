package dex3r.main.factions;

public class FactionMember
{
	public String nickname;
	public FactionMemberRank rank;
	public int xpGiven;
	
	public FactionMember(String nickname, FactionMemberRank rank)
	{
		this.nickname = nickname;
		this.rank = rank;
		xpGiven = 0;
	}
}
