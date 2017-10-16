package javatrek;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <P>This object keeps track of various actions the player has taken, 
 * 
 * <UL>
 * <LI>Version 1.0 - 04/04/2002 - the original class
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 2.0 - 11/09/2004 - changed the class name from Stats to Statistics, removed the HTML output functions and changed the storage method from specific int counters to a hash table which uses string-named fields provided by this class (kills use machine class names to specify which type was killed)
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/09/2004
 */

public class Statistics implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** id for points scored for kills*/
public static final String KILL_POINTS = "Points earned for Kills";
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** stores kill data by ship type */
private HashMap kills;

/** stores other data (shots fired, shots hit etc) */
private HashMap other;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**  Creates a statistics object.
 * 
 *		@since	1.0
 */

public Statistics ()
{
	// create the hash maps
	kills = new HashMap ();
	other = new HashMap ();
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Adds an object to the kills hash map.
 * 
 * 		@param		object		the type of object killed
 * 
 * 		@since		1.0
 */

public void addKill (String object)
{
	// input check
	if (object == null)
	{
		Exception e = new Exception ();
		System.out.println ("Statistics.addKill () called with a null argument.");
		e.printStackTrace ();
		System.exit (1);
	}
	
	// entries exist for this type, so increment the number of kills
	if (kills.containsKey (object) == true)
	{
		// get the count
		int count = ((Integer) kills.get (object)).intValue ();
		
		// increment it
		count += 1;
		
		// put the count back
		kills.put (object, new Integer (count));		
	}
	// no entries for this type, so create an initial one
	else
	{
		// add the object type to the hash table, with an initial kill tallied
		kills.put (object, new Integer (1));
	}
}

/**		Adds an object to the other hash map.
 * 
 * 		@param		object		the type of object killed
 * 
 * 		@since		2.0
 */

public void addOther (String object)
{
	// input check
	if (object == null)
	{
		Exception e = new Exception ();
		System.out.println ("Statistics.addOther () called with a null argument.");
		e.printStackTrace ();
		System.exit (1);
	}
	
	// entries exist for this type, so increment the number of kills
	if (other.containsKey (object) == true)
	{
		// get the count
		int count = ((Integer) other.get (object)).intValue ();
		
		// increment it
		count += 1;
		
		// put the count back
		other.put (object, new Integer (count));		
	}
	// no entries for this type, so create an initial one
	else
	{
		// add the object type to the hash table, with an initial kill tallied
		other.put (object, new Integer (1));
	}
}

}
