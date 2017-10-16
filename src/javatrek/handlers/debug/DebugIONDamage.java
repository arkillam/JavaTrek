package javatrek.handlers.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;
import javatrek.spaceobjects.Machine;

/** 
 * <P>Debugging command:  applies 101 points of ION-energy-weapon damage to
 * the player's ship.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/25/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/25/2004
 */

public class DebugIONDamage implements ActionListener
{
	
/**		Handles the action events.
 * 
 * 		@param		e		the incoming action event
 * 
 * 		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	JavaTrek.game.gamedata.space.getPlayersShip ().takeDamage (101, Machine.ION_DAMAGE);
	JavaTrek.game.refresh ();
}

}
