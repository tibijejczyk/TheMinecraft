package jantomedes.main.NPC.Tasks;


public enum EnumTaskTypes{
	
	//Akcje jakie bêd¹ obs³ugiwaæ TaskSety, gdy eNPeCeki bêd¹ ich u¿ywaæ.
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
