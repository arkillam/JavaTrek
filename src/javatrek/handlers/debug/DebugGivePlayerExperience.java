package javatrek.handlers.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;

/** 
 * <P>Debugging command:  gives the player 5000 experience points.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/26/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/26/2004
 */

public class DebugGivePlayerExperience implements ActionListener
{
	
/**		Handles the action events.
 * 
 * 		@param		e		the incoming action event
 * 
 * 		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	JavaTrek.game.gamedata.player.addExperience (5000);
	JavaTrek.game.refresh ();
}

}
