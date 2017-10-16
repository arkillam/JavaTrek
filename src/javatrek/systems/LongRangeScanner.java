package javatrek.systems;

import java.io.Serializable;

/**
 * Represents a long-range scanner system.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/15/2002 - this
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added getUpgradeName () function
 * <LI>Version 1.3 - 10/18/2004 - added a string-driven constructor
 * <LI>Version 2.0 - 10/23/2004 - removed upgrade options, made other small changes
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/23/2004
 */

public class LongRangeScanner extends MachineSystem implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the id for long-range scanners */
public static final String ID = "LR Scanner";

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the maximum level a long-range scanner can have */
private static final int MAX_LEVEL = 5;
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** stores the scanner's level */
private int level;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a long-range scanner.
 * 
 *		@param		l			the level of scanner
 * 
 *		@since		1.0
 */
public LongRangeScanner (int l)
{
	super ();
	
	setLevel (l);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Returns the name of the long-range scanner.
 * 
 *		@return		the scanner's name
 * 
 *		@since		1.0
 */

public String getName ()
{
	if (level == 1)			return "Basic LR Scanner";
	else if (level == 2)	return "Improved LR Scanner";
	else if (level == 3)	return "Enhanced LR Scanner";
	else if (level == 4)	return "Advanced LR Scanner";
	else if (level == 5)	return "Eagle Eye LR Scanner";
	else
	{
		return "Error! Invalid scanner level!";
	} 
}

/**		Returns the scanner's effective radius, modified for damage.  An undamaged
 * 		scanner is has an effective radius equal to its level.
 * 
 * 		@return		the effective radius
 * 
 * 		@since		1.0
 */

public int getRadius ()
{
	int radius = (int) Math.floor (level * getRepair ());
	return radius;
}

/**		Sets the level of the scanner.
 * 
 *		@param		l		the level of the scanner
 * 
 *		@since		1.0
 */

public void setLevel (int l)
{
	// bounds checking
	if (l < 1) l = 1;
	if (l > MAX_LEVEL) l = MAX_LEVEL;
	
	// change the setting
	level = l;
}

}