package javatrek.handlers.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;
import javatrek.systems.Computer;

/** 
 * <P>Debugging command:  marks all the quadrat regions visible in
 * the player's ship's computer. 
 * 
 * <UL>
 * <LI>Version 2.0 - 10/31/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/31/2004
 */

public class DebugRevealRegions implements ActionListener
{
	
/**		Handles the action events.
 * 
 * 		@param		e		the incoming action event
 * 
 * 		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	Computer computer = (Computer) JavaTrek.game.gamedata.space.getPlayersShip ().getSystem (Computer.class.getName ());
	if (computer != null)
	{
		computer.setAll (true);
		JavaTrek.game.refresh ();
	}
}

}
