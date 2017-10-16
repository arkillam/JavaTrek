package javatrek.systems;

import java.io.Serializable;

/**
 * This object represents a power generator system.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/15/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added getUpgradeName () and calculateUpgrade () functions
 * <LI>Version 2.0 - 10/22/2004 - made a few slight changes
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/22/12004
 */

public class Generator extends MachineSystem implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the id for generators */
public static final String ID = "Generator";
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the maximum allowed hourly output */
private static final int MAX_OUTPUT = 50000;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////
	
/** the amount of energy generated each hour */
private int energy_output;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a generator.
 * 
 * 		@param		output		the amount of energy generated each hour
 * 
 * 		@since		1.0
 */
public Generator (int output)
{
	super ();
	
	energy_output = output;
	
	if (energy_output > MAX_OUTPUT)
	{
		System.out.println ("Error!  Generator created with output > maximum!");
		System.exit (1);
	}
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Returns the maximum energy output the generator is capable of.
 * 
 * 		@since		1.1
 */

public int getCapacity ()
{
	return energy_output;
}

/**		Returns the name of the generator system.
 * 
 * 		@return		returns the name of the generator system
 * 
 *		@since		1.1
 */

public String getName ()
{
	return "Generator";
}

/**		Returns the hourly energy output, including the effects of any
 * 		damage to the system.
 * 
 * 		@return		the generator's hourly output
 * 
 * 		@since		1.0
 */

public int getOutput ()
{
	return (int)(energy_output * getRepair ());
}

/**		Sets the hourly output of the generator.
 * 
 *		@param		output	the hourly output of the generator
 * 
 *		@since		1.0
 */
public void setOutput (int output)
{
	// bounds checking
	if (output < 1) output = 1;
	if (output > MAX_OUTPUT) output = MAX_OUTPUT;
	
	// change the setting
	energy_output = output;
}

}