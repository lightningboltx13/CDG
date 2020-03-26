
public class Bag 
{
	//Bag AKA "Deck" is a collection of Dice
	
	
	String Name;
	
	int BagID;
	
	//This is the Collection of Dice that is used per game a Player may have a collection of Bags.
	//Standard Size is 30.
	Unit[] Dice = new Unit[30];
	
	
	//Experiment with different size Bags
	//Unit[] Dice = new Unit[20];
	//Unit[] Dice = new Unit[40];
	//Unit[] Dice = new Unit[50];
	//Unit[] Dice = new Unit[60];
	
	public Bag() 
	{
		// TODO Auto-generated constructor stub
	}

	
	public Unit[] StackedPool()
	{
		Unit[] PlaceHolder = new Unit[30];
		int Counter = 0;
		for(int Index = 0; Index<30;Index++)
		{
			for(int PHIndex = 0; PHIndex< 30; PHIndex++)
			{
				if(PlaceHolder[PHIndex] == null)
				{
					PlaceHolder[PHIndex] = Starter.UnitCopy(Dice[Index]);
					PlaceHolder[PHIndex].Quantity = 1;
					Counter++;
					break;
				}
				else if(PlaceHolder[PHIndex].Name.equals(Dice[Index].Name))
				{
					PlaceHolder[PHIndex].Quantity++;
					break;
				}
			}
		}
		
		Unit[] Stacked = new Unit[Counter];
		
		for(int Index = 0; Index < Counter;Index++)
		{
			Stacked[Index] = PlaceHolder[Index];
		}
		
		return Stacked;
	}
	
}
