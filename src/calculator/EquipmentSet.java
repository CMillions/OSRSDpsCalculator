package calculator;

import java.util.HashMap;
import java.util.Map;

import item.Equipment;
import item.Item;

public class EquipmentSet 
{
	private final int NUM_EQUIPMENT_SLOTS = 11;
	
	public static final Map<String, Integer> slotMap;
	static
	{
		slotMap = new HashMap<>();
		slotMap.put("2h", 0);
		slotMap.put("weapon", 0);
		slotMap.put("ammo", 1);
		slotMap.put("head", 2);
		slotMap.put("cape", 3);
		slotMap.put("neck", 4);
		slotMap.put("body", 5);
		slotMap.put("legs", 6);
		slotMap.put("shield", 7);
		slotMap.put("hands", 8);
		slotMap.put("feet", 9);
		slotMap.put("ring", 10);
	}
	
	private Item[] itemArray;
	
	// Use Equipment POJO to hold total set stats
	public Equipment totalStats;

	public boolean isSlayerHelmEquipped;
	
	public void equip(Item item)
	{
		String itemSlot = item.itemStats.slot.toLowerCase();
		
		if(slotMap.containsKey(itemSlot))
		{
			Item oldItem = itemArray[slotMap.get(itemSlot)];
			
			// If there was an item already equipped, remove the stats from
			// the total so the new stats can be added
			if(oldItem != null)
			{
				Equipment oldStats = oldItem.itemStats;
				
				totalStats.stabAttack -= oldStats.stabAttack;
				totalStats.slashAttack -= oldStats.slashAttack;
				totalStats.crushAttack -= oldStats.crushAttack;
				totalStats.magicAttack -= oldStats.magicAttack;
				totalStats.rangedAttack -= oldStats.rangedAttack;
				
				totalStats.stabDefense -= oldStats.stabDefense;
				totalStats.slashDefense -= oldStats.slashDefense;
				totalStats.crushDefense -= oldStats.crushDefense;
				totalStats.magicDefense -= oldStats.magicDefense;
				totalStats.rangedDefense -= oldStats.rangedDefense;
				
				totalStats.meleeStrength -= oldStats.meleeStrength;
				totalStats.rangedStrength -= oldStats.rangedStrength;
				totalStats.magicDamage -= oldStats.magicDamage;
				totalStats.prayerBonus -= oldStats.prayerBonus;
			}
			
			if(itemSlot.equalsIgnoreCase("head"))
			{
				if(item.getName().contains("slayer helmet"))
				{
					isSlayerHelmEquipped = true;
				}
				else
				{
					isSlayerHelmEquipped = false;
				}
			}
			
			itemArray[slotMap.get(itemSlot)] = item;
			
			Equipment newStats = item.itemStats;
			
			totalStats.stabAttack += newStats.stabAttack;
			totalStats.slashAttack += newStats.slashAttack;
			totalStats.crushAttack += newStats.crushAttack;
			totalStats.magicAttack += newStats.magicAttack;
			totalStats.rangedAttack += newStats.rangedAttack;
			
			totalStats.stabDefense += newStats.stabDefense;
			totalStats.slashDefense += newStats.slashDefense;
			totalStats.crushDefense += newStats.crushDefense;
			totalStats.magicDefense += newStats.magicDefense;
			totalStats.rangedDefense += newStats.rangedDefense;
			
			totalStats.meleeStrength += newStats.meleeStrength;
			totalStats.rangedStrength += newStats.rangedStrength;
			totalStats.magicDamage += newStats.magicDamage;
			totalStats.prayerBonus += newStats.prayerBonus;
		}
	}

	
	
	public EquipmentSet()
	{		
		isSlayerHelmEquipped = false;
		itemArray = new Item[NUM_EQUIPMENT_SLOTS];
		// Initialize totalStats
		totalStats = new Equipment(0, 0, 0, 0, 0,
								   0, 0, 0, 0, 0,
								   0, 0, 0, 0, "n/a");	
	}
	
	public Item getItem(String item)
	{
		if(slotMap.containsKey(item))
			return itemArray[slotMap.get(item)];
		
		return null;
	}
}
