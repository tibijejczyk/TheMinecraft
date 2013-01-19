package Oskar13.TheCharacters;

import java.util.HashMap;

import Oskar13.OskarStart;
import Oskar13.TheCharacters.ModelSystem.ModelGhast;
import Oskar13.TheCharacters.ModelSystem.ModelHuman;
import Oskar13.TheCharacters.ModelSystem.ModelHumanArmor;
import Oskar13.TheCharacters.ModelSystem.PlayerModel;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class Characters
{
	// Arrary of player
	public static final HashMap<String, Characters> PLAYERS = new HashMap<String, Characters>();
	// Arrary of models
	public static final HashMap<String, PlayerModel> MODELS = new HashMap<String, PlayerModel>();

	public static final String skinURL = "null";

	// Models

	static PlayerModel human;
	static PlayerModel ghast;

	public EntityPlayer player;
	public Stats stats;

	public Characters(EntityPlayer player, Stats stats)
	{

		this.player = player;
		this.stats = stats;
	}

	public static void addPlayer(String player, Characters chara)
	{
		if (!PLAYERS.containsKey(player))
		{
			PLAYERS.put(player, chara);
		}
	}

	public static void removePlayer(String player)
	{

		PLAYERS.remove(player);
	}

	public static Characters getPlayer(String nick)
	{

		if (!PLAYERS.containsKey(nick))
		{
			OskarStart.debug("Player isnt added");
			return null;
		}
		return PLAYERS.get(nick);
	}

	public Stats getStats()
	{
		return stats;

	}

	public static  PlayerModel OtherPlayerModel(EntityPlayer player)
	{
		if(player instanceof EntityPlayerSP)
		{
			return ghast;
		}
		
		String username = player.username;
		
		if(PLAYERS.containsKey(username))
		{
			String pmName = PLAYERS.get(username).getStats().modelName;
		
				return getModel(pmName, 1);

		}else {
	   OskarStart.debug("This player isn't in HashMap");
		return ghast;
		}
	}

	public static boolean isModelInstalled(String name)
	{

		return MODELS.containsKey(name);
	}

	public static void addModel(PlayerModel pm)
	{
		String pmName = pm.name;

		if (isModelInstalled(pmName))
		{

			OskarStart.debug("Model " + pmName + " is already installed");
			return;
		}

		pm.base.init(0F);
		MODELS.put(pmName, pm);
		return;
	}

	public static PlayerModel getModel(String nick)
	{
		Characters c = getPlayer(nick);
		if(c == null) return (PlayerModel)MODELS.get("Ghast");
		
		
		
		return (PlayerModel) MODELS.get(c.getStats().modelName);
	}
	
	public static PlayerModel getModel(String name, int NotUse){

		return (PlayerModel) MODELS.get(name);
	}

	static
	{

		human = new PlayerModel("Human", new ModelHuman(), new ModelHumanArmor()).setRenderScale(3F);
		ghast = new PlayerModel("Ghast", new ModelGhast(), null).setEyeHeight(0.12F).setRenderScale(0.9375F);

		addModel(human);
		addModel(ghast);

		OskarStart.debug("Added models");

	}

}
