/*
 * OSRS DPS Calculator
 * 
 * Author:			   CMillions
 * Project Start Date: 7/1/2020
 *
 * Standalone version of the Google Sheets DPS calculator found here:
 *     https://docs.google.com/spreadsheets/d/1nUgqaadPFRj8UvgvlXe2UkDDcIbQ2crTGjo57GhPm4M/edit
 *     
 * Big thanks to Bitterkoekje for the Google Sheets calculator. I split up the .csv files from
 * the online version to create this program.
 * 
 * Most of the layout is a pain in the ass to do since it's positioned by hand
 *
 */

package calculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import calculator.Weapon.AttackStyle;
import calculator.Weapon.DamageType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.ComboBoxAutoComplete;
import utils.LimitedTextField;


public class Main extends Application
{
	// Filenames that contain item data
	final static String WEAPONS_FILE   		  = "weapons.csv";
	final static String OFFHANDS_FILE 		  = "offhands.csv";
	final static String HELMETS_FILE 		  = "helmets.csv";
	final static String CHESTPIECES_FILE 	  = "chestpieces.csv";
	final static String LEGS_FILE 			  = "legs.csv";
	final static String RINGS_FILE 			  = "rings.csv";
	final static String CAPES_FILE 			  = "back_slot.csv";
	final static String AMMO_FILE 			  = "ammo.csv";
	final static String BOOTS_FILE 			  = "boots.csv";
	final static String GLOVES_FILE 		  = "gloves.csv";
	final static String NECKLACES_FILE 		  = "necklaces.csv";
	final static String ENEMIES_FILE		  = "enemies.csv";
	
	// URLs for icons
	final static String PROGRAM_ICON_URL  = "res/images/program_icon.png";
	final static String ATTACK_ICON_URL   = "res/images/attack_icon.png";
	final static String STRENGTH_ICON_URL = "res/images/strength_icon.png";
	final static String DEFENSE_ICON_URL  = "res/images/defense_icon.png";
	final static String MAGIC_ICON_URL    = "res/images/magic_icon.png";
	final static String RANGE_ICON_URL    = "res/images/ranged_icon.png";
	final static String HP_ICON_URL       = "res/images/hp_icon.png";
	final static String PRAYER_ICON_URL   = "res/images/prayer_icon.png";
	final static String STATS_ICON_URL    = "res/images/stats_icon.png";
	final static String POTION_ICON_URL	  = "res/images/potion_icon.png";
	final static String OVERLOAD_ICON_URL = "res/images/raids_overload_icon.png";
	final static String MULTI_PRAYER_ICON_URL = "res/images/multi_prayer_icons.png";
	
	final static String STAB_DEFENSE_ICON_URL = "res/images/stab_defense_icon.png";
	final static String SLASH_DEFENSE_ICON_URL = "res/images/slash_defense_icon.png";
	final static String CRUSH_DEFENSE_ICON_URL = "res/images/crush_defense_icon.png";
	final static String MAGIC_DEFENSE_ICON_URL = "res/images/magic_defense_icon.png";
	final static String RANGE_DEFENSE_ICON_URL = "res/images/range_defense_icon.png";
	
	// JavaFX Constants
	final static int PREF_COMBOBOX_WIDTH = 200;
	final static int PREF_COMBOBOX_HEIGHT = 20;
	final static int PREF_TEXTFIELD_WIDTH = 1;
	
	final static String BG_COLOR = "0x606060";
	
	// Other variables
	private static List<Weapon> weapons;
	private static List<Weapon> ammo;
	private static List<Armor> heads;
	private static List<Armor> capes;
	private static List<Armor> amulets;
	private static List<Armor> chests;
	private static List<Armor> legs;
	private static List<Armor> shields;
	private static List<Armor> gloves;
	private static List<Armor> boots;
	private static List<Armor> rings;
	private static List<Enemy> enemies;
	
	private static List<Label> comboBoxLabels;
	private static EquipmentSet currentSet;
	
	private final static int WINDOW_WIDTH = 1600;
	private final static int WINDOW_HEIGHT = 900;
	
	// Used to calculate max hit
	private static int atkLvl = 99;
	private static int strLvl = 99;
	private static int defLvl = 99;
	private static int magLvl = 99;
	private static int rngLvl = 99;
	private static int hpLvl = 99;
	private static int prayerLvl = 99;
	
	private static int atkPotionBonus;
	private static int strPotionBonus;
	private static int defPotionBonus;
	private static int magPotionBonus;
	private static int rngPotionBonus;
	
	private static float atkPrayerBonus;
	private static float strPrayerBonus;
	private static float defPrayerBonus;
	private static float magPrayerBonus;
	private static float magDefenseBonus;
	private static float rngPrayerBonus;
	private static float rngStrengthBonus;
	
	
	private static int prayerBonus;
	private static int otherBonus;
	private static int styleBonus;
	
	// floor((StrLvl + PotionBonus) * PrayerBonus * OtherBonus) + StyleBonus
	private static int effectiveStrength;
	
	// JavaFX Related Functions
	
	@Override
	public void start(Stage primaryStage)
	{	
		primaryStage.setWidth(WINDOW_WIDTH);
		primaryStage.setHeight(WINDOW_HEIGHT);
		primaryStage.setTitle("OSRS DPS Calculator");
		
		Scene mainScene = new Scene(createLayoutPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
		
		primaryStage.getIcons().add(new Image("file:res/images/program_icon.png"));
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	private static GridPane createLayoutPane()
	{
		GridPane container = new GridPane();
		container.setPadding(new Insets(20, 20, 20, 20));
		container.setHgap(5);
		
		// Center entire GridPane in scene
		container.setAlignment(Pos.CENTER);
		
		Color gray = Color.web("0x333333");
		container.setBackground(new Background(new BackgroundFill(gray, CornerRadii.EMPTY, Insets.EMPTY)));
		container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		Label statsLabel = createRunescapeLabelText("Stats", 16, HPos.CENTER);
		container.add(statsLabel, 1, 0);
		
		// Labels to show invisible stat levels after a prayer is selected
		
		Label buffedStatsLabel = createRunescapeLabelText("Buffed Stats", 16, HPos.CENTER);
		container.add(buffedStatsLabel, 6, 0);
		
		Label buffedAtkLvlLabel = createRunescapeLabelText(atkLvl + "/" + atkLvl, 16, HPos.CENTER);
		container.add(buffedAtkLvlLabel, 6, 1);
		
		Label buffedStrLvlLabel = createRunescapeLabelText(strLvl + "/" + strLvl, 16, HPos.CENTER);
		container.add(buffedStrLvlLabel, 6, 2);
		
		Label buffedDefLvlLabel = createRunescapeLabelText(defLvl + "/" + defLvl, 16, HPos.CENTER);
		container.add(buffedDefLvlLabel, 6, 3);
		
		Label buffedMagLvlLabel = createRunescapeLabelText(magLvl + "/" + magLvl, 16, HPos.CENTER);
		container.add(buffedMagLvlLabel, 6, 4);
		
		Label buffedRngLvlLabel = createRunescapeLabelText(rngLvl + "/" + rngLvl, 16, HPos.CENTER);
		container.add(buffedRngLvlLabel, 6, 5);
		
		String[] skillNames = {"Attack", "Strength", "Defense", "Magic", "Ranged", "Hitpoints", "Prayer"};
			
		for(int i = 0; i < skillNames.length; i++)
		{
			Label l = createRunescapeLabelText(skillNames[i], 16, HPos.LEFT);
			container.add(l, 1, i + 1);
		}
		
		// Player stats images
      
		container.add(createImageView(ATTACK_ICON_URL, HPos.CENTER), 0, 1);
		container.add(createImageView(STRENGTH_ICON_URL, HPos.CENTER), 0, 2);
		container.add(createImageView(DEFENSE_ICON_URL, HPos.CENTER), 0, 3);
		container.add(createImageView(MAGIC_ICON_URL, HPos.CENTER), 0, 4);
		container.add(createImageView(RANGE_ICON_URL, HPos.CENTER), 0, 5);
		container.add(createImageView(HP_ICON_URL, HPos.CENTER), 0, 6);
		container.add(createImageView(PRAYER_ICON_URL, HPos.CENTER), 0, 7);
		container.add(createImageView(STATS_ICON_URL, HPos.CENTER), 2, 0);
		container.add(createImageView(POTION_ICON_URL, HPos.CENTER), 3, 0);	
		
		// Showing monster stats
		Label monsterStatsLabel = createRunescapeLabelText("Monster Stats", 16, HPos.CENTER);
        GridPane.setColumnSpan(monsterStatsLabel, 4);
        container.add(monsterStatsLabel, 11, 0);
        container.add(createImageView(ATTACK_ICON_URL, HPos.CENTER), 11, 1);
        container.add(createImageView(STRENGTH_ICON_URL, HPos.CENTER), 11, 2);
        container.add(createImageView(DEFENSE_ICON_URL, HPos.CENTER), 11, 3);
        container.add(createImageView(MAGIC_ICON_URL, HPos.CENTER), 11, 4);
        container.add(createImageView(RANGE_ICON_URL, HPos.CENTER), 11, 5);
        container.add(createImageView(HP_ICON_URL, HPos.CENTER), 11, 6);
		
        // spaghetti code so labels can show monster stats on startup
        ComboBox<Enemy> enemyCB = createAutoCompleteComboBox(enemies);
		enemyCB.getSelectionModel().selectFirst();
        
        Label monsterAtk = createRunescapeLabelText(enemyCB.getValue().atkLvl + "", 16, HPos.CENTER);
        container.add(monsterAtk, 12, 1);
        Label monsterStr = createRunescapeLabelText(enemyCB.getValue().strLvl + "", 16, HPos.CENTER);
        container.add(monsterStr, 12, 2);
        Label monsterDef = createRunescapeLabelText(enemyCB.getValue().defLvl + "", 16, HPos.CENTER);
        container.add(monsterDef, 12, 3);
        Label monsterMag = createRunescapeLabelText(enemyCB.getValue().magLvl + "", 16, HPos.CENTER);
        container.add(monsterMag, 12, 4);
        Label monsterRng = createRunescapeLabelText(enemyCB.getValue().rngLvl + "", 16, HPos.CENTER);
        container.add(monsterRng, 12, 5);
        Label monsterHP = createRunescapeLabelText(enemyCB.getValue().hitpoints + "", 16, HPos.CENTER);
        container.add(monsterHP, 12, 6);
        
        container.add(createImageView(STAB_DEFENSE_ICON_URL,  HPos.CENTER), 13, 1);
        container.add(createImageView(SLASH_DEFENSE_ICON_URL, HPos.CENTER), 13, 2);
        container.add(createImageView(CRUSH_DEFENSE_ICON_URL, HPos.CENTER), 13, 3);
        container.add(createImageView(MAGIC_DEFENSE_ICON_URL, HPos.CENTER), 13, 4);
        container.add(createImageView(RANGE_DEFENSE_ICON_URL, HPos.CENTER), 13, 5);
        
        Label monsterStabDef = createRunescapeLabelText(enemyCB.getValue().stabDef + "", 16, HPos.CENTER);
        container.add(monsterStabDef, 14, 1);
        Label monsterSlashDef = createRunescapeLabelText(enemyCB.getValue().slashDef + "", 16, HPos.CENTER);
        container.add(monsterSlashDef, 14, 2);
        Label monsterCrushDef = createRunescapeLabelText(enemyCB.getValue().crushDef + "", 16, HPos.CENTER);
        container.add(monsterCrushDef, 14, 3);
        Label monsterMagDef = createRunescapeLabelText(enemyCB.getValue().magDef + "", 16, HPos.CENTER);
        container.add(monsterMagDef, 14, 4);
        Label monsterRngDef = createRunescapeLabelText(enemyCB.getValue().rngDef + "", 16, HPos.CENTER);
        container.add(monsterRngDef, 14, 5);
		
		ComboBox<Weapon> weaponCB = createAutoCompleteComboBox(weapons);
		ComboBox<Armor> shieldCB = createAutoCompleteComboBox(shields);
		
		shieldCB.setOnAction(e -> {
			Armor selected = shieldCB.getValue();
			
			if(selected != null)
			{
				currentSet.setShield(selected);
			}
		});
		
		ComboBox<Pair<AttackStyle, DamageType>> combatStyleCB = new ComboBox<>();
		combatStyleCB.setItems(FXCollections.observableArrayList(weaponCB.getSelectionModel().getSelectedItem().usableCombatOptions));
		combatStyleCB.getSelectionModel().selectFirst();
		combatStyleCB.setPrefWidth(PREF_COMBOBOX_WIDTH);
		combatStyleCB.setOnAction(e -> {
			Pair<AttackStyle, DamageType> selected = combatStyleCB.getValue();
		
			if(selected != null)
			{
				currentSet.setCombatStyle(selected);
			}
		});
		
		GridPane.setHalignment(combatStyleCB, HPos.CENTER);
		
		// Do things when item is selected
		weaponCB.setOnAction(e -> {
			Weapon w = weaponCB.getValue();
			
			if(w != null)
			{	
				// Disable shields ComboBox if weapon is two handed
				if(w.isTwoHand)
				{
					shieldCB.setDisable(true);
					shieldCB.getSelectionModel().selectFirst();
				}
				else
				{
					shieldCB.setDisable(false);
				}

				combatStyleCB.setItems(FXCollections.observableArrayList(w.usableCombatOptions));
				
				// Update current equipment set
				currentSet.setWeapon(w);
			}
		});

		
		ComboBox<Weapon> ammoCB = createAutoCompleteComboBox(ammo);
		ammoCB.setOnAction(e -> {
			Weapon selected = ammoCB.getValue();
			
			if(selected != null)
			{
				currentSet.setAmmo(selected);
			}
		});
		
		
		ComboBox<Armor> headCB = createAutoCompleteComboBox(heads);
		headCB.setOnAction(e -> {
			Armor selected = headCB.getValue();
			
			if(selected != null)
			{
				currentSet.setHead(selected);
			}
		});
		
		
		ComboBox<Armor> capeCB = createAutoCompleteComboBox(capes);
		capeCB.setOnAction(e -> {
			Armor selected = capeCB.getValue();
			
			if(selected != null)
			{
				currentSet.setCape(selected);
			}
		});
		
		
		ComboBox<Armor> amuletCB = createAutoCompleteComboBox(amulets);
		amuletCB.setOnAction(e -> {
			Armor selected = amuletCB.getValue();
			
			if(selected != null)
			{
				currentSet.setAmulet(selected);
			}
		});
		
		
		ComboBox<Armor> chestCB = createAutoCompleteComboBox(chests);
		chestCB.setOnAction(e -> {
			Armor selected = chestCB.getValue();
			
			if(selected != null)
			{
				currentSet.setChest(selected);
			}
		});
		
		
		ComboBox<Armor> legCB = createAutoCompleteComboBox(legs);
		legCB.setOnAction(e -> {
			Armor selected = legCB.getValue();
			
			if(selected != null)
			{
				currentSet.setLegs(selected);
			}
		});
		
		
		ComboBox<Armor> gloveCB = createAutoCompleteComboBox(gloves);
		gloveCB.setOnAction(e -> {
			Armor selected = gloveCB.getValue();
			
			if(selected != null)
			{
				currentSet.setGloves(selected);
			}
		});
		
		
		ComboBox<Armor> bootCB = createAutoCompleteComboBox(boots);
		bootCB.setOnAction(e -> {
			Armor selected = bootCB.getValue();
			
			if(selected != null)
			{
				currentSet.setBoots(selected);
			}
		});
		
		
		ComboBox<Armor> ringCB = createAutoCompleteComboBox(rings);
		ringCB.setOnAction(e -> {
			Armor selected = ringCB.getValue();
			
			if(selected != null)
			{
				currentSet.setRing(selected);
			}
		});
		
		
		enemyCB.setOnAction(e -> {
			Enemy selected = enemyCB.getValue();
			
			if(selected != null)
			{				
				monsterAtk.setText(selected.atkLvl + "");
				monsterStr.setText(selected.strLvl + "");
				monsterDef.setText(selected.defLvl + "");
				monsterMag.setText(selected.magLvl + "");
				monsterRng.setText(selected.rngLvl + "");
				monsterHP.setText(selected.hitpoints + "");
				
				monsterStabDef.setText(selected.stabDef + "");
				monsterSlashDef.setText(selected.slashDef + "");
				monsterCrushDef.setText(selected.crushDef + "");
				monsterMagDef.setText(selected.magDef + "");
				monsterRngDef.setText(selected.rngDef + "");
			}
		});


		// Potion ComboBoxes
		
		Potion raidsOverload = new Potion("Overload (raids)", Potion.Type.RAIDS_OVERLOAD);
		
		ComboBox<Potion> atkPotCB = new ComboBox<>();
		atkPotCB.setPrefWidth(150);
		atkPotCB.getItems().addAll(new Potion("None", Potion.Type.NONE), 
								   new Potion("Attack", Potion.Type.ATTACK),
								   new Potion("Super Attack", Potion.Type.SUPER_ATTACK),
								   new Potion("Zamorak Brew", Potion.Type.ZAMORAK_BREW)
								   );
		atkPotCB.getSelectionModel().selectFirst();
		atkPotCB.setOnAction(e -> {
			try
			{
				atkPotionBonus = atkPotCB.getValue().calculateBonus(atkLvl);
				buffedAtkLvlLabel.setText((int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus)) + "/" + atkLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ComboBox<Potion> strPotCB = new ComboBox<>();
		strPotCB.setPrefWidth(150);
		strPotCB.getItems().addAll(new Potion("None", Potion.Type.NONE),
								   new Potion("Strength", Potion.Type.STRENGTH),
								   new Potion("Super Strength", Potion.Type.SUPER_STRENGTH)
								  );
		strPotCB.getSelectionModel().selectFirst();
		strPotCB.setOnAction(e -> {
			try
			{
				strPotionBonus = strPotCB.getValue().calculateBonus(strLvl);
				buffedStrLvlLabel.setText((int)((strLvl + strPotionBonus)*(1 + strPrayerBonus)) + "/" + strLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ComboBox<Potion> defPotCB = new ComboBox<>();
		defPotCB.setPrefWidth(150);
		defPotCB.getItems().addAll(new Potion("None", Potion.Type.NONE),
								   new Potion("Defense", Potion.Type.DEFENSE),
								   new Potion("Super Defense", Potion.Type.SUPER_DEFENSE),
								   new Potion("Saradmoin Brew", Potion.Type.SARADOMIN_BREW)
								   );
		defPotCB.getSelectionModel().selectFirst();
		defPotCB.setOnAction(e -> {	
			try
			{
				defPotionBonus = defPotCB.getValue().calculateBonus(defLvl);
				buffedDefLvlLabel.setText((int)((defLvl + defPotionBonus)*(1 + defPrayerBonus)) + "/" + defLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		});

		ComboBox<Potion> magPotCB = new ComboBox<>();
		magPotCB.setPrefWidth(150);
		magPotCB.getItems().addAll(new Potion("None", Potion.Type.NONE),
							       new Potion("Magic", Potion.Type.MAGIC),
							       new Potion("Super Magic", Potion.Type.SUPER_MAGIC),
							       new Potion("Imbued Heart", Potion.Type.IMBUED_HEART)
							       );
		magPotCB.getSelectionModel().selectFirst();
		magPotCB.setOnAction(e -> {	
			try
			{
				magPotionBonus = magPotCB.getValue().calculateBonus(magLvl);
				buffedMagLvlLabel.setText((int)((magLvl + magPotionBonus)*(1 + magPrayerBonus)) + "/" + magLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		});
		
		ComboBox<Potion> rngPotCB = new ComboBox<>();
		rngPotCB.setPrefWidth(150);
		rngPotCB.getItems().addAll(new Potion("None", Potion.Type.NONE),
								   new Potion("Ranging", Potion.Type.RANGING),
								   new Potion("Super Ranging", Potion.Type.SUPER_RANGING)
				 				  );
		rngPotCB.getSelectionModel().selectFirst();
		
		rngPotCB.setOnAction(e -> {
			try
			{	
				rngPotionBonus = rngPotCB.getValue().calculateBonus(rngLvl);
				buffedRngLvlLabel.setText((int)((rngLvl + rngPotionBonus)*(1 + rngPrayerBonus)) + "/" + rngLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ImageView overloadIconView = createImageView(OVERLOAD_ICON_URL);
		GridPane.setHalignment(overloadIconView, HPos.CENTER);
		container.add(overloadIconView, 4, 0);
		
		CheckBox overloadCheckBox = new CheckBox();
		GridPane.setHalignment(overloadCheckBox, HPos.CENTER);
		overloadCheckBox.setOnAction(e -> {
			if(overloadCheckBox.isSelected())
			{			
				atkPotCB.getSelectionModel().selectFirst();
				strPotCB.getSelectionModel().selectFirst();
				defPotCB.getSelectionModel().selectFirst();
				magPotCB.getSelectionModel().selectFirst();
				rngPotCB.getSelectionModel().selectFirst();
				
				atkPotCB.setDisable(true);
				strPotCB.setDisable(true);
				defPotCB.setDisable(true);
				magPotCB.setDisable(true);
				rngPotCB.setDisable(true);
				
				atkPotionBonus = raidsOverload.calculateBonus(atkLvl);
				strPotionBonus = raidsOverload.calculateBonus(strLvl);
				defPotionBonus = raidsOverload.calculateBonus(defLvl);
				magPotionBonus = raidsOverload.calculateBonus(magLvl);
				rngPotionBonus = raidsOverload.calculateBonus(rngLvl);
				
				buffedAtkLvlLabel.setText((int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus)) + "/" + atkLvl);
				buffedStrLvlLabel.setText((int)((strLvl + strPotionBonus)*(1 + strPrayerBonus)) + "/" + strLvl);
				buffedDefLvlLabel.setText((int)((defLvl + defPotionBonus)*(1 + defPrayerBonus)) + "/" + defLvl);
				buffedMagLvlLabel.setText((int)((magLvl + magPotionBonus)*(1 + magPrayerBonus)) + "/" + magLvl);
				buffedRngLvlLabel.setText((int)((rngLvl + rngPotionBonus)*(1 + rngPrayerBonus)) + "/" + rngLvl);
			}
			else
			{
				atkPotCB.setDisable(false);
				strPotCB.setDisable(false);
				defPotCB.setDisable(false);
				magPotCB.setDisable(false);
				rngPotCB.setDisable(false);
				
				atkPotionBonus = 0;
				strPotionBonus = 0;
				defPotionBonus = 0;
				magPotionBonus = 0;
				rngPotionBonus = 0;
				
				buffedAtkLvlLabel.setText((int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus)) + "/" + atkLvl);
				buffedStrLvlLabel.setText((int)((strLvl + strPotionBonus)*(1 + strPrayerBonus)) + "/" + strLvl);
				buffedDefLvlLabel.setText((int)((defLvl + defPotionBonus)*(1 + defPrayerBonus)) + "/" + defLvl);
				buffedMagLvlLabel.setText((int)((magLvl + magPotionBonus)*(1 + magPrayerBonus)) + "/" + magLvl);
				buffedRngLvlLabel.setText((int)((rngLvl + rngPotionBonus)*(1 + rngPrayerBonus)) + "/" + rngLvl);
			}
		});
		container.add(overloadCheckBox, 4, 1);
		
		ImageView prayerIcon = createImageView(PRAYER_ICON_URL);
		GridPane.setHalignment(prayerIcon, HPos.CENTER);
		container.add(prayerIcon, 5, 0);
		
		ComboBox<String> atkPrayerCB = new ComboBox<String>();
		GridPane.setHalignment(atkPrayerCB, HPos.CENTER);
		atkPrayerCB.setPrefWidth(125);
		atkPrayerCB.getItems().addAll("None", "5%", "10%", "15%");
		atkPrayerCB.getSelectionModel().selectFirst();
		container.add(atkPrayerCB, 5, 1);
		atkPrayerCB.setOnAction(e ->  {
			
			String selected = atkPrayerCB.getValue();
			
			try
			{
				if(!selected.equals("None"))
				{
					String partial = selected.replaceAll("[^0-9]", "");
					atkPrayerBonus = Float.parseFloat(partial) / 100.f;
				}
				else
				{
					atkPrayerBonus = 0.0f;
				}
				
				buffedAtkLvlLabel.setText((int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus)) + "/" + atkLvl);	
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ComboBox<String> strPrayerCB = new ComboBox<>();
		GridPane.setHalignment(strPrayerCB, HPos.CENTER);
		strPrayerCB.setPrefWidth(125);
		strPrayerCB.getItems().addAll("None", "5%", "10%", "15%");
		strPrayerCB.getSelectionModel().selectFirst();
		container.add(strPrayerCB, 5, 2);
		strPrayerCB.setOnAction(e -> {
			
			String selected = strPrayerCB.getValue();
			
			try
			{
				if(!selected.equals("None"))
				{
					String partial = selected.replaceAll("[^0-9]", "");
					strPrayerBonus = Float.parseFloat(partial) / 100.f;
				}
				else
				{
					strPrayerBonus = 0.0f;
				}
				
				buffedStrLvlLabel.setText((int)((strLvl + strPotionBonus)*(1 + strPrayerBonus)) + "/" + strLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ComboBox<String> defPrayerCB = new ComboBox<>();
		GridPane.setHalignment(defPrayerCB, HPos.CENTER);
		defPrayerCB.setPrefWidth(125);
		defPrayerCB.getItems().addAll("None", "5%", "10%", "15%");
		defPrayerCB.getSelectionModel().selectFirst();
		container.add(defPrayerCB, 5, 3);
		defPrayerCB.setOnAction(e -> {
			
			String selected = defPrayerCB.getValue();
			
			try
			{
				if(!selected.equals("None"))
				{
					String partial = selected.replaceAll("[^0-9]", "");
					defPrayerBonus = Float.parseFloat(partial) / 100.f;
				}
				else
				{
					defPrayerBonus = 0.0f;
				}
				
				buffedDefLvlLabel.setText((int)((defLvl + defPotionBonus)*(1 + defPrayerBonus)) + "/" + defLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ComboBox<String> magPrayerCB = new ComboBox<>();
		GridPane.setHalignment(magPrayerCB, HPos.CENTER);
		magPrayerCB.setPrefWidth(125);
		magPrayerCB.getItems().addAll("None", "5%", "10%", "15%");
		magPrayerCB.getSelectionModel().selectFirst();
		container.add(magPrayerCB, 5, 4);
		magPrayerCB.setOnAction(e -> {
			
			String selected = magPrayerCB.getValue();
			
			try
			{
				if(!selected.equals("None"))
				{
					String partial = selected.replaceAll("[^0-9]", "");
					magPrayerBonus = magDefenseBonus = Float.parseFloat(partial) / 100.f;
				}
				else
				{
					magPrayerBonus = 0.f;
					magDefenseBonus = 0.f;
				}
				
				buffedMagLvlLabel.setText((int)((magLvl + magPotionBonus)*(1 + magPrayerBonus)) + "/" + magLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		ComboBox<String> rngPrayerCB = new ComboBox<>();
		GridPane.setHalignment(rngPrayerCB, HPos.CENTER);
		rngPrayerCB.setPrefWidth(125);
		rngPrayerCB.getItems().addAll("None", "5%", "10%", "15%");
		rngPrayerCB.getSelectionModel().selectFirst();
		container.add(rngPrayerCB, 5, 5);
		rngPrayerCB.setOnAction(e -> {
			
			String selected = rngPrayerCB.getValue();
			
			try
			{
				if(!selected.equals("None"))
				{
					String partial = selected.replaceAll("[^0-9]", "");
					rngPrayerBonus = rngStrengthBonus = Float.parseFloat(partial) / 100.f;
				}
				else
				{
					rngPrayerBonus = 0.0f;
					rngStrengthBonus = 0.0f;
				}
				
				buffedRngLvlLabel.setText((int)((rngLvl + rngPotionBonus)*(1 + rngPrayerBonus)) + "/" + rngLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		});
		
		ImageView multiPrayerIcons = createImageView(MULTI_PRAYER_ICON_URL);
		GridPane.setHalignment(multiPrayerIcons, HPos.CENTER);
		container.add(multiPrayerIcons, 5, 6);
		
		ComboBox<String> multiStatPrayerCB = new ComboBox<>();
		GridPane.setHalignment(multiStatPrayerCB, HPos.CENTER);
		multiStatPrayerCB.setPrefWidth(125);
		multiStatPrayerCB.getItems().addAll("None", "Chivalry", "Piety", "Rigour", "Augury");
		multiStatPrayerCB.getSelectionModel().selectFirst();
		container.add(multiStatPrayerCB, 5, 7);
		
		multiStatPrayerCB.setOnAction(e -> {
			
			String selected = multiStatPrayerCB.getValue();
			
			try
			{
				if(!selected.equals("None"))
				{
					atkPrayerCB.getSelectionModel().selectFirst();
					strPrayerCB.getSelectionModel().selectFirst();
					defPrayerCB.getSelectionModel().selectFirst();
					magPrayerCB.getSelectionModel().selectFirst();
					rngPrayerCB.getSelectionModel().selectFirst();
					
					atkPrayerCB.setDisable(true);
					strPrayerCB.setDisable(true);
					defPrayerCB.setDisable(true);
					magPrayerCB.setDisable(true);
					rngPrayerCB.setDisable(true);
					
					switch(selected)
					{
					case "Chivalry":
						atkPrayerBonus = 0.15f;
						strPrayerBonus = 0.18f;
						defPrayerBonus = 0.20f;
						magPrayerBonus = 0.0f;
						magDefenseBonus = 0.0f;
						rngPrayerBonus = 0.0f;
						rngStrengthBonus = 0.0f;
						break;
						
					case "Piety":
						atkPrayerBonus = 0.20f;
						strPrayerBonus = 0.23f;
						defPrayerBonus = 0.25f;
						magPrayerBonus = 0.0f;
						magDefenseBonus = 0.0f;
						rngPrayerBonus = 0.0f;
						rngStrengthBonus = 0.0f;
						break;
						
					case "Rigour":
						atkPrayerBonus = 0.0f;
						strPrayerBonus = 0.0f;
						defPrayerBonus = 0.25f;
						magPrayerBonus = 0.0f;
						magDefenseBonus = 0.0f;
						rngPrayerBonus = 0.20f;
						rngStrengthBonus = 0.23f;
						break;
						
					case "Augury":
						atkPrayerBonus = 0.0f;
						strPrayerBonus = 0.0f;
						defPrayerBonus = 0.25f;
						magPrayerBonus = 0.25f;
						magDefenseBonus = 0.25f;
						rngPrayerBonus = 0.0f;
						rngStrengthBonus = 0.0f;
						break;
						
					}
				}
				else
				{
					atkPrayerCB.setDisable(false);
					strPrayerCB.setDisable(false);
					defPrayerCB.setDisable(false);
					magPrayerCB.setDisable(false);
					rngPrayerCB.setDisable(false);
					
					atkPrayerBonus = 0.0f;
					strPrayerBonus = 0.0f;
					defPrayerBonus = 0.0f;
					magPrayerBonus = 0.0f;
					magDefenseBonus = 0.0f;
					rngPrayerBonus = 0.0f;
					rngStrengthBonus = 0.0f;
				}
				
				
				buffedAtkLvlLabel.setText((int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus)) + "/" + atkLvl);	
				buffedStrLvlLabel.setText((int)((strLvl + strPotionBonus)*(1 + strPrayerBonus)) + "/" + strLvl);
				buffedDefLvlLabel.setText((int)((defLvl + defPotionBonus)*(1 + defPrayerBonus)) + "/" + defLvl);
				buffedMagLvlLabel.setText((int)((magLvl + magPotionBonus)*(1 + magPrayerBonus)) + "/" + magLvl);
				buffedRngLvlLabel.setText((int)((rngLvl + rngPotionBonus)*(1 + rngPrayerBonus)) + "/" + rngLvl);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		});
		
		container.add(atkPotCB, 3, 1);
		container.add(strPotCB, 3, 2);
		container.add(defPotCB, 3, 3);
		container.add(magPotCB, 3, 4);
		container.add(rngPotCB, 3, 5);
		
		LimitedTextField atkInputBox = createSkillInputBox();
		atkInputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			
			String text = atkInputBox.getText();
			
			if(text != null && !text.equals("") && atkPotCB.getValue() != null)
			{
				atkLvl = Integer.parseInt(text);
				atkPotionBonus = atkPotCB.getValue().calculateBonus(atkLvl);
				buffedAtkLvlLabel.setText((int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus)) + "/" + atkLvl);
			}
		});
		atkInputBox.setIntegersOnly(true);
		atkInputBox.setText("99");
		
		LimitedTextField strInputBox = createSkillInputBox();
		strInputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			String text = strInputBox.getText();
			
			if(text != null && ! text.equals(""))
			{
				strLvl = Integer.parseInt(text);
				strPotionBonus = strPotCB.getValue().calculateBonus(strLvl);
				buffedStrLvlLabel.setText((int)((strLvl + strPotionBonus)*(1 + strPrayerBonus)) + "/" + strLvl);
			}
		});
		strInputBox.setIntegersOnly(true);
		strInputBox.setText("99");
		
		LimitedTextField defInputBox = createSkillInputBox();
		defInputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			String text = defInputBox.getText();
			
			if(text != null && ! text.equals(""))
			{
				defLvl = Integer.parseInt(text);
				defPotionBonus = defPotCB.getValue().calculateBonus(defLvl);
				buffedDefLvlLabel.setText((int)((defLvl + defPotionBonus)*(1 + defPrayerBonus)) + "/" + defLvl);
			}
		});
		defInputBox.setIntegersOnly(true);
		defInputBox.setText("99");
		
		LimitedTextField magInputBox = createSkillInputBox();
		magInputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			String text = magInputBox.getText();
			
			if(text != null && ! text.equals(""))
			{
				magLvl = Integer.parseInt(text);
				magPotionBonus = magPotCB.getValue().calculateBonus(magLvl);
				buffedMagLvlLabel.setText((int)((magLvl + magPotionBonus)*(1 + magPrayerBonus)) + "/" + magLvl);
			}
		});
		magInputBox.setIntegersOnly(true);
		magInputBox.setText("99");
		
		LimitedTextField rngInputBox = createSkillInputBox();
		rngInputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			String text = rngInputBox.getText();
			
			if(text != null && ! text.equals(""))
			{
				rngLvl = Integer.parseInt(text);
				rngPotionBonus = rngPotCB.getValue().calculateBonus(rngLvl);
				buffedRngLvlLabel.setText((int)((rngLvl + rngPotionBonus)*(1 + rngPrayerBonus)) + "/" + rngLvl);
			}
		});
		rngInputBox.setIntegersOnly(true);
		rngInputBox.setText("99");
		
		LimitedTextField hpInputBox = createSkillInputBox();
		hpInputBox.setIntegersOnly(true);
		hpInputBox.setText("99");
		
		LimitedTextField prayerInputBox = createSkillInputBox();
		prayerInputBox.setIntegersOnly(true);
		prayerInputBox.setText("99");
		
		container.add(atkInputBox, 2, 1);
		container.add(strInputBox, 2, 2);
		container.add(defInputBox, 2, 3);
		container.add(magInputBox, 2, 4);
		container.add(rngInputBox, 2, 5);
		container.add(hpInputBox, 2, 6);
		container.add(prayerInputBox, 2, 7);

		
		int equipmentCol = 8;
		// Add equipment labels to the GridPane
		for(int i = 0; i < comboBoxLabels.size(); i++)
		{
			comboBoxLabels.get(i).setTextFill(Color.YELLOW);
			comboBoxLabels.get(i).setFont(Font.font("Runescape UF", 18));
			GridPane.setHalignment(comboBoxLabels.get(i), HPos.RIGHT);
			container.add(comboBoxLabels.get(i), equipmentCol - 1, i + 1);
		}
		
		// Add ComboBoxes to the equipment pane
		container.add(weaponCB, equipmentCol, 1);
		container.add(combatStyleCB, equipmentCol, 2);
		container.add(ammoCB, equipmentCol, 4);
		container.add(headCB, equipmentCol, 5);
		container.add(capeCB, equipmentCol, 6);
		container.add(amuletCB, equipmentCol, 7);
		container.add(chestCB, equipmentCol, 8);
		container.add(legCB, equipmentCol, 9);
		container.add(shieldCB, equipmentCol, 10);
		container.add(gloveCB, equipmentCol, 11);
		container.add(bootCB, equipmentCol, 12);
		container.add(ringCB, equipmentCol, 13);
		
		// Make each column a percentage of the window width
		double[] columnPercentages = new double[] {2.5, 6, 2.75, 9, 2.75, 7, 7, 8, 15, 5, 15, 2.5, 6, 2.5, 6};
		ColumnConstraints[] colConstraints = new ColumnConstraints[columnPercentages.length];
		
		for(int i = 0; i < colConstraints.length; i++)
		{
			colConstraints[i] = new ColumnConstraints();
			colConstraints[i].setPercentWidth(columnPercentages[i]);
		}
		
		container.getColumnConstraints().addAll(colConstraints);
		
		
		
		
		/*** DEBUG ONLY REMOVE LATER ***/
		//container.setGridLinesVisible(true);
		
		Label enemyLabel = createRunescapeLabelText("Enemy", 16, HPos.CENTER);
		container.add(enemyLabel, equipmentCol + 1, 1);
		container.add(enemyCB, equipmentCol + 2, 1);
		
		Button calculateButton = new Button("Calculate");
		
		Alert errorBox = new Alert(AlertType.ERROR);
		errorBox.setHeaderText(null);
		
		calculateButton.setOnAction(e -> {
			try
			{
				if(weaponCB.getValue() != null)
				{
					currentSet.setWeapon(weaponCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in WEAPON slot");
				}
				
				if(combatStyleCB.getValue() != null)
				{
					currentSet.setCombatStyle(combatStyleCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in COMBAT STYLE slot");
				}
				
				if(ammoCB.getValue() != null)
				{
					currentSet.setAmmo(ammoCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in AMMO slot");
				}
				
				if(headCB.getValue() != null)
				{
					currentSet.setHead(headCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in HEAD slot");
				}
				
				if(capeCB.getValue() != null)
				{
					currentSet.setCape(capeCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in CAPE slot");
				}
				
				if(amuletCB.getValue() != null)
				{
					currentSet.setAmulet(amuletCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in AMULET slot");
				}
				
				if(chestCB.getValue() != null)
				{
					currentSet.setChest(chestCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in CHEST slot");
				}
				
				if(legCB.getValue() != null)
				{
					currentSet.setLegs(legCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in LEGS slot");
				}
				
				if(shieldCB.getValue() != null)
				{
					// Even if the weapon is 2H, the shieldCB box is guaranteed to be
					// set to 'None'
					currentSet.setShield(shieldCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in SHIELD slot");
				}
				
				if(gloveCB.getValue() != null)
				{
					currentSet.setGloves(gloveCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in GLOVES slot");
				}
				
				if(bootCB.getValue() != null)
				{
					currentSet.setBoots(bootCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in BOOTS slot");
				}
				
				if(ringCB.getValue() != null)
				{
					currentSet.setRing(ringCB.getValue());
				}
				else
				{
					throw new IllegalArgumentException("Invalid selection in RING slot");
				}
				
			}
			catch(IllegalArgumentException err)
			{
				errorBox.setContentText(err.getMessage());
				errorBox.showAndWait();
			}

		});
		
		GridPane.setHalignment(calculateButton, HPos.CENTER);
		container.add(calculateButton, 10, 12);
		
		return container;
	}
	
	private static void initLabels()
	{
		comboBoxLabels = new ArrayList<>();
		
		comboBoxLabels.add(new Label("Weapon"));
		comboBoxLabels.add(new Label("Combat"));
		comboBoxLabels.add(new Label("Spell"));
		comboBoxLabels.add(new Label("Ammo"));
		comboBoxLabels.add(new Label("Head"));
		comboBoxLabels.add(new Label("Cape"));
		comboBoxLabels.add(new Label("Amulet"));
		comboBoxLabels.add(new Label("Chest"));
		comboBoxLabels.add(new Label("Legs"));
		comboBoxLabels.add(new Label("Shield"));
		comboBoxLabels.add(new Label("Gloves"));
		comboBoxLabels.add(new Label("Boots"));
		comboBoxLabels.add(new Label("Ring"));

		for(Label l : comboBoxLabels)
		{
			l.setPadding(new Insets(10, 10, 10, 10));
		}
		
	}
		
	private static void initItems()
	{		
		List<List<String>> itemData = readFile("res/data/" + WEAPONS_FILE);
		
		weapons = new ArrayList<>();
		
		for(int i = 1; i < itemData.size(); i++)
		{
			weapons.add(new Weapon(itemData.get(i)));
		}
		
		
		
		itemData = readFile("res/data/" + AMMO_FILE);
		
		ammo = new ArrayList<>();
		
		for(int i = 1; i < itemData.size(); i++)
		{
			ammo.add(new Weapon(itemData.get(i)));
		}
		
		heads = new ArrayList<>();
		capes = new ArrayList<>();
		amulets = new ArrayList<>();
		chests = new ArrayList<>();
		legs = new ArrayList<>();
		shields = new ArrayList<>();
		gloves = new ArrayList<>();
		boots = new ArrayList<>();
		rings = new ArrayList<>();
		
		initArmorList(heads, HELMETS_FILE);
		initArmorList(capes, CAPES_FILE);
		initArmorList(amulets, NECKLACES_FILE);
		initArmorList(chests, CHESTPIECES_FILE);
		initArmorList(legs, LEGS_FILE);
		initArmorList(shields, OFFHANDS_FILE);
		initArmorList(gloves, GLOVES_FILE);
		initArmorList(boots, BOOTS_FILE);
		initArmorList(rings, RINGS_FILE);
	}
		
	private static void initEnemies()
	{
		List<List<String>> data = readFile("res/data/" + ENEMIES_FILE);
		
		enemies = new ArrayList<>();
		
		for(int i = 1; i < data.size(); i++)
		{
			enemies.add(new Enemy(data.get(i)));
		}
	}
	
	private static void initArmorList(List<Armor> input, String filename)
	{
		List<List<String>> itemData = readFile("res/data/" + filename);
		
		for(int i = 1; i < itemData.size(); i++)
		{
			input.add(new Armor(itemData.get(i)));
		}
	}
	
	private static <E> ComboBox<E> createAutoCompleteComboBox(List<E> items)
	{
		ComboBox<E> cb = new ComboBox<>();
		cb.setTooltip(new Tooltip());
		cb.getItems().addAll(items);
		cb.setPrefWidth(PREF_COMBOBOX_WIDTH);
		cb.getSelectionModel().selectFirst();
		GridPane.setHalignment(cb, HPos.CENTER);
		new ComboBoxAutoComplete<>(cb);
		
		return cb;
	}

	private static LimitedTextField createSkillInputBox()
	{
		LimitedTextField ltf = new LimitedTextField();		
		ltf.setMaxLength(2);
		ltf.setPrefWidth(PREF_TEXTFIELD_WIDTH);

		return ltf;
	}
	
	@SuppressWarnings("unused")
	private static ComboBox<String> createStringComboBox(String[] elements)
	{
		ComboBox<String> cb = new ComboBox<>();
		cb.getItems().addAll(elements);
		cb.getSelectionModel().selectFirst();
		
		return cb;
	}
	
	private static ImageView createImageView(String url)
	{
		return new ImageView(new Image(new File(url).toURI().toString()));
	}
	
	private static ImageView createImageView(String url, HPos alignment)
	{
		ImageView iv = new ImageView(new Image(new File(url).toURI().toString()));
		GridPane.setHalignment(iv, alignment);
		return iv;
	}

	@SuppressWarnings("unused")
	private static Label createRunescapeLabelText(String text, double fontSize)
	{
		Label rsLabel = new Label(text);
		rsLabel.setFont(Font.font("Runescape UF", fontSize));
		rsLabel.setTextFill(Color.YELLOW);
		
		return rsLabel;
	}
	
	private static Label createRunescapeLabelText(String text, double fontSize, HPos alignment)
	{
		Label rsLabel = new Label(text);
		rsLabel.setFont(Font.font("Runescape UF", fontSize));
		rsLabel.setTextFill(Color.YELLOW);
		GridPane.setHalignment(rsLabel, alignment);
		
		return rsLabel;
	}
	
	
	
	// Main & Utility Functions
	
	public static void main(String[] args)
	{
		currentSet = new EquipmentSet();
		
		initItems();
		initEnemies();
		initLabels();
		Application.launch(args);
	}
	
	private static List<List<String>> readFile(String filename)
	{
		List<List<String>> entries = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			String line;
			while((line = br.readLine()) != null)
			{
				String[] values = line.split(",");
				
				for(int i = 0; i < values.length; i++)
				{
					if(values[i].equals(""))
					{
						values[i] = "0";
					}
				}
				
				entries.add(Arrays.asList(values));
			}
			
			br.close();
			return entries;
		}
		catch(Exception e)
		{
			System.err.println("***Error in Main.java***");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean isNumeric(String str)
	{
		if(str == null)
		{
			return false;
		}
		
		try
		{
			Integer.parseInt(str);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		
		return true;
	}

}
