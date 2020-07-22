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
	
	public int getStabAttack() 
	{
		return stabAttack;
	}

	public void setStabAttack(int stabAttack) 
	{
		this.stabAttack = stabAttack;
	}

	public int getSlashAttack() 
	{
		return slashAttack;
	}

	public void setSlashAttack(int slashAttack) 
	{
		this.slashAttack = slashAttack;
	}

	public int getCrushAttack() 
	{
		return crushAttack;
	}

	public void setCrushAttack(int crushAttack) 
	{
		this.crushAttack = crushAttack;
	}

	public int getMagicAttack() 
	{
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) 
	{
		this.magicAttack = magicAttack;
	}

	public int getRangedAttack() 
	{
		return rangedAttack;
	}

	public void setRangedAttack(int rangedAttack) 
	{
		this.rangedAttack = rangedAttack;
	}

	public int getStabDefense() 
	{
		return stabDefense;
	}

	public void setStabDefense(int stabDefense) 
	{
		this.stabDefense = stabDefense;
	}

	public int getSlashDefense() 
	{
		return slashDefense;
	}

	public void setSlashDefense(int slashDefense) 
	{
		this.slashDefense = slashDefense;
	}

	public int getCrushDefense() 
	{
		return crushDefense;
	}

	public void setCrushDefense(int crushDefense) 
	{
		this.crushDefense = crushDefense;
	}

	public int getMagicDefense() 
	{
		return magicDefense;
	}

	public void setMagicDefense(int magicDefense) 
	{
		this.magicDefense = magicDefense;
	}

	public int getRangedDefense() 
	{
		return rangedDefense;
	}

	public void setRangedDefense(int rangedDefense) 
	{
		this.rangedDefense = rangedDefense;
	}

	public int getMeleeStrength() 
	{
		return meleeStrength;
	}

	public void setMeleeStrength(int meleeStrength) 
	{
		this.meleeStrength = meleeStrength;
	}

	public int getRangedStrength() 
	{
		return rangedStrength;
	}

	public void setRangedStrength(int rangedStrength) 
	{
		this.rangedStrength = rangedStrength;
	}

	public int getMagicDamage() 
	{
		return magicDamage;
	}

	public void setMagicDamage(int magicDamage) 
	{
		this.magicDamage = magicDamage;
	}

	public int getPrayerBonus() 
	{
		return prayerBonus;
	}

	public void setPrayerBonus(int prayerBonus) 
	{
		this.prayerBonus = prayerBonus;
	}

	public void setSlot(String slot) 
	{
		this.slot = slot;
	}

	public Equipment()
	{
		
	}
	
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
