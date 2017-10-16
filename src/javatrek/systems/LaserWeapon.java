package javatrek.systems;

import java.io.Serializable;

/**
 * Represents a laser system.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/30/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/30/2004
 */

public class LaserWeapon extends MachineSystem implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the id for long-range scanners */
public static final String ID = "Laser";

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** stores the number of lasers the class represents (each one allows one shot per round */
private int number;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates the laser object.
 * 
 *		@param		n			the number of lasers
 * 
 *		@since		2.0
 */
public LaserWeapon (int n)
{
	super ();

	setNumber (n);	
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Returns the name of the laser weapon.
 * 
 *		@return		the laser weapon's name
 * 
 *		@since		2.0
 */

public String getName ()
{
	if (number == 1)		return "Single Laser";
	else if (number == 2)	return "Dual Laser";
	else if (number == 3)	return "Triple Laser";
	else if (number == 4)	return "Quad Laser";
	else if (number > 4)	return "Hyper Laser";
	else
	{
		return "Error! Invalid scanner level!";
	} 
}

/**		Returns the number of functioning lasers.
 * 
 * 		@return		the number of functioning lasers
 * 
 * 		@since		2.0
 */

public int getNumber ()
{
	// always return at least one laser
	return Math.min (1, (int) (number * getRepair ()));
}

/**		Sets the number of lasers.
 * 
 *		@param		n		the number of lasers
 * 
 *		@since		2.0
 */

public void setNumber (int n)
{
	// bounds checking
	if (n < 1) n = 1;
	//if (n > MAX_NUMBER) n = MAX_NUMBER;
	
	// change the value
	number = n;
}

}