package item;

import com.google.gson.annotations.SerializedName;

public class Item implements Comparable<Item>
{
	@SerializedName("name")
	public String name;
	
	@SerializedName("equipable_by_player")
	public boolean isEquippableByPlayer;
	
	@SerializedName("equipable_weapon")
	public boolean isWeapon;
	
	@SerializedName("equipment")
	public Equipment itemStats;
	
	@SerializedName("weapon")
	public Weapon weaponStats;
	
	public Item(String name, boolean equippable, boolean isWeapon, Equipment itemStats, Weapon weaponStats)
	{
		this.name = name;
		this.isEquippableByPlayer = equippable;
		this.isWeapon = isWeapon;
		this.itemStats = itemStats;
		this.weaponStats = weaponStats;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	@Override
	public int compareTo(Item compared)
	{
		return this.name.compareTo(compared.name);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other == null || this.getClass() != other.getClass())
			return false;
		
		return this.name.equalsIgnoreCase(((Item)other).name);
	}
}
