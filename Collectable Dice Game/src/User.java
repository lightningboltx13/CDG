import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class User extends JPanel implements ActionListener
{
	//User is the term used for a player out of battle.
	
	String User_Name = "Test";
	Bag[] Bags = new Bag[5];
	int Credit = 0;
	int Rank_Value = 0;
	int User_ID;
	
	JPanel User_Box = new JPanel();
	JLabel Name_Label = new JLabel();
	JLabel Credit_Label = new JLabel();
	JLabel Rank_Label = new JLabel();
	
	//Unit Info DisplayBox
	public User(int ID, String UserName_Input, int Credit_Input) 
	{
		setVisible(true);
		add(User_Box);
		User_Box.add(Name_Label);
		User_Box.add(Credit_Label);
		User_Box.add(Rank_Label);
		
		User_ID = ID;
		User_Name = UserName_Input;
		Credit = Credit_Input;
		
		Name_Label.setText(User_Name);
		Credit_Label.setText("Credits:" + Credit);
		Rank_Label.setText("Rank:" + Rank_Value);
		
		User_Box.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

}
