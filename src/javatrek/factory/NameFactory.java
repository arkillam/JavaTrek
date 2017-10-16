package javatrek.factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.thekillams.widgets.utilities.NumbersUtility;

/**
 * <P>
 * This class provides a set of names for various objects. The names are stored in array lists, and removed after being
 * given, so no name will be given twice from the same instance of this class.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/18/2002 - the original
 * <LI>Version 1.1 - 11/24/2002 - added the serializable interface
 * <LI>Version 1.2 - 11/25/2002 - added federation person name generator
 * <LI>Version 1.3 - 02/23/2003 - added version function (ship names are reused, with roman numerals added after their
 * names)
 * <LI>Version 1.4 - 09/30/2004 - moved the Roman numeral suffixes to a function (utility_numbers.convertRoman ()) and
 * improved the code style slightly
 * <LI>Version 2.0 - 10/19/2004 - moved the names into text data files, and changed the name from Names to NameFactory
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/19/2004
 */

public class NameFactory implements Serializable {

	private static final String FEDERATION_PEOPLE_NAMES = "/data/federation_people_names.txt";

	private static final String FEDERATION_SHIP_NAMES = "/data/federation_ship_names.txt";

	private static final String PIRATE_SHIP_NAMES = "/data/pirate_ship_names.txt";

	private static final String RAIDER_SHIP_NAMES = "/data/raider_ship_names.txt";

	private static final long serialVersionUID = 1L;

	/** names for federation people */
	private List<String> federationPeopleNames;

	/** indicates the number of times the full set of Federation ship names has been used */
	private int federationShipNameRepeats = 0;

	/** names for federation ships */
	private List<String> federationShipNames;

	/** indicates the number of times the full set of pirate ship names has been used */
	private int pirateShipNameRepeats = 0;

	/** names for pirate ships */
	private List<String> pirateShipNames;

	/** indicates the number of times the full set of raider ship names has been used */
	private int raiderShipNameRepeats = 0;

	/** names for raider ships */
	private List<String> raiderShipNames;

	/**
	 * The default constructor.
	 * 
	 * @since 1.0
	 */
	public NameFactory() {

		federationPeopleNames = loadNames(FEDERATION_PEOPLE_NAMES);
		federationShipNames = loadNames(FEDERATION_SHIP_NAMES);
		pirateShipNames = loadNames(PIRATE_SHIP_NAMES);
		raiderShipNames = loadNames(RAIDER_SHIP_NAMES);
	}

	/**
	 * Retrieves a random federation person name from the list. The name is removed from the list, so that each person
	 * in the game will have a unique name.
	 * 
	 * @return a federation person name
	 * 
	 * @since 1.2
	 */

	public String getFederationPersonName() {
		// get the size of the array
		int size = federationPeopleNames.size();

		// if the array is not empty, proceed
		if (size > 0) {
			// choose an element
			int index = (int) (Math.random() * 10000) % size;

			// retrieve the element
			String name = (String) (federationPeopleNames.get(index));

			// remove the element from the array
			federationShipNames.remove(index);

			// return the element
			return name;
		}

		return "Unloved";
	}

	/**
	 * Retrieves a random federation ship name from the list. The name is removed from the list, so that each ship in
	 * the game will have a unique name.
	 * 
	 * @return a federation ship name
	 * 
	 * @since 1.0
	 */

	public String getFederationShipName() {
		// get the size of the array
		int size = federationShipNames.size();

		// if the array is not empty, proceed
		if (size > 0) {
			// choose an element
			int index = (int) (Math.random() * 1000) % size;

			// retrieve the element
			String name = (String) (federationShipNames.get(index));

			// remove the element from the array
			federationShipNames.remove(index);

			// if necessary, append a version number to the name
			if (federationShipNameRepeats > 1) {
				name = name + " " + NumbersUtility.convertRoman(federationShipNameRepeats);
			}

			// return the element
			return name;
		}

		// re-populate the array
		federationShipNames = loadNames(FEDERATION_SHIP_NAMES);
		federationShipNameRepeats++;

		// get a name and return it
		return getFederationShipName();
	}

	/**
	 * Retrieves a random pirate ship name from the list. The name is removed from the list, so that each ship in the
	 * game will have a unique name.
	 * 
	 * @return a pirate ship name
	 * 
	 * @since 1.0
	 */

	public String getPirateShipName() {
		// get the size of the array
		int size = pirateShipNames.size();

		// if the array is not empty, proceed
		if (size > 0) {
			// choose an element
			int index = (int) (Math.random() * 1000) % size;

			// retrieve the element
			String name = (String) (pirateShipNames.get(index));

			// remove the element from the array
			pirateShipNames.remove(index);

			// if necessary, append a version number to the name
			if (pirateShipNameRepeats > 1) {
				name = name + " " + NumbersUtility.convertRoman(pirateShipNameRepeats);
			}

			// return the element
			return name;
		}

		// re-populate the array
		pirateShipNames = loadNames(PIRATE_SHIP_NAMES);
		pirateShipNameRepeats++;

		// get a name and return it
		return getPirateShipName();
	}

	/**
	 * Retrieves a random raider ship name from the list. The name is removed from the list, so that each ship in the
	 * game will have a unique name.
	 * 
	 * @return a raider ship name
	 * 
	 * @since 1.0
	 */

	public String getRaiderShipName() {
		// get the size of the array
		int size = raiderShipNames.size();

		// if the array is not empty, proceed
		if (size > 0) {
			// choose an element
			int index = (int) (Math.random() * 1000) % size;

			// retrieve the element
			String name = (String) (raiderShipNames.get(index));

			// remove the element from the array
			raiderShipNames.remove(index);

			// if necessary, append a version number to the name
			if (raiderShipNameRepeats > 1) {
				name = name + " " + NumbersUtility.convertRoman(raiderShipNameRepeats);
			}

			// return the element
			return name;
		}

		// re-populate the array
		raiderShipNames = loadNames(RAIDER_SHIP_NAMES);
		raiderShipNameRepeats++;

		// get a name and return it
		return getRaiderShipName();
	}

	/**
	 * Loads a list of strings from the specified file and returns them in a list.
	 * 
	 * @param filename
	 * 
	 * @since 3.0
	 */
	private List<String> loadNames(String filename) {
		List<String> list = new ArrayList<>();

		try {
			InputStream in = getClass().getResourceAsStream(filename);
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			Scanner scanner = new Scanner(input);
			scanner.useDelimiter("\n");
			while (scanner.hasNext())
				list.add(scanner.next().trim());
			scanner.close();
			input.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return list;
	}

}