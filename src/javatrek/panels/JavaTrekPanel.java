package javatrek.panels;

/**
 * Requires that a panel have a refresh function.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/29/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/29/2004
 */

public interface JavaTrekPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////
	
/**		Updates the displayed data, and calls the refresh function of
 * 		panels it contains.
 * 
 * 		@since		1.0
 */

public void refresh ();

}
