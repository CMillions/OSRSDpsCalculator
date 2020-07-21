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
	
	public Weapon(int atkSpd, String weaponType, List<Stance> stances)
	{	
		this.attackSpeed = atkSpd;
		this.weaponType = weaponType;
		this.stances = stances;
	}
}
