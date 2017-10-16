package javatrek.systems;

import java.io.Serializable;

/**
 * This object represents a light drive system - a system which can propel a ship
 * at faster-than-light speeds.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/13/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added getUpgradeName () function
 * <LI>Version 2.0 - 10/23/2004 - renamed the Hyperdrive to the LightDrive and updated the class' design
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/23/2004
 */

public class LightDrive extends MachineSystem implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** max speed setting */
private float max_speed = 1.00f;

/** current speed setting */
private float setting = 1.00f;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a light drive system object.
 * 
 * 		@param		max			the maximum setting this system is capable of
 * 
 * 		@since		1.0
 */

public LightDrive (float max)
{
	super ();
	
	// bounds checking
	if (max < 1.0) max = 1.0f;
	
	setting = 1.0f;
	max_speed = max;
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/** 	Applies points of damage.  This function over-rides the parent class'
 * 		function to ensure that when the repair level is lowered, the current
 * 		setting is not made impossible.
 * 
 * 		@param		d		the number of points of damage to apply
 * 
 * 		@return		unused points, if any
 * 
 *		@since		1.0
 */

public float applyDamage (float d)
{
	d = super.applyDamage (d);

	float lim = getAvailable ();
	
	if (setting > lim)
	{
		setting = lim;	
	}
	
	return d;
}

/** 	Returns the light drive's maximum available setting.
 * 
 * 		@return		the light drive's maximum available setting
 * 
 * 		@since		1.0
 */

public float getAvailable ()
{
	return (float)(max_speed * getRepair ());
}

/** 	Returns the light drive's maximum setting.
 * 
 * 		@return		the light drive's maximum setting
 * 
 * 		@since		1.0
 */

public float getMax ()
{
	return max_speed;
}

/**  	Returns the name of the light drive system, as a string.
 * 
 * 		@return		returns the name of the light drive system
 * 
 * 		@since		1.1
 */

public String getName ()
{
	return "Light Drive";
}


/** 	Returns the current light drive setting.
 * 
 * 		@return		the current setting
 * 
 * 		@since		1.0
 */

public float getSetting ()
{
	// check that the setting is possible
	if (setting > getAvailable ())
	{
		setting = getAvailable ();
	}
	
	return setting;
}

/** 	Sets the light drive's maximum speed setting.
 * 
 * 		@param		s		maximum speed setting
 * 
 * 		@since		1.0
 */

public void setMax (float s)
{
	// bounds checking
	if (s < 1.0) s = 1.0f;
	
	// change the setting
	max_speed = s;
}

/** 	Sets the current light drive setting.
 * 
 * 		@param		c		the setting
 * 
 * 		@since		1.0
 */

public void setSetting (float c)
{
	// bounds checking (damaged engines can't go full speed)
	float limit = getAvailable ();
	if (c > limit)	c = limit;
	if (c < 1.0)	c = 1.0f;
	
	// change the setting
	setting = c;
}

}