import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//Creating the Static Unit[] holding all the Units from the database
//Ran only after a successful login, Ran from the Login_Prompt
public class UnitLoader 
{
	public UnitLoader()
	{
		try
	    {
	    Starter.Catalog_Size = Starter.Catalog_Counter();
	    Starter.Catalog = new Unit[Starter.Catalog_Size];
	    }catch(Exception e){System.out.println(e);}
	
		
		String UnitName;
		char Rarity;
		String[] Sides = new String[6];
		String[] Side_Keys = new String[6];
		String[] Side_Disc = new String[6];
		int UnitQuantity = 0;
		int UnitID;
		
		try 
	    {
			Connection con = Starter.getConnection();
			PreparedStatement UnitPull = con.prepareStatement("SELECT * From unitcatalog");
			ResultSet UnitResult = UnitPull.executeQuery();
			PreparedStatement AbilityPull = con.prepareStatement("SELECT * From abilitytable");
			ResultSet AbilityResult;
			PreparedStatement CollectionPull = con.prepareStatement("SELECT * From collectiontable");
			ResultSet CollectionResult;

			
			int Collection_Count = 0; //this is used to stop the search if all units have been found during a search, only helps if you have all the units...
			
			int[][] QuantityList = new int[2][Starter.Catalog_Size];
			
			CollectionResult = CollectionPull.executeQuery();
			while(CollectionResult.next())
			{
				if(CollectionResult.getInt("UserID") == Starter.Current_Player.User_ID)
				{
					QuantityList[0][Collection_Count] = CollectionResult.getInt("UnitID");
					QuantityList[1][Collection_Count] = CollectionResult.getInt("Quantity");
					Collection_Count++;
				}
			}
			
			while(UnitResult.next())
			{
				Sides = new String[6];
				Side_Keys = new String[6];
				Side_Disc = new String[6];
				
				UnitQuantity = 0;
				
				UnitName = UnitResult.getString("Name");
				Rarity = UnitResult.getString("Rarity").charAt(0);
				UnitID = UnitResult.getInt("UnitID");
				
				for(int SideIndex = 0; SideIndex < 6; SideIndex++)
				{
					AbilityResult = AbilityPull.executeQuery();
					while(AbilityResult.next())
					{
						if(UnitResult.getInt("Side" + (SideIndex+1)) == AbilityResult.getInt("AbilityID"))
						{
							Sides[SideIndex] = AbilityResult.getString("Name");
							Side_Keys[SideIndex] = AbilityResult.getString("Key");
							Side_Disc[SideIndex] = AbilityResult.getString("Discription");
							break;
						}
					}
				}
				
				
				for(int Collection_Index = 0; Collection_Index < Collection_Count; Collection_Index++)
				{
					if(UnitResult.getInt("UnitID") == QuantityList[0][Collection_Index])
					{
						UnitQuantity = QuantityList[1][Collection_Index];
					}
				}
						
				
				Starter.Catalog[UnitResult.getRow()-1] = new Unit(UnitID, UnitQuantity, UnitName, Rarity, Sides, Side_Keys, Side_Disc, UnitResult.getRow()-1);
				
			}
		}catch(Exception e){e.printStackTrace();}
		
		
		
		
	}
}
