import javax.swing.*;

import java.net.InetAddress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Starter extends JFrame implements ActionListener
{
	
	//Eventually will need to add the login portion
	static User Current_Player = null;
	
	
	//Main Menu
	JPanel Btn_Pane = new JPanel();
	
	JButton Play_Btn = new JButton("PLAY");
	//Host a Game [PVP]
	//Join a Game [PVP]
	//Offline [PVC]
	
	JButton Collection_Btn = new JButton("COLLECTION");
	
	
	
	//adding Catalog of Unit to starter.
	//Load in the begining and then pull from this instead of loading from database every time.
	static int Catalog_Size;
	static Unit[] Catalog;
	
	static Bag[] Bag_Catalog;
	

	static Unit Placeholder;
	
	
	public Starter() 
	{
		setTitle("Collectable Dice Game");
		setVisible(true);
		setResizable(false);
	    setSize(700, 600);
	    setLocationRelativeTo(null);
	    
	    
	    addWindowListener(new WindowAdapter()
	    {
	      public void windowClosing(WindowEvent e)
	      {
	        System.exit(0);
	      }
	    });
		    
		    
		    
		this.add(Btn_Pane);
		Btn_Pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		Btn_Pane.add(Play_Btn, c);
		c.gridy = 1;
		Btn_Pane.add(Collection_Btn, c);
		Play_Btn.addActionListener(this);
		Collection_Btn.addActionListener(this);
		
		Login_Prompt UserLogin_Window;
		if(Current_Player == null)
			UserLogin_Window = new Login_Prompt();
	}

	//This begins the Program
	public static void main(String[] args){new Starter();}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == Play_Btn)
		{
			
		}
		if(e.getSource() == Collection_Btn)
		{
			this.hide();
			for(int i = 0; i < Catalog.length; i++)
				Catalog[i].Unit_Display("Craft");
			try 
			{
				new Collection_Menu(getX(), getY());
			}catch(Exception e1){e1.printStackTrace();}
			
		}
		
	}
	
	//Static Method that is used throughout the program to open a connection to the database.
	public static Connection getConnection() throws Exception
	{
		try
		{
			InetAddress inetAddress = InetAddress.getLocalHost();
			//System.out.println(InetAddress.getLocalHost().getHostAddress());
			//System.out.println(System.getenv("DICEGAME_DB"));
			
			
			String driver = "com.mysql.cj.jdbc.Driver";
			String url;
			if(System.getenv("DICEGAME_DB") != null)
			{
				//host from environment variable
				//Search Windows bar "env" >> 'Edit System Environment Variables' >> [Environment Variables]
				url = "jdbc:mysql://" + System.getenv("DICEGAME_DB") + ":3306/dicegamedatabase";
			}
			else if(InetAddress.getLocalHost().getHostAddress().equals("192.168.1.165"))
			{
				//Private
				url = "jdbc:mysql://192.168.1.165:3306/dicegamedatabase";
			}
			else
			{
				//public IP
				url = "jdbc:mysql://99.119.4.29:3306/dicegamedatabase";
			}
			
			
			
			//glowing robot
			//String url = "jdbc:mysql://66.228.43.9:3306/dicegamedatabase";
			
			
			String username = "Jamison";
			String password = "Tucker.92";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		}catch(Exception e){System.out.println(e);}
		return null;
	}
	
	//Method that is used to find the number total Units found in the Database
	//It will be able change when adding or removing Units from the Database
	static int Catalog_Counter() throws Exception
	{
		int counter = 0;
	    try 
	    {
			Connection con = Starter.getConnection();
			PreparedStatement UnitIDPull = con.prepareStatement("SELECT 'UnitID' From unitcatalog");
			ResultSet UnitsCount = UnitIDPull.executeQuery();
			UnitsCount.last();
			counter = UnitsCount.getRow();
		}catch(Exception e){e.printStackTrace();}
		return counter;
	}
	
	
	
	//Used during the Search on the Collection Menu.
	//Pulls a small Pool of Units from Catalog, Shallow Copy
	static Unit[] UnitPoolGenerator(int[] UnitIDList)
	{
		Unit[] Unit_Pool = new Unit[UnitIDList.length];
		int Counter = 0;
		
		for(int i = 0; i < Catalog_Size; i++)
			for(int u = 0; u < UnitIDList.length; u++)
				if(Catalog[i].UnitID == UnitIDList[u])
				{
					Unit_Pool[Counter] = Catalog[i];
					break;
				}
		
		return Unit_Pool;
	}
	
	//Creating Deep Copies of the Units from the catalog and converting it to Bag Class
	static void BagGenerator()
	{
		int[] BagIDList = new int[10];
		int UnitCounter;
	    try 
	    {
			Connection con = Starter.getConnection();
			PreparedStatement BagPull = con.prepareStatement("SELECT * From bagtable WHERE `UserID` = ('" + Current_Player.User_ID + "')");
			ResultSet BagResult = BagPull.executeQuery();
			
			BagResult.last();
			Bag_Catalog = new Bag[BagResult.getRow()];
			BagResult.beforeFirst();
			
			while(BagResult.next())
			{
				Bag_Catalog[BagResult.getRow()-1] = new Bag();
				Bag_Catalog[BagResult.getRow()-1].Name = BagResult.getString("BagName");
				Bag_Catalog[BagResult.getRow()-1].BagID = BagResult.getInt("BagID");	
			}
			
			for(int Bag_Index = 0;  Bag_Index < Bag_Catalog.length; Bag_Index++)
			{
				PreparedStatement BagCollectionPull = con.prepareStatement("SELECT * From bagcollection WHERE `BagID` = ('" + Bag_Catalog[Bag_Index].BagID + "')");
				ResultSet BagCollectionResult = BagCollectionPull.executeQuery();
				
				UnitCounter = 0;
				while(BagCollectionResult.next())
				{
					int UnitIndex = 0;
					for(int Index = 0; Index < Catalog.length; Index++)
						if(BagCollectionResult.getInt("UnitID") == Catalog[Index].UnitID)
							UnitIndex = Index;
		
					for(int Index = 0; Index < BagCollectionResult.getInt("Quantity"); Index++)
					{
						Bag_Catalog[Bag_Index].Dice[UnitCounter] = UnitCopy(Catalog[UnitIndex]);
						UnitCounter++;
					}
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	//Creates a Deep Copy of a Unit. Passes a Unit from the Unit[] = Catalog and returns a new Unit.
	static Unit UnitCopy(Unit Source)
	{
		Unit Copy = new Unit(Source.UnitID, Source.Quantity, Source.Name, Source.Rarity, Source.Face_Names, Source.Face_Keys, Source.Face_Discriptions, Source.CatalogIndex);
		return Copy;
	}
	
	
	static Unit[] Unit_Pool_Removal(Unit[] Unit_Pool, Unit Target)
	{
		Unit[] Temp_Pool = new Unit[Unit_Pool.length - 1];
		int Counter = 0;
		for(int i = 0; i < Unit_Pool.length; i++)
		{
			if(Unit_Pool[i] != Target)
			{
				Temp_Pool[Counter] = Unit_Pool[i];
				Counter++;
			}
		}
		return Temp_Pool;
	}
	
	static Unit[] Unit_Pool_Add(Unit[] Unit_Pool, Unit Target)
	{
		Unit[] Temp_Pool = new Unit[Unit_Pool.length + 1];
		for(int i = 0; i < Unit_Pool.length; i++)
			Temp_Pool[i] = Unit_Pool[i];
		Temp_Pool[Temp_Pool.length-1] = Target;
		
		return Temp_Pool;
	}
}
