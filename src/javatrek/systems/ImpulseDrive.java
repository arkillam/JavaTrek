package javatrek.systems;

import java.io.Serializable;

/**
 * This object represents an impulse drive system.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/15/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added getUpgradeName () function
 * <LI>Version 2.0 - 10/22/2004 - changed the design only slightly
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/22/2004
 */

public class ImpulseDrive extends MachineSystem implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the id for impulse drives */
public static final String ID = "Impulse Drive";

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		The default constructor.
 * 
 *		@since		1.0
 */
public ImpulseDrive ()
{
		super ();
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Returns the name of the impulse drive system, as a string.
 * 
 *		@return		returns the name of the impulse drive system
 * 
 *		@since		1.1
 */

public String getName ()
{
	return "Impulse Drive";
}

}