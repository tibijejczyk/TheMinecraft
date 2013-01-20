package dex3r.main.factions.skills;

public class Skill
{
	public final static Skill Strenght;
	public final static Skill Regeneration;
	public final static Skill Resistance;
	public final static Skill HealthRestore;

	protected String name;
	protected int iconIndex;
	protected int[] cooldown;
	protected int[] length;
	protected int[] power;
	
	public Skill(String name, int iconIndex, int[] cooldown, int[] length, int[] power)
	{
		if (cooldown.length != 7 || length.length != 7 || power.length != 7)
		{
			throw new IllegalArgumentException("Wszystkie skille musz¹ mieæ 7 poziomów.");
		}
		this.name = name;
		this.iconIndex = iconIndex;
	}
	
	public String getName()
	{
		return name;
	}

	static
	{
		Strenght = new Skill("G: Si³a", 0, new int[] { 120, 115, 110, 105, 100, 95, 90 }, new int[] { 3, 4, 5, 5, 6, 7, 8 }, new int[] { 2, 3, 4, 5, 6, 7, 8 });
		Regeneration = new Skill("G: Regeneracja", 0, new int[] { 120, 110, 100, 95, 90, 85, 80 }, new int[] { 3, 4, 5, 5, 7, 7, 8 }, new int[] { 30, 27, 24, 21, 18, 15, 12 });
		Resistance = new Skill("G: Odpornoœæ", 0, new int[] { 120, 110, 100, 95, 90, 85, 80 }, new int[] { 5, 7, 9, 11, 13, 15, 17, }, new int[] { 3, 7, 10, 13, 17, 21, 25 });
		//HealthRestore = new Skill("G: Przywrócenie zdrowia", 0, new int[] {120, 110, 100, 90, 80, 70, 60, }, new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 6, 8, 10, 12, 14, 16, 18 });
		HealthRestore = new Skill("G: Przywrócenie zdrowia", 0, new int[] { 110, 100, 90, 80, 70, 60, }, new int[] { 0, 0, 0, 0, 0, 0, 0 }, new int[] { 6, 8, 10, 12, 14, 16, 18 });
	}
}
