package item;

import com.google.gson.annotations.SerializedName;

public class Stance 
{
	@SerializedName("attack_type")
	public String attackType;
	
	@SerializedName("attack_style")
	public String attackStyle;
	
	public String getAttackType() 
	{
		return attackType;
	}

	public void setAttackType(String attackType) 
	{
		this.attackType = attackType;
	}

	public String getAttackStyle() 
	{
		return attackStyle;
	}

	public void setAttackStyle(String attackStyle) 
	{
		this.attackStyle = attackStyle;
	}
	
	public Stance()
	{
		
	}
	
	public Stance(String atkType, String atkStyle)
	{
		this.attackType = atkType;
		this.attackStyle = atkStyle;
	}
	
	@Override
	public String toString()
	{
		return (this.attackStyle.toUpperCase() + " " + this.attackType.toUpperCase());
	}

}
