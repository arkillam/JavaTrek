package javatrek.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;

/** 
 * <P>Handles rest events.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/31/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/31/2004
 */

public class Rest implements ActionListener
{
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////
	
/** stores how many hours to have the player rest for */
private int hours;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a rest handler.
 * 
 * 		@param		h		the number of hours to rest
 * 
 * 		@since		2.0
 */
	
public Rest (int h)
{
	// bounds checking
	if (h < 0)
	{
		h = 0;
	}
	
	// assign the value
	hours = h;
}

//////////////////////////////////////////////////////////////////////////////
//  action handler
//////////////////////////////////////////////////////////////////////////////
	
/**		Handles the action events.
 * 
 * 		@param		e		the incoming action event
 * 
 * 		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	JavaTrek.game.gamedata.passTime (hours);
	JavaTrek.game.refresh ();
}

}
