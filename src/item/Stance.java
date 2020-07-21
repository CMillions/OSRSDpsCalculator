package item;

import com.google.gson.annotations.SerializedName;

public class Stance 
{
	@SerializedName("attack_type")
	public String attackType;
	
	@SerializedName("attack_style")
	public String attackStyle;
	
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
