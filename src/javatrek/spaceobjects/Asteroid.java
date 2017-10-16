package javatrek.spaceobjects;

import java.io.Serializable;

/**
 * The asteroid object represents an asteroid in a region.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/09/2002 - the original
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 2.0 - 11/18/2004 - updated
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/18/2004
 */

public class Asteroid extends SpaceObject implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a asteroid.
 * 
 * 		@param		quad	the quadrant the asteroid is located in
 * 		@param		qx		the quadrant x-co-ordinate
 * 		@param		qy		the quadrant y-co-ordinate
 * 		@param		rx		the region x-co-ordinate
 * 		@param		ry		the region y-co-ordinate
 * 
 *		@since		1.0
 */

public Asteroid (int quad, int qx, int qy, int rx, int ry)
{
	// the asteroid's name is a combination of its location values
	super ("Asteroid " + qx + qy + rx + ry, "/images/space/asteroid_0" + ((((int)(Math.random () * 100)) % 7) + 1) + ".bmp", SpaceObject.NEUTRAL, 50, quad, qx, qy, rx, ry);
}

}