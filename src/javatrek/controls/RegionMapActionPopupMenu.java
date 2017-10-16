package javatrek.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.SpaceObject;
import javatrek.systems.ShortRangeScanner;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Provides action options for the player when they right-click on a space
 * object displayed in the regional map.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/29/2004 - the original class
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/29/2004
 */

public class RegionMapActionPopupMenu extends JPopupMenu implements ActionListener
{
	
/////////////////////////////////////////////////////////////////////////////
//  private fields
/////////////////////////////////////////////////////////////////////////////

/** scans the space object */
private JMenuItem mi_scan;

/** a handle to the player's ship */
private Ship ship;

/** the object that was clicked on */
private SpaceObject object;
	
/////////////////////////////////////////////////////////////////////////////
//  constructor
/////////////////////////////////////////////////////////////////////////////
	
/**		Creates the popup menu.
 * 
 * 		@param		x		the x location for the popup menu
 * 		@param		y		the y location for the popup menu
 *		@param		obj		the space object to list actions for
 *
 *		@since		2.0
 */
	
public RegionMapActionPopupMenu (JComponent comp, int x, int y, SpaceObject obj)
{
	super ();
	
	// store the passed-in space object
	object = obj;
	
	// get a handle to the player's ship
	ship = JavaTrek.game.gamedata.space.getPlayersShip ();

	// create the menu's components
	createComponents ();

	// pop up the menu	
	show (comp, x, y);
}

/////////////////////////////////////////////////////////////////////////////
//  functions
/////////////////////////////////////////////////////////////////////////////

/**		Creates the menu items.
 * 
 * 		@since		2.0
 */

private void createComponents ()
{
	// scanner menu item
	mi_scan = new JMenuItem ("Scan");
	mi_scan.addActionListener (this);
	if (ship.getSystem (ShortRangeScanner.class.getName ()) == null) mi_scan.setEnabled (false);
	add (mi_scan);
}

/////////////////////////////////////////////////////////////////////////////
//  action handler
/////////////////////////////////////////////////////////////////////////////

/**		Handles action events.
 * 
 * 		@param		e		the incoming event
 * 
 * 		@since		2.0
 */

public void actionPerformed (ActionEvent e)
{
	if (e.getSource () == mi_scan)
	{
		ShortRangeScanner sr = (ShortRangeScanner) ship.getSystem (ShortRangeScanner.class.getName ());
		if (sr != null)
		{
			String data = sr.scan (object);
			JavaTrek.game.console.addMessage ("GreenLeft", data);
		}
	}
}

}
