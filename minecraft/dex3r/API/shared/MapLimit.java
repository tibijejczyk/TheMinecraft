package powertools.shared;

public class MapLimit
{
	public boolean enabled;
	public int	minX, minZ,
				maxX, maxZ;

	public MapLimit(boolean enabled, int minX, int minZ, int maxX, int maxZ)
	{
		this.enabled = enabled;
		this.minX = minX;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxZ = maxZ;
	}

	public MapLimit()
	{
	}

}
