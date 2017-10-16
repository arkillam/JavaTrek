package javatrek.handlers.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;
import javatrek.spaceobjects.Machine;

/** 
 * <P>Debugging command:  applies 101 points of projectile-weapon damage to
 * the player's ship's systems.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/14/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/14/2004
 */

public class DebugProjectileDamageSystem implements ActionListener
{
	
/**		Handles the action events.
 * 
 * 		@param		e		the incoming action event
 * 
 * 		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	JavaTrek.game.gamedata.space.getPlayersShip ().damageSystems (101, Machine.PROJECTILE_DAMAGE);
	JavaTrek.game.refresh ();
}

}
