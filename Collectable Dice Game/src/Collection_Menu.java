import java.sql.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.xml.internal.resolver.Catalog;


/*
	Also needs to work on some of the repainting after using the Search.
	Implement 'Apply Search' to Editor.
	
	Delete Bag
	
	Save Bag is messing up the Bag_Selector (JList) by forcing it to act as though whichever item is selected it only returns Zero for getSelectedIndex()
	
	Save Button needs to save to datebase.
	
	Main Menu Button needs to work.
	
	Key and Lock Items need added.
	also will need to update all icons to Dad's Icons.
*/



public class Collection_Menu extends JFrame implements ActionListener, ItemListener
{
	
	JPanel Main_Pane = new JPanel();
	

	static DynamicCatalogView CatalogDisplay;
	DynamicCatalogView BagDisplay;

	FilterBox Search = new FilterBox();
	
	JButton BackBtn = new JButton("Main Menu");

	
	//For all other uses... my remove b seems useless (Helps stay organized???)
	GridBagConstraints c = new GridBagConstraints();
	
	//For BagEditor use only
	GridBagConstraints b = new GridBagConstraints();
	
	//Bag List
	JPanel Bag_Pane = new JPanel();
	JPanel Bag_Selector_Pane = new JPanel();
	
	DefaultListModel BagModel = new DefaultListModel();
	JList Bag_Selector;
	
	JButton NewBtn = new JButton("New");
	JButton EditBtn = new JButton("Edit");
	JButton DeleteBtn = new JButton("Delete");
	
	JPanel Bag_Editor_Pane = new JPanel();
	JLabel Name_Lbl = new JLabel("Bag Name:");
	JTextField NameEntry = new JTextField();
	JLabel UnitCount = new JLabel("00/30 Units");
	JCheckBox ApplySearch = new JCheckBox("Apply Filter");
	JButton SaveBtn = new JButton("Save");
	JButton CancelBtn = new JButton("Cancel");

	
	
	
	public Collection_Menu(int X_loc, int Y_loc) throws Exception 
	{
		// TODO Auto-generated constructor stub
		
		setTitle("Collectable Dice Game");
		setVisible(true);
		setResizable(false);
	    setBounds(X_loc, Y_loc, 700, 600);
	    //setLocationRelativeTo(null);
		    
	    
	    addWindowListener(new WindowAdapter()
	    {public void windowClosing(WindowEvent e){System.exit(0);}});
		
	    add(Main_Pane);
	    //Main_Pane.setLayout(new GridBagLayout());
	    
	    GridBagConstraints c = new GridBagConstraints();
		
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0,0,0,0);
	    
		Main_Pane.add(Starter.Current_Player);
	    //Main_Pane.add(Starter.Current_Player, c);

	    Main_Pane.add(Search);
	    //Main_Pane.add(Search, c);
	    
	    //Adding ItemListeners to all the Search Items.
	    Search.Both_Box.addItemListener(this);
	    Search.Owned_Box.addItemListener(this);
	    Search.Unowned_Box.addItemListener(this);
	    Search.Com_Box.addItemListener(this);
	    Search.Unc_Box.addItemListener(this);
	    Search.Rar_Box.addItemListener(this);
	    Search.Leg_Box.addItemListener(this);
	    ApplySearch.addItemListener(this);
	    
	    
	    CatalogDisplay = new DynamicCatalogView(ApplySearch(Starter.Catalog), "Craft");
	    
	    for(int index = 0; index < Starter.Catalog_Size; index++)
		{
			Starter.Catalog[index].View_Btn.addActionListener(this);
		}
	    
	    Main_Pane.add(CatalogDisplay);
		//Main_Pane.add(CatalogDisplay, c);
		
	    
	    Main_Pane.add(Bag_Pane);
		//Main_Pane.add(Bag_Pane, c);
		
	    Bag_Pane.setLayout(new GridBagLayout());
	    
	    GridBagConstraints b = new GridBagConstraints();
	    
	    b.gridheight = 1;
		b.gridwidth = 1;
		b.weightx = 1;
		
		b.gridx = 0;
		b.gridy = 0;
		b.insets = new Insets(0,0,0,0);
	    
	    
		Bag_Pane.add(Bag_Selector_Pane, b);
		
	//Unsure as to what this is may delete if i figure out it is useless.
		//Bag_Pane.setLayout(new GridLayout(1,2));
		//need to edit size of the Bag_Editor_Pane
		//ADD GridBoxLayout to Main_Pane.
		
		Bag_Selector_Updater();
	
		
		Bag_Selector.setLayoutOrientation(JList.VERTICAL);
		Bag_Selector_Pane.add(Bag_Selector);
		Bag_Selector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Bag_Selector.setSize(60,100);
		Bag_Selector.setVisibleRowCount(4);
		Bag_Selector.setFixedCellHeight(20);
		Bag_Selector.setFixedCellWidth(100);
		Bag_Selector.setSelectedIndex(0);

		Bag_Selector_Pane.add(NewBtn);
			NewBtn.addActionListener(this);
		Bag_Selector_Pane.add(EditBtn);
			EditBtn.addActionListener(this);
		Bag_Selector_Pane.add(DeleteBtn);
			DeleteBtn.addActionListener(this);
			

		Bag_Editor_Pane.setBorder(BorderFactory.createLineBorder(Color.black));
		Bag_Editor_Pane.setSize(100, 100);
		Bag_Editor_Pane.add(Name_Lbl);
		Bag_Editor_Pane.add(NameEntry);
		NameEntry.setPreferredSize(new Dimension(100,30));
		Bag_Editor_Pane.add(UnitCount);
		Bag_Editor_Pane.add(ApplySearch);
		Bag_Editor_Pane.add(SaveBtn);
			SaveBtn.addActionListener(this);
		Bag_Editor_Pane.add(CancelBtn);
			CancelBtn.addActionListener(this);
		
			
		Main_Pane.add(BackBtn);
			BackBtn.addActionListener(this);
			
		
	}

	
	//Used to get Bag's Names to populate the BagSelector List
	//Used to initialize the Bag Selector
	public void Bag_Selector_Updater()
	{
		for(int Bag_Counter = 0;Bag_Counter < 10; Bag_Counter++)
			if(Bag_Counter < Starter.Bag_Catalog.length)
				BagModel.addElement(Starter.Bag_Catalog[Bag_Counter].Name);
		Bag_Selector = new JList(BagModel);

	}
	//Used to add new entry to Bag Selector
	public void Bag_Selector_Updater(String NewName)
	{
		BagModel.addElement(NewName);
		Bag_Selector.setModel(BagModel);
	}
	//Used to edit existing entry in Bag Selector
	public void Bag_Selector_Updater(String NewName, int Index)
	{
		BagModel.setElementAt(NewName, Index);
		Bag_Selector.setModel(BagModel);
	}
	
	
	//Creates a Modified/Small Unit[] (Unit_Pool) to Display. This will be a SHALLOW COPY of what ever the 'MaxPool' is.
	public Unit[] ApplySearch(Unit[] MaxPool)
	{
		Unit[] NewPool = new Unit[0];

		for(int Index = 0; Index < MaxPool.length; Index++)
		{
			
			//first make pool of owned or unowned, if both just pass MaxPool as NewPool
			if(Search.Owned_Box.isSelected() & MaxPool[Index].Quantity > 0)
			{
		    	if(MaxPool[Index].Quantity > 0)
		    		NewPool = Starter.Unit_Pool_Add(NewPool, MaxPool[Index]);
			}
			else if(Search.Unowned_Box.isSelected() & MaxPool[Index].Quantity == 0)
			{
			    	if(MaxPool[Index].Quantity == 0)
			    		NewPool = Starter.Unit_Pool_Add(NewPool, MaxPool[Index]);
			}
			else if(Search.Both_Box.isSelected() & MaxPool[Index].Quantity == 0)
				NewPool = MaxPool;
			
		}

		//Second check NewPool for Rarity
		Unit[] Temp_Pool = new Unit[0]; 
		
		if(!(Search.Com_Box.isSelected()) && !(Search.Unc_Box.isSelected()) && !(Search.Rar_Box.isSelected()) && !(Search.Leg_Box.isSelected()))
		{
			Temp_Pool = RarityFilter('C', NewPool, Temp_Pool);
			Temp_Pool = RarityFilter('U', NewPool, Temp_Pool);
			Temp_Pool = RarityFilter('R', NewPool, Temp_Pool);
			Temp_Pool = RarityFilter('L', NewPool, Temp_Pool);
		}
		
		if(Search.Com_Box.isSelected())
			Temp_Pool = RarityFilter('C', NewPool, Temp_Pool);
		
		if(Search.Unc_Box.isSelected())
			Temp_Pool = RarityFilter('U', NewPool, Temp_Pool);
		
		if(Search.Rar_Box.isSelected())
			Temp_Pool = RarityFilter('R', NewPool, Temp_Pool);
		
		if(Search.Leg_Box.isSelected())
			Temp_Pool = RarityFilter('L', NewPool, Temp_Pool);
		
		NewPool = Temp_Pool;
		
	return NewPool;
	}

	public Unit[] RarityFilter(char Rarity, Unit[] PullPool, Unit[] PushPool)
	{
		for(int i = 0; i < PullPool.length; i++)
			if(PullPool[i].Rarity == Rarity)
				PushPool = Starter.Unit_Pool_Add(PushPool, PullPool[i]);
		return PushPool;
	}
	
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == EditBtn || e.getSource() == NewBtn)
		{
			Bag_Selector_Pane.hide();
			
			Unit[] Bag_Editor_Pool;
			
			if(e.getSource() == EditBtn)
			{
				//'Starter.Bag_Catalog' is a DEEP Copy of 'Starter.Catalog'
				Bag_Editor_Pool = Starter.Bag_Catalog[Bag_Selector.getSelectedIndex()].StackedPool();
				for(int i = 0; i < Bag_Editor_Pool.length; i++)
				{
					Bag_Editor_Pool[i] = Starter.UnitCopy(Bag_Editor_Pool[i]);
					Bag_Editor_Pool[i].View_Btn.addActionListener(this);
				}
				NameEntry.setText(Starter.Bag_Catalog[Bag_Selector.getSelectedIndex()].Name);
			}
			else//if NewBtn
			{
				Bag_Selector.clearSelection();
				Bag_Editor_Pool = new Unit[0];
			}
				
			BagDisplay = new DynamicCatalogView(Bag_Editor_Pool, "Remove");
			
			b.gridx = 0;
			b.gridy = 0;
			b.insets = new Insets(0,0,0,0);
			
			Bag_Pane.add(Bag_Editor_Pane, b);
			
			b.gridx = 0;
			b.gridy = 1;
			b.insets = new Insets(0,0,0,0);
			
			int Counter = 0;
			for(int i=0;i<BagDisplay.Unit_Pool.length;i++)
				Counter += BagDisplay.Unit_Pool[i].Quantity;
			UnitCount_Updater(Counter);
			
			Bag_Pane.add(BagDisplay, b);
			Main_Pane.remove(BackBtn);
			
		
			//This Section updates the UnitDisplay to ViewMode="Add", by changing the entire Starter.Catalog
			//Depending on if the Unit is in the Bag or not changes the Quantity Formula.
			for(int index = 0; index < Starter.Catalog.length; index++)
			{
				for(int BagIndex = 0; BagIndex < BagDisplay.Unit_Pool.length+1; BagIndex++)
				{
					if(BagIndex == BagDisplay.Unit_Pool.length)
					{
						Starter.Catalog[index].Unit_Display_Updater("Add", Starter.Catalog[Starter.Catalog[index].CatalogIndex].Quantity, Starter.Catalog[Starter.Catalog[index].CatalogIndex].Quantity);
					}
					else
						if(Starter.Catalog[index].CatalogIndex == BagDisplay.Unit_Pool[BagIndex].CatalogIndex)
						{
							Starter.Catalog[index].Unit_Display_Updater("Add", Starter.Catalog[Starter.Catalog[index].CatalogIndex].Quantity - BagDisplay.Unit_Pool[BagIndex].Quantity, Starter.Catalog[Starter.Catalog[index].CatalogIndex].Quantity);
							break;
						}
				}
			}
			
		}
		
		if(e.getSource() == DeleteBtn)
		{
			Bag_Selector.setSelectedIndex(1);
		}
		
		for(int index = 0; index < CatalogDisplay.Unit_Pool.length; index++)
		{
			if(e.getSource() == CatalogDisplay.Unit_Pool[index].View_Btn & CatalogDisplay.Unit_Pool[index].ViewMode.equals("Add"))
			{
				
				for(int BagIndex = 0; BagIndex < BagDisplay.Unit_Pool.length + 1; BagIndex++)
				{
					if(BagIndex == BagDisplay.Unit_Pool.length || BagDisplay.Unit_Pool.length == 0)
					{
						BagDisplay.Unit_Pool = Starter.Unit_Pool_Add(BagDisplay.Unit_Pool, Starter.UnitCopy(CatalogDisplay.Unit_Pool[index]));
						BagDisplay.Unit_Pool[BagDisplay.Unit_Pool.length-1].Quantity = 0;
						BagDisplay.Unit_Pool[BagDisplay.Unit_Pool.length-1].Unit_Display("Remove");
						BagDisplay.Unit_Pool[BagDisplay.Unit_Pool.length-1].View_Btn.addActionListener(this);
					}
					if(CatalogDisplay.Unit_Pool[index].CatalogIndex == BagDisplay.Unit_Pool[BagIndex].CatalogIndex)
					{
						BagDisplay.Unit_Pool[BagIndex].Quantity++;
						BagDisplay.Unit_Pool[BagIndex].Unit_Display_Updater("Remove", BagDisplay.Unit_Pool[BagIndex].Quantity, Starter.Catalog[CatalogDisplay.Unit_Pool[index].CatalogIndex].Quantity);
						CatalogDisplay.Unit_Pool[index].Unit_Display_Updater("Add", Starter.Catalog[CatalogDisplay.Unit_Pool[index].CatalogIndex].Quantity - BagDisplay.Unit_Pool[BagIndex].Quantity, Starter.Catalog[CatalogDisplay.Unit_Pool[index].CatalogIndex].Quantity);
						BagDisplay.Catalog_Scroller('U');
						UnitCount_Updater(1);
						break;
					}
				}
				break;
			}
		}
		
		if(BagDisplay != null)
			for(int i = 0; i < BagDisplay.Unit_Pool.length; i++)
			{
				if(e.getSource() == BagDisplay.Unit_Pool[i].View_Btn & BagDisplay.Unit_Pool[i].ViewMode.equals("Remove"))
				{
					if(BagDisplay.Unit_Pool[i].Quantity == 1)
					{
						BagDisplay.Card_Catalog.remove(BagDisplay.Unit_Pool[i]);
						BagDisplay.Unit_Pool = Starter.Unit_Pool_Removal(BagDisplay.Unit_Pool, BagDisplay.Unit_Pool[i]);
						if(BagDisplay.Visable_Index + 5 > BagDisplay.Unit_Pool.length & BagDisplay.Visable_Index != 0)
							BagDisplay.Visable_Index--;
						BagDisplay.Catalog_Scroller('U');
					}
					UnitCount_Updater(-1);
					break;
				}
			}
		
		
		//This is going to be a big section.
		//It is going to take the current Bag Units and update the Players current Bag_Catalog and also the Database Bag Tables
		//This is also where notes about the Bag_Editor will go
		/*
			Format in:	Stacked DEEP Copy of 'Starter.Catalog' >>> 'Starter.Bag_Collection' >?> Possible SHALLOW Copy using the 'ApplyFilter()'
			Format during:	(2 Ideas)
				1st Idea: Copy other Digital Collection games (Hearthstone & Runeterra) and have the Unit_Pool update the database with every change
				2nd Idea: Make the Unit_Pool a DEEP copy that only applies updates if the save button is pressed. If the Cancel button is pressed no changes are made.
			Format out:	Stacked Unit_Pool that will need to be unstacked to update the Bag_Collection, but be stacked while updating the Database.
			
			I preffer the save or cancel method (2nd Idea), it will demand less times the user must connect to the database. 
			
		*/
		if(e.getSource() == SaveBtn)
		{
			//update name
			//Idea 1: remove all units and then add new ones.
			//Idea 2: check database and update quanities and add or remove if added or removed.
			//Return to Bag Selector
			
			//Update Bag_Catalog and Database
			if(Bag_Selector.getSelectedIndex() != -1)
			{
//Need to Update Ingame data and upload changes to database.
				Starter.Bag_Catalog[Bag_Selector.getSelectedIndex()].Name = NameEntry.getText();
				Bag_Selector_Updater(NameEntry.getText() ,Bag_Selector.getSelectedIndex());
			}
			else
			{
//Just need to upload to database and generate BagID.
				Bag[] TempBagCatalog = new Bag[Starter.Bag_Catalog.length+1];
				for(int i=0;i<Starter.Bag_Catalog.length;i++)
					TempBagCatalog[i] = Starter.Bag_Catalog[i];
				TempBagCatalog[Starter.Bag_Catalog.length] = new Bag();
				TempBagCatalog[Starter.Bag_Catalog.length].Name = NameEntry.getText();
				Bag_Selector_Updater(NameEntry.getText());
				
				int counter = 0;
				for(int i=0;i<BagDisplay.Unit_Pool.length;i++)
					for(int Index = 0; Index < BagDisplay.Unit_Pool[i].Quantity; Index++)
					{
						TempBagCatalog[Starter.Bag_Catalog.length].Dice[counter] = Starter.UnitCopy(Starter.Catalog[BagDisplay.Unit_Pool[i].CatalogIndex]);
						counter++;
					}
				Starter.Bag_Catalog = TempBagCatalog;
				
			}
			Return_BagSelector();
		}
		
		if(e.getSource() == CancelBtn)
		{
			Return_BagSelector();
		}
		
		
		if(e.getSource() == BackBtn)
		{
			this.hide();
			new Starter();
		}
		
		
	}

	public void Return_BagSelector()
	{
		for(int index = 0; index < Starter.Catalog.length; index++)
		{
			Starter.Catalog[index].Unit_Display_Updater("Craft", Starter.Catalog[index].Quantity, Starter.Catalog[index].MaxQuantity());
		}
		Bag_Pane.remove(Bag_Editor_Pane);
		Bag_Pane.remove(BagDisplay);
		Main_Pane.add(BackBtn);
		UnitCount.setText("00/30");
		Bag_Selector_Pane.show();
		Bag_Selector.setSelectedIndex(0);
		Main_Pane.revalidate();
		BagDisplay = null;
	}
	
	public void UnitCount_Updater(int Modifier)
	{
		int Input;
		Input = Integer.parseInt(UnitCount.getText().substring(0, 2));
		Input+=Modifier;
		if(Input < 10)
			UnitCount.setText("0" + Input + "/30");
		else
			UnitCount.setText(Input + "/30");
		if(Input<30)
			SaveBtn.setEnabled(false);
		else
			SaveBtn.setEnabled(true);
	}


	public void itemStateChanged(ItemEvent e) 
	{
		CatalogDisplay.ClearVisable();
		CatalogDisplay.Unit_Pool = ApplySearch(Starter.Catalog);
		CatalogDisplay.Catalog_Scroller('S');
		if(ApplySearch.isSelected())
		{
			
		}
		else
		{
			
		}
	}


	
}
