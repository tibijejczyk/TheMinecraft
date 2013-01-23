package dex3r.main;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.proxyServer.proxyServer;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import dex3r.main.factions.Faction;
import dex3r.main.factions.FactionMember;
import dex3r.main.factions.skills.FactionSkill;
import dex3r.main.factions.skills.Skill;

@Mod(modid = "DexTheMc", name = "DeX3r TheMinecraft MOD", version = "0.1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class DexMain
{
	// The instance of your mod that Forge uses.
	@Instance("DexTheMc")
	public static DexMain instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "net.minecraft.client.proxyClient.proxyClient", serverSide = "cpw.mods.fml.common.proxyServer.proxyServer")
	public static proxyServer proxy;

	// ----------
	// Blocks:
	//public final static Block customBlock = new CustomBlock(500, 0, Material.ground, instance).setHardness(0.5F).setStepSound(Block.soundStoneFootstep).setCreativeTab(CreativeTabs.tabBlock);
	// ----------
	
	// ---------
	// Items:
	//public final static Item textureInfo = new Item(5502).setItemName("textureInfo").setCreativeTab(CreativeTabs.tabMisc).setMaxStackSize(1);
	// ---------

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		// Stub Method
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		if(event.getSide() == Side.SERVER)
		{
			Faction.init();
		}
		
		MinecraftForge.EVENT_BUS.register(this);
		
		// ----------
		// Blocks:
//		LanguageRegistry.addName(customBlock , "Custom block");
//		MinecraftForge.setBlockHarvestLevel(customBlock, "pickaxe", 0);
//		GameRegistry.registerBlock(customBlock, "x custom x Block x");
		
		// ----------
		
		// ----------
		// Items:
		
		// ----------
//		LanguageRegistry.addName(antena, "Antena");
//		GameRegistry.addRecipe(new ItemStack(antena), "I", "I", 'I', Item.ingotIron);
		// ----------
		// Rendering:
		
		// ----------
		
		// ----------
		// GUIs:
		
		// ----------
		
		//TEMP:
		proxy.Dexter();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		// Stub Method
	}
	
	public static boolean isPlayerOnline(String nickname)
	{
		String[] playersOnline = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getAllUsernames();
		for(int i = 0; i < playersOnline.length; i++)
		{
			if(playersOnline[i].equals(nickname))
			{
				return true;
			}
		}
		return false;
	}
	
	@ForgeSubscribe
	public void attackTargetEntityWithCurrentItem(AttackEntityEvent event)
	{
		if(event.entityPlayer != null)
		{
			FactionMember member = Faction.allMembers.get(event.entityPlayer.username);
			if(member != null && member.onWar && member.faction.activeSkills > 0)
			{
				FactionSkill skill = member.faction.getSkill(Skill.Strenght);
				if(skill.isActive())
				{
					event.entityPlayer.addStat(StatList.damageDealtStat, member.faction.getSkill(Skill.Strenght).getPower());
				}
			}
			event.target.attackEntityFrom(DamageSource.causePlayerDamage(event.entityPlayer), 50);
		}
	}
	
	@ForgeSubscribe
	public void damageEntity(LivingHurtEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			FactionMember member = Faction.allMembers.get(player.username);
			if(member != null && member.onWar && member.faction.activeSkills > 0)
			{
				FactionSkill skill = member.faction.getSkill(Skill.Resistance);
				if(skill.isActive())
				{
					event.ammount -= (double)skill.getPower() / 100.0D * event.ammount;
				}
			}
		}
		
		event.ammount -= (double)50 / 100.0D * event.ammount;
	}
}

