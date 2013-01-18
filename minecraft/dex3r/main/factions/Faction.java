package dex3r.main.factions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.ChunkCoordinates;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.server.FMLServerHandler;

public class Faction
{
	public int lvl;
	public int maxMembers;
	public String owner;
	public List<FactionMember> members;
	
	public Faction(String owner)
	{
		this.owner = owner;
		lvl = 1;
		maxMembers = 5;
		members = new ArrayList<FactionMember>();
		members.add(new FactionMember(owner, FactionMemberRank.Owner));
	}
	
	public boolean addMember(String nickname)
	{
		if(PlayerSelector.matchOnePlayer(RConConsoleSource.consoleBuffer, nickname) != null)
		{
			members.add(new FactionMember(nickname, FactionMemberRank.Warrior));
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void editRank(String caller, String target, FactionMemberRank rank)
	{
		FactionMember mCaller = getMember(caller);
		FactionMember mTarget = getMember(target);
		if(mCaller == null || mTarget == null || !mTarget.rank.isDeputy) return;
		if(rank == FactionMemberRank.Owner)
		{
			if(mCaller.rank == FactionMemberRank.Owner)
			{
				FactionMember fm = getMember(FactionMemberRank.Deputy);
				if(fm != null)
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
		else if(rank == FactionMemberRank.Deputy)
		{
			if(mCaller.rank == FactionMemberRank.Owner)
			{
				FactionMember fm = getMember(FactionMemberRank.Deputy);
				if(fm != null)
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
		for(FactionMember member : members)
		{
			if(member.nickname.equals(nickname))
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
		for(FactionMember member : members)
		{
			if(member.rank == rank)
			{
				fm = member;
				break;
			}
		}
		return fm;
	}
}
