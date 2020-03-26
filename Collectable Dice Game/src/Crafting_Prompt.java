import java.sql.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


public class Crafting_Prompt extends JFrame implements FocusListener, ActionListener
{
	/*
		Common = 125c
		Uncommon = 250c
		Rare = 500c
		Legendary = 1500c
	*/
	
	
	
	/*
		Money Subtracted from User and Pushed to Database
		Unit Quantity increased by 1 and new line or line update in collectiontable of database
		update Unit display to show accurate quantity
		disable button if maxed or if insuffient funds		
	*/
	
	int Cost = 0;
	Unit Target;
	
	JPanel Main_Pane = new JPanel();
	
	JLabel UnitName_Lbl = new JLabel();
	JLabel Credit_Lbl = new JLabel(); 
	
	JButton Buy_Btn = new JButton();
	JButton Back_Btn = new JButton("Back");
	
	public Crafting_Prompt(Unit Crafting_Target)
	{
		Target = Crafting_Target;
		
		setUndecorated(true);
		//setVisible(true);
	    setSize(200, 150);
	    setLocationRelativeTo(null);
	    setAlwaysOnTop(true);
	    
	    this.addFocusListener(this);
		
	    UnitName_Lbl.setText(Target.Name);
	    Credit_Lbl.setText(Starter.Current_Player.Credit+"c");
	    
		if(Target.Rarity == 'C')
			Cost = 125;
		if(Target.Rarity == 'U')
			Cost = 250;
		if(Target.Rarity == 'R')
			Cost = 500;
		if(Target.Rarity == 'L')
			Cost = 1500;
		
		
		Buy_Btn = new JButton("Craft: " + Cost + "c");
		Buy_Btn.addActionListener(this);
		
		add(Main_Pane);
		Main_Pane.setBorder(BorderFactory.createLineBorder(Color.black));
		Main_Pane.setBackground(new Color(100,100,150));
		
		Main_Pane.add(UnitName_Lbl);
		Main_Pane.add(Credit_Lbl);
		Main_Pane.add(Buy_Btn);
			Buy_Btn.setFocusable(false);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e1) 
	{
		if(e1.getSource() == Buy_Btn)
		{
			PreparedStatement push;
			try 
		    {
				Connection con = Starter.getConnection();
				
				Target.Quantity++;
				Starter.Current_Player.Credit -= Cost;
				Starter.Current_Player.Credit_Label.setText("Credit:" + Starter.Current_Player.Credit);

			
				push = con.prepareStatement("UPDATE usertable SET `Credit` =  ('" + Starter.Current_Player.Credit + "') WHERE (`UserID` = '" + Starter.Current_Player.User_ID + "')");
				push.executeUpdate();
				
				if(Target.Quantity == 1)
				{
					push = con.prepareStatement("INSERT INTO collectiontable (`UserID`, `UnitID`, `Quantity`) VALUES ('" + Starter.Current_Player.User_ID + "', '" + Target.UnitID + "', '1')");
					push.executeUpdate();
				}
				if(Target.Quantity > 1)
				{
					push = con.prepareStatement("UPDATE collectiontable SET `Quantity` =  '" + Target.Quantity + "' WHERE (`UserID` = '" + Starter.Current_Player.User_ID + "') AND (`UnitID` = '" + Target.UnitID + "')");
					push.executeUpdate();
				}
				
				Target.Unit_Display_Updater("Craft", Target.Quantity, Target.MaxQuantity());
				//Target.CollectionUpdate();
				Collection_Menu.CatalogDisplay.AffordableUpdate();
			
			}catch(Exception e){e.printStackTrace();}
		}
		
		//This section is used to close the Prompt if the User owns the MaxQuantity or can no longer afford the next Unit.
		if(Starter.Current_Player.Credit < Cost)
			dispose();
		if(Target.Rarity == 'L')
		{
			dispose();
		}
		else
		{
			if(Target.Quantity == 3)
				dispose();
		}
		//^^^end of Close Function Section
	}

	//Will close Prompt if lost focus.
	public void focusLost(FocusEvent e){dispose();}
	
	public void focusGained(FocusEvent e){}

}
