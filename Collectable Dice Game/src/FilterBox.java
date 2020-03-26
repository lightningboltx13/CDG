import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class FilterBox extends JPanel
{

	JPanel Main_Pane = new JPanel();
	
	ButtonGroup Owned_Group = new ButtonGroup();
	JPanel Owned_Pane = new JPanel();
	JRadioButton Owned_Box = new JRadioButton("Owned");
	JRadioButton Unowned_Box = new JRadioButton("Unowned");
	JRadioButton Both_Box = new JRadioButton("Both");
	
	JPanel Rarity_Pane = new JPanel();
	JCheckBox Com_Box = new JCheckBox("Common");
	JCheckBox Unc_Box = new JCheckBox("Uncommon");
	JCheckBox Rar_Box = new JCheckBox("Rare");
	JCheckBox Leg_Box = new JCheckBox("Legendary");
	
	
	JTextField WordSearch_Box = new JTextField();
	
	//Ability Search
	
	
	public FilterBox()
	{
		add(Main_Pane);
		
		Main_Pane.add(Owned_Pane);
		Owned_Pane.add(Owned_Box);
		Owned_Box.setSelected(true);
		Owned_Pane.add(Unowned_Box);
		Owned_Pane.add(Both_Box);
		Owned_Group.add(Owned_Box);
		Owned_Group.add(Unowned_Box);
		Owned_Group.add(Both_Box);
		
		Main_Pane.add(Rarity_Pane);
		Rarity_Pane.add(Com_Box);
		Rarity_Pane.add(Unc_Box);
		Rarity_Pane.add(Rar_Box);
		Rarity_Pane.add(Leg_Box);
		
	}
	
}
