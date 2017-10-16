package javatrek.spaceobjects;

import java.awt.Point;

import javatrek.JavaTrek;
import javatrek.Pilot;
import javatrek.Space;
import javatrek.systems.Computer;
import javatrek.systems.Generator;
import javatrek.systems.ImpulseDrive;
import javatrek.systems.LightDrive;
import javatrek.systems.LongRangeScanner;
import javatrek.systems.Shields;
import javatrek.systems.ShortRangeScanner;

/**
 * This is the basic ship object, used to describe both the player's ship and any other ships occuring in the game.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/01/2002 - the original ship object
 * <LI>Version 1.1 - 03/27/2002 - had much of the ship creation (types, for example) moved to another object
 * <LI>Version 1.2 - 04/05/2002 - ironically, this moves some of the functions back (ships are now created by a ship
 * factory class)
 * <LI>Version 1.3 - 11/19/2002 - added dodge ability
 * <LI>Version 1.4 - 11/27/2002 - moved dodge ability to super class object_machine
 * <LI>Version 1.5 - 05/12/2003 - added the rest of the ship types (27 in all)
 * <LI>Version 1.6 - 10/15/2004 - changed the ship type to a string, and added a ship image name field
 * <LI>Version 1.7 - 10/19/2004 - added a second constructor, which specifies the ship type, and removed ship-type
 * constants and the function which returned a set of ship-type constants
 * <LI>Version 2.0 - 11/04/2004 - moved appropriate functions to the Machine class, reworked the move commands and made
 * some other minor improvvements
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 11/04/2004
 */

public class Ship extends Machine {

	//////////////////////////////////////////////////////////////////////////////
	// private fields
	//////////////////////////////////////////////////////////////////////////////

	/** the class of ship */
	private String ship_type = null;

	//////////////////////////////////////////////////////////////////////////////
	// constructor
	//////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a ship.
	 * 
	 * @param name
	 *            the name of the ship
	 * @param st
	 *            the name of the type of ship
	 * @param img
	 *            the ship's image's name
	 * @param team
	 *            the team the ship is on
	 * @param is_ai
	 *            specifies if the ship is computer-controlled
	 * @param quad
	 *            the quadrant the ship is located in
	 * @param qx
	 *            quadrant x-location
	 * @param qy
	 *            quadrant y-location
	 * @param rx
	 *            region x-location
	 * @param ry
	 *            region y-location
	 * @param e
	 *            the ship's max energy rating
	 * @param gen
	 *            the ship's energy generating capacity
	 * @param hp
	 *            the ship's max hit points
	 * @param rp
	 *            the ship's repair points
	 * @param d
	 *            the ship's dodge rating
	 * @param pv
	 *            the ship's point value
	 * @param com
	 *            the ship's computer
	 * @param p
	 *            the ship's pilot
	 * @param ld
	 *            the ship's light drive
	 * @param lr
	 *            the ship's long-range scanner
	 * @param sr
	 *            the ship's short-range scanner
	 * @param sh
	 *            the ship's shields
	 * 
	 * @since 1.7
	 */

	public Ship(String name, String st, String img, int team, boolean is_ai, int quad, int qx, int qy, int rx, int ry,
			int e, int gen, int hp, float rp, int d, int pv, Computer com, Pilot p, LightDrive ld, LongRangeScanner lr,
			ShortRangeScanner sr, Shields sh) {
		super(name, "/images/ships/" + img, team, hp, quad, qx, qy, rx, ry);

		// set the ship type
		ship_type = st;

		// set the ai type
		setAI(is_ai);

		// set the ship's max energy and initial energy ratings
		setEnergyMax(e);
		setEnergyRemaining(e);

		// create the ship's generator
		addSystem(new Generator(gen));

		// set the ship's repair points
		setRepairPoints(rp);

		// set the ship's dodge rating
		setDodge(d);

		// set the ship's point value
		setPointValue(pv);

		// set the pilot
		setPilot(p);

		// set systems
		addSystem(com);
		addSystem(new ImpulseDrive());
		addSystem(ld);
		addSystem(lr);
		addSystem(sh);
		addSystem(sr);
	}

	//////////////////////////////////////////////////////////////////////////////
	// functions
	//////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the description of the ship, including name, type and team.
	 * 
	 * @return the ship's description
	 * 
	 * @since 1.0
	 */

	public String getDescription() {
		// use a string buffer for performance reasons
		StringBuffer buffer = new StringBuffer(1000);

		// ship's name
		buffer.append(getName());

		// ship's team
		buffer.append(" (");
		buffer.append(getTeamName());
		buffer.append("), ");

		// ship's type
		buffer.append(ship_type);

		// return the string
		return buffer.toString();
	}

	/**
	 * Returns the ship's type.
	 * 
	 * @return the ship's type
	 * 
	 * @since 1.0
	 */

	public String getShipType() {
		return ship_type;
	}

	/**
	 * Moves the ship to a different region (using the light drive).
	 * 
	 * @param q
	 *            the destination region
	 * @param r
	 *            the location within the destination region
	 * 
	 * @return returns true if the move was made successfully, false otherwise
	 * 
	 * @since 1.0
	 */

	public boolean lightDriveMove(Point q, Point r) {
		// make sure the ship has a light drive
		LightDrive ld = (LightDrive) getSystem(LightDrive.class.getName());
		if (ld == null)
			return false;

		// bounds checking
		if (q.x < 0)
			return false;
		if (q.x >= Space.QUADRANT_WIDTH)
			return false;
		if (q.y < 0)
			return false;
		if (q.y >= Space.QUADRANT_HEIGHT)
			return false;
		if (r.x < 0)
			return false;
		if (r.x >= Space.REGION_WIDTH)
			return false;
		if (r.y < 0)
			return false;
		if (r.y >= Space.REGION_HEIGHT)
			return false;

		// make sure this really is a move
		if (getQuadrantLocation().equals(q) == true)
			return false;

		// check that the destination location is empty
		SpaceObject obj = JavaTrek.game.gamedata.space.getSpaceObject(getQuadrant(), q, r);
		if (obj != null) {
			if (getAI() == false) {
				JavaTrek.game.console.addMessage("RedLeft", "I am sorry, but that location is blocked.\n");
			}
			return false;
		}

		// calculate the distance for the trip
		float d = distance(getQuadrantLocation(), q);

		// The cost is equal to (7.8125 x distance * speed ^ 2). This was calculated
		// so that it costs 5000 units of energy to move a distance of 10 units at
		// warp 8.
		int cost = (int) (d * 6.138 * Math.pow(ld.getSetting(), 2));

		// apply energy savings due to advanced computer types
		Computer computer = (Computer) getSystem(Computer.class.getName());
		if (computer != null) {
			cost -= (int) (cost * computer.getSaved());
		}

		// if the shields are up, the cost is doubled
		Shields shields = (Shields) getSystem(Shields.class.getName());
		if (shields != null) {
			if (shields.getShieldsOn() == true) {
				cost *= 2;
			}
		}

		// if the final cost is higher than the remaining energy, do not
		// allow the move
		if (cost > getEnergyRemaining()) {
			if (getAI() == false) {
				JavaTrek.game.console.addMessage("RedLeft", "You do not have enough energy to move there.\n");
			}

			return false;
		}

		// find out how long this trip will take (the time is equal to
		// (19.2 x distance / speed); this was calculated so that it takes
		// 24 hours at warp 8 to cross the entire quadrant (when a quadrant was
		// 10x10).
		int time = (int) ((float) (d * 15.08) / ld.getSetting());

		// *** if we make it this far, the move can go ahead ***

		// remove the energy cost
		removeEnergy(cost);

		// only pass time if this is the player's ship
		if (getAI() == false) {
			// pass time before relocating the ship (simulates time passed in hyperspace)
			JavaTrek.game.gamedata.passTime(time);
		}

		// set the new region
		setQuadrantLocation(q.x, q.y);

		// set the new regional location
		setRegionLocation(r.x, r.y);

		// do a long-range scan of the new area
		longRangeScan();

		// the move was successful, so return true
		return true;
	}

	/**
	 * Moves the ship within its current region.
	 * 
	 * @param x
	 *            the new x co-ordinate
	 * @param y
	 *            the new y co-ordinate
	 *
	 * @return true if successful, false otherwise
	 */

	public boolean localMove(int x, int y) {
		// bounds checking
		if (x < 0)
			return false;
		if (x >= Space.REGION_WIDTH)
			return false;
		if (y < 0)
			return false;
		if (y >= Space.REGION_HEIGHT)
			return false;

		// check to see if the destination is free of obstruction
		SpaceObject obj = JavaTrek.game.gamedata.space.getSpaceObject(getQuadrant(), getQuadrantLocation(),
				new Point(x, y));
		if (obj != null) {
			if (getAI() == false)
				JavaTrek.game.console.addMessage("RedLeft", "That location is blocked.\n");
			return false;
		}

		// check to see if the engines are damaged
		ImpulseDrive idrv = (ImpulseDrive) getSystem(ImpulseDrive.class.getName());
		if (idrv == null)
			return false;
		if (idrv.getRepair() < 0.7) {
			if (getAI() == false) {
				JavaTrek.game.console.addMessage("RedLeft", "You cannot move there - your engines are too damaged.\n");
			}
			return false;
		}

		// check to see if the ship has enough energy
		int cost = 50;
		Shields shields = (Shields) getSystem(Shields.class.getName());
		if ((shields != null) && (shields.getShieldsOn() == true))
			cost = cost << 1;
		if (getEnergyRemaining() < cost) {
			if (getAI() == false)
				JavaTrek.game.console.addMessage("RedLeft", "You do not have enough energy to move there.\n");
			return false;
		} else {
			// deduct the energy cost
			removeEnergy(cost);
		}

		// move the ship
		setRegionLocation(x, y);

		// if we get this far, the move was a success
		return true;
	}

	/**
	 * Passes time in the game, and handles all the effects time passing has. Note that passTime (0) is valid - it is
	 * called for quick actions (like firing a laser) which take up a trivial amount of time, but should allow other
	 * game entities a chance to react.
	 * 
	 * @param h
	 *            the number of hours to pass
	 * 
	 * @since 1.0
	 */

	public void passTime(int h) {
		super.passTime(h);

		// if the ship is ai controlled, run appropriate AI routines
		if (getAI() == true) {
		}
	}

	/**
	 * Sets the ship type.
	 * 
	 * @param stype
	 *            the ship's new type
	 * 
	 * @since 1.0
	 */

	public void setShipType(String stype) {
		if (stype != null) {
			ship_type = stype;
		}
	}

	/**
	 * This function handles damage done to the ship (regardless of source). If the ship is docked, it takes no damage;
	 * the station's shields and armour protect the ship.
	 * 
	 * @param damage
	 *            the amount of damage taken
	 * @param cause
	 *            the cause of the damage
	 * 
	 * @return true if the ship survives, false if it is destroyed
	 * 
	 * @since 1.2
	 */

	public boolean takeDamage(int damage, int cause) {
		// TODO: handle if the ship is docked

		return super.takeDamage(damage, cause);
	}

}