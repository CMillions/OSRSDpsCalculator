package item;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Weapon 
{
	@SerializedName("attack_speed")
	public int attackSpeed;

	@SerializedName("weapon_type")
	public String weaponType;
	
	@SerializedName("stances")
	public List<Stance> stances;
	
	public int getAttackSpeed() 
	{
		return attackSpeed;
	}

	public void setAttackSpeed(int attackSpeed) 
	{
		this.attackSpeed = attackSpeed;
	}

	public String getWeaponType() 
	{
		return weaponType;
	}

	public void setWeaponType(String weaponType) 
	{
		this.weaponType = weaponType;
	}

	public List<Stance> getStances() 
	{
		return stances;
	}

	public void setStances(List<Stance> stances) 
	{
		this.stances = stances;
	}
	
	public Weapon()
	{
		
	}
	
	public Weapon(int atkSpd, String weaponType, List<Stance> stances)
	{	
		this.attackSpeed = atkSpd;
		this.weaponType = weaponType;
		this.stances = stances;
	}
}
