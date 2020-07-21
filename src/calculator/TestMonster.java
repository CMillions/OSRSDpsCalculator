package calculator;

import com.google.gson.annotations.SerializedName;

public class TestMonster 
{
	@SerializedName("id")
	public String id;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("combat_level")
	public int combatLevel;
	
	public TestMonster(String id, String name, int combat_level)
	{
		this.id = id;
		this.name = name;
		this.combatLevel = combat_level;
	}
	
	@Override
	public String toString()
	{
		return this.name + " | " + this.combatLevel;
	}
	
}
