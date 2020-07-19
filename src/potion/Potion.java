package potion;

public class Potion 
{	
	public enum Type
	{
		NONE,
		ATTACK,
		STRENGTH,
		DEFENSE,
		MAGIC,
		RANGING,
		SUPER_ATTACK,
		SUPER_STRENGTH,
		SUPER_DEFENSE,
		SUPER_MAGIC,
		SUPER_RANGING,
		RAIDS_OVERLOAD,
		ZAMORAK_BREW,
		IMBUED_HEART,
		SARADOMIN_BREW
	}
	
	private String name;
	private Type type;
	
	private int additionalBonus;
	private float modifier;
	
	public Potion(String potionName, Type potionType)
	{
		this.name = potionName;
		this.type = potionType;
	}
	
	public int calculateBonus(int lvl)
	{
		switch(type)
		{
		case NONE:
			additionalBonus = 0;
			modifier = 0.f;
			break;
			
		case ATTACK:
			additionalBonus = 3;
			modifier = 0.10f;
			break;
			
		case SUPER_ATTACK:
			additionalBonus = 5;
			modifier = 0.15f;
			break;
			
		case ZAMORAK_BREW:
			additionalBonus = 2;
			modifier = 0.20f;
			break;
			
		case STRENGTH:
			additionalBonus = 3;
			modifier = 0.10f;
			break;
			
		case SUPER_STRENGTH:
			additionalBonus = 5;
			modifier = 0.15f;
			break;
			
		case RAIDS_OVERLOAD:
			additionalBonus = 5;
			modifier = 0.13f;
			break;
			
		case DEFENSE:
			additionalBonus = 3;
			modifier = 0.10f;
			break;
			
		case SUPER_DEFENSE:
			additionalBonus = 5;
			modifier = 0.15f;
			break;
			
		case SARADOMIN_BREW:
			additionalBonus = 2;
			modifier = 0.20f;
			break;
			
		case MAGIC:
			additionalBonus = 3;
			modifier = 0.10f;
			break;
		
		case SUPER_MAGIC:
			additionalBonus = 5;
			modifier = 0.15f;
			break;
			
		case IMBUED_HEART:
			additionalBonus = 1;
			modifier = 0.10f;
			
		case RANGING:
			additionalBonus = 3;
			modifier = 0.10f;
			break;
			
		case SUPER_RANGING:
			additionalBonus = 5;
			modifier = 0.15f;
			break;
			
		default:
			break;
		}
		
		return (int)(Math.floor(lvl * modifier) + additionalBonus);
	}
	
	public Type getType()
	{
		return this.type;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
