package javatrek.spaceobjects;

import java.io.Serializable;

/**
 * The star object represents a star in a region.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/09/2002 - the original
 * <LI>Version 1.1 - 11/24/2002 - added serializable interface
 * <LI>Version 2.0 - 10/22/2004 - updated
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/22/2004
 */

public class Star extends SpaceObject implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a star.
 * 
 * 		@param		quad	the quadrant the star is located in
 * 		@param		qx		the quadrant x-co-ordinate
 * 		@param		qy		the quadrant y-co-ordinate
 * 		@param		rx		the region x-co-ordinate
 * 		@param		ry		the region y-co-ordinate
 * 
 *		@since		1.0
 */

public Star (int quad, int qx, int qy, int rx, int ry)
{
	// the star's name is a combination of its location values
	super ("Star " + qx + qy + rx + ry, "/images/space/sun.bmp", SpaceObject.NEUTRAL, SpaceObject.MAX_HP, quad, qx, qy, rx, ry);
}

}