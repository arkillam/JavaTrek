package javatrek.spaceobjects;

import java.awt.Point;
import java.io.Serializable;

import javatrek.Space;

/**
 * This is a base object, which many of the other objects can be based on.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/09/2002 - the original instance
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 1.2 - 10/22/2004 - moved hit points to this class and renamed the class from RootDatatype to SpaceObject
 * <LI>Version 2.0 - 10/22/2004 - added a USI value
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/22/2004
 */

public class SpaceObject implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////
	
/** the federation team */
public static final int FEDERATION = 1001;

/** the maximum allowed hit points */
public static final int MAX_HP = 50000;

/** the neutral team */
public static final int NEUTRAL = 1002;

/** the pirate team */
public static final int PIRATES = 1003;

/** the invading team */
public static final int RAIDERS = 1004;

//////////////////////////////////////////////////////////////////////////////	
//  private static fields
//////////////////////////////////////////////////////////////////////////////

/** tracks the current highest unique SpaceObject identifier (USI) */
private static long highest_usi = 0;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** the object's current number of hit points */
private int hitpoints;

/** the object's maximum number of hit points */
private int hitpoints_max;

/** the quadrant the object is located in */
private int quadrant;

/** which team the object belongs to */
private int team;

/** unique location code (ULC) */
private long ulc;

/** unique SpaceObject identifier (USI) */
private long usi;

/** location within a quadrant */
private Point qloc;

/** location within a region */
private Point rloc;

/** the space object's image name */
private String image_name;

/** the space object's name */
private String name;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates an instance of the SpaceObject class.
 * 
 * 		@param		n		the object's name
 * 		@param		in		the object's image's name
 * 		@param		t		the team the object is aligned with
 * 		@param		hp		the remaining hit points the object has
 * 		@param		quad	the quad the object is located in
 * 		@param		qx		the quadrant x-co-ordinate
 * 		@param		qY		the quadrant y-co-ordinate
 * 		@param		rx		the region x-co-ordinate
 * 		@param		ry		the region y-co-ordinate
 * 
 * 		@since		1.2
 */

public SpaceObject (String n, String in, int t, int hp, int quad, int qx, int qy, int rx, int ry)
{
	// store the name and image name
	name = n;
	image_name = in;
	
	// create the location-storing points
	qloc = new Point ();
	rloc = new Point ();
	
	// set the object's hit points
	setHPMax (hp);
	setHP (hp);
	
	// set the object's team
	setTeam (t);
	
	// set the object's location
	setQuadrant (quad);
	setQuadrantLocation (qx, qy);
	setRegionLocation (rx, ry);
		
	// assign a usi and increment the counter
	usi = highest_usi;
	highest_usi++;
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Calculates the distance between the object and another point in the same region.
 * 
 *		@param		p		the point
 *
 *		@since		1.0
 */

public float distance (Point p)
{
	// get the machine's location within the region
	Point r = getRegionLocation ();
	
	// distance = [ (x2 - x1) ^ 2 + (y2 - y1) ^ 2 ] ^ 0.5
	double dx = Math.pow ((p.x - r.x), 2);
	double dy = Math.pow ((p.y - r.y), 2);
	double d = Math.sqrt (dx + dy);
	
	return (float) d;
}

/**		Calculates the distance between two points.
 *
 * 		@param		a		the first point
 * 		@param		b		the second point
 * 
 * 		@return		the distance between two points 
 * 
 *		@since		1.0
 */

public float distance (Point a, Point b)
{
	// distance = [ (x2 - x1) ^ 2 + (y2 - y1) ^ 2 ] ^ 0.5
	double x = Math.pow ((double)(b.x - a.x), 2);
	double y = Math.pow ((double)(b.y - a.y), 2);
	double d = Math.sqrt (x + y);
	
	return (float) d;
}

/**		Calculates the unique location code (ULC) for a SpaceObject.  The code is
 * 		constructed using the seven location values, and should be recalculated
 * 		when the object's location changes.
 * 
 * 		@param		quad	the quadrant the object is located in
 * 		@param		q		the object's location within its quadrant
 * 		@param		r		the object's location within its region
 * 
 * 		@return		the object's unique location code
 * 
 * 		@since		2.0
 */

public static long calculateULC (int quad, Point q, Point r)
{
	// inside region location value
	long value = r.x + (r.y * Space.REGION_HEIGHT);
	
	// inside quadrant location value
	value += (q.x + (q.y * Space.QUADRANT_HEIGHT)) * (10 ^ 4);

	// quadrant location value
	value += quad * (10 ^ 8);
	
	// return the ulc
	return value;
}

/**		Gets the current number of hit points for this object.
 * 
 * 		@return		the current number of hit points for this object
 * 
 * 		@since		1.2
 */

public int getHP ()
{
	return hitpoints;
}

/**		Gets the maximum number of hit points for this object.
 * 
 * 		@return		the maximum number of hit points for this object
 * 
 * 		@since		1.2
 */

public int getHPMax ()
{
	return hitpoints_max;
}

/**		Returns the object's image's name.
 * 
 * 		@return		the object's image's name
 * 
 * 		@since		2.0
 */

public String getImageName ()
{
	return image_name;
}

/**		Returns the object's name.
 * 
 * 		@return		the object's name
 * 
 * 		@since		2.0
 */

public String getName ()
{
	return name;
}

/**		Gets which quadrant the space object is located in.
 * 
 * 		@return		which quadrant the space object is located in
 * 
 * 		@since		2.0
 */

public int getQuadrant ()
{
	return quadrant;
}

/**		Returns the object's location within the current quadrant.
 * 
 *		@return		the object's location within the current quadrant
 * 
 *		@since		1.0
 */
public Point getQuadrantLocation ()
{
	return (Point) qloc.clone ();
}

/**		Returns the object's location within the current region.
 * 
 *		@return		the object's location within the current region
 * 
 *		@since		1.0
 */
public Point getRegionLocation ()
{
	return (Point) rloc.clone ();
}

/**		Returns the object's team affiliation.
 * 
 *		@return		the object's team affiliation
 * 
 *		@since		1.0
 */

public int getTeam ()
{
	return team;
}

/**		Returns the object's team affiliation name.
 * 
 *		@return		the object's team affiliation name
 * 
 *		@since		1.0
 */

public String getTeamName ()
{
	if (team == FEDERATION)		return "Federation";
	else if (team == NEUTRAL)	return "Neutral";
	else if (team == PIRATES)	return "Pirates";
	else if (team == RAIDERS)	return "Raiders";
	else
	{
		System.out.println ("SpaceObject.getTeamName () was asked to name an unknown team:  " + team);
		System.exit (1);
		return null;
	} 
}

/**		Gets the object's ULC (unique location code).
 * 
 * 		@return		the object's ULC
 * 
 * 		@since		2.0
 */

public long getULC ()
{
	return ulc;
}

/**		Gets the object's USI (unique SpaceObject identifier).
 *
 * 		@return		 the object's USI
 * 
 * 		@since		2.0
 */

public long getUSI ()
{
	return usi;
}

/**		Determines if the space object is located in the specified region.
 * 
 * 		@param		quad	a quadrant id
 * 		@param		q		the location within the quadrant
 * 
 * 		@return		true if the object is within the specified region, false otherwise
 * 
 * 		@since		2.0
 */

public boolean locatedIn (int quad, Point q)
{
	// bounds checking
	if (quad >= Space.QUADRANTS) return false;
	if (q.x < 0) return false;
	if (q.x >= Space.QUADRANT_WIDTH) return false;
	if (q.y < 0) return false;
	if (q.y >= Space.QUADRANT_HEIGHT) return false;

	if ((quadrant == quad) && (qloc.equals (q) == true))
	{
		return true;
	}
	else
	{
		return false;
	}
}

/**		Determines if the space object is located in the specified location.
 * 
 * 		@param		quad	a quadrant id
 * 		@param		q		the location within the quadrant
 * 		@param		r		the location within the region
 * 
 * 		@return		true if the object is within the specified location, false otherwise
 * 
 * 		@since		2.0
 */

public boolean locatedIn (int quad, Point q, Point r)
{
	// bounds checking
	if (quad >= Space.QUADRANTS) return false;
	if (q.x < 0) return false;
	if (q.x >= Space.QUADRANT_WIDTH) return false;
	if (q.y < 0) return false;
	if (q.y >= Space.QUADRANT_HEIGHT) return false;
	if (r.x < 0) return false;
	if (r.x >= Space.REGION_WIDTH) return false;
	if (r.y < 0) return false;
	if (r.y >= Space.REGION_HEIGHT) return false;

	if (((quadrant == quad) && (qloc.equals (q) == true)) && (rloc.equals (r) == true))
	{
		return true;
	}
	else
	{
		return false;
	}
}

/**		Passes time for the space object.
 * 
 *		@param		h		the number of hours to pass
 * 
 *		@since		2.0
 */

public void passTime (int h)
{
}

/**		Sets the current number of hit points for this object.
 * 
 * 		@param		hp		the current number of hit points for this object
 * 
 * 		@since		1.2
 */

public void setHP (int hp)
{
	if (hp <= hitpoints_max)
	{
		hitpoints = hp;
	}
}

/**		Sets the maximum number of hit points for this object.
 * 
 * 		@param		hp		the maximum number of hit points for this object
 * 
 * 		@since		1.2
 */

public void setHPMax (int hp)
{
	if ((hp <= SpaceObject.MAX_HP) && (hp > 0))
	{
		hitpoints_max = hp;
		
		if (hitpoints > hitpoints_max)
		{
			hitpoints = hitpoints_max;
		}
	}
}

/**		Sets which quadrant the space object is located in.
 * 
 * 		@param		q		the quadrant
 * 
 * 		@since		2.0
 */

public void setQuadrant (int q)
{
	// bounds checking
	if (q < 0) q = 0;
	if (q >= Space.QUADRANTS) q = Space.QUADRANTS - 1;
	
	quadrant = q;
}

/**		Sets the object's location within the current quadrant.
 * 
 *		@param		x		the x-co-ordinate
 *		@param		y		the y-co-ordinate
 * 
 *		@since		1.0
 */

public void setQuadrantLocation (int x, int y)
{
	// bounds checking
	if (x < 0) x = 0;
	if (x >= Space.QUADRANT_WIDTH) x = Space.QUADRANT_WIDTH - 1;
	if (y < 0) y = 0;
	if (y > Space.QUADRANT_HEIGHT) y = Space.QUADRANT_HEIGHT - 1;
	
	// change the setting
	qloc.x = x;
	qloc.y = y;
	
	// update the ulc value
	calculateULC (quadrant, qloc, rloc);
}

/**		Sets the object's location within the current region.
 * 
 *		@param		x		the x-co-ordinate
 *		@param		y		the y-co-ordinate
 * 
 *		@since		1.0
 */

public void setRegionLocation (int x, int y)
{
	// bounds checking
	if (x < 0) x = 0;
	if (x >= Space.REGION_WIDTH) x = Space.REGION_WIDTH - 1;
	if (y < 0) y = 0;
	if (y > Space.REGION_HEIGHT) y = Space.REGION_HEIGHT - 1;
	
	// change the setting
	rloc.x = x;
	rloc.y = y;
	
	// update the ulc value
	calculateULC (quadrant, qloc, rloc);
}

/**		Sets the object's team.
 * 
 *		@param		t		a team
 * 
 *		@since		1.0
 */

private void setTeam (int t)
{
	// bounds checking
	if (t == SpaceObject.FEDERATION)
	{
		team = t;
	} 
	else if (t == SpaceObject.NEUTRAL)
	{
		team = t;
	} 
	else if (t == SpaceObject.PIRATES)
	{
		team = t;
	} 
	else if (t == SpaceObject.RAIDERS)
	{
		team = t;
	} 
	else
	{
		System.out.println ("SpaceObject.setTeam () was passed an invalid team id:  " + t);
		System.exit (1);
	}
}

}
