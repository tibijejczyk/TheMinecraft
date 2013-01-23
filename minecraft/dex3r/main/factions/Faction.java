package dex3r.main.factions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import dex3r.main.DexMain;
import dex3r.main.factions.skills.FactionSkill;
import dex3r.main.factions.skills.Skill;

public class Faction implements ITickHandler
{
	public static List<Faction> factions;
	public static HashMap<String, FactionMember> allMembers;
	public static final HashMap<Integer, FactionLevel> STATS;

	public int lvl;
	public int maxMembers;
	public String owner;
	public List<FactionMember> members;
	
	public List<Faction> enemies;
	public List<Faction> allies;
	private int xp;
	public String name;
	public FactionSkill[] skills;
	public int activeSkills;

	public Faction(String owner, String name)
	{
		this.owner = owner;
		lvl = 1;
		maxMembers = 5;
		this.name = name;
		members = new ArrayList<FactionMember>();
		members.add(new FactionMember(this, owner, FactionMemberRank.Owner));
		enemies = new ArrayList<Faction>();
		allies = new ArrayList<Faction>();
		skills = new FactionSkill[4];
		activeSkills = 0;
	}

	public static void init()
	{
		
	}

	public static Faction getFaction(String name)
	{
		for (Faction f : factions)
		{
			if (f.name.equals(name))
			{
				return f;
			}
		}
		return null;
	}
	
	public void tick()
	{
		if(activeSkills > 0)
		{
			for(int i = 0; i < skills.length; i++)
			{
				skills[i].tick();
				if(skills[i].timeLeft > 0)
				{
					skills[i].timeLeft--;
					if(skills[i].timeLeft == 0)
					{
						activeSkills--;
					}
				}
			}
			FactionSkill rk = getSkill(Skill.Regeneration);
			if(rk.isActive())
			{
				if(rk.ticks == 0)
				{
					for (FactionMember member : members)
					{
						if (member.onWar && member.getPlayer() != null)
						{
							member.getPlayer().heal(1);
						}
					}
				}
				rk.ticks = rk.getPower();
			}
		}
	}
	
	public boolean addAlly(Faction newF)
	{
		if(allies.contains(newF))
		{
			return false;
		}
		else if(enemies.contains(newF))
		{
			enemies.remove(newF);
		}
		allies.add(newF);
		return true;
	}
	
	public boolean addEnemy(Faction newF)
	{
		if(enemies.contains(newF))
		{
			return false;
		}
		else if(allies.contains(newF))
		{
			allies.remove(newF);
		}
		enemies.add(newF);
		return true;
	}
	
	public boolean addNeutral(Faction newF)
	{
		if(!enemies.contains(newF) && !allies.contains(newF))
		{
			return false;
		}
		enemies.remove(newF);
		allies.remove(newF);
		return true;
	}

	public FactionSkill getSkill(Skill skill)
	{
		for (int i = 0; i < skills.length; i++)
		{
			if (skills[i].getName().equals(skill.getName()))
			{
				return skills[i];
			}
		}
		throw new IllegalArgumentException("Nie ma takiego skilla!");
	}

	public void castSkill(String caller, Skill skill)
	{
		if (!getMember(caller).rank.canCastSkills)
		{
			return;
		}
		FactionSkill s = getSkill(skill);
		if (s.cooldown > 0)
		{
			return;
		}
		for (FactionMember member : members)
		{
			if (member.onWar)
			{
				if (skill == Skill.HealthRestore)
				{
					member.getPlayer().heal(s.getPower());
				}
			}
		}
		activeSkills++;
		s.cooldown = s.getCooldown();
	}

	public boolean addMember(String caller, String target)
	{
		if (!DexMain.isPlayerOnline(caller) || !DexMain.isPlayerOnline(target) || getMember(caller) == null || getMember(target) != null || !getMember(caller).rank.canInvite)
		{
			return false;
		}
		FactionMember n = new FactionMember(this, target, FactionMemberRank.Warrior);
		members.add(n);
		allMembers.put(target, n);
		return true;
	}
	
	public boolean removeMember(String caller, String target)
	{
		FactionMember mTarget = getMember(target);
		FactionMember mCaller = getMember(caller);
		if(!DexMain.isPlayerOnline(caller) || mCaller == null || mCaller.rank.isOwner || mTarget == null || (!getMember(caller).rank.isDeputy || target == caller) || mTarget.faction != this)
		{
			return false;
		}
		members.remove(mTarget);
		allMembers.remove(target);
		return true;
	}

	public void editRank(String caller, String target, FactionMemberRank rank)
	{
		FactionMember mCaller = getMember(caller);
		FactionMember mTarget = getMember(target);
		if (mCaller == null || mTarget == null || !mTarget.rank.isDeputy)
			return;
		if (rank == FactionMemberRank.Owner)
		{
			if (mCaller.rank == FactionMemberRank.Owner)
			{
				FactionMember fm = getMember(FactionMemberRank.Deputy);
				if (fm != null)
				{
					fm.rank = FactionMemberRank.Diplomat;
				}
				mCaller.rank = FactionMemberRank.Deputy;
				mTarget.rank = FactionMemberRank.Owner;
			}
			else
			{
				return;
			}
		}
		else if (rank == FactionMemberRank.Deputy)
		{
			if (mCaller.rank == FactionMemberRank.Owner)
			{
				FactionMember fm = getMember(FactionMemberRank.Deputy);
				if (fm != null)
				{
					fm.rank = FactionMemberRank.Diplomat;
				}
				mTarget.rank = FactionMemberRank.Deputy;
			}
			else
			{
				return;
			}
		}
		else
		{
			mTarget.rank = rank;
		}
	}

	private FactionMember getMember(String nickname)
	{
		FactionMember fm = null;
		for (FactionMember member : members)
		{
			if (member.nickname.equals(nickname))
			{
				fm = member;
				break;
			}
		}
		return fm;
	}

	private FactionMember getMember(FactionMemberRank rank)
	{
		FactionMember fm = null;
		for (FactionMember member : members)
		{
			if (member.rank == rank)
			{
				fm = member;
				break;
			}
		}
		return fm;
	}

	public int addXp(int xp)
	{
		if(lvl == 30)
		{
			return xp;
		}
		xp += xp;
		if (STATS.get(lvl).xpToLvl <= xp)
		{
			lvl++;
			int t = xp;
			xp = 0;
			addXp(t);
		}
		return 0;
	}

	public int getXp()
	{
		return xp;
	}

	public String getState(String factionName)
	{
		if (enemies.contains(factionName))
		{
			return "enemy";
		}
		else if (allies.contains(factionName))
		{
			return "ally";
		}
		return "neutral";
	}
	

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		tick();
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		EnumSet<TickType> tickTypes = EnumSet.of(TickType.SERVER);
		return tickTypes;
	}

	@Override
	public String getLabel()
	{
		return "FactionsTick";
	}

	static
	{
		allMembers = new HashMap<String, FactionMember>();
		STATS = new HashMap<Integer, FactionLevel>();
		FactionLevel lvl;
		int xpToLvl;
		double maxChunks = 0;
		for (int i = 1; i <= 30; i++)
		{
			if (i == 1)
			{
				xpToLvl = 0;
			}
			else if (i == 2)
			{
				xpToLvl = 825;
			}
			else
			{
				xpToLvl = 825 + ((i * i / 25) * 825);
			}
			if (i == 1)
			{
				maxChunks = 4;
			}
			else
			{
				maxChunks = maxChunks + 1.22D;
			}
			lvl = new FactionLevel(i + 4, xpToLvl, (int) Math.ceil(maxChunks));
		}
	}

}