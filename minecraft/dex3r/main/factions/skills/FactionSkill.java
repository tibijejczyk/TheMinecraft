package dex3r.main.factions.skills;

public class FactionSkill
{
	private final Skill skill;
	public int lvl;
	public int cooldown;
	public int timeLeft;
	
	public FactionSkill(Skill skill)
	{
		this.skill = skill;
		timeLeft = 0;
	}
	
	public int getCooldown()
	{
		return skill.cooldown[lvl - 1] * 60 * 20;
	}
	
	public int getLength()
	{
		return skill.length[lvl - 1];
	}
	
	public int getPower()
	{
		return skill.power[lvl - 1];
	}
	
	public String getName()
	{
		return skill.name;
	}
	
	public void tick()
	{
		if(cooldown > 0)
		{
			cooldown--;
		}
	}
}
