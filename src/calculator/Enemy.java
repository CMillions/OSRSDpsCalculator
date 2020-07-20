package calculator;

import java.util.List;

public class Enemy 
{
	public enum Type
	{
		DEMON,
		DRAGON,
		FIRE,
		GUARDIAN,
		KALPHITE,
		KURASK,
		UNDEAD,
		VORKATH,
		NONE
	}
	
	public String name = "";
	public String location = ""; // unnecessary?
	
	public float expBonus = 0.f;
	
	public int combatLvl = 3;
	public int hitpoints = 10;
	public int atkLvl = 1;
	public int strLvl = 1;
	public int defLvl = 1;
	public int magLvl = 1;
	public int rngLvl = 1;
	
	// AttackStyle enum?
	
	// Aggressive stats
	public int atkSpeed = 0;
	public int stabAtk = 0;
	public int slashAtk = 0;
	public int crushAtk = 0;
	public int atkBonus = 0;
	public int meleeStr = 0;
	public int magAtk = 0;
	public float magStr = 0;
	public int rngAtk = 0;
	public int rngStr = 0;
	
	// Defensive stats
	public int stabDef = 0;
	public int slashDef = 0;
	public int crushDef = 0;
	public int magDef = 0;
	public int rngDef = 0;
	
	// Interval in .csv?
	
	public Type type;
	
	public Enemy(List<String> list)
	{
		name = list.get(0);
		location = list.get(1);
		//expBonus = Float.parseFloat(list.get(2));
		
		combatLvl = Integer.parseInt(list.get(3));
		hitpoints = Integer.parseInt(list.get(4));
		atkLvl = Integer.parseInt(list.get(5));
		strLvl = Integer.parseInt(list.get(6));
		defLvl = Integer.parseInt(list.get(7));
		magLvl = Integer.parseInt(list.get(8));
		rngLvl = Integer.parseInt(list.get(9));
		
		// list.get(10) is main attack type
		atkSpeed = Integer.parseInt(list.get(11));
		stabAtk = Integer.parseInt(list.get(12));
		slashAtk = Integer.parseInt(list.get(13));
		crushAtk = Integer.parseInt(list.get(14));
		atkBonus = Integer.parseInt(list.get(15));
		meleeStr = Integer.parseInt(list.get(16));
		magAtk = Integer.parseInt(list.get(17));
		magStr = Float.parseFloat(list.get(18));
		rngAtk = Integer.parseInt(list.get(19));
		rngStr = Integer.parseInt(list.get(20));
		
		stabDef = Integer.parseInt(list.get(21));
		slashDef = Integer.parseInt(list.get(22));
		crushDef = Integer.parseInt(list.get(23));
		magDef = Integer.parseInt(list.get(24));
		rngDef = Integer.parseInt(list.get(25));

		switch(list.get(26).toLowerCase())
		{
			case "demon":
				type = Type.DEMON;
				break;
				
			case "dragon":
				type = Type.DRAGON;
				break;
				
			case "fire":
				type = Type.FIRE;
				break;
				
			case "guardian":
				type = Type.GUARDIAN;
				break;
				
			case "kalphite":
				type = Type.KALPHITE;
				break;
				
			case "kurask":
				type = Type.KURASK;
				
			case "undead":
				type = Type.UNDEAD;
				break;
				
			case "vorkath":
				type = Type.VORKATH;
				break;
				
			default:
				type = Type.NONE;
				break;
		}		
	} // End Enemy();

	@Override
	public String toString()
	{
		return this.name;
	}
}
