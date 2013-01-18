package dex3r.main.factions;

public enum FactionMemberRank
{
	Warrior("Wojownik", null),
	Recruiter("Rekruter", Warrior),
	Diplomat("Dyplomata", Recruiter),
	Magician("Magik", Diplomat),
	Deputy("Zastepca", Magician),
	Owner("Wlasciciel", Deputy);
	
	private final String name;
	public boolean canInvite;
	public boolean canDeclareWar;
	public boolean canCastSkills;
	public boolean isDeputy;
	public boolean isOwner;
	private final FactionMemberRank rankBelow;
	
	private FactionMemberRank(String name, FactionMemberRank rankBelow)
	{
		this.name = name;
		canInvite = false;
		canDeclareWar = false;
		canCastSkills = false;
		isDeputy = false;
		isOwner = false;
		this.rankBelow = rankBelow;
		
		FactionMemberRank f = rankBelow;
		while(f.rankBelow != null)
		{
			this.canInvite = f.canInvite;
			this.canDeclareWar = f.canDeclareWar;
			this.canCastSkills = f.canCastSkills;
			this.isDeputy = f.isDeputy;
			
			f = f.rankBelow;
		}
	}
	
	static
	{
		Recruiter.canInvite = true;
		Diplomat.canDeclareWar = true;
		Magician.canCastSkills = true;
		Deputy.isDeputy = true;
		Owner.isOwner = true;
	}
}
