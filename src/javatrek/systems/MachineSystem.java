package javatrek.systems;

import java.io.Serializable;

/**
 * This object represents a basic system.  The object_system class is meant to be extended to form the
 * various systems that can exist.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/10/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added abstract upgrade-naming functions
 * <LI>Version 2.0 - 10/22/2004 - removed the option to upgrade a system, made other minor changes
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/22/2004
 */

public abstract class MachineSystem implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** the laser system's repair level */
private float repair = 1.00f;

//////////////////////////////////////////////////////////////////////////////
//constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a machine system.
 * 
 * 		@since		1.0
 */

public MachineSystem ()
{
}


//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Applies points of damage and returns unused points of damage.
 * 
 *		@param		d		the number of points of damage to apply
 * 
 *		@return		unused points, if any
 * 
 * 		@since		1.0
 */

public float applyDamage (float d)
{
	// bounds checking
	if (d < 0.0) d = 0.0f;
	
	repair -= d;
	
	if (repair < 0.0f)
	{
		float value = Math.abs (repair);
		repair = 0.0f;
		return value;
	}
	else
	{
		return 0.0f;
	}
}

/**		Provides a string to identify the system.
 * 
 * 		@return		a string to identify the system
 * 
 * 		@since		2.0
 */

public abstract String getName ();

/**		Returns the repair status of the system.
 * 
 * 		@return		the repair status
 * 
 * 		@since		1.0
 */
public float getRepair ()
{
	return repair;
}

/**		Repairs points of damage and returns unused repair points.
 * 
 * 		@param		r		the number of points to repair
 * 
 * 		@return		unused points, if any
 * 
 * 		@since		1.0
 */
public float repairDamage (float r)
{
	// bounds checking
	if (r < 0.0) r = 0.0f;
	
	repair += r;
	
	if (repair > 1.0f)
	{
		float remainder = repair - 1.0f;
		repair = 1.0f;
		return remainder;
	}
	else
	{
		return 0.0f;
	}
}

/**		Sets the system's repair status.
 * 
 * 		@param		r		the repair status
 * 
 * 		@since		1.0
 */
public void setRepair (float r)
{
	// bounds checking
	if (r < 0.0) r = 0.0f;
	if (r > 1.0) r = 1.00f;
	
	// change the setting
	repair = r;
}

}