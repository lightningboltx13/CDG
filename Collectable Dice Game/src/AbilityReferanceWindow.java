import java.awt.Color;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class AbilityReferanceWindow extends JFrame implements FocusListener
{
	
	JPanel Main_Pane = new JPanel();
	
	JLabel Name = new JLabel("Name");
	JLabel Icon = new JLabel("");
	JLabel Discription = new JLabel("");
	
	
	public AbilityReferanceWindow(String AbiName, String AbiDisc) 
	{
		setUndecorated(true);
		setVisible(true);
	    setSize(200, 150);
	    setLocationRelativeTo(null);
	    setAlwaysOnTop(true);
	    addFocusListener(this);
	    
	    add(Main_Pane);
		Main_Pane.setBorder(BorderFactory.createLineBorder(Color.black));
		Main_Pane.setBackground(new Color(100,100,150));

	    
	    Main_Pane.add(Name);
	    Name.setText(AbiName);
	    
	    String IconLocation = "Icons/" + AbiName + ".bmp";
	    Image img;
		try{
		    img = ImageIO.read(getClass().getResource(IconLocation));
		    Icon.setIcon(new ImageIcon(img));
		} 
		catch(Exception ex){IconLocation = "Icons/Unknown.bmp";}
		
		if(Icon.equals("Icons/Unknown.bmp"))
		{
			try{
			    img = ImageIO.read(getClass().getResource(IconLocation));
			    Icon.setIcon(new ImageIcon(img));
			} 
			catch(Exception ex){System.out.println(ex);}
		}
	    Main_Pane.add(Icon);
	    
	    Main_Pane.add(Discription);
	    Discription.setText(AbiDisc);
	    
	}

	//Closes the window when Referance Window loses focus.
	public void focusLost(FocusEvent e){dispose();}

	public void focusGained(FocusEvent e){}

	

}
