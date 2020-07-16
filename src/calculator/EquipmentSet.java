package calculator;

import calculator.Weapon.AttackStyle;
import calculator.Weapon.DamageType;
import javafx.util.Pair;

public class EquipmentSet 
{
	private Weapon weapon = null;
	private Weapon ammo   = null;
	private Armor head    = null;
	private Armor cape    = null;
	private Armor amulet  = null;
	private Armor chest   = null;
	private Armor legs    = null;
	private Armor shield  = null;
	private Armor gloves  = null;
	private Armor boots   = null;
	private Armor ring    = null;
	
	private Pair<AttackStyle, DamageType> combatStyle;
	
	private Armor[] armorArray = {head, cape, amulet, chest, legs, shield, gloves, boots, ring};
	
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
	
	public void setWeapon(Weapon weapon) { this.weapon = weapon; }
	public void setAmmo(Weapon ammo) 	 { this.ammo = ammo; 	 }
	public void setHead(Armor head) 	 { this.head = head; 	 }
	public void setCape(Armor cape) 	 { this.cape = cape; 	 }
	public void setAmulet(Armor amulet)  { this.amulet = amulet; }
	public void setChest(Armor chest)    { this.chest = chest;   }
	public void setLegs(Armor legs) 	 { this.legs = legs;	 }
	public void setShield(Armor shield)  { this.shield = shield; }
	public void setGloves(Armor gloves)  { this.gloves = gloves; }
	public void setBoots(Armor boots)    { this.boots = boots;	 }
	public void setRing(Armor ring)      { this.ring = ring;	 }
	public void setCombatStyle(Pair<AttackStyle, DamageType> style)
		{ this.combatStyle = style; }
	

	public EquipmentSet()
	{
	}
	
	public void calculateTotalStats()
	{
		stabAtk = weapon.stabAtk;
		slashAtk = weapon.slashAtk;
		crushAtk = weapon.crushAtk;
		magicAtk = weapon.magicAtk;
		rangeAtk = weapon.rangeAtk + ammo.rangeAtk;
		
		stabDef = weapon.stabDef;
		slashDef = weapon.slashDef;
		crushDef = weapon.crushDef;
		magicDef = weapon.magicDef;
		rangeDef = weapon.rangeDef;
		
		meleeStr = weapon.meleeStr;
		rangeStr = weapon.rangeStr + ammo.rangeStr;
		magicDmg = weapon.magicDmg;
		prayerBonus = weapon.prayerBonus + ammo.prayerBonus;
		
		combatStyle.equals(null);
		
		for(int i = 0; i < armorArray.length; i++)
		{
			stabDef += armorArray[i].stabDef;
			slashDef += armorArray[i].slashDef;
			crushDef += armorArray[i].crushDef;
			magicDef += armorArray[i].magicDef;
			rangeDef += armorArray[i].rangeDef;
			
			meleeStr += armorArray[i].meleeStr;
			rangeStr += armorArray[i].rangeStr;
			magicDmg += armorArray[i].magicDmg;
			prayerBonus += armorArray[i].prayerBonus;
		}
	}
	
}
