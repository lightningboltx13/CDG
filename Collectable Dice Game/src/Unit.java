import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;



public class Unit extends JPanel implements ActionListener, MouseListener
{
	//Unit represents a Die
	
	String Name;
	char Rarity;
	String Set;
	String[] Face_Names = new String[6];
	String[] Face_Keys = new String[6];
	String[] Face_Discriptions = new String[6];
	String Active_Face;
	int CatalogIndex;
	
	int UnitID;
	int Quantity;
	String ViewMode;
	
	Color L_Clr = new Color(190,190,230);
	Color R_Clr = new Color(180,160,90);
	Color U_Clr = new Color(130,130,130);
	Color C_Clr = new Color(160,130,90);
	
	//This is the Constructor for a Unit_Stat Display Box
	
	JPanel Unit_Box = new JPanel();
	
	JLabel Name_Label = new JLabel(this.Name);
	
	
	//Battle Mode
	JPanel Battle_Pane = new JPanel();
	
	//1 Image
	
	JLabel Active_Display = new JLabel();
	
	JPanel Battle_Btn_Pane = new JPanel();
	JButton Damage_Btn = new JButton("");
	
	
	
	JButton Select_Btn = new JButton("");
	JButton Information_Btn = new JButton("Info");
	
	//View Mode
	JPanel View_Pane = new JPanel();
	
	//6 Images
	JPanel Face_Pane = new JPanel();
	JLabel[] Face_Icons = new JLabel[6];
	
	JButton View_Btn = new JButton();
	
	
	//Selection states of Buttons
	LineBorder Selected = new LineBorder(Color.BLUE, 2);
	LineBorder UnSelected = new LineBorder(Color.BLACK, 2);
	
	
	
	//This is the Constructor for a Die.
	//importing a Die Constructor Hash
	//Name,Rarity,(Faces,)
	public Unit(int ID, int UnitQuantity, String UnitName, char UnitRarity, String[] UnitSides, String[] Keys, String[] Discriptions, int CatalogID) 
	{
		UnitID = ID;
		Quantity = UnitQuantity;
		Name = UnitName;
		Name_Label = new JLabel(Name);
		Rarity = UnitRarity;
		Face_Names = UnitSides;
		Face_Keys = Keys;
		Face_Discriptions = Discriptions;
		CatalogIndex = CatalogID;
		
		//Unit Display Constructor
		add(Unit_Box);
		Unit_Box.setBorder(BorderFactory.createLineBorder(Color.black));
		Unit_Box.setLayout(new BoxLayout(Unit_Box, BoxLayout.Y_AXIS));
		
		Set_Unit_Color();
		
		Dimension Unit_Size = new Dimension(100,150);
		Unit_Box.setPreferredSize(Unit_Size);
		
		Unit_Box.add(Name_Label);
		Name_Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Battle Mode
		Battle_Pane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		try{
		    Image img = ImageIO.read(getClass().getResource("Icons/Blank.bmp"));
		    Active_Display.setIcon(new ImageIcon(img));
		} 
		catch(Exception ex){System.out.println(ex);}
		Battle_Pane.add(Active_Display);
			Active_Display.addMouseListener(this);
		
		
		Battle_Pane.add(Battle_Btn_Pane);
		Battle_Btn_Pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0,0,0,0);
		Battle_Btn_Pane.add(Damage_Btn, c);
		try{
		    Image img = ImageIO.read(getClass().getResource("Icons/Sword.bmp"));
		    Damage_Btn.setIcon(new ImageIcon(img));
		} 
		catch(Exception ex){System.out.println(ex);}
		Damage_Btn.setMargin(new Insets(0,0,0,0));
		Damage_Btn.addActionListener(this);
		Damage_Btn.setBackground(Color.BLACK);
		Damage_Btn.setBorder(UnSelected);

		c.gridx = 1;
		c.gridy = 0;
		Battle_Btn_Pane.add(Select_Btn, c);
			Select_Btn.addActionListener(this);
		try{
		    Image img = ImageIO.read(getClass().getResource("Icons/Select.bmp"));
		    Select_Btn.setIcon(new ImageIcon(img));
		} 
		catch(Exception ex){System.out.println(ex);}
		Select_Btn.setMargin(new Insets(0,0,0,0));
		Select_Btn.setBackground(Color.BLACK);
		Select_Btn.setBorder(UnSelected);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 1;
		Battle_Btn_Pane.add(Information_Btn, c);
			Information_Btn.addActionListener(this);
		
		//View Mode
		//View_Pane is added in Unit_Display
		View_Pane.setAlignmentX(Component.CENTER_ALIGNMENT);
		View_Pane.setLayout(new BoxLayout(View_Pane, BoxLayout.Y_AXIS));
		
		View_Pane.add(Face_Pane);
			Face_Pane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Face_Pane.setLayout(new GridBagLayout());
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		
		
		for(int Side_Index = 0; Side_Index < 6; Side_Index++)
		{
			c.gridx = Math.round((Side_Index)%2);
			c.gridy = Math.round((Side_Index)/2);
			Face_Icons[Side_Index] = new JLabel();
			String Icon = "Icons/" + Face_Names[Side_Index] + ".bmp";
			Image img;
			try{
			    img = ImageIO.read(getClass().getResource(Icon));
			    Face_Icons[Side_Index].setIcon(new ImageIcon(img));
			} 
			catch(Exception ex){Icon = "Icons/Unknown.bmp";}
			
			if(Icon.equals("Icons/Unknown.bmp"))
			{
				try{
				    img = ImageIO.read(getClass().getResource(Icon));
				    Face_Icons[Side_Index].setIcon(new ImageIcon(img));
				} 
				catch(Exception ex){System.out.println(ex);}
			}
			Face_Pane.add(Face_Icons[Side_Index], c);
			Face_Icons[Side_Index].addMouseListener(this);
		}
		
		
		View_Pane.add(View_Btn);
		View_Btn.addActionListener(this);
		View_Btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
	}

	
	//This will be used to roll a Die to determine its Face
	//Returning a Hash Key
	//HashMap will be located in the 'Game' Class
	public void Roll()
	{
		Random rand = new Random();
		Active_Face = Face_Names[rand.nextInt(6)];
	}
	
	
	
	//Takes the Units Rarity and determines the Background color of the Unit_Box, Ran in the Unit Constructor.
	public void Set_Unit_Color()
	{
		Color Target_Clr = Color.BLACK;
		if(Rarity == 'L')//Legendary
			Target_Clr = L_Clr;
		if(Rarity == 'R')//Rare
			Target_Clr = R_Clr;
		if(Rarity == 'U')//Uncommon
			Target_Clr = U_Clr;
		if(Rarity == 'C')//Common
			Target_Clr = C_Clr;
		
		
		Unit_Box.setBackground(Target_Clr);
		Battle_Pane.setBackground(Target_Clr);
		Battle_Btn_Pane.setBackground(Target_Clr);
		View_Pane.setBackground(Target_Clr);
		Face_Pane.setBackground(Target_Clr);
		
	}
	
	//Modular Display Box
	/*
	  In-BattleField Mode
	  Name
	  Active Side
	  Buttons:
	  	Damage
	  	Select
	  	Information
	  
	  View Mode
	  Name
	  Set
	  All 6 Sides
	  Buttons:
	  	Select/Throw/Craft/Add/Back/Remove
	  	
	  List of Places you see View Mode:
	  Collection	(Craft)
	  Bag Editing 	(Add) or (Remove)
	  Hand In-Game	(Throw)
	  Dieyard In-Game	(Nothing)
	  Information Button 'In-BattleField Mode' switches it to this	(Back)
	*/
	
	
	
	
	/*
		Determine which Display mode it should be.
		Determine ViewMode.
		Determine View Button Text:
			"Craft #/#" (QuantityOwned/MaxQuantity)
			"Add #/#" (AvalibleQuantity/OwnedQuantity)
			"Remove #/#" (AvalibleQuantity/OwnedQuantity)
	*/
	
//This set of Methods is "Messy" can be streamlined	
	//Create 1 Method that handles all Display Functions.
	//Construct list of all button "Modes"
	//Determine how all "Modes" should look like
	//Find all locations that use these methods 
//FLAG_UNIT_DISPLAY
	
	//Switches the Unit_Box from Battle Mode to Info Mode
	
	//This needs to be Preformed at Unit Construction only after its Location is determined. Example: Collection.
	//Default to InfoMode

	public void Unit_Display(String Mode)
	{
		ViewMode = Mode;
		if(ViewMode == "Battle")
		{
			Unit_Box.remove(View_Pane);
			Unit_Box.add(Battle_Pane, BorderLayout.CENTER);
			Unit_Display_Updater("ViewMode");
		}
		else
		{
			View_Btn.setText(Mode);
			Unit_Box.remove(Battle_Pane);
			Unit_Box.add(View_Pane, BorderLayout.CENTER);
		}
		Unit_Box.revalidate();
	}

	
	//Used for Ingame Unit_Display Functions
	public void Unit_Display_Updater(String Type)
	{
		ViewMode = Type;
		View_Btn.setEnabled(true);
		String BtnText = Type;
		View_Btn.setText(BtnText);
		View_Btn.revalidate();
	}
	
	//Use MaxQuantity for Crafting to determine the MaxQuan.
	//Used for Collection Unit_Display Functions
	public void Unit_Display_Updater(String Type, int CurQuan, int MaxQuan)
	{
		ViewMode = Type;
		View_Btn.setEnabled(true);
		String BtnText = Type + " " + CurQuan + "/" + MaxQuan;
		
		if(ViewMode.equals("Craft"))
		{
			if(Quantity == MaxQuan)
			{
				BtnText = "Maxed";
				View_Btn.setEnabled(false);
			}
			CanAfford();
		}
		else
			if(CurQuan == 0)
				View_Btn.setEnabled(false);
		
		//Change Font Size
		if(ViewMode.equals("Remove"))
			View_Btn.setFont(new Font(View_Btn.getFont().getFontName(), Font.PLAIN, 10));
		
		View_Btn.setText(BtnText);
		View_Btn.revalidate();
	}
	
	//Used to Determine the maxquantity of a Unit.
	public int MaxQuantity()
	{
		int MaxQuan = 3;
		if(Rarity == 'L')
			MaxQuan = 1;
		return MaxQuan;
	}
	
	//Used to determine if a Unit can be afforded by the User.
	public void CanAfford()
	{
		//Disable Button if you can not afford them
		int Cost = 0;
		if(Rarity == 'C')
			Cost = 125;
		if(Rarity == 'U')
			Cost = 250;
		if(Rarity == 'R')
			Cost = 500;
		if(Rarity == 'L')
			Cost = 1500;
		
		if(Starter.Current_Player.Credit < Cost)
			View_Btn.setEnabled(false);
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
		if(e.getSource() == Damage_Btn)
		{
			if(Damage_Btn.getBorder() == UnSelected)
				Damage_Btn.setBorder(Selected);
			else
				Damage_Btn.setBorder(UnSelected);
			
			//Debug! Dice roll updating Active_Face
			Roll();
			String Icon = "Icons/" + Active_Face + ".bmp";
			try{
			    Image img = ImageIO.read(getClass().getResource(Icon));
			    Active_Display.setIcon(new ImageIcon(img));
			} 
			catch(Exception ex){System.out.println(ex);}
			
		}
		if(e.getSource() == Select_Btn)
		{
			if(Select_Btn.getBorder() == UnSelected)
				Select_Btn.setBorder(Selected);
			else
				Select_Btn.setBorder(UnSelected);
		}
		
		if(e.getSource() == Information_Btn)
		{
			Unit_Display("Back");
		}
		
		if(e.getSource() == View_Btn)
		{
			if(View_Btn.getText() == "Back")
			{
				Unit_Display("Battle");
			}
			if(ViewMode == "Craft")
			{
				Crafting_Prompt CraftingWindow = new Crafting_Prompt(this);
			}
			
			//Add is done in the Collection Menu, it needs access to the BagDisplay[DynamicCatalogView].
			if(ViewMode == "Remove")
			{
				Quantity--;
				Starter.Catalog[CatalogIndex].Unit_Display_Updater("Add", Starter.Catalog[CatalogIndex].Quantity - Quantity, Starter.Catalog[CatalogIndex].Quantity);
				Unit_Display_Updater("Remove", Quantity, Starter.Catalog[CatalogIndex].Quantity);
			}
			
			//In_Game Hand option. Switch to "Select when you Throw a Legendary Unit.
			if(View_Btn.getText() == "Throw")
			{
				
			}
			
			
			//used for the discard for Throwing a Legendary Unit
			if(View_Btn.getText() == "Select")
			{
				
			}
			
		}	
	}


	public void mouseClicked(MouseEvent e) 
	{
		for(int Side_Index = 0; Side_Index < 6; Side_Index++)
		{
			if(e.getSource().equals(Face_Icons[Side_Index]))
			{
				AbilityReferanceWindow Referance = new AbilityReferanceWindow(Face_Names[Side_Index], Face_Discriptions[Side_Index]);
			}
		}
	}


	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



}
