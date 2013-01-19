package Oskar13.TheCharacters.ModelSystem;


public class PlayerModel
{
    public String name;
    public ModelPlayerBase base, armor;
    public float
		width = 0.6F,
		height = 1.8F,
		renderScale = 1F,
		shadowSize = 0.5F,
		yOffset = 1.62F,
		eyeHeight = 0F,
		cameraDistance = 4F
	;

	//NAME - The name must be unique, otherwise it will be overridden - it is the key for the player model hash map
	//BASE - The base model, musn't be null, otherwise it will crash
	//ARMOR - The armor, no need to be set, the model file must implement IModelArmor
    public PlayerModel(String name, ModelPlayerBase base, ModelPlayerBase armor)
    {
		if(name == null)
		{
			System.out.println("An unnamed player model has been created!");
			name = "";
		}
		if(base == null)
			System.out.println(" - \"" + name + "\" has no base model!");
		if(!(armor == null || armor instanceof IModelArmor))
			
			System.out.println("- \"" + name + "\" does not implement IModelArmor!");
			
        this.name = name;
        this.base = base;
		this.armor = armor;
    }
	
	
	public PlayerModel setModelSize(float width, float height)
	{
		this.width = width;
		this.height = height;
		return this;
	}
	
	
	public PlayerModel setYOffset(float yOffset)
	{
		this.yOffset = yOffset;
		return this;
	}

	public PlayerModel setEyeHeight(float eyeHeight)
	{
		this.eyeHeight = eyeHeight;
		return this;
	}
	

	public PlayerModel setCameraDistance(float cameraDistance)
	{
		this.cameraDistance = cameraDistance;
		return this;
	}

	public PlayerModel setRenderScale(float renderScale)
	{
		this.renderScale = renderScale;
		return this;
	}

	public PlayerModel setShadowSize(float shadowSize)
	{
		this.shadowSize = shadowSize;
		return this;
	}
}