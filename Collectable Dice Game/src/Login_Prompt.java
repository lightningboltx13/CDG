import java.sql.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;


public class Login_Prompt extends JFrame implements ActionListener, WindowListener
{
	JPanel Main_Pane = new JPanel();
	
	JLabel Instructor = new JLabel("Enter Username and Password to login");
	
	JLabel User_Lbl = new JLabel("Username");
	JLabel Password_Lbl = new JLabel("Password");
	
	JTextField User_Entry = new JTextField("");
	JTextField Password_Entry = new JTextField("");
	
	JButton New_User = new JButton("New Player");
	JButton Login = new JButton("Login");
	
	public Login_Prompt() 
	{
		setUndecorated(true);
	    setSize(200, 150);
	    setLocationRelativeTo(null);
	    setAlwaysOnTop(true);
	    
	    addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
	    
		add(Main_Pane);
			Main_Pane.setBorder(BorderFactory.createLineBorder(Color.black));
			Main_Pane.setBackground(new Color(100,100,150));
			Main_Pane.setLayout(new BoxLayout(Main_Pane, BoxLayout.PAGE_AXIS));
		
		Main_Pane.add(Instructor);

		Main_Pane.add(User_Lbl);
		Main_Pane.add(User_Entry);

		Main_Pane.add(Password_Lbl);
		Main_Pane.add(Password_Entry);

		Main_Pane.add(New_User);
			New_User.addActionListener(this);
		Main_Pane.add(Login);
			Login.addActionListener(this);
			
		revalidate();
		setVisible(true);
		addWindowListener(this);
				
	}

	
	
	//Create a new user
	/*
		Inserting into the database:
		User
		Password
		BaseSet into Collection
		StarterBag into Bagtable and BagCollection
	*/
	public static void CreateNewUser(String Username, String Password)throws Exception
	{
		//add catches for existing username
		//add catches for "weak" passwords
		//Find Base Set
		//Add 3 Quantity to Collection Table of BaseSet
		//Build "Base Bag"
	
		try
		{
			Connection con = Starter.getConnection();
			
			PreparedStatement push = con.prepareStatement("INSERT INTO usertable (`Username`, `Credit`) VALUES ('" + Username +"', '0')");
			push.executeUpdate();
			PreparedStatement UserIDPull = con.prepareStatement("SELECT UserID From usertable WHERE `Username` = ('" + Username + "')");
			ResultSet UserIDResult = UserIDPull.executeQuery();
			UserIDResult.next();
			int UserID = UserIDResult.getInt(1);
			push = con.prepareStatement("INSERT INTO passwords (`UserID`, `Password`) VALUES ('" + UserID + "', '" + Password +"')");
			push.executeUpdate();
			
			PreparedStatement UnitPull = con.prepareStatement("SELECT * From unitcatalog");
			ResultSet UnitResult = UnitPull.executeQuery();
			
			int[] BaseSetID = new int[10];
			int StarterSetIndex = 0;
			
			while(UnitResult.next())
			{
				if(UnitResult.getString("Name").equals("Mana Forge"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Spell Spitter"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Academic"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Skeleton"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Rouge"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Goblin"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Dire Hawk"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Servent"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Guard"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
				if(UnitResult.getString("Name").equals("Blacksmith"))
				{
					BaseSetID[StarterSetIndex] = UnitResult.getInt("UnitID");
					StarterSetIndex++;
				}
			}
			
			for(StarterSetIndex = 0; StarterSetIndex < 10; StarterSetIndex++)
			{
				push = con.prepareStatement("INSERT INTO collectiontable (`UserID`, `UnitID`, `Quantity`) VALUES ('" + UserID + "', '" + BaseSetID[StarterSetIndex] + "', '3')");
				push.executeUpdate();
			}
			
			push = con.prepareStatement("INSERT INTO bagtable (`UserID`, `BagName`) VALUES ('" + UserID + "', 'Starter Bag')");
			push.executeUpdate();
			
			PreparedStatement BagPull = con.prepareStatement("SELECT BagID From bagtable WHERE `UserID` = ('" + UserID + "')");
			ResultSet BagResult = BagPull.executeQuery();
			BagResult.next();

			for(StarterSetIndex = 0; StarterSetIndex < 10; StarterSetIndex++)
			{
				push = con.prepareStatement("INSERT INTO bagcollection (`BagID`, `UnitID`, `Quantity`) VALUES ('" + BagResult.getInt("BagID") + "', '" + BaseSetID[StarterSetIndex] + "', '3')");
				push.executeUpdate();
			}
			
			
		}catch(Exception e){System.out.println(e);}
	}
	
	//Method used for Checking Username and Password for match in database and then Generates Starter.Current_Player
	public static void CheckLogin(String Username, String Password) throws Exception
	{
		//first we check for username, if we dont find a matching username we do not waste effort pulling all the passwords.
		ResultSet UsernameResult;
		ResultSet PasswordResult;
		int UserID = -1;
		try
		{
			Connection con = Starter.getConnection();
			PreparedStatement UsernamePull = con.prepareStatement("SELECT * From usertable");

			UsernameResult = UsernamePull.executeQuery();
			
			boolean booleanSwitch = true;//this is used in case we fail to find a username
			
			
			while(UsernameResult.next() & booleanSwitch)
			{
				if(Username.equals(UsernameResult.getString("Username")))
				{
					booleanSwitch = false;
					UserID = UsernameResult.getInt("UserID");
					break;//this break is super important, else it will preform a .next() one extra time.
				}
			}
			if(booleanSwitch)//this is where failure to find a matching username error will be
			{
				System.out.println("Failed to find Username");
			}
			else
			{
				PreparedStatement PasswordPull = con.prepareStatement("SELECT * From passwords");
				PasswordResult = PasswordPull.executeQuery();
				booleanSwitch = true;
				while(PasswordResult.next() & booleanSwitch)
				{

					if(UserID == PasswordResult.getInt("UserID"))
					{
						booleanSwitch = false;
						String CheckingPassword = PasswordResult.getString("Password");
						if(Password.equals(CheckingPassword))
						{
							//Successful Login HERE!!!
							Starter.Current_Player = new User(UsernameResult.getInt("UserID"), UsernameResult.getString("Username"), UsernameResult.getInt("Credit"));
						}
						if(!Password.equals(PasswordResult.getString("Password")))
						{
							System.out.println("Password does NOT match");
							//not a matching password
						}
					}
				}
			}
		}catch(Exception e){System.out.println(e);}
	}	
	
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == Login)
		{
			if(Login.getText() == "Login")
			{
				//check userfile for username and then check to see if password is a match.
				try{
					CheckLogin(User_Entry.getText(), Password_Entry.getText());
				}catch(Exception e1){e1.printStackTrace();}
				
				this.hide();
			}
			if(Login.getText() == "Cancel")
			{
				Login.setText("Login");
				New_User.setText("New Player");
				Instructor.setText("Enter Username and Password to login");
			}
					}
		if(e.getSource() == New_User)
		{
			if(New_User.getText() == "New Player")
			{
				Login.setText("Cancel");
				New_User.setText("Create");
				Instructor.setText("Enter new Username and Password");
			}
			if(New_User.getText() == "Create")
			{
				//create a new user entry in the userfile and then create a usercollection file.
				if(!User_Entry.getText().equals("") & !Password_Entry.getText().equals(""))
				{
					try{
						CreateNewUser(User_Entry.getText(), Password_Entry.getText());
					}catch(Exception e1){e1.printStackTrace();}
				}
			}
		}
	}
	
	//Upon Closing of this Login_Prompt after a SUCCESSFUL login:
	//Runs UnitLoader to generate Unit[] Catalog
	//Generates all Bags from database
	public void windowDeactivated(WindowEvent e) 
	{
		UnitLoader Loader = new UnitLoader();
		Starter.BagGenerator();
	}
	
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void windowClosed(WindowEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
