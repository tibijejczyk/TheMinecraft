package dex3r.main.factions;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.ChunkCoordinates;
 
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.server.FMLServerHandler;
import dex3r.main.DexMain;
 
public class Faction
{
        public int lvl;
        public int maxMembers;
        public String owner;
        public List<FactionMember> members;
        public static final HashMap<Integer, FactionLevel> stats;
        private int xp;
       
        public Faction(String owner)
        {
                this.owner = owner;
                lvl = 1;
                maxMembers = 5;
                members = new ArrayList<FactionMember>();
                members.add(new FactionMember(owner, FactionMemberRank.Owner));
        }
       
        public boolean addMember(String caller, String target)
        {
                if(!DexMain.isPlayerOnline(caller) || !DexMain.isPlayerOnline(target) || getMember(caller) == null || getMember(target) != null || !getMember(caller).rank.canInvite)
                {
                        return false;
                }
                members.add(new FactionMember(target, FactionMemberRank.Warrior));
                return true;
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
       
        public void addXp(int xp)
        {
                xp += xp;
                if(stats.get(lvl).xpToLvl <= xp)
                {
                        lvl++;
                        int t = xp;
                        xp = 0;
                        addXp(t);
                }
        }
       
        public int getXp()
        {
                return xp;
        }
       
       
        static
        {
                stats =  new HashMap<Integer, FactionLevel>();
                FactionLevel lvl;
                int xpToLvl;
                double maxChunks = 0;
                for(int i = 1; i <= 30; i++)
                {
                        if(i == 1)
                        {
                                xpToLvl = 0;
                        }
                        else if(i == 2)
                        {
                                xpToLvl = 825;
                        }
                        else
                        {
                                xpToLvl = 825 + ((i*i / 25) * 825);
                        }
                        if(i == 1)
                        {
                                maxChunks = 4;
                        }
                        else
                        {
                                maxChunks = maxChunks + 1.22D;
                        }
                        lvl = new FactionLevel(i + 4, xpToLvl, (int)Math.ceil(maxChunks));
                }
        }
}