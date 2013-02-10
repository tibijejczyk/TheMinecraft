package jantomedes.main.NPC.Tasks;


public enum EnumTaskTypes{
	
	//Akcje jakie b�d� obs�ugiwa� TaskSety, gdy eNPeCeki b�d� ich u�ywa�.
	NOTHING(0),
	ATTACK(1),
	IGNORE(2),
	FOLLOW(3),
	PROTECT(4),
	MINE(5),
	MOVE(6);
	
	public int id;
	
	EnumTaskTypes(int id){
		this.id = id;
	}
	
}
