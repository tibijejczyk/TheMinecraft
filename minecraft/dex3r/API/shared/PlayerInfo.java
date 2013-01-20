package dex3r.API.shared;

public class PlayerInfo
{
	public String name;
	public String password;
	public boolean isConnected;
	public boolean isRegistered;
	public boolean isLoggedIn;
	public double loginPosX;
	public double loginPosY;
	public double loginPosZ;

	public static DimChunkCoordinates currentChunkCoordinates;
	public static String lastOwnerMessage;
	public byte chunksOwned;
	public boolean borderInfo;

	public long time1;
	public long time2;

	public byte chatMode; // 0=normal, 1=player, 2=whisper
	public String chatToPlayers;

	public PlayerInfo(String plName)
	{
		this.name = plName;
		this.password = "";
		this.isConnected = false;
		this.isRegistered = false;
		this.isLoggedIn = false;
		this.loginPosX = 0;
		this.loginPosY = 0;
		this.loginPosZ = 0;

		this.currentChunkCoordinates = new DimChunkCoordinates(0, 0, 0);
		this.lastOwnerMessage = "";
		this.chunksOwned = 0;
		this.borderInfo = true;

		this.time1 = PowerTools.date.getTime();
		this.time2 = this.time1;

		this.chatMode = 0;
		this.chatToPlayers = "";
	}
}
