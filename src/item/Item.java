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
	
	public Item()
	{
		
	}
	
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
		
		Item newItem = ((Item)other);
		
		return this.name.equalsIgnoreCase(newItem.name);
	}
	
	@Override
	public int hashCode()
	{
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEquippableByPlayer() {
		return isEquippableByPlayer;
	}

	public void setEquippableByPlayer(boolean isEquippableByPlayer) {
		this.isEquippableByPlayer = isEquippableByPlayer;
	}

	public boolean isWeapon() {
		return isWeapon;
	}

	public void setWeapon(boolean isWeapon) {
		this.isWeapon = isWeapon;
	}

	public Equipment getItemStats() {
		return itemStats;
	}

	public void setItemStats(Equipment itemStats) {
		this.itemStats = itemStats;
	}

	public Weapon getWeaponStats() 
	{
		if(this.weaponStats != null)
			return weaponStats;
		
		return null;
	}

	public void setWeaponStats(Weapon weaponStats) {
		this.weaponStats = weaponStats;
	}
}
