package calculator;

import calculator.Weapon.AttackStyle;
import calculator.Weapon.DamageType;
import javafx.util.Pair;

public class EquipmentSet 
{
	final static int NUM_EQUIPMENT_SLOTS = 11;
	
	private Pair<AttackStyle, DamageType> combatStyle;
	
	private Equippable[] itemArray;
	
	public int stabAtk     = 0;
	public int slashAtk    = 0;
	public int crushAtk    = 0;
	public int magicAtk    = 0;
	public int rangeAtk    = 0;
	
	public int stabDef     = 0;
	public int slashDef    = 0;
	public int crushDef    = 0;
	public int magicDef    = 0;
	public int rangeDef    = 0;
	
	public int meleeStr    = 0;
	public int rangeStr    = 0;
	public float magicDmg  = 0;
	public int prayerBonus = 0;

	public enum Slot
	{
		WEAPON,
		AMMO,
		HEAD,
		CAPE,
		AMULET,
		CHEST,
		LEGS,
		SHIELD,
		GLOVES,
		BOOTS,
		RING
	}
	
	public void setEquipmentSlot(Equippable item, Slot slot)
	{
		switch(slot)
		{
		case WEAPON:
			setWeapon(item);
			break;
			
		case AMMO:
			setAmmo(item);
			break;
			
		case HEAD:
			setHead(item);
			break;
			
		case CAPE:
			setCape(item);
			break;
			
		case AMULET:
			setAmulet(item);
			break;
			
		case CHEST:
			setChest(item);
			break;
			
		case LEGS:
			setLegs(item);
			break;
			
		case SHIELD:
			setShield(item);
			break;
			
		case GLOVES:
			setGloves(item);
			break;
			
		case BOOTS:
			setBoots(item);
			break;
		}
	}
	
	public void setWeapon(Equippable newWeapon) 
	{
		itemArray[0] = newWeapon;
	}

	public void setAmmo(Equippable newAmmo) 
	{
		itemArray[1] = newAmmo;
	}

	public void setHead(Equippable head) 
	{
		itemArray[2] = head;
	}

	public void setCape(Equippable cape) 
	{
		itemArray[3] = cape;
	}

	public void setAmulet(Equippable amulet) 
	{
		itemArray[4] = amulet;
	}

	public void setChest(Equippable chest) 
	{
		itemArray[5]= chest;
	}

	public void setLegs(Equippable legs) 
	{
		itemArray[6] = legs;
	}

	public void setShield(Equippable shield) 
	{
		itemArray[7] = shield;
	}

	public void setGloves(Equippable gloves) 
	{
		itemArray[8] = gloves;
	}

	public void setBoots(Equippable boots) 
	{
		itemArray[9] = boots;
	}

	public void setRing(Equippable ring) 
	{
		itemArray[10] = ring;
	}

	public void setCombatStyle(Pair<AttackStyle, DamageType> combatStyle) 
	{
		this.combatStyle = combatStyle;
	}

	public EquipmentSet()
	{
		stabAtk     = 0;
		slashAtk    = 0;
		crushAtk    = 0;
		magicAtk    = 0;
		rangeAtk    = 0;
		
		stabDef     = 0;
		slashDef    = 0;
		crushDef    = 0;
		magicDef    = 0;
		rangeDef    = 0;
		
		meleeStr    = 0;
		rangeStr    = 0;
		magicDmg    = 0;
		prayerBonus = 0;
		
		itemArray = new Equippable[NUM_EQUIPMENT_SLOTS];
	}
	
	public void calculateTotalStats()
	{
		stabAtk     = 0;
		slashAtk    = 0;
		crushAtk    = 0;
		magicAtk    = 0;
		rangeAtk    = 0;
		
		stabDef     = 0;
		slashDef    = 0;
		crushDef    = 0;
		magicDef    = 0;
		rangeDef    = 0;
		
		meleeStr    = 0;
		rangeStr    = 0;
		magicDmg    = 0;
		prayerBonus = 0;
		
		for(int i = 0; i < itemArray.length; i++)
		{
			if(itemArray[i] != null)
			{
				System.out.println(itemArray[i].name + itemArray[i].meleeStr);
				
				if(itemArray[i] instanceof Weapon)
				{
					// do some other stuff
				}
				
				stabAtk += itemArray[i].stabAtk;
				slashAtk += itemArray[i].slashAtk;
				crushAtk += itemArray[i].crushAtk;
				magicAtk += itemArray[i].magicAtk;
				rangeAtk += itemArray[i].rangeAtk;
				
				stabDef += itemArray[i].stabDef;
				slashDef += itemArray[i].slashDef;
				crushDef += itemArray[i].crushDef;
				magicDef += itemArray[i].magicDef;
				rangeDef += itemArray[i].rangeDef;
				
				meleeStr += itemArray[i].meleeStr;
				rangeStr += itemArray[i].rangeStr;
				magicDmg += itemArray[i].magicDmg;
				prayerBonus += itemArray[i].prayerBonus;
			}
		}
	}
	
}
