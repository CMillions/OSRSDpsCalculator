package item;

import com.google.gson.annotations.SerializedName;

public class Stance 
{
	@SerializedName("combat_style")
	public String combatStyle;
	
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
	
	public Stance(String combatStyle, String atkType, String atkStyle)
	{
		this.combatStyle = combatStyle;
		this.attackType = atkType;
		this.attackStyle = atkStyle;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		if(combatStyle == null)
		{
			if(attackStyle != null && attackType != null)
			{
				sb.append(attackStyle.toUpperCase() + " " + attackType.toUpperCase());
			}
		}
		else
		{
			if(attackStyle == null && attackType == null)
			{
				sb.append(combatStyle.toUpperCase());
			}
			else
			{
				sb.append(attackStyle.toUpperCase() + " " + attackType.toUpperCase());
			}
		}
		
		return sb.toString();
	}

}
