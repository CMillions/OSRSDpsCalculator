package calculator;

import com.google.gson.annotations.SerializedName;

public class Enemy implements Comparable<Enemy>
{
	@SerializedName("id")
	public int id;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("combat_level")
	public int combatLvl;
	
	@SerializedName("hitpoints")
	public int hitpoints;
	
	@SerializedName("immune_poison")
	public boolean isImmuneToPoison;
	
	@SerializedName("immune_venom")
	public boolean isImmuneToVenom;
	
	@SerializedName("slayer_monster")
	public boolean isSlayerMonster;
	
	@SerializedName("attack_level")	
	public int atkLvl;
	
	@SerializedName("strength_level")
	public int strLvl;
	
	@SerializedName("defence_level")
	public int defLvl ;
	
	@SerializedName("magic_level")
	public int magLvl;
	
	@SerializedName("ranged_level")
	public int rngLvl;
	

	// Aggressive stats
	@SerializedName("attack_stab")
	public int stabAtk;
	
	@SerializedName("attack_slash")
	public int slashAtk;
	
	@SerializedName("attack_crush")
	public int crushAtk;
	
	@SerializedName("attack_magic")
	public int magAtk;
	
	@SerializedName("attack_ranged")
	public int rngAtk;

	
	// Defensive stats
	@SerializedName("defence_stab")
	public int stabDef;
	
	@SerializedName("defence_slash")
	public int slashDef = 0;
	
	@SerializedName("defence_crush")
	public int crushDef = 0;
	
	@SerializedName("defence_magic")
	public int magDef = 0;
	
	@SerializedName("defence_ranged")
	public int rngDef = 0;
	
	@SerializedName("attack_accuracy")
	public float attackAccuracy;
	
	@SerializedName("melee_strength")
	public int meleeStr;
	
	@SerializedName("ranged_strength")
	public int rngStr;
	
	@SerializedName("magic_damage")
	public int magDamage;
	
	// Used for scythe calculations
	@SerializedName("size")
	public int size;
	
	public Enemy(int id, String name, int combatLvl, int hitpoints, boolean poisonImmune, boolean venomImmune, boolean slayerMonster,
				int atkLvl, int strLvl, int defLvl, int magLvl, int rngLvl,
				int stabAtk, int slashAtk, int crushAtk, int magAtk, int rngAtk,
				int stabDef, int slashDef, int crushDef, int magDef, int rngDef,
				float accuracy, int meleeStr, int rngStr, int magDamage, int size)
	{
		this.id = id;
		this.name = name;
		this.combatLvl = combatLvl;
		this.hitpoints = hitpoints;
		
		this.isImmuneToPoison = poisonImmune;
		this.isImmuneToVenom = venomImmune;
		this.isSlayerMonster = slayerMonster;
		
		this.atkLvl = atkLvl;
		this.strLvl = strLvl;
		this.defLvl = defLvl;
		this.magLvl = magLvl;
		this.rngLvl = rngLvl;
		
		this.stabAtk = stabAtk;
		this.slashAtk = slashAtk;
		this.crushAtk = crushAtk;
		this.magAtk = magAtk;
		this.rngAtk = rngAtk;
		
		this.stabDef = stabDef;
		this.slashDef = slashDef;
		this.crushDef = crushDef;
		this.magDef = magDef;
		this.rngDef = rngDef;
		
		this.attackAccuracy = accuracy;
		this.meleeStr = meleeStr;
		this.rngStr = rngStr;
		this.magDamage = magDamage;
		
		this.size = size;
	} // End Enemy();

	@Override
	public String toString()
	{
		return this.name+ " | Level " + this.combatLvl;
	}
	
	@Override
	public int compareTo(Enemy comparedEnemy)
	{
		String newName = comparedEnemy.name;
		
		return this.name.compareTo(newName);
	}
	
	@Override
	public boolean equals(Object rhs)
	{
		boolean same = false;
		
		if(rhs instanceof Enemy)
		{
			Enemy temp = (Enemy) rhs;
			same = (temp.name.equalsIgnoreCase(this.name));
		}
		
		return same;
	}
}
