package javatrek.systems;

import java.io.Serializable;

import javatrek.Space;

/**
 * Represents a machine's computer system.
 * 
 * Once a computer reaches level two, damage to it no longer clears parts
 * of the memory.
 * 
 * Once a computer reaches level three, it can plot increasingly accurate
 * long-distance travel routes, reducing the energy used by a blast drive.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/16/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 11/26/2002 - converted to constants and look-up functions
 * <LI>Version 1.3 - 04/18/2003 - restored the computer as a true object and merged it with the memory system
 * <LI>Version 1.4 - 04/29/2003 - added getUpgradeName () function
 * <LI>Version 1.5 - 05/04/2003 - added multiple computer types
 * <LI>Version 1.6 - 10/15/2004 - added a string-driven constructor
 * <LI>Version 2.0 - 10/23/2004 - simplified the computer system
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/23/2004
 */

public class Computer extends MachineSystem implements Serializable
{
	
// TODO:  add support for multiple quadrants
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the id for computers */
public static final String ID = "Computer";

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the maximum level a computer can have */
private static final int MAX_LEVEL = 5;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** tracks of which regions are known */
private boolean region_known[];

/** stores the computer's level */
private int level;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**  	Creates a computer.
 * 
 * 		@param		l		the level of computer
 * 
 *		@since		1.3
 */

public Computer (int l)
{
	super ();
	
	// set the computer level
	setLevel (l);
	
	// initially, no regions are known
	region_known = new boolean[Space.QUADRANT_WIDTH * Space.QUADRANT_HEIGHT];
	setAll (false);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/** 	Applies points of damage.  This function over-rides the parent class'
 * 		function to add extra effects.  When the computer's status is lowered,
 * 		a corresponding number of regions flags are reset to false to simulate
 * 		memory loss.  Once the computer is of level two or better, the memory
 * 		loss no longer happens
 * 
 * 		@param		d		the number of points of damage to apply
 * 
 * 		@return		unused points, if any
 * 
 *		@since		1.0
 */

public float applyDamage (float d)
{
	// bounds checking
	if (d < 0.0) d = 0.0f;
	
	// randomly blank "difference" number of locations
	// (if the computer is susceptible to memory damage)
	if (level < 2)
	{
		int damage = (int) Math.ceil ((double)((d) * 100));
		for (int i = 0; i < damage; i++)
		{
			int bx = ((int)(Math.random () * 100)) % 10;
			int by = ((int)(Math.random () * 100)) % 10;
			
			setKnown (bx, by, false);
		}
	}
	
	return super.applyDamage (d);
}

/** 	Gets whether a specific region's contents is known or not.
 * 
 * 		@param		qx			the x-co-ordinate for the region
 * 		@param		qy			the y-co-ordinate for the region
 * 
 * 		@since		1.3
 */
public boolean getKnown (int qx, int qy)
{
	// bounds checking
	if (qx < 0) qx = 0;
	if (qx >= Space.QUADRANT_WIDTH) qx = Space.QUADRANT_WIDTH - 1;
	if (qy < 0) qy = 0;
	if (qy >= Space.QUADRANT_HEIGHT) qy = Space.QUADRANT_HEIGHT - 1;
	
	return region_known[qx + (qy * Space.QUADRANT_WIDTH)];
}

/**		Returns the name of the computer system, as a string.
 * 
 *		@return		returns the name of the specified computer system
 * 
 *		@since		1.3
 */

public String getName ()
{
	if (level == 1)				return "Intel Computer";
	else if (level == 2)		return "AMD Computer";
	else if (level == 3)		return "Sparc Computer";
	else if (level == 4)		return "Power4 Computer";
	else if (level == 5)		return "Power5 Computer";
	else
	{
		return "Error!  Invalid level!";
	} 
}

/**		Returns the percentage of energy saved during blaast drive trips.  
 * 		Once a computer reaches level three, it can plot increasingly
 * 		accurate long-distance travel routes, reducing the energy used by
 * 		a blast drive.
 * 
 * 		@return		the percentage of energy saved
 * 
 * 		@since		1.5
 */

public float getSaved ()
{
	if (level < 3)
	{
		return 0.0f;
	}
	else
	{
		return (float) (level * 0.1f);
	}
}

/**  	Sets all of the regions to a given value.
 * 
 * 		@param		value		the value to apply
 * 
 * 		@since		1.3
 */

public void setAll (boolean value)
{
	int size = region_known.length;
	for (int i = 0; i < size; i++)
	{
		region_known[i] = value;
	} 
}

/** 	Sets whether a specific region's contents are known or not.
 * 
 * 		@param		qx			the x-co-ordinate for the region
 * 		@param		qy			the y-co-ordinate for the region
 * 		@param		known		whether the contents are known or not
 * 
 * 		@since		1.3
 */
public void setKnown (int qx, int qy, boolean known)
{
	// bounds checking
	if (qx < 0) qx = 0;
	if (qx >= Space.QUADRANT_WIDTH) qx = Space.QUADRANT_WIDTH - 1;
	if (qy < 0) qy = 0;
	if (qy >= Space.QUADRANT_HEIGHT) qy = Space.QUADRANT_HEIGHT - 1;
	
	region_known[qx + (qy * Space.QUADRANT_WIDTH)] = known;
}

/**		Sets the level of the computer.
 * 
 * 		@param		l		the level to set
 * 
 * 		@since		1.5
 */

public void setLevel (int l)
{
	// bounds checking
	if (l < 0) l = 0;
	if (l > MAX_LEVEL) l = MAX_LEVEL;
	
	level = l;
}

}