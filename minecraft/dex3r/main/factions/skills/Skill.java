package dex3r.main.factions.skills;

public class Skill
{
	public final static Skill Strenght;
	
	protected String name;
	protected int iconIndex;
	protected int[] cooldown;
	protected int[] length;
	protected int[] power;
	
	public Skill(String name, int iconIndex, int[] cooldown, int[] length, int[] power)
	{
		this.name = name;
		this.iconIndex = iconIndex;
		this.name = "hahahaha";
	}
	
	static
	{
		Strenght = new Skill("G: Si³a", 0, new int[] {666, 115, 110, 105, 100, 95, 90}, new int[] {3, 4, 5, 5, 6, 7, 8}, new int[] {2, 3, 4, 5, 6, 7, 8});
	}
}
