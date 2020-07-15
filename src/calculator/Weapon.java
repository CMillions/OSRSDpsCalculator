package calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Weapon extends Equippable 
{
	public enum AttackStyle
	{
		ACCURATE,
		AGGRESSIVE,
		DEFENSIVE,
		CONTROLLED,
		RAPID,
		LONG,
		BLAZE
	}

	public enum DamageType
	{
		STAB,
		SLASH,
		CRUSH,
		MAGIC,
		RANGE,
		NONE
	}
	
	// Would normally put specType first, but .csv from Google Sheets puts spec defense roll after
	public float specAcc;
	public float specDamage1;
	public float specDamage2;
	public DamageType specType;
	
	public int spellMaxHit;
	public int atkSpeed;
	public int weaponClass;
	
	public int reqMiningLvl;

	// Bruh, might be bad idk lmao
	private static final Map<String, MyPair<AttackStyle, DamageType>> COMBAT_OPTIONS = 
			new HashMap<String, MyPair<AttackStyle, DamageType>>();
	static 
	{
		COMBAT_OPTIONS.put("ACC_SLASH", new MyPair<>(AttackStyle.ACCURATE, DamageType.SLASH));
		COMBAT_OPTIONS.put("AGG_SLASH", new MyPair<>(AttackStyle.AGGRESSIVE, DamageType.SLASH));
		COMBAT_OPTIONS.put("DEF_SLASH", new MyPair<>(AttackStyle.DEFENSIVE, DamageType.SLASH));
		COMBAT_OPTIONS.put("CTRL_SLASH", new MyPair<>(AttackStyle.CONTROLLED, DamageType.SLASH));
		
		COMBAT_OPTIONS.put("ACC_STAB", new MyPair<>(AttackStyle.ACCURATE, DamageType.STAB));
		COMBAT_OPTIONS.put("AGG_STAB", new MyPair<>(AttackStyle.AGGRESSIVE, DamageType.STAB));
		COMBAT_OPTIONS.put("DEF_STAB", new MyPair<>(AttackStyle.DEFENSIVE, DamageType.STAB));
		COMBAT_OPTIONS.put("CTRL_STAB", new MyPair<>(AttackStyle.CONTROLLED, DamageType.STAB));
		
		COMBAT_OPTIONS.put("ACC_CRUSH", new MyPair<>(AttackStyle.ACCURATE, DamageType.CRUSH));
		COMBAT_OPTIONS.put("AGG_CRUSH", new MyPair<>(AttackStyle.AGGRESSIVE, DamageType.CRUSH));
		COMBAT_OPTIONS.put("DEF_CRUSH", new MyPair<>(AttackStyle.DEFENSIVE, DamageType.CRUSH));
		COMBAT_OPTIONS.put("CTRL_CRUSH", new MyPair<>(AttackStyle.CONTROLLED, DamageType.CRUSH));
		
		COMBAT_OPTIONS.put("ACC_RANGE", new MyPair<>(AttackStyle.ACCURATE, DamageType.RANGE));
		COMBAT_OPTIONS.put("RAPID_RANGE", new MyPair<>(AttackStyle.RAPID, DamageType.RANGE));
		COMBAT_OPTIONS.put("LONG_RANGE", new MyPair<>(AttackStyle.LONG, DamageType.RANGE));
		
		COMBAT_OPTIONS.put("ACC_MAGIC", new MyPair<>(AttackStyle.ACCURATE, DamageType.MAGIC));
		COMBAT_OPTIONS.put("LONG_MAGIC", new MyPair<>(AttackStyle.LONG, DamageType.MAGIC));
		COMBAT_OPTIONS.put("BLAZE_MAGIC", new MyPair<>(AttackStyle.BLAZE, DamageType.MAGIC));
	}
	
	public List<MyPair<AttackStyle, DamageType>> usableCombatOptions;
	public int numCombatOptions;
	public boolean isTwoHand;
	
	public Weapon()
	{
		name = "";
	}
	
	public Weapon(List<String> list)
	{	
		super(list);
		
		ArrayList<String> data = new ArrayList<>();
		for(String s : list)
			data.add(s);
		
		while(data.size() < 23)
			data.add("0");
		
		specAcc = Float.parseFloat(data.get(15));
		specDamage1 = Float.parseFloat(data.get(16));
		specDamage2 = Float.parseFloat(data.get(17));
		
		String specDmgType = data.get(18);
		
		if(specDmgType.equalsIgnoreCase("stab"))
		{
			specType = DamageType.STAB;
		}
		else if(specDmgType.equalsIgnoreCase("slash"))
		{
			specType = DamageType.SLASH;
		}
		else if(specDmgType.equalsIgnoreCase("crush"))
		{
			specType = DamageType.CRUSH;
		}
		else if(specDmgType.equalsIgnoreCase("magic"))
		{
			specType = DamageType.MAGIC;
		}
		else
		{
			specType = DamageType.NONE;
		}
		
		spellMaxHit = Integer.parseInt(data.get(19));
		atkSpeed = Integer.parseInt(data.get(20));
		weaponClass = Integer.parseInt(data.get(21));
		
		usableCombatOptions = new ArrayList<MyPair<AttackStyle, DamageType>>();
		
		// data.get(22) is required mining level
		if(data.size() < 24)
		{
			isTwoHand = false;
		}
		else
		{
			if(data.get(23).equalsIgnoreCase("true"))
				isTwoHand = true;
		}

		
		
		switch(weaponClass)
		{
			// NONE
			case 3:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_CRUSH"));
				break;
				
			// DAGGER
			case 4:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_STAB"));
				break;
				
			// SWORD
			case 5:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_SLASH"));
				break;
				
			// Longsword/Scimitar/Claws
			case 6:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("CTRL_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_SLASH"));
				break;
				
			// Mace/Flail
			case 7:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("CTRL_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_CRUSH"));
				break;
				
			// Axe
			case 8:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_SLASH"));
				break;
				
			// Hammer
			case 9:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_CRUSH"));
				break;
			
			// 2H Sword
			case 10:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_SLASH"));
				break;
				
			// Halberd
			case 11:
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("CTRL_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_STAB"));
				break;
				
			// Spear
			case 12:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_STAB"));
				break;
				
			// Whip
			case 13:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("CTRL_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_SLASH"));
				break;
			
			
			// Bludgeon
			case 14:
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				break;
				
			// Staff
			case 15:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_CRUSH"));
				break;
				
			// Pickaxe
			case 16:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_STAB"));
				break;
				
			// Ranged
			case 17:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_RANGE"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("RAPID_RANGE"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("LONG_RANGE"));
				break;
			
			// Salamander
			case 18:
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("RAPID_RANGE"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("BLAZE_MAGIC"));
				break;
				
			// SotD
			case 19:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_STAB"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("AGG_SLASH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_CRUSH"));
				break;
				
			// Trident
			case 20:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_MAGIC"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("LONG_MAGIC"));
				break;
				
			// 2H Shield
			case 21:
				usableCombatOptions.add(COMBAT_OPTIONS.get("ACC_CRUSH"));
				usableCombatOptions.add(COMBAT_OPTIONS.get("DEF_CRUSH"));
				break;
				
			default:
				//System.out.println("Default case triggered in Weapon.java switch statement");
				break;
		}
		
	}
	
}
