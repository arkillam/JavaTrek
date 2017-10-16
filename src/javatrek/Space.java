package javatrek;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javatrek.spaceobjects.Asteroid;
import javatrek.spaceobjects.Base;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.SpaceObject;
import javatrek.spaceobjects.Star;

/**
 * <P>Tracks the game's objects that exist in space.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/22/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/22/2004
 */

public class Space implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** the height of a quadrant (in regions) */
public static final int QUADRANT_HEIGHT = 10;

/** the width of a quadrant (in regions) */
public static final int QUADRANT_WIDTH = 10;

/** the number of quadrants in the game */
public static final int QUADRANTS = 1;

/** the height of a region (in locations) */
public static final int REGION_HEIGHT = 10;

/** the width of a region (in locations) */
public static final int REGION_WIDTH = 10;

//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the number of bases in each quadrant */
private static int BASES_PER_QUADRANT = 7;

/** the initial size of the objects array */
private static int MAX_OBJECTS = 5000;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** stores the game's space objects */
private ArrayList objects;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a space object and the player's ship.
 * 
 * 		@param		ship	the player's ship
 * 
 * 		@since		2.0
 */

public Space (Ship ship)
{
	// create the array for space objects
	objects = new ArrayList (MAX_OBJECTS);
	
	// add the player's ship
	objects.add (ship); // necessary so that the array won't be size 0
	setPlayersShip (ship); // necessary to ensure index of 0 for player's ship
	
	// create the game's bases, stars and asteroids
	createAsteroids ();
	createBases ();
	createStars ();
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Adds a space object to the array of space objects.
 * 
 * 		@param		obj		the object to add
 * 
 * 		@since		2.0
 */

public void addSpaceObject (SpaceObject obj)
{
	objects.add (obj);
	
	// check to see if the number of space objects has exceded
	// the estimated maximum
	if (objects.size () > MAX_OBJECTS)
	{
		System.out.println ("Warning - there are now " + objects.size () + " space objects in the game; the estimated maximum is " + MAX_OBJECTS + ".");
	}
}

/**		Creates randomly located asteroids.
 * 
 * 		@since		2.0
 */

public void createAsteroids ()
{
	for (int quad = 0; quad < QUADRANTS; quad++)
	{
		for (int qx = 0; qx < Space.QUADRANT_WIDTH; qx++)
		{
			for (int qy = 0; qy < Space.QUADRANT_HEIGHT; qy++)
			{
				// for each region in each quadrant, create a random number of
				// stars, in random locations
				int howmany = ((int)(Math.random () * 100)) % 10;
				for (int i = 0; i < howmany; i++)
				{
					// create the asteroid
					Asteroid asteroid = new Asteroid (quad, qx, qy, ((int)(Math.random () * 100)) % Space.REGION_WIDTH, ((int)(Math.random () * 100)) % Space.REGION_HEIGHT);
					
					// make sure it is not being placed in an occupied location
					while (getSpaceObject (quad, asteroid.getQuadrantLocation (), asteroid.getRegionLocation ()) != null)
					{
						asteroid = new Asteroid (quad, qx, qy, ((int)(Math.random () * 100)) % Space.REGION_WIDTH, ((int)(Math.random () * 100)) % Space.REGION_HEIGHT);
					}
					
					// place the star
					addSpaceObject (asteroid);
				}
			}
		}
	}
}

/**		Creates randomly located bases.
 * 
 * 		@since		2.0
 */

public void createBases ()
{
	for (int quad = 0; quad < QUADRANTS; quad++)
	{
		// each quadrant gets a set number of bases
		for (int i = 0; i < BASES_PER_QUADRANT; i++)
		{
			// find an empty location to insert the base into
			int qx = ((int)(Math.random () * 100)) % Space.QUADRANT_WIDTH;
			int qy = ((int)(Math.random () * 100)) % Space.QUADRANT_HEIGHT;
			int rx = ((int)(Math.random () * 100)) % Space.REGION_WIDTH;
			int ry = ((int)(Math.random () * 100)) % Space.REGION_HEIGHT;
			while (getSpaceObject (quad, new Point (qx, qy), new Point (rx, ry)) != null)
			{
				qx = ((int)(Math.random () * 100)) % Space.QUADRANT_WIDTH;
				qy = ((int)(Math.random () * 100)) % Space.QUADRANT_HEIGHT;
				rx = ((int)(Math.random () * 100)) % Space.REGION_WIDTH;
				ry = ((int)(Math.random () * 100)) % Space.REGION_HEIGHT;
			}
			
			// insert the base
			Base base = new Base (quad, qx, qy, rx, ry);
			addSpaceObject (base);
		}
	}
}


/**		Creates randomly located stars.
 * 
 * 		@since		2.0
 */

public void createStars ()
{
	for (int quad = 0; quad < QUADRANTS; quad++)
	{
		for (int qx = 0; qx < Space.QUADRANT_WIDTH; qx++)
		{
			for (int qy = 0; qy < Space.QUADRANT_HEIGHT; qy++)
			{
				// for each region in each quadrant, create a random number of
				// stars, in random locations
				int howmany = ((int)(Math.random () * 100)) % 2;
				for (int i = 0; i < howmany; i++)
				{
					// create the star
					Star star = new Star (quad, qx, qy, ((int)(Math.random () * 100)) % Space.REGION_WIDTH, ((int)(Math.random () * 100)) % Space.REGION_HEIGHT);
					
					// make sure it is not being placed in an occupied location
					while (getSpaceObject (quad, star.getQuadrantLocation (), star.getRegionLocation ()) != null)
					{
						star = new Star (quad, qx, qy, ((int)(Math.random () * 100)) % Space.REGION_WIDTH, ((int)(Math.random () * 100)) % Space.REGION_HEIGHT);
					}
					
					// place the star
					addSpaceObject (star);
				}
			}
		}
	}
}

/**		Gets an array of space objects located in a specific region.
 * 
 * 		@param		quad		the quadrant to check in
 * 		@param		qloc		the regional location within the quadrant to look in
 * 		@param		team		filters out members of this team (0 performs no filtering by team)
 * 		@param		classname	filters for a class of space object (null performs no filtering by class name)
 * 
 * 		@return		an array of space objects located in a specific region, or null if there are none
 * 
 *		@since	2.0
 */

public SpaceObject[] getInRegion (int quad, Point qloc, int team, String classname)
{
	// array for the ships found
	ArrayList temp = new ArrayList (Space.REGION_WIDTH * Space.REGION_HEIGHT);
	
	// find space objects located in the specified region
	Iterator i = objects.iterator ();
	while (i.hasNext () == true)
	{
		SpaceObject obj = (SpaceObject) i.next ();
		if (obj.locatedIn (quad, qloc) == true)
		{
			// filter for team
			if ((team == 0) || (obj.getTeam () != team))
			{
				if ((classname == null) || (obj.getClass ().getName () == classname))
				{
					temp.add (obj);
				}
			}
		}
	}
		
	// convert the array to a proper one
	int size = temp.size ();
	if (size > 0) 
	{
		SpaceObject ret[] = new SpaceObject[size];
		for (int x = 0; x < size; x++)
		{
			ret[x] = (SpaceObject) temp.get (x);
		}
	
		// return the array
		return ret;	
	}
	else
	{
		return null;
	}
}

/**		Retrieves the player's ship.
 * 
 *		@return		the player's ship
 * 
 *		@since		2.0
 */

public Ship getPlayersShip ()
{
	// the player's ship is always located at the first position in the array
	return ((Ship) objects.get (0));
}

/**		Returns a space object at a fully-qualified location.
 * 
 * 		@param		quad		the quadrant
 * 		@param		q			the location within the quadrant
 * 		@param		r			the location within the region
 * 
 * 		@return		an object at the location, or null if none exists
 * 
 * 		@since		2.0
 */

public SpaceObject getSpaceObject (int quad, Point q, Point r)
{
	// find space objects located in the specified region
	Iterator i = objects.iterator ();
	while (i.hasNext () == true)
	{
		SpaceObject obj = (SpaceObject) i.next ();
		if (obj.locatedIn (quad, q, r) == true)
		{
			return obj;
		}
	}

	// if we get this far, no such object exists
	return null;
}

/**		Passes time in the game for the game's space objects.
 * 
 *		@param		h		the number of hours to pass
 * 
 *		@since		2.0
 */

public void passTime (int h)
{
	// bounds checking
	if (h < 0) h = 0;
	
	// iterate over a copy of the objects array list, rather than the array itself
	ArrayList copy = (ArrayList) objects.clone ();

	// pass time for the objects
	Iterator i = copy.iterator ();
	while (i.hasNext () == true)
	{
		((SpaceObject) i.next ()).passTime (h);
	}
}

/**		Removes all the space objects, except for the player's ship.
 * 
 * 		@since		2.0
 */

public void removeAll ()
{
	SpaceObject ship = (SpaceObject) objects.get (0);
	objects = new ArrayList (MAX_OBJECTS);
	objects.add (ship);
	objects.set (0, ship);
}

/**		Removes a space object from the array of space objects.  If the object
 * 		is the player's ship, then the game ends.
 * 
 * 		@param		obj		the object to remove
 * 
 * 		@since		2.0
 */

public void removeSpaceObject (SpaceObject obj)
{
	if (objects.contains (obj) == true)
	{
		if (objects.indexOf (obj) == 0)
		{
			JavaTrek.game.endgame (false, "Your ship was destroyed.");
		}
		else
		{
			objects.remove (obj);
		}
	}
	else
	{
		Exception e = new Exception ();
		e.printStackTrace ();
		System.out.println ("Space.removeSpaceObject () was asked to remove an object that did not exist in the array.");
		System.exit (1);
	}
}

/**		Sets the player's ship.
 * 
 *		@param		ship		the player's ship
 * 
 *		@since		2.0
 */

public void setPlayersShip (Ship ship)
{
	objects.set (0, ship);
}

}