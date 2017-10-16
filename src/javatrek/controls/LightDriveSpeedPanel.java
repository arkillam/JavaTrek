package javatrek.controls;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.GameData;
import javatrek.JavaTrek;
import javatrek.panels.JavaTrekPanel;
import javatrek.spaceobjects.Ship;
import javatrek.systems.LightDrive;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * <P>Provides a control for the speed setting for the player's ship's light drive.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/03/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/03/2004
 */

public class LightDriveSpeedPanel extends JPanel implements ActionListener, JavaTrekPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the string that appears beside the light drive speed setting */
private static final String ID = "Light Drive Setting:  ";

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/**  displays the current light drive setting and allows the user to select a new one */
private JComboBox cb_setting = null;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates the panel.
 * 
 * 		@since		2.0
 */

public LightDriveSpeedPanel ()
{
	super (new GridLayout (1, 1));
	
	// create the hyperdrive setting combo box
	cb_setting = new JComboBox ();
	cb_setting.setToolTipText ("Select a hyperdrive setting from the pull-down menu.");
	cb_setting.addActionListener (this);
	add (cb_setting);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Updates the display to match the game's settings.
 * 
 * 		@since		2.0
 */

public void refresh ()
{
	// handle to gamedata object
	GameData gamedata = JavaTrek.game.gamedata;	
	if (gamedata == null) return;
	
	// handle to the player's ship
	Ship ship = gamedata.space.getPlayersShip ();
	
	// handle to the player's ship's light drive
	LightDrive ld = (LightDrive) ship.getSystem (LightDrive.class.getName ());
	
	if (ld != null)
	{
		// remove the action listener
		cb_setting.removeActionListener (this);
		
		float f = 1.0f;
				
		cb_setting.removeAllItems ();
		
		while (f <= ld.getMax ())
		{
			cb_setting.addItem (ID + Float.toString (f));	
			f++;
		}
		
		// select current setting
		// (the temporary editable status is to allow non-list-items to be added for odd settings, e.g. 3.75)
		cb_setting.setEditable (true);
		cb_setting.setSelectedItem (ID + Float.toString (ld.getSetting ()));
		cb_setting.setEditable (false);
		
		// reinstate the listener
		cb_setting.addActionListener (this);
		
		// enable the component
		cb_setting.setEnabled (true);
	}	
	else
	{
		cb_setting.removeActionListener (this);
		cb_setting.removeAllItems ();
		cb_setting.addItem ("No Light Drive");
		cb_setting.setEnabled (false);
	}
}

//////////////////////////////////////////////////////////////////////////////
//  action listener
//////////////////////////////////////////////////////////////////////////////

/** 	Implements the action listener function/interface.
 * 
 * 		@param		e		The incoming event.
 * 
 * 		@since		2.0
 */

public void actionPerformed (ActionEvent e)
{
	// get the selected item
	String selected = cb_setting.getSelectedItem ().toString ();
		
	// remove the leading "Hyperdrive Setting:  " text
	int i = selected.indexOf (ID) + ID.length ();
	selected = selected.substring (i);
		
	// convert the value to a float value
	float f = Float.valueOf (selected).floatValue ();
	
	// handle to gamedata object
	GameData gamedata = JavaTrek.game.gamedata;	
	if (gamedata == null) return;
	
	// handle to the player's ship
	Ship ship = gamedata.space.getPlayersShip ();
	
	// handle to the player's ship's light drive
	LightDrive ld = (LightDrive) ship.getSystem (LightDrive.class.getName ());
		
	// set the ship's new hyperdrive setting
	if (ld != null) ld.setSetting (f);
		
	// refresh the display
	JavaTrek.game.refresh ();
}

}
