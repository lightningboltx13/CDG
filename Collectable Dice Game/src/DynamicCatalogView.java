import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JPanel;


//This class is used to display a set of units.
//Pass in the set of Units "Stacked" and "Unstacked" depending on "Type"
//Type: Purpose of the display [Collection, Bag, Hand, Selector]
//Stacked: implies that the Units are not duplicated, but instead uses the Unit's Quantity
//Unstacked: implies that multiple of the same units occupy different spots in a single Unit_Pool
//Must be passed into Constructor as a Stacked or Unstacked, this class constructor will not handle that function
//Also it will not handle the function of converting it to a DEEP or SHALLOW Copy
public class DynamicCatalogView extends JPanel implements ActionListener
{
	
	Unit[] Unit_Pool = new Unit[0];
	int DisplaySize = 5;

	
	JPanel ScrollZone = new JPanel();
	
	//Card Catalog
	JPanel Card_Catalog = new JPanel();
	
	
	int Visable_Index = 0;
	JButton View_Left_Btn = new JButton("<<");
	JButton View_Right_Btn = new JButton(">>");
	
	GridBagConstraints c = new GridBagConstraints();
	
	public DynamicCatalogView(Unit[] Pool, String Type)
	{
	
		Unit_Pool = Pool;
		
		for(int Unit_Index = 0; Unit_Index < Unit_Pool.length; Unit_Index++)
		{
			Unit_Pool[Unit_Index].Unit_Display(Type);
			if(Type.equals("Craft"))
				Unit_Pool[Unit_Index].Unit_Display_Updater("Craft", Unit_Pool[Unit_Index].Quantity, Unit_Pool[Unit_Index].MaxQuantity());
			if(Type.equals("Remove"))
			{
				Unit_Pool[Unit_Index].Unit_Display_Updater("Remove", Unit_Pool[Unit_Index].Quantity, Starter.Catalog[Unit_Pool[Unit_Index].CatalogIndex].Quantity);
			}
		}
			
		if(Unit_Pool.length<DisplaySize)
			DisplaySize = Unit_Pool.length;
		
	    add(ScrollZone);
		
		ScrollZone.add(View_Left_Btn);
	    View_Left_Btn.addActionListener(this);
		
	    ScrollZone.add(Card_Catalog);
	    Card_Catalog.setLayout(new GridBagLayout());
	    Catalog_Scroller('S');
	    
	    ScrollZone.add(View_Right_Btn);
	    View_Right_Btn.addActionListener(this);
	}
	
	
	
	//This is used to operate the Scroll Buttons of the CatalogView
	public void Catalog_Scroller(char LeftRight)
	{		
		if(Unit_Pool.length<=5)
			DisplaySize = Unit_Pool.length;
		else
			DisplaySize = 5;
		//'L' = Left
		//'R' = Right
		//'S' = Starter
		if(LeftRight != 'S')
		{
			ClearVisable();
			if(LeftRight == 'L')
				Visable_Index--;
			if(LeftRight == 'R')
				Visable_Index++;
		}
		else
			Visable_Index = 0;
		
		if(Visable_Index == 0)
			View_Left_Btn.setEnabled(false);
		else
			View_Left_Btn.setEnabled(true);
		
		if(Visable_Index + DisplaySize == Unit_Pool.length)
			View_Right_Btn.setEnabled(false);
		else
			View_Right_Btn.setEnabled(true);
		
		if(DisplaySize < 5)
		{
			View_Left_Btn.setEnabled(false);
			View_Right_Btn.setEnabled(false);
		}
		
		//Card_Catalog.revalidate();
		c.gridheight =0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		

		
		for(int index = 0; index < DisplaySize; index++)
	    {
	    	c.gridx = index;
	    	Card_Catalog.add(Unit_Pool[Visable_Index + index], c);
	    }
		//Card_Catalog.revalidate();
		revalidate();
		
	}
		
	//Maybe unused, but it will run throw Starter.Catalog and check if the User can afford a Unit.
	public void AffordableUpdate()
	{
		for(int i=0;i<Starter.Catalog.length;i++)
		{
			Starter.Catalog[i].CanAfford();
		}
	}
	
	public void ClearVisable()
	{
		for(int index = 0; index < DisplaySize; index++)
	    	Card_Catalog.remove(Unit_Pool[Visable_Index + index]);
		revalidate();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == View_Left_Btn & Visable_Index > 0)
			Catalog_Scroller('L');
		if(e.getSource() == View_Right_Btn & (Visable_Index + DisplaySize) < Unit_Pool.length)//5 units are visable at a time
			Catalog_Scroller('R');
	}
	
	
}
