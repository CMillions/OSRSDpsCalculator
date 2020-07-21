package item;

import com.google.gson.annotations.SerializedName;

public class Item 
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
}
