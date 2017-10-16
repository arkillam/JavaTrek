package javatrek.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This panel displays information about how the game ended, fun statistics and
 * other bits of data.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/30/2002 - the original clas
 * <LI>Version 1.1 - 11/25/2002 - uses a final score modifier based on start-up options
 * <LI>Version 1.2 - 11/26/2002 - converted to a modal JDialog (from a JFrame)
 * <LI>Version 1.3 - 10/18/2003 - converted from a window to a panel
 * <LI>Version 2.0 - 11/22/2004 - simplified this panel and removed legacy content
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/22/2004
 */

public class EndOfGamePanel extends JPanel implements ActionListener
{
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** closes the window */
private JButton b_exit;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates the end-of-game panel.
 * 
 * 		@param		victory		indicates whether the player won or lost
 * 		@param		message		closing message indicating reason for end of game
 * 
 * 		@since		1.0
 */

public EndOfGamePanel (boolean victory, String message)
{
	// TODO:  create content for this panel
	
	super (new BorderLayout ());
	
	// bounds checking
	if ((message == null) || (message.length () < 1))
	{
		message = "No message submitted to EndOfGamePanel constructor!";
	}
	
	// create and add the tabbed pane
	JTabbedPane tabbed_pane = new JTabbedPane ();
	add (tabbed_pane, BorderLayout.CENTER);
	
	// create the end game message to the top of the panel
	add (new JLabel (message, JLabel.CENTER), BorderLayout.NORTH);
	
	// create the south panel and its exit button
	JPanel p_south = new JPanel (new GridLayout (1, 1));
	b_exit = new JButton ("Exit");
	b_exit.addActionListener (this);
	p_south.add (b_exit);
	add (p_south, BorderLayout.SOUTH);
	
	// create a place-holder panel for the tabbed panethe high scores panel, and add it to the tabbed pane
	JPanel p_temp = new JPanel (new GridLayout (1, 1));
	p_temp.add (new JLabel ("TODO:  develop panels! :)"), JLabel.CENTER);
	tabbed_pane.addTab ("Work Needed", p_temp);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//  action listener
//////////////////////////////////////////////////////////////////////////////

/**		Handles action events.
 * 
 *		@param		e		the incoming action event
 * 
 *		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	// currently, the exit button is the only source of events
	// by calling dispose we get a proper exit
	JavaTrek.game.dispose ();
	System.exit (1);		
}

}