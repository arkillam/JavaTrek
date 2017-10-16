package javatrek.factory;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javatrek.JavaTrek;
import javatrek.Pilot;
import javatrek.Space;
import javatrek.spaceobjects.Ship;
import javatrek.systems.Computer;
import javatrek.systems.LightDrive;
import javatrek.systems.LongRangeScanner;
import javatrek.systems.Shields;
import javatrek.systems.ShortRangeScanner;

/**
 * The ship factory provides functions create a ship with a specific name, team and type.
 * 
 * <UL>
 * <LI>Version 1.0 - 04/05/2002 - the original class
 * <LI>Version 1.1 - 05/12/2003 - added/altered the functions to generate the "new and improved" ship types
 * <LI>Version 1.2 - 05/15/2003 - ship creation now requires a specified initial level for the ship's crew
 * <LI>Version 1.3 - 10/07/2004 - ships are no longer created with specific crew sizes
 * <LI>Version 2.0 - 10/19/2004 - updated to be a class, rather than a provider of static functions, and to use a CSV
 * spreadsheet rather than functions to store the values for various classes of ships
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/19/2004
 */

public class ShipFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	/** stores the blast drive values */
	private Map<String, Float> blastdrive;

	/** stores the names of the classes of ships */
	private List<String> classes;

	/** stores the computer values */
	private Map<String, Integer> computer;

	/** stores the dodge values */
	private Map<String, Integer> dodge;

	/** stores the energy weapon values */
	private Map<String, String> energy_weapon;

	/** stores the generator values */
	private Map<String, Integer> generator;

	/** stores the hit point values */
	private Map<String, Integer> hitpoints;

	/** stores the names of the ships' images */
	private Map<String, String> images;

	/** stores the launcher load values */
	private Map<String, Integer> launcher_load;

	/** stores the launcher tubes values */
	private Map<String, Integer> launcher_tubes;

	/** stores the launcher type values */
	private Map<String, String> launcher_type;

	/** stores the LR scanner type values */
	private Map<String, Integer> lr_scanner;

	/** stores the max energy values */
	private Map<String, Integer> max_energy;

	/** stores the point values */
	private Map<String, Integer> points;

	/** stores the repair values */
	private Map<String, Float> repair;

	/** stores the shield energy values */
	private Map<String, Integer> shield_energy;

	/** stores the shield type values */
	private Map<String, Integer> shield_level;

	/** stores the shuttle type values */
	private Map<String, String> shuttle;

	/** stores the SR scanner type values */
	private Map<String, Integer> sr_scanner;

	/** stores the teleporter type values */
	private Map<String, String> teleporter;

	/**
	 * Creates the ship factory object.
	 * 
	 * @since 2.0
	 */

	public ShipFactory() {
		loadShipData();
	}

	/**
	 * Creates a ship.
	 * 
	 * @param class_name
	 *            the name of the class of ship
	 * @param name
	 *            the name of the ship
	 * @param team
	 *            the team the ship is on
	 * @param level
	 *            the experience level of the pilot
	 * @param ai
	 *            is the ship an NPC?
	 * @param quad
	 *            the name of the quadrant to place the ship in
	 * @param qx
	 *            quadrant x-location
	 * @param qy
	 *            quadrant y-location
	 * @param rx
	 *            region x-location
	 * @param ry
	 *            region y-location
	 * 
	 * @since 2.0
	 */

	public Ship createShip(String class_name, String name, int team, int level, boolean ai, int quad, int qx, int qy,
			int rx, int ry) {
		// bounds checking
		if (quad >= Space.QUADRANTS) {
			System.out.println("Ship.createShip () called with invalid quadrant value:  " + quad);
			Exception e = new Exception();
			e.printStackTrace();
			System.exit(1);
		} else if ((qx < 0) || (qx >= Space.QUADRANT_WIDTH)) {
			System.out.println("Ship.createShip () called with invalid qx value:  " + qx);
			Exception e = new Exception();
			e.printStackTrace();
			System.exit(1);
		} else if ((qy < 0) || (qy >= Space.QUADRANT_HEIGHT)) {
			System.out.println("Ship.createShip () called with invalid qy value:  " + qy);
			Exception e = new Exception();
			e.printStackTrace();
			System.exit(1);
		} else if ((rx < 0) || (rx >= Space.REGION_WIDTH)) {
			System.out.println("Ship.createShip () called with invalid rx value:  " + rx);
			Exception e = new Exception();
			e.printStackTrace();
			System.exit(1);
		} else if ((rx < 0) || (rx >= Space.REGION_HEIGHT)) {
			System.out.println("Ship.createShip () called with invalid ry value:  " + ry);
			Exception e = new Exception();
			e.printStackTrace();
			System.exit(1);
		}

		// make sure the ship is being created in an empty location
		if ((JavaTrek.game != null) && (JavaTrek.game.gamedata != null)) {
			if (JavaTrek.game.gamedata.space.getSpaceObject(quad, new Point(qx, qy), new Point(rx, ry)) != null) {
				System.out.println("Ship.createShip () told to create a ship in an occupied location.");
				Exception e = new Exception();
				e.printStackTrace();
				System.exit(1);
			}
		}

		// verify that the class_name value is correct
		if (classes.contains(class_name) == false) {
			System.out.println("ShipFactory.createShip () called with invald class_name value " + class_name);
			System.exit(1);
		}

		// retrieve the ship's image
		String image_name = (String) images.get(class_name);

		// retrieve the ship's max energy, generator capacity, hit point,
		// repair point, dodge rating and point value
		int e = ((Integer) max_energy.get(class_name)).intValue();
		int gen = ((Integer) generator.get(class_name)).intValue();
		int hp = ((Integer) hitpoints.get(class_name)).intValue();
		float rp = ((Float) repair.get(class_name)).floatValue() / 100.0f;
		int d = ((Integer) dodge.get(class_name)).intValue();
		int pv = ((Integer) points.get(class_name)).intValue();

		// create the ship's pilot
		Pilot p = new Pilot(ai, level);

		// create the ship's computer
		Computer com = new Computer(((Integer) computer.get(class_name)).intValue());

		// create the ship's blastdrive
		LightDrive bd = new LightDrive(((Float) blastdrive.get(class_name)).floatValue());

		// create the ship's scanners
		LongRangeScanner lr = new LongRangeScanner(((Integer) lr_scanner.get(class_name)).intValue());
		ShortRangeScanner sr = new ShortRangeScanner(((Integer) sr_scanner.get(class_name)).intValue());

		// create the ship's shields
		int slevel = ((Integer) shield_level.get(class_name)).intValue();
		int smax = ((Integer) shield_energy.get(class_name)).intValue();
		;
		Shields sh = null;
		if (slevel > 0)
			sh = new Shields(slevel, smax);

		// create the ship
		Ship ship = new Ship(name, class_name, image_name, team, ai, quad, qx, qy, rx, ry, e, gen, hp, rp, d, pv, com,
				p, bd, lr, sr, sh);

		return ship;
	}

	/**
	 * Retrieves a list of the classes of ships the factory can build.
	 * 
	 * @return an array containing a list of the classes of ships the factory can build
	 * 
	 * @since 2.0
	 */

	public List<String> getShipClasses() {
		return classes;
	}

	/**
	 * Loads in the ship data from the ships.csv file.
	 * 
	 * @since 2.0
	 */

	private void loadShipData() {
		// create the data structures required for ship data
		classes = new ArrayList<>();
		blastdrive = new HashMap<>();
		computer = new HashMap<>();
		dodge = new HashMap<>();
		energy_weapon = new HashMap<>();
		generator = new HashMap<>();
		hitpoints = new HashMap<>();
		images = new HashMap<>();
		launcher_load = new HashMap<>();
		launcher_tubes = new HashMap<>();
		launcher_type = new HashMap<>();
		lr_scanner = new HashMap<>();
		max_energy = new HashMap<>();
		points = new HashMap<>();
		repair = new HashMap<>();
		shield_energy = new HashMap<>();
		shield_level = new HashMap<>();
		shuttle = new HashMap<>();
		sr_scanner = new HashMap<>();
		teleporter = new HashMap<>();

		try {
			InputStream in = getClass().getResourceAsStream("/data/ship_types.csv");
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			Scanner scanner = new Scanner(input);
			scanner.useDelimiter("\n");

			while (scanner.hasNext()) {
				String row = scanner.next();
				if (row.startsWith("Ship Class"))
					continue;
				if (row.trim().length() < 1)
					continue;

				// divide the row into columns
				String cols[] = row.split(",");

				// for iterating through the columns
				int c = 0;

				try {
					// the first entry will be the class name
					String name = cols[c++];
					classes.add(name);

					// store the other entries in order
					images.put(name, cols[c++]);
					max_energy.put(name, new Integer(cols[c++]));
					dodge.put(name, new Integer(cols[c++]));
					hitpoints.put(name, new Integer(cols[c++]));
					repair.put(name, new Float(cols[c++]));
					computer.put(name, new Integer(cols[c++]));
					generator.put(name, new Integer(cols[c++]));
					blastdrive.put(name, new Float(cols[c++]));
					energy_weapon.put(name, cols[c++]);
					launcher_type.put(name, cols[c++]);
					launcher_tubes.put(name, new Integer(cols[c++]));
					launcher_load.put(name, new Integer(cols[c++]));
					lr_scanner.put(name, new Integer(cols[c++]));
					sr_scanner.put(name, new Integer(cols[c++]));
					shield_level.put(name, new Integer(cols[c++]));
					shield_energy.put(name, new Integer(cols[c++]));
					shuttle.put(name, cols[c++]);
					teleporter.put(name, cols[c++]);
					points.put(name, new Integer(cols[c++]));
				} catch (Exception e) {
					System.out.println("bad row:  " + row);
					e.printStackTrace();
					System.exit(1);
				}
			}

			scanner.close();
			input.close();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}