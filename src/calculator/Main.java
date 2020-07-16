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
	
	// URLs for skill icons
	final static String ATTACK_ICON_URL   = "res/images/attack_icon.png";
	final static String STRENGTH_ICON_URL = "res/images/strength_icon.png";
	final static String DEFENSE_ICON_URL  = "res/images/defense_icon.png";
	final static String MAGIC_ICON_URL    = "res/images/magic_icon.png";
	final static String RANGE_ICON_URL    = "res/images/ranged_icon.png";
	final static String HP_ICON_URL       = "res/images/hp_icon.png";
	final static String PRAYER_ICON_URL   = "res/images/prayer_icon.png";
	
	// JavaFX Constants
	final static int PREF_COMBOBOX_WIDTH = 225;
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
	
	private static Armor emptyArmor; 
	
	private final static int WINDOW_WIDTH = 1280;
	private final static int WINDOW_HEIGHT = 720;
	
	private static int atkLvl;
	private static int strLvl;
	private static int defLvl;
	private static int magLvl;
	private static int rngLvl;
	private static int hpLvl;
	private static int prayerLvl;
	
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
		
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	private static GridPane createLayoutPane()
	{
		GridPane container = new GridPane();
		container.setPadding(new Insets(20, 20, 20, 20));
		// Center entire GridPane in scene
		container.setAlignment(Pos.CENTER);
		
		Color gray = Color.web(BG_COLOR);
		container.setBackground(new Background(new BackgroundFill(gray, CornerRadii.EMPTY, Insets.EMPTY)));
		
		container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		Label statsLabel = new Label("Stats");
		GridPane.setColumnSpan(statsLabel, 2);
		statsLabel.setFont(Font.font(16));
		statsLabel.setTextFill(Color.WHITE);
		
		GridPane.setHalignment(statsLabel, HPos.CENTER);
		
		LimitedTextField atkInputBox = createSkillInputBox();
		LimitedTextField strInputBox = createSkillInputBox();
		LimitedTextField defInputBox = createSkillInputBox();
		LimitedTextField magInputBox = createSkillInputBox();
		LimitedTextField rngInputBox = createSkillInputBox();
		LimitedTextField hpInputBox = createSkillInputBox();
		LimitedTextField prayerInputBox = createSkillInputBox();
		
		container.add(statsLabel, 0, 0);
		container.add(createImageView(ATTACK_ICON_URL), 0, 1);
		container.add(createImageView(STRENGTH_ICON_URL), 0, 2);
		container.add(createImageView(DEFENSE_ICON_URL), 0, 3);
		container.add(createImageView(MAGIC_ICON_URL), 0, 4);
		container.add(createImageView(RANGE_ICON_URL), 0, 5);
		container.add(createImageView(HP_ICON_URL), 0, 6);
		container.add(createImageView(PRAYER_ICON_URL), 0, 7);
		container.add(atkInputBox, 1, 1);
		container.add(strInputBox, 1, 2);
		container.add(defInputBox, 1, 3);
		container.add(magInputBox, 1, 4);
		container.add(rngInputBox, 1, 5);
		container.add(hpInputBox, 1, 6);
		container.add(prayerInputBox, 1, 7);	

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
				shieldCB.setValue(emptyArmor);
				shieldCB.setDisable(w.isTwoHand);
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
		
		ComboBox<Enemy> enemyCB = createAutoCompleteComboBox(enemies);
		enemyCB.setOnAction(e -> {
			Enemy selected = enemyCB.getValue();
			
			if(selected != null)
			{
				// do stuff
			}
		});
		
		int equipmentCol = 3;
		// Add equipment labels to the GridPane
		for(int i = 0; i < comboBoxLabels.size(); i++)
		{
			comboBoxLabels.get(i).setTextFill(Color.WHITE);
			comboBoxLabels.get(i).setFont(Font.font(14));
			GridPane.setHalignment(comboBoxLabels.get(i), HPos.RIGHT);
			container.add(comboBoxLabels.get(i), equipmentCol - 1, i);
		}
		
		
		// Add ComboBoxes to the equipment pane
		container.add(weaponCB, equipmentCol, 0);
		container.add(combatStyleCB, equipmentCol, 1);
		container.add(ammoCB, equipmentCol, 3);
		container.add(headCB, equipmentCol, 4);
		container.add(capeCB, equipmentCol, 5);
		container.add(amuletCB, equipmentCol, 6);
		container.add(chestCB, equipmentCol, 7);
		container.add(legCB, equipmentCol, 8);
		container.add(shieldCB, equipmentCol, 9);
		container.add(gloveCB, equipmentCol, 10);
		container.add(bootCB, equipmentCol, 11);
		container.add(ringCB, equipmentCol, 12);
		
		// Make each column a percentage of the window width
		ColumnConstraints col1, col2, col3, col4, col5, col6;
		col1 = new ColumnConstraints();
		col2 = new ColumnConstraints();
		col3 = new ColumnConstraints();
		col4 = new ColumnConstraints();
		col5 = new ColumnConstraints();
		col6 = new ColumnConstraints();
		col1.setPercentWidth(2);
		col2.setPercentWidth(3);
		col3.setPercentWidth(6);
		col4.setPercentWidth(20);
		col5.setPercentWidth(20);
		col6.setPercentWidth(20);
		container.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
		
		Label enemyLabel = new Label("Enemy");
		enemyLabel.setAlignment(Pos.CENTER);
		enemyLabel.setTextFill(Color.WHITE);
		enemyLabel.setPadding(new Insets(10, 10, 10, 10));
		
		GridPane.setHalignment(enemyLabel, HPos.CENTER);
		
		/*** DEBUG ONLY REMOVE LATER ***/
		//container.setGridLinesVisible(true);
		
		container.add(enemyLabel, equipmentCol + 1, 0);
		container.add(enemyCB, equipmentCol + 2, 0);
		
		Button calculateButton = new Button("Calculate DPS");
		
		Alert errorBox = new Alert(AlertType.ERROR);
		errorBox.setHeaderText(null);
		
		
		
		calculateButton.setOnAction(e -> {
			String statBoxErrorMsg = "Make sure the stat boxes only have integers";
			try
			{
				
				if(isNumeric(atkInputBox.getText()))
				{
					atkLvl = Integer.parseInt(atkInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
				
				if(isNumeric(strInputBox.getText()))
				{
					strLvl = Integer.parseInt(strInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
				
				if(isNumeric(defInputBox.getText()))
				{
					defLvl = Integer.parseInt(defInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
					
				if(isNumeric(magInputBox.getText()))
				{
					magLvl = Integer.parseInt(magInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
				
				if(isNumeric(rngInputBox.getText()))
				{
					rngLvl = Integer.parseInt(rngInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
				
				if(isNumeric(hpInputBox.getText()))
				{
					hpLvl = Integer.parseInt(hpInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
				
				if(isNumeric(prayerInputBox.getText()))
				{
					prayerLvl = Integer.parseInt(prayerInputBox.getText());
				}
				else
				{
					throw new IllegalArgumentException(statBoxErrorMsg);
				}
						
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
					currentSet.setShield(shieldCB.getValue());
				}
				else if(weaponCB.getValue().isTwoHand)
				{
					currentSet.setShield(emptyArmor);
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
		
		container.add(calculateButton, 4, 12);
		
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
		
		for(Armor a : shields)
		{
			if(a.name.equalsIgnoreCase("none"))
			{
				emptyArmor = a;
				break;
			}
		}
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
		return new ImageView(new Image(new File(url).toURI().toString()));
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
			Integer i = Integer.parseInt(str);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		
		return true;
	}
	

}
