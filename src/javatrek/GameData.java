package javatrek;

import java.awt.Point;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javatrek.factory.NameFactory;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.SpaceObject;
import javatrek.systems.Shields;

/**
 * This object collects the various game objects - quadrants, regions, ships etc - and stores them in one place.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/02/2002 - the original gamedata object
 * <LI>Version 1.1 - 04/04/2002 - had much of the statistical information moved to object_stats
 * <LI>Version 1.2 - 11/24/2002 - added serializable interface
 * <LI>Version 1.3 - 10/19/2004 - updated to use ShipFactory and its createShips () function
 * <LI>Version 2.0 - 11/12/2004 - removed many settings, reduced to a holder for major game objects (space, name server, stats etc), moved time keeping chores to a GregorianCalendar instance and renamed the class from GameDataRoot to GameData 
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/12/2004
 */

public class GameData implements Serializable
{
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the initial amount of money the player receives */
private static final int INITIAL_FUNDS = 10000;
	
//////////////////////////////////////////////////////////////////////////////
//  public fields
//////////////////////////////////////////////////////////////////////////////
	
/** provides names for objects */
public NameFactory name_server;

/** handle to the player's pilot object */
public Pilot player;

/** holds all of the game's space objects */
public Space space;

/** stats about the game */
public Statistics stats;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** tracks time elapsed since the game began */
private GregorianCalendar calendar;

/** the player's name */
private String players_name;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a game with a player name and level specified.
 * 
 *		@param		pname		the player's name
 *		@param		fmult		final score modifier
 * 
 *		@since		1.0
 */

public GameData (String pname)
{
	// stores the name server
	name_server = new NameFactory ();
	
	// initially, no time has passed
	calendar = new GregorianCalendar (3000, 0, 1, 0, 0);
	
	// store the player's name and settings
	players_name = pname;
	
	// create a stats object
	stats = new Statistics ();
	
	// create the player's pilot
	player = new Pilot (false, INITIAL_FUNDS);
	
	// create the fleet object, and the player's ship
	int pqx = ((int)(Math.random () * 100)) % 10;
	int pqy = ((int)(Math.random () * 100)) % 10;
	int prx = ((int)(Math.random () * 100)) % 10;
	int pry = ((int)(Math.random () * 100)) % 10;	
	space = new Space (JavaTrek.ship_factory.createShip ("Venture Starship", name_server.getFederationShipName (), Ship.FEDERATION, 1, false, 0, pqx, pqy, prx, pry));
	
	// set the player's pilot as the player's ship's pilot
	space.getPlayersShip ().setPilot (player);

	// turn the player's ship's shields on	
	Shields shields = (Shields) space.getPlayersShip ().getSystem (Shields.class.getName ());
	shields.setShieldsOn (true);
	
	passTime (0);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		This function adds a single, specific ship to a specified team at a random
 * 		location within a specified quadrant.
 * 
 * 		@param		shiptype			the type of ship to be added
 * 		@param		team				the team to add the ship to
 * 		@param		quadrant			which quadrant to add the ship to
 * 
 * 		@since		1.2
 */

public void addShip (String shiptype, int team, int quadrant)
{
	// get a name for the ship
	String shipname;
	if (team == SpaceObject.RAIDERS)
	{
		shipname = name_server.getRaiderShipName ();
	}
	else if (team == SpaceObject.PIRATES)
	{
		shipname = name_server.getPirateShipName () ;
	}
	else if (team == SpaceObject.FEDERATION)
	{
		shipname = name_server.getFederationShipName ();
	}
	else
	{
		shipname = "No Name";
	}
	
	// pick a region and a location within the region
	int qx = ((int)(Math.random () * 100)) % 10;
	int qy = ((int)(Math.random () * 100)) % 10;
	int rx = ((int)(Math.random () * 100)) % 10;
	int ry = ((int)(Math.random () * 100)) % 10;
	
	// make sure the location picked is empty, and not in the player's quadrant/region
	Ship player = space.getPlayersShip ();
	int pquad = player.getQuadrant ();
	Point pq = player.getQuadrantLocation ();
	while (((quadrant == pquad) && ((pq.x == qx) && (pq.y == qy))) && ((space.getSpaceObject (quadrant, new Point (qx, qy), new Point (rx, ry)) != null)))
	{
		qx = ((int)(Math.random () * 100)) % 10;
		qy = ((int)(Math.random () * 100)) % 10;
		rx = ((int)(Math.random () * 100)) % 10;
		ry = ((int)(Math.random () * 100)) % 10;
	}
	
	// get the player's pilot's level, if possible
	// (if not, use 1 as the player's pilot's assumed level)
	int level = 1;
	Ship players_ship = space.getPlayersShip (); 
	if (players_ship != null)
	{
		level = players_ship.getPilot ().getLevel ();
	}
	
	// create the ship
	Ship new_npc = JavaTrek.ship_factory.createShip (shiptype, shipname, team, level, true, 1, qx, qy, rx, ry);
		
	// add the ship to the array
	space.addSpaceObject (new_npc);
}

/**		Returns the player's name
 * 
 *		@return		the player's name
 * 
 *		@since 1.0
 */

public String getPlayersName ()
{
	return players_name;
}

/**		Passes time in the game, and handles all the effects time passing has.
 * 		Please note that passTime (0) is valid, though most of the time-based
 * 		functions will ignore it.
 * 
 *		@param		h		the number of hours to pass
 * 
 *		@since		1.0
 */

public void passTime (int h)
{
	// bounds checking
	if (h < 0) h = 0;
	
	// add the hours to the calendar
	calendar.add (Calendar.HOUR_OF_DAY, h);

	// pass the time
	space.passTime (h);

	// check and see if the player's ship has been destroyed
	if (space.getPlayersShip ().getHP () <= 0)
	{
		// if the player's ship is destroyed, call the game over function
		JavaTrek.game.endgame (false, "Your ships was destroyed.");
	}	
}

}