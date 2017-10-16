package javatrek.systems;

import java.io.Serializable;

/**
 * This object represents a shielding system.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/10/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added getUpgradeName () function
 * <LI>Version 1.3 - 10/18/2004 - added a string-driven constructor
 * <LI>Version 2.0 - 10/24/2004 - simplified the shield system
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/24/2004
 */

public class Shields extends MachineSystem implements Serializable
{

//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the id for shields */
public static final String ID = "Shields";
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the maximum level shields can have */
private static final int MAX_LEVEL = 5;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** indicates if the shieldss on or off */
private boolean shields_on;

/** the shields energy capacity */
private int capacity;

/** the type of shields system */
private int level;

/** the current shields energy supply */
private int remaining;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/** 	Creates a shielding system.
 * 
 * 		@param		l		the level of the shielding system
 * 		@param		c		the energy capacity for the shields
 * 
 * 		@since		1.0
 */

public Shields (int l, int c)
{
	super ();
	
	setLevel (l);
	
	// set initial levels
	setCapacity (c);
	setRemaining (getCapacity ());
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**  	This function adds energy to the shields; if too much is given,
 * 		the extra is returned.
 * 
 * 		@param		energy		the amount of energy to add
 * 
 * 		@return		overflow energy, if any
 * 
 * 		@since		1.0
 */

public int addEnergy (int energy)
{
	remaining += energy;
	
	int overflow = 0;
	if (remaining > capacity)
	{
		overflow = remaining - capacity;
		remaining = capacity;
		if (overflow < 0) overflow = 0;
	}
	
	return overflow;
}

/**		Returns the shield's energy capacity.
 * 
 *		@return		the shield's energy capacity
 * 
 *		@since		1.0
 */

public int getCapacity ()
{
	return capacity;
}

/**		Returns the shield's damage divider.  The damage divider is the level
 * 		of the shield system, modified by the repair of the system.  The divider
 * 		can never be less than 1.
 * 
 *		@return		the shield system's damage divider
 * 
 *		@since		1.0
 */

public int getDamageDivider ()
{
	int divider = (int) Math.floor (level * getRepair ());
	if (divider < 1) divider = 1;
	return divider;
}

/**		Returns the level of the shield system.
 * 
 * 		@return		the shield's level
 * 
 *		@since		1.0
 */
public int getLevel ()
{
	return level;
}

/**		Returns the shield's remaining energy.
 * 
 *		@return		the shield's remaining energy
 * 
 *		@since		1.0
 */

public int getRemaining ()
{
	return remaining;
}

/**		Returns the shield's status (on or off);
 * 
 *		@return		the shield's status
 * 
 *		@since		1.0
 */

public boolean getShieldsOn ()
{
	// make sure the shields should be on :)
	if ((remaining == 0) || (getRepair () < 0.01f))
	{
		shields_on = false;
	}

	return shields_on;
}

/**		Returns the name of the shielding system.
 * 
 *		@return		the shielding system's name
 * 
 *		@since		1.0
 */
public String getName ()
{
	if (level == 1)			return "Basic Shields";
	else if (level == 2)	return "Standard Shields";
	else if (level == 3)	return "Strong Shields";
	else if (level == 4)	return "Advanced Shields";	
	else if (level == 5)	return "Superior Shields";
	else
	{
		return "Error!  Invalid level!";
	} 
}

/**  	This function removes energy from the shields; if too much is asked for,
 * 		none is taken.
 * 
 * 		@param		energy		the amount of energy requested
 * 
 * 		@return		true if the energy can be removed, false otherwise
 * 
 * 		@since		1.0
 */

public boolean removeEnergy (int energy)
{
	if (energy < 0) return false;
	if (energy > remaining) return false;
	
	remaining -= energy;
	
	// if shield energy reaches zero, the shields turn off
	if (remaining <= 0)
	{
		// turn off the shields
		shields_on = false;
	}
	
	return true;
}

/**		Sets the shield's energy capacity.
 * 
 *		@param		c		the shield's energy capacity
 * 
 *		@since		1.0
 */

public void setCapacity (int c)
{
	// bounds checking
	if (c < 1) c = 1;
	
	// change the setting
	capacity = c;
}

/**		Sets the level of shielding.
 * 
 * 		@param		l		the level of shielding
 * 
 * 		@since		1.0
 */

public void setLevel (int l)
{
	// bounds checking
	if (l < 0) l = 0;
	if (l > MAX_LEVEL) l = MAX_LEVEL;
	
	// set the level
	level = l;
}

/**		Sets the remaining shield's energy.
 * 
 *		@param		r		the shield's remaining energy
 * 
 * 		@since		1.0
 */

public void setRemaining (int r)
{
	// bounds checking
	if (r < 0) r = 0;
	if (r > capacity) r = capacity;
	
	 // if shields drop to 0, they turn off
	 if (r == 0) shields_on = false;
	
	// set the remaining energy
	remaining = r;
}

/**		Sets the shield's status to on or off.  Shields with no energy
 * 		remaining cannot be activated.
 * 
 *		@param		s		the shield's status
 * 
 *		@since		1.0
 */

public void setShieldsOn (boolean s)
{
	if ((remaining == 0) && (getRepair () < 0.01f))
	{
		shields_on = false;
	}
	else
	{
		shields_on = s;
	}
}

/**		This function determines how much damage the shields absorb.  If
 * 		the shields fail to absorb all of the damage, the remainder is
 * 		returned.
 * 
 *		@param		damage		the amount of damage inflicted on the shields
 * 
 * 		@return		damage not absorbed by the shields
 * 
 * 		@since		1.0
 */

public int takeDamage (int damage)
{
	// bounds checking
	if (damage < 1) return 0;
	
	// modify the incoming damage by the shield's damage divider
	// (damage can be reduced to one, but not zero)
	damage = damage / getDamageDivider ();
	if (damage < 1) damage = 1;
	
	// either absorb the damage, or absorb part of it and return the rest
	// (if the shields are reduced to zero, they turn off)
	if (damage <= remaining)
	{
		remaining -= damage;
		return damage = 0;
	}
	else
	{
		damage -= remaining;
		remaining = 0;
		shields_on = false;
	}

	// return damage points that were not absorbed by the shields
	return damage;
}

}