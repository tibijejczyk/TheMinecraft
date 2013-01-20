package dex3r.main.factions;

import java.util.List;

public class FactionWar
{
	public static List<FactionWar> wars;
	
	public final Faction aggressor;
	public final Faction defender;
	
	// Czy defender zaakceptowa³ wojnê
	public boolean accept;
	
	public int timeLeft;
	
	public FactionWar(Faction aggressor, Faction defender)
	{
		this.aggressor = aggressor;
		this.defender = defender;
		accept = false;
		timeLeft = 6000; // 5 minut (5 * 60s * 20tick)
		wars.add(this);
	}
	
	public void tick()
	{
		if(timeLeft > 0)
		{
			timeLeft--;
			if(timeLeft == 0)
			{
				startWar();
			}
		}
	}
	
	public void startWar()
	{
		
	}
}
