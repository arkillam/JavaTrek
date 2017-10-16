package javatrek.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;

/** 
 * <P>Handles quick game-loading events.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/21/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/21/2004
 */

public class QuickLoad implements ActionListener
{
	
/**		Handles the action events.
 * 
 * 		@param		e		the incoming action event
 * 
 * 		@since		1.0
 */

public void actionPerformed (ActionEvent e)
{
	JavaTrek.game.loadgame ("quick");
}

}
