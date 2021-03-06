/*
 * OSRS DPS Calculator
 * 
 * Author:			   CMillions
 * Project Start Date: 7/1/2020
 *
 * Standalone version of the Google Sheets DPS calculator found here:
 *     https://docs.google.com/spreadsheets/d/1nUgqaadPFRj8UvgvlXe2UkDDcIbQ2crTGjo57GhPm4M/edit
 *     
 * Big thanks to Bitterkoekje for the Google Sheets calculator.
 * 
 * Most of the layout is a pain in the ass to do since it's positioned by hand
 *
 */

package calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import item.Equipment;
import item.Item;
import item.Stance;
import item.Weapon;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.ComboBoxAutoComplete;
import utils.LimitedTextField;


public class Main extends Application
{
	// Filenames that contain item data
	final static String ITEMS_FILE	  = "items-filtered.json";
	final static String ENEMIES_FILE  = "monsters-filtered.json";
	
	// URLs for icons
	final static String PROGRAM_ICON_URL  = "/images/program_icon.png";
	final static String ATTACK_ICON_URL   = "/images/attack_icon.png";
	final static String STRENGTH_ICON_URL = "/images/strength_icon.png";
	final static String DEFENSE_ICON_URL  = "/images/defense_icon.png";
	final static String MAGIC_ICON_URL    = "/images/magic_icon.png";
	final static String RANGE_ICON_URL    = "/images/ranged_icon.png";
	final static String HP_ICON_URL       = "/images/hp_icon.png";
	final static String PRAYER_ICON_URL   = "/images/prayer_icon.png";
	final static String STATS_ICON_URL    = "/images/stats_icon.png";
	final static String POTION_ICON_URL	  = "/images/potion_icon.png";
	final static String OVERLOAD_ICON_URL = "/images/raids_overload_icon.png";
	final static String MULTI_PRAYER_ICON_URL = "/images/multi_prayer_icons.png";
	
	final static String STAB_ICON_URL = "/images/stab_defense_icon.png";
	final static String SLASH_ICON_URL = "/images/slash_defense_icon.png";
	final static String CRUSH_ICON_URL = "/images/crush_defense_icon.png";
	final static String MAGIC_DEFENSE_ICON_URL = "/images/magic_defense_icon.png";
	final static String RANGE_DEFENSE_ICON_URL = "/images/range_defense_icon.png";
	
	final static String RANGE_STRENGTH_ICON_URL = "/images/range_strength_icon.png";
	final static String MAGIC_STRENGTH_ICON_URL = "/images/magic_strength_icon.png";
	
	// JavaFX Constants
	final static int PREF_COMBOBOX_WIDTH = 200;
	final static int PREF_COMBOBOX_HEIGHT = 20;
	final static int PREF_TEXTFIELD_WIDTH = 1;
	
	final static String BG_COLOR = "0x606060";
	
	// Other variables
	private static List<Item> weaponsList;
	private static List<Item> ammoList;
	private static List<Item> dartList;
	private static List<Item> headsList;
	private static List<Item> capesList;
	private static List<Item> amuletsList;
	private static List<Item> chestsList;
	private static List<Item> legsList;
	private static List<Item> shieldsList;
	private static List<Item> glovesList;
	private static List<Item> bootsList;
	private static List<Item> ringsList;
	private static List<Enemy> enemiesList;
	
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
	private static int atkStyleBonus;
	private static int strStyleBonus;
	
	// This depends on the selected stance
	private static int effectiveEquipmentBonus;
	
	// Any effectiveLevel = floor((lvl + PotionBonus) * PrayerBonus * OtherBonus) + StyleBonus
	// OtherBonus is like slayer, salve, etc
	private static int effectiveStrengthLevel;
	private static int effectiveAttackLevel;
	private static int maxHit;
	private static int maxAttackRoll;
	private static int maxDefenseRoll;
	private static double accuracy;
	private static double dps;
	
	
	private static Label maxHitLabel;
	private static Label numberMaxHitLabel;
	
	private static Label dpsLabel;
	private static Label numberDpsLabel;

	private static Label accuracyLabel;
	private static Label numberAccuracyLabel;
	
	private static ComboBox<Item> weaponCB;
	private static ComboBox<Item> ammoCB;
	private static ComboBox<Item> dartCB;
	private static ComboBox<Item> headCB;
	private static ComboBox<Item> capeCB;
	private static ComboBox<Item> amuletCB;
	private static ComboBox<Item> chestCB;
	private static ComboBox<Item> legsCB;
	private static ComboBox<Item> shieldCB;
	private static ComboBox<Item> glovesCB;
	private static ComboBox<Item> bootsCB;
	private static ComboBox<Item> ringCB;
	
	private static ComboBox<Stance> combatStyleCB;
	
	private static ComboBox<Enemy> enemyCB;
	
	static boolean showBlowpipeAlert = true;
	static boolean blowpipeEquipped = false;
	
	// JavaFX Related Functions
	static ProgressBar pb;
	
	@Override
	public void start(Stage primaryStage) throws InterruptedException
	{	
		primaryStage.setWidth(WINDOW_WIDTH);
		primaryStage.setHeight(WINDOW_HEIGHT);
		primaryStage.setTitle("OSRS DPS Calculator");
		primaryStage.getIcons().add(new Image(Main.class.getResource(PROGRAM_ICON_URL).toExternalForm()));
		primaryStage.setResizable(false);
		primaryStage.show();

		pb = new ProgressBar();
		BorderPane pane = new BorderPane();
		pane.setCenter(pb);
		
		try
		{
			initItems();
			initEnemies();
			initLabels();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		
		Scene mainScene = new Scene(createLayoutPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
		primaryStage.setScene(mainScene);
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
		
		int importantTextSize = 20;
		
		maxHitLabel = createRunescapeLabelText("Max hit: ", importantTextSize, HPos.LEFT);
		container.add(maxHitLabel, 3, 10);
		
		numberMaxHitLabel = createRunescapeLabelText(null, importantTextSize, HPos.RIGHT);
		container.add(numberMaxHitLabel, 3, 10);
		
		accuracyLabel = createRunescapeLabelText("Accuracy: ", importantTextSize, HPos.LEFT);
		container.add(accuracyLabel, 3, 11);
		
		numberAccuracyLabel = createRunescapeLabelText(null, importantTextSize, HPos.RIGHT);
		container.add(numberAccuracyLabel, 3, 11);
		
		dpsLabel = createRunescapeLabelText("DPS: ", importantTextSize, HPos.LEFT);
		container.add(dpsLabel, 3, 12);
		
		numberDpsLabel = createRunescapeLabelText(null, importantTextSize, HPos.RIGHT);
		container.add(numberDpsLabel, 3, 12);
		
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
		Label monsterCombatStatsLabel = createRunescapeLabelText("Combat Stats", 16, HPos.CENTER);
        GridPane.setColumnSpan(monsterCombatStatsLabel, 5);
        container.add(monsterCombatStatsLabel, 11, 0);
        container.add(createImageView(ATTACK_ICON_URL, HPos.CENTER), 11, 1);
        container.add(createImageView(STRENGTH_ICON_URL, HPos.CENTER), 12, 1);
        container.add(createImageView(DEFENSE_ICON_URL, HPos.CENTER), 13, 1);
        container.add(createImageView(MAGIC_ICON_URL, HPos.CENTER), 14, 1);
        container.add(createImageView(RANGE_ICON_URL, HPos.CENTER), 15, 1);
        //container.add(createImageView(HP_ICON_URL, HPos.CENTER), 11, 6);
		
        Label monsterAttackBonusLabel = createRunescapeLabelText("Attack Bonuses", 16, HPos.CENTER);
        container.add(monsterAttackBonusLabel, 11, 3);
        GridPane.setColumnSpan(monsterAttackBonusLabel, 5);
        
        enemyCB = createAutoCompleteComboBox(enemiesList);
		enemyCB.getSelectionModel().selectFirst();
        
		Enemy defaultEnemy = enemyCB.getValue();
		
        Label monsterAtk = createRunescapeLabelText(defaultEnemy.atkLvl + "", 16, HPos.CENTER);
        container.add(monsterAtk, 11, 2);
        Label monsterStr = createRunescapeLabelText(defaultEnemy.strLvl + "", 16, HPos.CENTER);
        container.add(monsterStr, 12, 2);
        Label monsterDef = createRunescapeLabelText(defaultEnemy.defLvl + "", 16, HPos.CENTER);
        container.add(monsterDef, 13, 2);
        Label monsterMag = createRunescapeLabelText(defaultEnemy.magLvl + "", 16, HPos.CENTER);
        container.add(monsterMag, 14, 2);
        Label monsterRng = createRunescapeLabelText(defaultEnemy.rngLvl + "", 16, HPos.CENTER);
        container.add(monsterRng, 15, 2);
        /*Label monsterHP = createRunescapeLabelText(enemyCB.getValue().hitpoints + "", 16, HPos.CENTER);
        container.add(monsterHP, 12, 6);*/
        
        container.add(createImageView(STAB_ICON_URL,  HPos.CENTER), 11, 4);
        container.add(createImageView(SLASH_ICON_URL, HPos.CENTER), 12, 4);
        container.add(createImageView(CRUSH_ICON_URL, HPos.CENTER), 13, 4);
        container.add(createImageView(MAGIC_ICON_URL, HPos.CENTER), 14, 4);
        container.add(createImageView(RANGE_ICON_URL, HPos.CENTER), 15, 4);
        
        Label monsterStabAtk = createRunescapeLabelText(defaultEnemy.stabAtk + "", 16, HPos.CENTER);
        container.add(monsterStabAtk, 11, 5);
        Label monsterSlashAtk = createRunescapeLabelText(defaultEnemy.slashAtk + "", 16, HPos.CENTER);
        container.add(monsterSlashAtk, 12, 5);
        Label monsterCrushAtk = createRunescapeLabelText(defaultEnemy.crushAtk + "", 16, HPos.CENTER);
        container.add(monsterCrushAtk, 13, 5);
        Label monsterMagAtk = createRunescapeLabelText(defaultEnemy.magAtk + "", 16, HPos.CENTER);
        container.add(monsterMagAtk, 14, 5);
        Label monsterRngAtk = createRunescapeLabelText(defaultEnemy.rngAtk + "", 16, HPos.CENTER);
        container.add(monsterRngAtk, 15, 5);
		
        Label monsterDefenseBonusLabel = createRunescapeLabelText("Defense Bonuses", 16, HPos.CENTER);
        container.add(monsterDefenseBonusLabel, 11, 6);
        GridPane.setColumnSpan(monsterDefenseBonusLabel, 5);
        
        container.add(createImageView(SLASH_ICON_URL, HPos.CENTER), 11, 7);
        container.add(createImageView(STAB_ICON_URL, HPos.CENTER), 12, 7);
        container.add(createImageView(CRUSH_ICON_URL, HPos.CENTER), 13, 7);
        container.add(createImageView(MAGIC_DEFENSE_ICON_URL, HPos.CENTER), 14, 7);
        container.add(createImageView(RANGE_DEFENSE_ICON_URL, HPos.CENTER), 15, 7);
        
        Label monsterStabDef = createRunescapeLabelText(defaultEnemy.stabDef + "", 16, HPos.CENTER);
        container.add(monsterStabDef, 11, 8);
        Label monsterSlashDef = createRunescapeLabelText(defaultEnemy.slashDef + "", 16, HPos.CENTER);
        container.add(monsterSlashDef, 12, 8);
        Label monsterCrushDef = createRunescapeLabelText(defaultEnemy.crushDef + "", 16, HPos.CENTER);
        container.add(monsterCrushDef, 13, 8);
        Label monsterMagDef = createRunescapeLabelText(defaultEnemy.magDef + "", 16, HPos.CENTER);
        container.add(monsterMagDef, 14, 8);
        Label monsterRngDef = createRunescapeLabelText(defaultEnemy.rngDef + "", 16, HPos.CENTER);
        container.add(monsterRngDef, 15, 8);
        
        Label otherBonusLabel = createRunescapeLabelText("Other Bonuses", 16, HPos.CENTER);
        container.add(otherBonusLabel, 11, 9);
        GridPane.setColumnSpan(otherBonusLabel, 5);
        
        container.add(createImageView(STRENGTH_ICON_URL, HPos.CENTER), 11, 10);
        container.add(createImageView(RANGE_STRENGTH_ICON_URL, HPos.CENTER), 12, 10);
        container.add(createImageView(MAGIC_STRENGTH_ICON_URL, HPos.CENTER), 13, 10);
        
        
        Label monsterStrBonus = createRunescapeLabelText(defaultEnemy.meleeStr + "", 16, HPos.CENTER);
        container.add(monsterStrBonus, 11, 11);
        Label monsterRngStrBonus = createRunescapeLabelText(defaultEnemy.rngStr + "", 16, HPos.CENTER);
        container.add(monsterRngStrBonus, 12, 11);
        Label monsterMagDamageBonus = createRunescapeLabelText(defaultEnemy.magDamage + "", 16, HPos.CENTER);
        container.add(monsterMagDamageBonus, 13, 11);
        
		weaponCB = createAutoCompleteComboBox(weaponsList);
		currentSet.equip(weaponCB.getValue());
		shieldCB = createAutoCompleteComboBox(shieldsList);
		currentSet.equip(shieldCB.getValue());
		
		shieldCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			 
			if(oldValue != null && newValue != null && !oldValue.equals(newValue))
			{
				Item selected = shieldCB.getValue();
				
				if(selected != null)
				{
					currentSet.equip(selected);
					calculateDPS();
				}
			}
		});
		
		combatStyleCB = new ComboBox<>();
		combatStyleCB.setItems(
				FXCollections.observableArrayList(
						weaponCB.getSelectionModel().getSelectedItem().getWeaponStats().getStances()
						)
				);
		combatStyleCB.getSelectionModel().selectFirst();
		combatStyleCB.setPrefWidth(PREF_COMBOBOX_WIDTH);
		combatStyleCB.setOnAction(e -> {
			Stance selected = combatStyleCB.getValue();
		
			if(selected != null)
			{	
				if(selected.attackStyle != null)
				{
					switch(selected.attackStyle.toLowerCase())
					{
					case "accurate":
						atkStyleBonus = 3;
						strStyleBonus = 0;
						break;
						
					case "aggressive":
						atkStyleBonus = 0;
						strStyleBonus = 3;
						break;
						
					case "controlled":
						atkStyleBonus = 1;
						strStyleBonus = 1;
						break;
						
					default:
						break;
					}
				}
								
				calculateDPS();
			}
		});
		
		// Run once to make default values ok
		Stance defaultStance = combatStyleCB.getValue();
		
		if(defaultStance != null)
		{			
			switch(defaultStance.attackStyle.toLowerCase())
			{
			case "accurate":
				atkStyleBonus = 3;
				strStyleBonus = 0;
				break;
				
			case "aggressive":
				atkStyleBonus = 0;
				strStyleBonus = 3;
				break;
				
			case "controlled":
				atkStyleBonus = 1;
				strStyleBonus = 1;
				break;
				
			default:
				break;
			}
		}
			
		
		GridPane.setHalignment(combatStyleCB, HPos.CENTER);
		
		
		Alert infoBox = new Alert(AlertType.INFORMATION);
		infoBox.setTitle("Information");
		
		
		// Do things when item is selected
		weaponCB.setOnAction(e -> {
			Item w = weaponCB.getValue();
			
			if(w != null)
			{	
				// Disable shields ComboBox if weapon is two handed
				if(w.itemStats.slot.equals("2h"))
				{
					shieldCB.setDisable(true);
					shieldCB.getSelectionModel().selectFirst();
					currentSet.equip(shieldCB.getValue());
				}
				else
				{
					shieldCB.setDisable(false);
				}
				
				if(!blowpipeEquipped && w.getName().equalsIgnoreCase("toxic blowpipe"))
				{
					
					if(showBlowpipeAlert)
					{
						infoBox.setHeaderText("Toxic Blowpipe Ammo");
						infoBox.setContentText("Blowpipe ammo is selected in the 'ammo' dropdown list. This message will " + 
											   "not be shown again.");
						infoBox.show();
						
						showBlowpipeAlert = false;
					}
					
					
					blowpipeEquipped = true;
					
					ammoCB.getSelectionModel().clearSelection();
					ammoCB.setDisable(true);
					ammoCB.setVisible(false);
					
					dartCB.getSelectionModel().selectFirst();
					dartCB.setDisable(false);
					dartCB.setVisible(true);
					
				}
				else if(blowpipeEquipped && !w.getName().equalsIgnoreCase("toxic blowpipe"))
				{
					blowpipeEquipped = false;
					
					dartCB.getSelectionModel().clearSelection();
					dartCB.setDisable(true);
					dartCB.setVisible(false);
					
					ammoCB.getSelectionModel().selectFirst();
					ammoCB.setDisable(false);
					ammoCB.setVisible(true);
					
				}

				// Update combat style
				combatStyleCB.setItems(FXCollections.observableArrayList(w.weaponStats.stances));
				combatStyleCB.getSelectionModel().selectFirst();
				
				// Update current equipment set
				currentSet.equip(w);
				calculateDPS();
			}
		});

		
		ammoCB = createAutoCompleteComboBox(ammoList);
		currentSet.equip(ammoCB.getValue());
		ammoCB.setOnAction(e -> {
			Item selected = ammoCB.getValue();
			
			if(selected != null && !blowpipeEquipped)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		dartCB = createAutoCompleteComboBox(dartList);
		dartCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			
			if(newValue != null && oldValue != null)
			{
				if(!oldValue.getName().equalsIgnoreCase(newValue.getName()))
				{
					System.out.println("breh");
				}
			}
			
			
		});
		
		dartCB.setDisable(true);
		dartCB.setVisible(false);
		
		
		headCB = createAutoCompleteComboBox(headsList);
		currentSet.equip(headCB.getValue());
		headCB.setOnAction(e -> {
			Item selected = headCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		capeCB = createAutoCompleteComboBox(capesList);
		currentSet.equip(capeCB.getValue());
		capeCB.setOnAction(e -> {
			Item selected = capeCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		amuletCB = createAutoCompleteComboBox(amuletsList);
		currentSet.equip(amuletCB.getValue());
		amuletCB.setOnAction(e -> {
			Item selected = amuletCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		chestCB = createAutoCompleteComboBox(chestsList);
		currentSet.equip(chestCB.getValue());
		chestCB.setOnAction(e -> {
			Item selected = chestCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		legsCB = createAutoCompleteComboBox(legsList);
		currentSet.equip(legsCB.getValue());
		legsCB.setOnAction(e -> {
			Item selected = legsCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		glovesCB = createAutoCompleteComboBox(glovesList);
		currentSet.equip(glovesCB.getValue());
		glovesCB.setOnAction(e -> {
			Item selected = glovesCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		bootsCB = createAutoCompleteComboBox(bootsList);
		currentSet.equip(bootsCB.getValue());
		bootsCB.setOnAction(e -> {
			Item selected = bootsCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
			}
		});
		
		
		ringCB = createAutoCompleteComboBox(ringsList);
		currentSet.equip(ringCB.getValue());
		ringCB.setOnAction(e -> {
			Item selected = ringCB.getValue();
			
			if(selected != null)
			{
				currentSet.equip(selected);
				calculateDPS();
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
				
				monsterStabAtk.setText(selected.stabAtk + "");
				monsterSlashAtk.setText(selected.slashAtk + "");
				monsterCrushAtk.setText(selected.crushAtk + "");
				monsterMagAtk.setText(selected.magAtk + "");
				monsterRngAtk.setText(selected.rngAtk + "");
				
				monsterStabDef.setText(selected.stabDef + "");
				monsterSlashDef.setText(selected.slashDef + "");
				monsterCrushDef.setText(selected.crushDef + "");
				monsterMagDef.setText(selected.magDef + "");
				monsterRngDef.setText(selected.rngDef + "");
				
				monsterStrBonus.setText(selected.meleeStr + "");
				monsterRngStrBonus.setText(selected.rngStr + "");
				monsterMagDamageBonus.setText(selected.magDamage + "");
				
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				
				calculateDPS();
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
				
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
		
		// CSS testing
		//multiStatPrayerCB.getStylesheets().add(Main.class.getResource("/stylesheets/combobox.css").toExternalForm());
		
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
				calculateDPS();
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
			container.add(comboBoxLabels.get(i), equipmentCol - 1, i + 1);
		}
		
		// Add ComboBoxes to the equipment pane
		container.add(weaponCB, equipmentCol, 1);
		container.add(combatStyleCB, equipmentCol, 2);
		container.add(ammoCB, equipmentCol, 4);
		container.add(dartCB, equipmentCol, 4);
		container.add(headCB, equipmentCol, 5);
		container.add(capeCB, equipmentCol, 6);
		container.add(amuletCB, equipmentCol, 7);
		container.add(chestCB, equipmentCol, 8);
		container.add(legsCB, equipmentCol, 9);
		container.add(shieldCB, equipmentCol, 10);
		container.add(glovesCB, equipmentCol, 11);
		container.add(bootsCB, equipmentCol, 12);
		container.add(ringCB, equipmentCol, 13);
		
		// Make each column a percentage of the window width
		double[] columnPercentages = new double[] {2.5, 6, 2.75, 9, 2.75, 7, 7, 8, 15, 5, 15, 2.5, 2.5, 2.5, 2.5, 2.5};
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
		
		return container;
	}
	
	private static void initLabels()
	{
		comboBoxLabels = new ArrayList<>();		
		
		String[] equipmentText = new String[] {"Weapon", "Combat", "Spell", "Ammo", 
											   "Head", "Cape", "Amulet", "Chest", "Legs",
											   "Shield", "Gloves", "Boots", "Ring"
											  };
		
		for(String s : equipmentText)
		{
			comboBoxLabels.add(createRunescapeLabelText(s, 16, HPos.CENTER));
		}
		
		for(Label l : comboBoxLabels)
		{
			l.setPadding(new Insets(10, 10, 10, 10));
		}
		
	}
		
	private static void initItems()
	{		
		weaponsList = new ArrayList<>();
		ammoList = new ArrayList<>();
		dartList = new ArrayList<>();
		headsList = new ArrayList<>();
		capesList = new ArrayList<>();
		amuletsList = new ArrayList<>();
		chestsList = new ArrayList<>();
		legsList = new ArrayList<>();
		shieldsList = new ArrayList<>();
		glovesList = new ArrayList<>();
		bootsList = new ArrayList<>();
		ringsList = new ArrayList<>();
		
	
		String jsonData;
		try 
		{
			jsonData = readFile("res/data/" + ITEMS_FILE);
			
			Type type = new TypeToken<HashMap<Integer, Item>>(){}.getType();
			HashMap<Integer, Item> jsonMap = new Gson().fromJson(jsonData, type);
			
			for(Map.Entry<Integer, Item> entry : jsonMap.entrySet())
			{
				Item item = entry.getValue();
				
				if(item != null && item.isEquippableByPlayer)
				{	
					String itemSlot = entry.getValue().itemStats.slot.toLowerCase();
					
					/*sb.append("\"" + entry.getKey() + "\":");
					sb.append(new Gson().toJson(item));
					sb.append(",");*/
					
					switch(itemSlot)
					{
					case "2h":
					case "weapon":
						weaponsList.add(item);
						if(item.getName().toLowerCase().contains("dart"))
						{
							dartList.add(item);
						}
						break;
						
					case "ammo":
						ammoList.add(item);
						break;
						
					case "head":
						headsList.add(item);
						break;
						
					case "cape":
						capesList.add(item);
						break;
						
					case "neck":
						amuletsList.add(item);
						break;
						
					case "body":
						chestsList.add(item);
						break;
						
					case "legs":
						legsList.add(item);
						break;
						
					case "shield":
						shieldsList.add(item);
						break;
						
					case "feet":
						bootsList.add(item);
						break;
						
					case "hands":
						glovesList.add(item);
						break;
						
					case "ring":
						ringsList.add(item);
						break;
					}
				}
				else
				{
					entry.setValue(null);
				}
				
			}
			
			/*sb.append("}");
			out.println(sb.toString());
			out.close();*/
					
			List<Stance> unarmedStance = new ArrayList<>();
			unarmedStance.add(new Stance(null, "crush", "accurate"));
			unarmedStance.add(new Stance(null, "crush", "aggressive"));
			unarmedStance.add(new Stance(null, "crush", "defensive"));
			
			// Create 'None' as on option for all slots
			// Attack speed with no weapon equipped is 6
			Item none = new Item("None", true, true,
					new Equipment(0, 0, 0, 0, 0, 
							      0, 0, 0, 0, 0,
							      0, 0, 0, 0, "all"),
					new Weapon(6, "unarmed", unarmedStance));
			
			Collections.sort(weaponsList);
			Collections.sort(ammoList);
			Collections.sort(dartList);
			Collections.sort(headsList);
			Collections.sort(capesList);
			Collections.sort(amuletsList);
			Collections.sort(chestsList);
			Collections.sort(legsList);
			Collections.sort(shieldsList);
			Collections.sort(bootsList);
			Collections.sort(glovesList);
			Collections.sort(ringsList);
			
			weaponsList.add(0, none);
			ammoList.add(0, none);
			headsList.add(0, none);
			capesList.add(0, none);
			amuletsList.add(0, none);
			chestsList.add(0, none);
			legsList.add(0, none);
			shieldsList.add(0, none);
			bootsList.add(0, none);
			glovesList.add(0, none);
			ringsList.add(0, none);
		} 
		catch (FileNotFoundException ex) 
		{
			System.err.println("Could not find " + ITEMS_FILE);
			ex.printStackTrace();
		}
	}
		
	private static void initEnemies() throws FileNotFoundException
	{
		String jsonData = readFile("res/data/" + ENEMIES_FILE);
		
		enemiesList = new ArrayList<>();
		
		// https://howtodoinjava.com/gson/gson-serialize-deserialize-hashmap/
		Type type = new TypeToken<HashMap<Integer, Enemy>>(){}.getType();
		HashMap<Integer, Enemy> jsonMap = new Gson().fromJson(jsonData, type);
		
		// https://stackoverflow.com/questions/20350206/populate-combobox-with-hashmap
		
		for(Map.Entry<Integer, Enemy> entry : jsonMap.entrySet())
		{
			if(!enemiesList.contains(entry.getValue()))
				enemiesList.add(entry.getValue());
				
		}
		
		Collections.sort(enemiesList);
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
	
	private static ImageView createImageView(String url)
	{
		return new ImageView(new Image(Main.class.getResource(url).toExternalForm()));
	}
	
	private static ImageView createImageView(String url, HPos alignment)
	{	
		ImageView iv = new ImageView(new Image(Main.class.getResource(url).toExternalForm()));
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
		
		Application.launch(args);
	}
	
	private static String readFile(String filename) throws FileNotFoundException
	{
		File file = new File(filename);
		Scanner scanner =  new Scanner(file);
		
		StringBuilder output = new StringBuilder();
		
		while(scanner.hasNextLine())
		{
			output.append(scanner.nextLine());
		}
		
		scanner.close();
		
		return output.toString();
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
	
	private static void calculateDPS()
	{
		try
		{
			Enemy enemy = enemyCB.getValue();
			
			effectiveStrengthLevel = (int)((strLvl + strPotionBonus)*(1 + strPrayerBonus) + strStyleBonus + 8);
			maxHit = (int)(0.5 + effectiveStrengthLevel * (currentSet.totalStats.meleeStrength + 64) / 640.0);
			
			effectiveAttackLevel = (int)((atkLvl + atkPotionBonus)*(1 + atkPrayerBonus) + atkStyleBonus + 8);
			
			// Determine equipment bonus based on selected attack style
			maxAttackRoll = effectiveAttackLevel * (effectiveEquipmentBonus + 64);
			
			if(headCB.getValue().name.toLowerCase().contains("slayer helmet") && enemy.isSlayerMonster)
			{
				System.out.println("On task, fighting slayer monster");
				maxAttackRoll *= 7.0/6.0;
				maxHit *= 7.0/6.0;
			}
			
			if(combatStyleCB.getValue().getAttackType() != null)
			{
				String attackType = combatStyleCB.getValue().getAttackType().toLowerCase();
				
				int enemyDefBonus = 0;
	
				switch(attackType)
				{
				case "stab":
					effectiveEquipmentBonus = currentSet.totalStats.stabAttack;
					enemyDefBonus = enemy.stabDef;
					break;
					
				case "slash":
					effectiveEquipmentBonus = currentSet.totalStats.slashAttack;
					enemyDefBonus = enemy.slashDef;
					break;
					
				case "crush":
					effectiveEquipmentBonus = currentSet.totalStats.crushAttack;
					enemyDefBonus = enemy.crushDef;
					break;
				
				case "magic":
					effectiveEquipmentBonus = currentSet.totalStats.magicAttack;
					enemyDefBonus = enemy.magDef;
					break;
					
				case "ranged":
					effectiveEquipmentBonus = currentSet.totalStats.rangedAttack;
					enemyDefBonus = enemy.rngDef;
					break;
					
				default:
					break;
				}
			
				maxDefenseRoll = (enemyCB.getValue().defLvl + 9) * (enemyDefBonus + 64);
			}
			
			
			if(maxAttackRoll > maxDefenseRoll)
			{
				accuracy = 1 - (maxDefenseRoll + 2) / ((double)(2 * (maxAttackRoll + 1.0)));
			}
			else if(maxDefenseRoll > maxAttackRoll)
			{
				accuracy = maxAttackRoll / ((double)(2 * maxDefenseRoll + 1.0));
			}

			dps = accuracy * (maxHit / 2.0) / (weaponCB.getValue().weaponStats.attackSpeed * 0.6);
			
			updateDPSLabels();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static void updateDPSLabels()
	{
		
		numberMaxHitLabel.setText(Integer.toString(maxHit));
		numberAccuracyLabel.setText(Double.toString(accuracy).substring(0, 6));
		numberDpsLabel.setText(Double.toString(dps).substring(0, 6));
	}
}
