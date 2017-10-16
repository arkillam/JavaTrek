package javatrek;

import java.io.Serializable;

/**
 * This object represents a pilot.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/24/2002 - the original
 * <LI>Version 1.1 - 11/24/2002 - moved the ship upgrade aspect of the addXP function to the object_machine class and added the serializable interface
 * <LI>Version 1.2 - 10/07/2004 - removed tracking of the size of the crew
 * <LI>Version 2.0 - 11/12/2004 - converted the Crew class to a Pilot class and implemented skills
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/12/2004
 */

public class Pilot implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the number of skills a pilot can have (equal to the highest int id for a skill plus one) */
public static final int HOWMANY_SKILLS = 5;

/** the number of skill points a pilot receivves upon gaining a new level */
public static final int POINTS_PER_LEVEL = 25;

/** computer hacking rating */
public static final int SKILL_HACKING = 0;

/** mechanic rating */
public static final int SKILL_MECHANIC = 1;

/** merchant rating */
public static final int SKILL_MERCHANT = 2;

/** piloting rating */
public static final int SKILL_PILOTING = 3;

/** piloting rating */
public static final int SKILL_WEAPONS = 4;

/** skill names */
public static final String SKILLS[] = {"Hacking", "Mechanic", "Merchant", "Piloting", "Weapons"};

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** indicates whether the pilot is AI-controlled */
private boolean ai;

/** the pilot's funds */
private long funds;

/** the experience level of the pilot */
private int level;

/** stores the pilot's various skill ratings */
private int skills[];

/** stores unassigned skill points */
private int unassigned;

/** the amount of experience the pilot has accrued */
private long experience;

//////////////////////////////////////////////////////////////////////////////
//  constructors
//////////////////////////////////////////////////////////////////////////////

/**		Creates a 1st level pilot with zero skill ratings.
 * 
 * 		@param		npc		if true, the pilot is AI-controlled
 * 		@param		f		the initial funds for the pilot
 * 
 *		@since		1.0
 */

public Pilot (boolean npc, int f)
{
	ai = npc;
	level = 1;
	experience = 0;
	unassigned = POINTS_PER_LEVEL;
	setFunds (f);
	
	skills = new int[HOWMANY_SKILLS];
	for (int i = 0; i < HOWMANY_SKILLS; i++)
	{
		skills[i] = 0;
	}
}

/**		Creates a pilot of a specified level, with evenly distributed skill points.
 * 
 * 		@param		npc		if true, the pilot is AI-controlled
 * 		@param		l		the level of the pilot
 * 		@param		f		the initial funds for the pilot
 * 
 *		@since		2.0
 */

public Pilot (boolean npc, int l, int f)
{
	// bounds checking
	if (l < 1) l = 1;
	
	ai = npc;
	level = l;
	experience = 0;
	setFunds (f);
	
	// for AI pilots, assign the skill points to the various skills
	if (ai == true)
	{
		// skill points available for each skill
		int sp = POINTS_PER_LEVEL * level / HOWMANY_SKILLS;
		unassigned = (POINTS_PER_LEVEL * level) - (sp * HOWMANY_SKILLS);

		skills = new int[HOWMANY_SKILLS];
		for (int i = 0; i < HOWMANY_SKILLS; i++)
		{
			skills[i] = sp;
		}
	}
	else
	{
		// create the skills
		skills = new int[HOWMANY_SKILLS];
		for (int i = 0; i < HOWMANY_SKILLS; i++)
		{
			skills[i] = 0;
		}

		// initial points are all unassigned
		unassigned = POINTS_PER_LEVEL * level;
	}
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Adds experience to the pilots total, and returns the number of levels (if any)
 * 		that the pilot gained.
 * 
 *		@param		xp		the amount of experience to add
 * 
 * 		@return		the number of levels gained
 * 
 * 		@since		1.0
 */

public int addExperience (long xp)
{
	// bounds checking
	if (xp < 0) xp = 0;
	
	// store the pilots level
	int old_level = level;

	// add the experience
	experience += xp;
	
	// set the pilot's new level
	level = calculateExperienceLevel (experience);
	
	// determine the number of levels gained
	int gained = level - old_level;
	
	// determine how many skill points have been gained
	unassigned += gained * POINTS_PER_LEVEL;
	
	// if the pilot is AI-controlled, evenly distribute the new skill points
	if ((ai == true) && (unassigned > 0))
	{
		int add = unassigned / HOWMANY_SKILLS;
		unassigned -= add * HOWMANY_SKILLS;
		
		for (int i = 0; i < HOWMANY_SKILLS; i++)
		{
			skills[i] += add;
		}
	}
	
	// return the number of levels gained
	return gained;	
}

/**		Adds funds for the pilot.
 * 
 * 		@param		f		the funds to add
 * 
 * 		@since		2.0
 */

public void addFunds (int f)
{
	if (f > 0)
	{
		funds += f;
	}
	else
	{
		System.out.println ("Pilot.addFunds () called with illegal value:  " + f);
		Exception e = new Exception ();
		e.printStackTrace ();
		System.exit (1);
	}
}

/**		Calculates the level of a pilot based on a given amount of experience.
 * 
 *		@param		xp		the experience of the pilot
 * 
 *		@return		the pilot's level
 * 
 *		@since		1.1
 */

public static int calculateExperienceLevel (long xp)
{
	if (xp < 1) return 1;
	
	if (xp > getExperienceRequired (11))
	{
		// account for the first 9 levels of experience
		xp -= 128000;
		
		// return the levels past nine
		return 11 + (int) Math.floor (xp / 64000);
	}
	
	if (xp >= getExperienceRequired (11))		return 11;
	if (xp >= getExperienceRequired (10))		return 10;
	if (xp >= getExperienceRequired (9))		return 9;
	if (xp >= getExperienceRequired (8))		return 8;
	if (xp >= getExperienceRequired (7))		return 7;
	if (xp >= getExperienceRequired (6))		return 6;
	if (xp >= getExperienceRequired (5))		return 5;
	if (xp >= getExperienceRequired (4))		return 4;
	if (xp >= getExperienceRequired (3))		return 3;
	if (xp >= getExperienceRequired (2))		return 2;
	
	return 1;
}

/**		Retrieves the amount of experience required to achieve a given level.
 * 
 *		@param		level	the level to be attained
 * 
 *		@return		the amount of experience (total) required to gain the specified level
 * 
 *		@since		1.1
 */

public static long getExperienceRequired (int level)
{
	if (level == 1)						return 0;
	else if (level == 2)				return 500;
	else if (level == 3)				return 1000;
	else if (level == 4)				return 2000;
	else if (level == 5)				return 4000;
	else if (level == 6)				return 8000;
	else if (level == 7)				return 16000;
	else if (level == 8)				return 32000;
	else if (level == 9)				return 64000;
	else if (level == 10)				return 96000;
	else if (level == 11)				return 128000;
	
	else if (level > 11)
	{
		return 128000 + ((level - 11) * 64000);
	}
	else
	{
		return -1;
	}
}

/**		Assigns a number of skill points to a skill.
 * 
 * 		@param		s		the skill to increase
 * 		@param		p		the number of points to assign
 * 
 * 		@return		true if successful, false of the request could not be satisfied
 * 
 * 		@since		2.0
 */

public boolean assignPoints (int s, int p)
{
	if (p <= unassigned)
	{
		skills[s] += p;
		
		unassigned -= p;
		
		return true;
	}
	
	return false;
}

/**		Returns the pilot's available funcs.
 * 
 * 		@return		the pilot's available funds
 * 
 * 		@since		2.0
 */

public long getFunds ()
{
	return funds;
}

/**		Returns the pilot's experience level.
 * 
 *		@return		the experience level of the pilot
 * 
 *		@since		1.0
 */

public int getLevel ()
{
	return level;
}

/**		Returns the pilot's skill level for a specified category.
 * 
 * 		@param		s		the skill to return
 * 
 * 		@return		the pilot's skill level for a specified category
 * 
 * 		@since		2.0
 */

public int getSkill (int s)
{
	return skills[s];
}

/**		Returns the name of a skill.
 * 
 * 		@param		s		the skill to return
 * 
 * 		@return		the name of a skill
 * 
 * 		@since		2.0
 */

public static String getSkillName (int s)
{
	return SKILLS[s];
}

/**		Returns the number of unassigned skill points the pilot has.
 * 
 * 		@return		the number of unassigned skill points the pilot has
 * 
 * 		@since		2.0
 */

public int getUnassigend ()
{
	return unassigned;
}

/**		Returns the experience of the pilot.
 * 
 *		@return		the experience of the pilot
 * 
 *		@since		1.0
 */

public long getXP ()
{
	return experience;
}

/**		Removes funds from the pilot's account.
 * 
 * 		@param		f		the amount of funds to withdraw
 * 
 * 		@return		true if successful, false if the funds were unavailable
 * 
 * 		@since		2.
 */

public boolean removeFunds (int f)
{
	if (f > 0)
	{
		if (f <= funds)
		{
			funds -= f;
			return true;
		}

		return false;
	}

	System.out.println ("Pilot.removeFunds () called with illegal value:  " + f);
	Exception e = new Exception ();
	e.printStackTrace ();
	System.exit (1);
	return false;
}

/**		Sets the pilots funds.
 * 
 * 		@param		f		the amount of funds to set
 * 
 * 		@since		2.0
 */

public void setFunds (int f)
{
	if (f >= 0)
	{
		funds = f;
	}
	else
	{
		System.out.println ("Pilot.setFunds () called with illegal value:  " + f);
		Exception e = new Exception ();
		e.printStackTrace ();
		System.exit (1);
	}
}

}