package item;

import com.google.gson.annotations.SerializedName;

public class Equipment 
{
	@SerializedName("attack_stab")
	public int stabAttack;
	
	@SerializedName("attack_slash")
	public int slashAttack;
	
	@SerializedName("attack_crush")
	public int crushAttack;
	
	@SerializedName("attack_magic")
	public int magicAttack;
	
	@SerializedName("attack_ranged")
	public int rangedAttack;
	
	@SerializedName("defence_stab")
	public int stabDefense;
	
	@SerializedName("defence_slash")
	public int slashDefense;
	
	@SerializedName("defense_crush")
	public int crushDefense;
	
	@SerializedName("defense_magic")
	public int magicDefense;
	
	@SerializedName("defense_ranged")
	public int rangedDefense;
	
	@SerializedName("melee_strength")
	public int meleeStrength;
	
	@SerializedName("ranged_strength")
	public int rangedStrength;
	
	@SerializedName("magic_damage")
	public int magicDamage;
	
	@SerializedName("prayer")
	public int prayerBonus;
	
	@SerializedName("slot")
	public String slot;
	
	public Equipment(int stabAtk, int slashAtk, int crushAtk, int magicAtk, int rangedAtk,
					 int stabDef, int slashDef, int crushDef, int magicDef, int rangedDef,
					 int meleeStr, int rangedStr, int magicDamage, int prayerBonus, String slot)
	{
		this.stabAttack = stabAtk;
		this.slashAttack = slashAtk;
		this.crushAttack = crushAtk;
		this.magicAttack = magicAtk;
		this.rangedAttack = rangedAtk;
		
		this.stabDefense = stabDef;
		this.slashDefense = slashDef;
		this.crushDefense = crushDef;
		this.magicDefense = magicDef;
		this.rangedDefense = rangedDef;
		
		this.meleeStrength = meleeStr;
		this.rangedStrength = rangedStr;
		this.magicDamage = magicDamage;
		this.prayerBonus = prayerBonus;
		this.slot = slot;
	}
	
	public String getSlot()
	{
		return this.slot;
	}
}
