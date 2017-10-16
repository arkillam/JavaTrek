package javatrek.spaceobjects;


/**
 * Instances of this class represent bases and similar installations.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/29/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/29/2004
 */

public class Base extends Machine
{
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////
	
//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a base.
 * 
 * 		@param		quad	the quadrant the star is located in
 * 		@param		qx		the quadrant x-co-ordinate
 * 		@param		qy		the quadrant y-co-ordinate
 * 		@param		rx		the region x-co-ordinate
 * 		@param		ry		the region y-co-ordinate
 * 
 *		@since		1.0
 */

public Base (int quad, int qx, int qy, int rx, int ry)
{
	// the star's name is a combination of its location values
	super ("Base " + qx + qy + rx + ry, "/images/bases/star_base.bmp", SpaceObject.FEDERATION, SpaceObject.MAX_HP, quad, qx, qy, rx, ry);
	
	// TODO:  add shields, a generator, weapons etc
	// TODO: add AI controls to deal with enemy ships
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

}
