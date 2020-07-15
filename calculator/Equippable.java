package calculator;

import java.util.ArrayList;
import java.util.List;

enum CombatOptions
{
	ACC_SLASH,
	AGG_SLASH,
	DEF_SLASH,
	CTRL_SLASH,
	
	ACC_STAB,
	AGG_STAB,
	DEF_STAB,
	CTRL_STAB,
	
	ACC_CRUSH,
	AGG_CRUSH,
	DEF_CRUSH,
	CTRL_CRUSH,
	
	ACC_RANGE,
	RAPID_RANGE,
	LONG_RANGE,
	
	ACC_MAGIC,
	LONG_MAGIC,
	BLAZE_MAGIC
}

public class Equippable
{	
	public String name;
	
	public int stabAtk;
	public int slashAtk;
	public int crushAtk;
	public int magicAtk;
	public int rangeAtk;
	
	public int stabDef;
	public int slashDef;
	public int crushDef;
	public int magicDef;
	public int rangeDef;
	
	public int meleeStr;
	public int rangeStr;
	public float magicDmg;
	public int prayerBonus;
	
	
	public Equippable()
	{
		this.name = "DEFAULT";
		stabAtk = 0;
		slashAtk = 0;
		crushAtk = 0;
		magicAtk = 0;
		rangeAtk = 0;
		
		stabDef = 0;
		slashDef = 0;
		crushDef = 0;
		magicDef = 0;
		rangeDef = 0;
		
		prayerBonus = 0;
	}
	
	public Equippable(List<String> list)
	{		
		ArrayList<String> data = new ArrayList<>();
		
		for(String s : list)
			data.add(s);
		
		while(data.size() < 24)
		{
			data.add("0");
		}
				
		name = data.get(0);
		
		stabAtk = Integer.parseInt(data.get(1));
		slashAtk = Integer.parseInt(data.get(2));
		crushAtk = Integer.parseInt(data.get(3));
		magicAtk = Integer.parseInt(data.get(4));
		rangeAtk = Integer.parseInt(data.get(5));
		
		stabDef = Integer.parseInt(data.get(6));
		slashDef = Integer.parseInt(data.get(7));
		crushDef = Integer.parseInt(data.get(8));
		magicDef = Integer.parseInt(data.get(9));
		rangeDef = Integer.parseInt(data.get(10));
		
		meleeStr = Integer.parseInt(data.get(11));
		rangeStr = Integer.parseInt(data.get(12));
		magicDmg = Float.parseFloat(data.get(13));
		prayerBonus = Integer.parseInt(data.get(14));
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
