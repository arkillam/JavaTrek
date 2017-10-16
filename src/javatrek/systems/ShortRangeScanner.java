package javatrek.systems;

import java.io.Serializable;

import javatrek.spaceobjects.Asteroid;
import javatrek.spaceobjects.Base;
import javatrek.spaceobjects.Machine;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.Star;

/**
 * This object represents a short-range scanner.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/16/2002 - this
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 04/29/2003 - added getUpgradeName () function
 * <LI>Version 1.3 - 10/18/2004 - added a string-driven constructor
 * <LI>Version 2.0 - 10/23/2004 - removed upgrade options, made other small changes
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/23/2004
 */

public class ShortRangeScanner extends MachineSystem implements Serializable
{
	
/////////////////////////////////////////////////////////////////////////////
//  public constants
/////////////////////////////////////////////////////////////////////////////

/** the id for impulse drives */
public static final String ID = "SR Scanner";

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the maximum level a short-range scanner can have */
private static final int MAX_LEVEL = 5;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** the level of scanner */
private int level;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a short-range scanner.
 * 
 * 		@param		l			the level of scanner
 * 
 * 		@since		1.0
 */

public ShortRangeScanner (int l)
{
	super ();
	
	setLevel (l);	
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Returns the name of the short-range scanner.
 * 
 *		@return		the short-range scanner's name
 * 
 *		@since		1.0
 */

public String getName ()
{
	if (level == 1)				return "Basic SR Scanner";
	else if (level == 2)		return "Improved SR Scanner";
	else if (level == 3)		return "Enhanced SR Scanner";
	else if (level == 4)		return "Advanced SR Scanner";
	else if (level == 5)		return "Eagle SR Scanner";
	else
	{
		return "Error! Invalid scanner level!";
	} 
}

/**		Sets the level of the scanner.
 * 
 * @param		l		the level of the scanner
 * 
 * @since		1.0
 */

public void setLevel (int l)
{
	// bounds checking
	if (l < 1) l = 1;
	if (l > MAX_LEVEL) l = MAX_LEVEL;
	
	// change the setting
	level = l;
}

/**		Returns a string describing any object.  The better the scanner, and the less
 * 		damage the scanner has, the more information the user receives.
 * 
 * 		@param		object		the object to scan
 * 
 * 		@since		2.0
 */

public String scan (Object object)
{
	// get the object's class name
	String cn = object.getClass ().getName ();
	
	// ships are the most interesting
	if (cn.compareTo (Ship.class.getName ()) == 0)
	{
		return scanMachine ((Machine) object);
	}
	// then bases
	else if (cn.compareTo (Base.class.getName ()) == 0)
	{
		// TODO:  write a function for this
		return "Base:  work to do\n";
	}
	// then machines
	else if (cn.compareTo (Machine.class.getName ()) == 0)
	{
		return scanMachine ((Machine) object);
	}
	// then asteroids and stars
	else if ((cn.compareTo (Star.class.getName ()) == 0) || (cn.compareTo (Asteroid.class.getName ()) == 0))
	{
		// TODO:  write a function for this
		return "Star/Asteroid:  work to do\n";
	}
	// others are an error
	else
	{
		return "ShortRangeScanner.scan ():  invalid object\n";
	}	
}

/**  	Returns a string describing a machine.  The better the scanner, and the less
 * 		damage the scanner has, the more information the user receives.
 * 
 *		@param	machine		the machine to be scanned
 * 
 *		@since	1.0
 */

public String scanMachine (Machine machine)
{
	// retrieve the machine's relevant systems
	Shields shields = (Shields) machine.getSystem (Shields.class.getName ());
	
	// use a string buffer
	StringBuffer buffer = new StringBuffer (200);
	
	// get the scanner's effective level
	int eff_level = (int) Math.floor (level * getRepair ());
	
	if (eff_level < 1)
	{
		return "Short-Range scanner is completely non-functional.\n";
	}
	
	// determine what is shown
	// (the order of effective level is scrambled below to allow
	// data to be shown in a more useful order)
	
	// 1:  name and team
	if (eff_level >= 1) 
	{
		// the name of the ship
		buffer.append (machine.getName ());
		
		// the team of the ship
		buffer.append (" (Team:  ");
		buffer.append (machine.getTeamName ());
		buffer.append (")\n");		
		
		// the dodge rating of the ship
		buffer.append (" (Dodge:  ");
		buffer.append (machine.getDodge ());
		buffer.append (")\n");				
	}	
	
	// 2:  shields, as a percentage
	if ((eff_level >= 2) && (eff_level < 5))
	{
		// the shield's remaining power, as a percentage		
		int percentage = (int)(((float) shields.getRemaining () / (float) shields.getCapacity ()) * 100);
		buffer.append ("");
		buffer.append ("Shield Energy: ");
		buffer.append (percentage);
		buffer.append ("%\n");
	}

	// 3:  hit points, as a percentage
	if ((eff_level >= 3) && (eff_level < 5))
	{
		// the remaining hit points, as a percentage
		buffer.append ("Hit Points:  ");
		int percentage = (int)(((float)machine.getHP () / (float)machine.getHPMax()) * 100);
		buffer.append (percentage);
		buffer.append ("%\n");
	}
	
	// 4:  energy remaining, as a percentage
	if ((eff_level >= 4) && (eff_level < 5)) 
	{
		// energy level, as a percentage	
		int percentage = (int)(((float)machine.getEnergyRemaining () / (float)machine.getEnergyMax () ) * 100);
		buffer.append ("");
		buffer.append ("Energy Remaining: ");
		buffer.append (percentage);
		buffer.append ("%\n");
	}

	// 5:  shield type and energy, hit points, energy remaining
	if (eff_level >= 5) 
	{
		buffer.append ("Shield Type:  ");
		buffer.append (shields.getName ());
		buffer.append ("\n");
		buffer.append ("Shield Energy:  ");
		buffer.append (shields.getRemaining ());
		buffer.append (" / ");
		buffer.append (shields.getCapacity ());
		buffer.append ("\n");
				
		buffer.append ("Main Energy Energy:  ");
		buffer.append (machine.getEnergyRemaining ());
		buffer.append (" / ");
		buffer.append (machine.getEnergyMax ());
		buffer.append ("\n");		

		buffer.append ("Hit Points:  ");
		buffer.append (machine.getHP ());
		buffer.append (" / ");
		buffer.append (machine.getHPMax ());
		buffer.append ("\n");		
	}
	
	// return the string
	return buffer.toString ();
}

}