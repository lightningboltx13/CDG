import java.util.Random;


public class Battle 
{
	//This is a match between players
	
	//Construct AI Opponents to load in
	/*
		Draw or Throw
		If 0 units in hand 0% chance to throw
		Legendary Units do not increase the Throw chance unless you have 3 or more Units in hand
		5 Units in hand = 50/50
		Starts 100% to Draw.
		Unit = 10% to Throw.
		Legendary Unit = if(HandSize >= 3){10% to Throw} else{0% to Throw}
		
		Which to Throw?
		Start with a Random pick.
		Eventually you can try to implement Higher Rarity Priority.
	*/
	
	
	//Could add more players
	Player[] Players = new Player[2];
	
	Unit[] Bag;
	
	DynamicCatalogView PlayerHand;
	DynamicCatalogView PlayerActive;
	DynamicCatalogView OpponentActive;
	
	Random rand = new Random();
	//Playing a Legendary Unit costs 2 Units from hand to be discarded. Also can't be played 1st turn.
	
	
	public Battle(Unit[] BagLoader) 
	{
		// TODO Auto-generated constructor stub
		Bag = BagLoader;
		//PlayerHand = new DynamicCatalogView();
	}
	
	
	
	public void Draw()
	{
		int Index = rand.nextInt(Bag.length-1);
		Unit Target = Bag[Index];
		Starter.Unit_Pool_Removal(Bag, Target);
		Starter.Unit_Pool_Add(PlayerHand.Unit_Pool, Target);
	}

}
