package javatrek.spaceobjects;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javatrek.JavaTrek;
import javatrek.Pilot;
import javatrek.Space;
import javatrek.systems.Computer;
import javatrek.systems.Generator;
import javatrek.systems.LongRangeScanner;
import javatrek.systems.MachineSystem;
import javatrek.systems.Shields;

/**
 * This is a basic object which can be extended to represent the various types of
 * mechanical systems:  space ships, star bases etc.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/10/2002 - the original
 * <LI>Version 1.1 - 11/24/2002 - moved the addXP () function from the crew class to this class and added the serializable interface
 * <LI>Version 1.2 - 11/27/2002 - moved dodge rating here from object_ship class
 * <LI>Version 1.3 - 10/18/2004 - removed shuttle and transporter systems, as no ships actually have them and the game does nothing with them at this point
 * <LI>Version 2.0 - 11/25/2004 - moved the systems into a hashmap, improved the code in many areas
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/25/2004
 */

public class Machine extends SpaceObject implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/**  identifies damage caused by non-ion energy weapons */
public static final int ENERGY_DAMAGE = 1001;

/**  identifies damage caused by ion energy weapons */
public static final int ION_DAMAGE = 1002;

/**  the multiplier for the amount of system damage caused by ion-based weapons */
public static final int ION_MULTIPLIER = 10;

/**  identifies damage caused by non-ion projectile weapons */
public static final int PROJECTILE_DAMAGE = 1003;

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//  public fields
//////////////////////////////////////////////////////////////////////////////

/** the machine's pilot */
private Pilot pilot;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** computer-controlled by default */
private boolean ai = true;

/** number of points of repair available each hour */
private float repair_points = 0.0f;

/** stores the ship's systems */
private HashMap systems;

/** the ship's innate ability to avoid being hit */
private int dodge_ability = 0;

/** the energy carrying capacity */
private int energy_max = 1;

/** the current energy supply */
private int energy_remaining = 1;

/** the value of the machine (for a kill) */
private int point_value = 1;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates an instance of the Machine class.
 * 
 * 		@param		n		the machine's name
 * 		@param		in		the machine's image's name
 * 		@param		t		the team the object is aligned with
 * 		@param		hp		the remaining hit points the object has
 * 		@param		quad	the quadrant the machine is located in
 * 		@param		qx		the quadrant x-co-ordinate
 * 		@param		qY		the quadrant y-co-ordinate
 * 		@param		rx		the region x-co-ordinate
 * 		@param		ry		the region y-co-ordinate
 * 
 * 		@since		2.0
 */

public Machine (String n, String in, int t, int hp, int quad, int qx, int qy, int rx, int ry)
{
	super (n, in, t, hp, quad, qx, qy, rx, ry);
	
	// create the systems hashmap
	systems = new HashMap (12);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Adds energy to the machine; if too much is given, the extra is returned.
 * 
 * 		@param		e		the amount of energy added
 * 
 * 		@return		overflow energy, if any
 * 
 * 		@since		1.0
 */

public int addEnergy (int e)
{
	energy_remaining += e;

	int overflow = 0;
	if (energy_remaining > energy_max)
	{
		overflow = energy_remaining - energy_max;
		energy_remaining = energy_max;
		if (overflow < 0) overflow = 0;
	}

	return overflow;
}

/**		Adds experience to the machine's pilot and checks for level gains.
 * 
 * 		@param		xp		the amount of experience to add
 * 
 * 		@since		2.0
 */

public void addPilotExperience (long xp)
{
	// TOOD:  implement
}

/**		Adds or replaces a system (a machine can only hold one of each type).
 * 
 * 		@param		s		the system to add
 * 
 * 		@since		2.0
 */

public void addSystem (MachineSystem s)
{
	systems.put (s.getClass ().getName (), s);
}

/**		Applies damage to the machine's systems.
 * 
 * 		@param		damage			the number of points of damage to do
 * 		@param		cause			the type of damage caused
 * 
 * 		@since		1.0
 */

public void damageSystems (int damage, int cause)
{
	// bounds checking
	if (damage < 1) return;
	
	// apply projectile damage a point at a time
	int lots = 1;
	if (cause == PROJECTILE_DAMAGE)
	{
		lots = 1;
	}
	// apply energy damage in lots of 25
	else if ((cause == ENERGY_DAMAGE) || (cause == ION_DAMAGE))
	{
		lots = 25;
	}
	// catch invalid input
	else
	{
		System.out.println ("Machine.damageSystems () called with invalid cause:  " + cause);
		Exception e = new Exception ();
		e.printStackTrace ();
		System.exit (1);
	}
		
	// convert the machine's systems into an array
	MachineSystem sys[] = new MachineSystem[systems.size ()];
	int index = 0;
	Iterator it = systems.keySet ().iterator ();
	while (it.hasNext ())
	{
		sys[index++] = (MachineSystem) systems.get (it.next ());
	}

	// determine the number of times to apply damage, based
	// on the size of a lot of damage and the amount of total
	// damage to apply	
	float remainder = (damage % lots) / 100.0f;
	float points = lots / 100.0f;
	int rounds = damage / lots;
		
	// apply the rounds of damage
	for (int i = 0; i < rounds; i++)
	{
		// choose a system and damage it
		// (0% systems may be hit further, to no effect)
		int x = ((int)(Math.random () * 100)) % sys.length;
		sys[x].applyDamage (points);
	}

	// apply any remaining damage		
	if (remainder > 0)
	{
		// choose a system and damage it
		// (0% systems may be hit further, to no effect)
		int x = ((int)(Math.random () * 100)) % sys.length;
		sys[x].applyDamage (remainder);
	}
}

/**		This function finds the target closest to the machine.  This is used to
 * 		determine which target is the most likely to be hit (projectile),
 * 		or will take the most damage (energy weapon).
 * 
 * 		@return		the closest target, or null if none exit
 * 
 * 		@since		2.0
 */

public SpaceObject findClosestTarget ()
{
	// TODO:  logic will likely be required to rule out indestructable objects
	
	// determine which team the machine is a member of
	int team = getTeam ();
	
	// get the machine's location
	int quadrant = getQuadrant ();
	Point qloc = getQuadrantLocation ();
		
	// get a list of potential targets
	SpaceObject targets[] = JavaTrek.game.gamedata.space.getInRegion (quadrant, qloc, team, null);
	SpaceObject t = null;
	if (targets != null)
	{
		for (int i = 1; i < targets.length; i++)
		{
			if (t != null)
			{
				double current = getRegionLocation ().distance (t.getRegionLocation ());
				double next = getRegionLocation ().distance (targets[i].getRegionLocation ());
				
				if (next < current)
				{
					t = targets[i];
				}
			}
			else
			{
				t = targets[i];
			}
		}				
	}

	return t;	
}

/**		Handles the power generation.  Power generation is split evenly
 * 		between main and shield energy, with overflow given to the other
 * 		system, if possible.
 * 
 * 		@param		h		the number of hours worth of energy to generate
 * 
 * 		@since		1.0
 */

private void generatePower (int h)
{
	// bounds checking
	if (h < 1) return;
	
	// get the machine's generator
	Generator generator = (Generator) getSystem (Generator.class.getName ());
	Shields shields = (Shields) getSystem (Shields.class.getName ());
	
	// power generation only occurs if the ship has a generator
	if (generator == null) return;
	
	// local variables
	int power = generator.getOutput () * h;
	
	// add up to half the power to shields
	if (shields != null)
	{
		// at most, half the power will be added
		int half = power / 2;
		
		// determine how much energy is missing from the shields
		int space = shields.getCapacity () - shields.getRemaining ();
		
		// if energy is missing, add some
		if (space > 0)
		{
			// which is less, half the power or the amount required?
			int add = Math.min (half, space);
			
			// add the amount determined
			shields.setRemaining (shields.getRemaining () + add);
			
			// remove the amount added from the power available
			power -= add;
		}
	}
	
	// add power to the main energy supply
	setEnergyRemaining (getEnergyRemaining () + power);
}

/**		Returns whether or not the machine is computer-controlled.
 * 	
 * 		@return		the ai setting for the ship
 * 
 * 	@since		1.0
 */

public boolean getAI ()
{
	return ai;
}

/**		Returns the ship's dodge ability, including the pilot's skill.
 * 
 *		@return		the ship's dodge ability, including the pilot's skill
 * 
 *		@since		1.2
 */

public int getDodge ()
{
	return dodge_ability + pilot.getSkill (Pilot.SKILL_PILOTING);
}

/**		Returns the  energy carrying capacity.
 * 
 *		@return		the  energy carrying capacity
 * 
 *		@since		1.0
 */

public int getEnergyMax ()
{
	return energy_max;
}

/**		Returns the  energy level.
 * 
 *		@return		the  energy level
 * 
 *		@since		1.0
 */

public int getEnergyRemaining ()
{
	return energy_remaining;
}

/**		Returns the pilot.
 * 
 * 		@return		the pilot object
 * 
 * 		@since		2.0
 */

public Pilot getPilot ()
{
	return pilot;
}

/** 	Returns the machine's point value.
 * 
 *		@return		the machine's point value
 * 
 *		@since		1.0
 */

public int getPointValue ()
{
	// TODO:  incorporate the pilot's level in this returned value
	
	return point_value;
}

/**		Returns the number of repair points a ship has available each hour, including
 * 		an extra point for every ten points of mechaincal skill the pilot has.
 * 
 *		@return		the number of repair points
 * 
 *		@since		1.0
 */

public float getRepairPoints ()
{
	// if the machine has a pilot, add the pilot's mechanical skill
	// to the base repair rate
	float percentage = 0.0f;
	if (pilot != null)
	{
		percentage = ((pilot.getSkill (Pilot.SKILL_MECHANIC)) / 100.0f);
	}
	
	return repair_points + percentage;
}


/**		Retrieves one of the machine's systems.
 * 
 * 		@param		s		the system type to retrieve
 * 
 * 		@return		the specified system, or null if one is not installed
 * 
 * 		@since		2.0
 */

public MachineSystem getSystem (String s)
{
	return (MachineSystem) systems.get (s);
}

/**		Retrieves the complete set of systems owned by the machine.
 * 
 * 		@return		the machine's set of systems (null if none exist)
 * 
 * 		@since		2.0
 */

public MachineSystem[] getSystems ()
{
	if (systems.size () > 0)
	{
		int size = systems.size ();
		
		MachineSystem ms[] = new MachineSystem[size];
		
		int i = 0;
		Iterator it = systems.keySet ().iterator ();
		while (it.hasNext ())
		{
			MachineSystem sys = (MachineSystem) systems.get (it.next ());
			ms[i++] = sys;
		}
		
		return ms; 
	}
	else
	{
		return null;
	}
}

/**		Scans the area in a cube centred on the machine.
 * 
 *		@since		2.0
 */

public void longRangeScan ()
{
	Point q = getQuadrantLocation ();
	longRangeScan (q.x, q.y);
}

/**		Scans the area in a cube centred on the given location.
 * 
 *		@param		x		the x-co-ordinate to centre on
 * 		@param		y		the y-co-ordinate to centre on
 * 
 * 		@since		2.0
 */

public void longRangeScan (int x, int y)
{
	// bounds checking
	if (x >= Space.QUADRANT_WIDTH) return;
	if (y < 0) return;
	if (y >= Space.QUADRANT_HEIGHT) return;
	if (x < 0) return;

	// get the required systems
	Computer computer = (Computer) getSystem (Computer.class.getName ());
	LongRangeScanner lr = (LongRangeScanner) getSystem (LongRangeScanner.class.getName ());
	
	// this is only necessary if a memory system exists to store the data
	if ((computer != null) && (lr != null))
	{
		int r = lr.getRadius ();
		
		for (int i = x - r; i <= x + r; i++)
		{
			for (int j = y - r; j <= y + r; j++)
			{
				computer.setKnown (i, j, true);
			}
		}
	}
}

/**		Transfers as much energy as possible from shields to main energy.
 * 
 *		@since 2.0
 */

public void maxEnergy ()
{
	// get the shield system
	Shields shields = (Shields) getSystem (Shields.class.getName ());
	
	// only works if a shield system is present
	if (shields != null)
	{
		// space for more energy in the main supply
		int space = getEnergyMax () - getEnergyRemaining ();
		
		// energy available in the shield systems
		int available = shields.getRemaining ();
		
		// remove min (space, available)
		int amount = Math.min (space, available);
		
		// remove the energy from shields
		shields.removeEnergy (amount);
		
		// add the energy to main energy
		addEnergy (amount);
	}
}

/**		Transfers as much energy as possible from main energy to shields.
 * 
 * 		@since 1.2
 */
public void maxShields ()
{
	// get relevant systems
	Shields shields = (Shields) getSystem (Shields.class.getName ());
	
	// only works if a shield system is present
	if (shields != null)
	{
		// space for more energy in the shield system
		int space = shields.getCapacity () - shields.getRemaining ();
		
		// energy available in main energy
		int available = getEnergyRemaining ();
		
		// remove min (space, available)
		int amount = Math.min (space, available);
		
		// remove the energy from main energy
		removeEnergy (amount);
				
		// add the energy to the shield system
		shields.addEnergy (amount);
	}
}

/**		Passes time in the game, and handles all the effects time passing has.
 * 		Note that passTime (0) is valid - it is called for quick actions
 * 		(like firing a laser) which take up a trivial amount of time, but should
 * 		allow other game entities a chance to react.
 * 
 *		@param		h			the number of hours to pass
 * 
 *		@since		2.0
 */

public void passTime (int h)
{
	// scan the area
	longRangeScan ();
	
	// complete h hours of repairs
	repairs (h);
	
	// generate energy
	generatePower (h);
}

/**		Removes energy units from the main energy supply.  Returns false if this failed.
 * 
 *		@param		energy		the energy to be removed
 * 
 *		@since		1.0
 */

public boolean removeEnergy (int energy)
{
	// bounds checking
	if (energy < 0) return false;
	if (energy > energy_remaining) return false;
					
	// subtract the energy
	energy_remaining -= energy;
	
	// operation successful
	return true;
}

/**		Completes repair work.
 * 
 * 		@param		h		the number of hours of repair work to perform
 * 
 *		@since		2.0
 */

public void repairs (int h)
{
	// TODO:  add bonus to repair points for being docked
	
	// bounds checking
	if (h < 1) return;
	
	// get the number of repair points available
	float rp = getRepairPoints () * h;
	
	// create and populate an array of systems which require repairs
	ArrayList in_need = new ArrayList (systems.size ());
	Iterator it = systems.keySet ().iterator ();
	while (it.hasNext ())
	{
		MachineSystem sys = (MachineSystem) systems.get (it.next ()); 
		if (sys.getRepair () < 1.0f)
		{
			in_need.add (sys);
		}
	}
	
	// while repairs are required and repair points are available,
	// repair the machine
	while ((rp >= 0.01f) && ((in_need.size () > 0) || (getHPMax () > getHP ())))
	{
		// repair a point of the machine's HP, if necessary
		if ((getHPMax () > getHP ()))
		{
			rp -= 0.01f;
			setHP (getHP () + 1);
		}
		
		// if repair points and work remain, continue on
		if ((rp > 0.0f) && (in_need.size () > 0))
		{
			// repair a point of damage for as many systems as possible
			int loop = Math.min ((int)(rp * 100), in_need.size ());
			for (int i = 0; i < loop; i++)
			{
				MachineSystem sys = ((MachineSystem) in_need.get (i));
				sys.setRepair (sys.getRepair () + 0.01f);
				rp -= 0.01f;
			}
		}
		
		// re-populate the in-need array
		in_need = new ArrayList (systems.size ());
		it = systems.keySet ().iterator ();
		while (it.hasNext ())
		{
			MachineSystem sys = (MachineSystem) systems.get (it.next ()); 
			if (sys.getRepair () < 1.0f)
			{
				in_need.add (sys);
			}
		}
	}
}

/**		Sets whether or not the machine is computer-controlled.
 * 
 *		@param		is_ai	whether the machine is computer controlled
 * 
 *		@since		1.0
 */

public void setAI (boolean is_ai)
{
	ai = is_ai;
}

/**		Sets the ship's dodge ability.
 * 
 * 		@param		dodge	the ship's new dodge ability
 * 
 * 		@since		1.2
 */

public void setDodge (int dodge)
{
	if (dodge >= 0)
	{
		dodge_ability = dodge;
	}
}

/** 		Sets the  maximum energy capacity.
 * 
 * 		@param		c		the energy capacity
 * 
 * 		@since		1.0
 */

public void setEnergyMax (int c)
{
	// bounds checking
	if (c < 1) c = 1;
	
	// change the setting
	energy_max = c;
}

/**		Sets the machine's remaining energy.
 * 
 *		@param		c		the energy level
 * 
 *		@since		1.0
 */
public void setEnergyRemaining (int c)
{
	// bounds checking
	if (c < 0) c = 0;
	if (c > energy_max) c = energy_max;
	
	// change the setting
	energy_remaining = c;
}

/**		Sets the pilot.
 * 
 * 		@param		p		the new pilot object
 * 
 * 		@since		2.0
 */

public void setPilot (Pilot p)
{
	pilot = p;
}

/**		Sets the point value for killing this machine.
 * 
 *		@param		v		the machine's point value
 * 
 *		@since		1.0
 */

public void setPointValue (int v)
{
	if (v > 0)
	{
		point_value = v;
	}
	else
	{
		point_value = 0;
	}
}

/**		Sets the ship's number of repair points.
 * 
 *		@param		r		the number of repair points
 * 
 *		@since		1.0
 */

public void setRepairPoints (float r)
{
	// bounds checking
	if (r < 0.0) r = 0.0f;
	
	// change the setting
	repair_points = r;
}

/**		This function handles damage done to a machine (regardless of source).
 * 
 *		@param		damage		the amount of damage taken
 *		@param		cause		the cause/type of the damage
 * 
 *		@return		true if the ship survives, false if it is destroyed
 * 
 *		@since		1.0
 */

public boolean takeDamage (int damage, int cause)
{
	// bounds checking
	if (damage < 1) return true;
	
	// store the amount of damage done, as it may change below
	// and is required for system damage as well
	int points = damage;
	
	// get the machine's shield system
	Shields shields = (Shields) getSystem (Shields.class.getName ());
	
	// first, check to seee if the shields can absorb some or
	// all of the damage
	if ((shields != null) && (shields.getShieldsOn () == true))
	{
		// apply damage to the shields
		damage = shields.takeDamage (damage);
	}
	
	// next, if damage remains, apply it to the hull and the machine's
	// systems
	if (damage > 0)
	{
		// damage the hull
		setHP (getHP () - damage);
	}	
	
	// finally apply damage to the machine's systems ...
	
	// ION damage always occurs, regardless of shielding, is not
	// reduced by shields, and is multiplied by the ION_MULTIPLIER
	// value
	if (cause == ION_DAMAGE)
	{
		damageSystems (points * ION_MULTIPLIER, ION_DAMAGE);
	}
	// if shields are on but have a damage divider of one, all damage
	// is applied to systems
	else if ((shields != null) && (shields.getDamageDivider () < 2))
	{
		damageSystems (points, cause);
	}
	// otherwise, only damage that got through the shields is applied
	// to the system
	else
	{
		damageSystems (damage, cause);
	}
	
	// return true if the ship survived, false otherwise
	if (getHP () > 0)
	{
		return true;
	}
	else
	{
		return false;
	}
}

}
