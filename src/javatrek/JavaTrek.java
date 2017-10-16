package javatrek;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import javatrek.factory.ShipFactory;
import javatrek.panels.AssignPilotPointsPanel;
import javatrek.panels.ConsolePanel;
import javatrek.panels.EndOfGamePanel;
import javatrek.panels.FramePanel;
import javatrek.panels.InstructionsPanel;
import javatrek.panels.JavaTrekPanel;
import javatrek.panels.MainScreen;

/**
 * <P>
 * This is the main object of the JavaTrek game.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/01/2002 - the original instance
 * <LI>Version 1.1 - 03/24/2002 - updated, bug fixes etc
 * <LI>Version 1.2 - 11/25/2002 - added save game ability
 * <LI>Version 1.3 - 05/03/2003 - seperated the interface and game-startup code
 * (to allow multiple ways to start a game, such as from saved games)
 * <LI>Version 1.4 - 10/18/2003 - updated to support windowed and full screen
 * playing modes
 * <LI>Version 1.5 - 10/21/2004 - removed full screen option, as it limits what
 * one can do with multiple windows
 * <LI>Version 2.0 - 11/13/2004 - cleaned up the code, made minor alterations
 * <LI>Version 3.0 - 09/16/2017 - modernizing the code
 * </UL>
 * 
 * @author Andrew Killam
 * @version 3.0 - 09/16/2017
 */

public class JavaTrek extends JFrame {

	/** a handle to the application */
	public static JavaTrek game;

	/** game title and version */
	public static final String GAME_TITLE = "JavaTrek 3.0 (alpha)";

	public static ImageManager imgMgr = new ImageManager();

	/** the initial height of the game */
	private static final int INITIAL_HEIGHT = 600;

	/** the initial width of the game */
	private static final int INITIAL_WIDTH = 800;

	private static final long serialVersionUID = 1L;

	/** the game's ship factory */
	public static ShipFactory ship_factory;

	/**
	 * The main function, which allows the application to run.
	 * 
	 * @param args
	 *            command-line arguments
	 * 
	 * @since 1.0
	 */
	public static void main(String args[]) {
		// specify that if an image is accessed once it will be
		// switched to a volatile image (if possible)
		System.setProperty("sun.java2d.accthreshold", "1");

		// create the ship factory
		ship_factory = new ShipFactory();

		// set the look and feel
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			// UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error!  Couldn't set look and feel!");
			System.exit(1);
		}

		// create the game, update it's display and make it visible
		if ((args.length == 1) && (args[0].compareTo("debug") == 0)) {
			game = new JavaTrek(true);
		} else {
			game = new JavaTrek(false);
		}
		game.refresh();
		game.setVisible(true);
	}

	/** handles cards/panels that fill the entire window */
	private CardLayout cl_full;

	/** holds the console objects */
	public ConsolePanel console;

	/** holds the game's data objects */
	public GameData gamedata;

	/** holds the cards/panels for the centre of the entire window */
	private JPanel p_full;

	/**
	 * holds JavaTrekPanels so that they can all be refreshed at once, and quickly
	 * accessed if needed
	 */
	private HashMap<String, JavaTrekPanel> panels = new HashMap<>();
	
	/**
	 * Creates an instance of the game
	 * 
	 * @param dbg
	 *            sets debugging mode (or not :)
	 * 
	 * @since 1.0
	 */

	public JavaTrek(boolean dbg) {
		super("Javatrek");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(GAME_TITLE);

		// create the game data object with the specified player name and playing level
		// TODO: use a preferences setting for the player's name
		gamedata = new GameData("Andrew");

		// use a border layout for the content pane
		setContentPane(new JPanel(new BorderLayout()));

		// create and add the centre panel and its card layout manager
		cl_full = new CardLayout();
		p_full = new JPanel(cl_full);
		getContentPane().add(p_full, BorderLayout.CENTER);

		// create and add the menu
		setJMenuBar(new CommandMenu(dbg));

		// create the user interface bits
		setupInterface();

		// allows time-triggered events occur
		gamedata.passTime(0);

		// centre the window of the desktop
		resizeCentre();
	}

	/**
	 * Ends the game.
	 * 
	 * @param victory
	 *            indicates if the game was won or lost
	 * @param message
	 *            an end of game message, indicating the reason for the end of the
	 *            game
	 *
	 * @since 1.3
	 */

	public void endgame(boolean victory, String message) {
		// clear all remaining space objects
		gamedata.space.removeAll();

		// display the endgame panel
		EndOfGamePanel p_itsover = new EndOfGamePanel(victory, message);
		// p_full.add ("endgame", p_itsover);
		// cl_full.show (p_full, "endgame");
		setContentPane(p_itsover);
	}

	/**
	 * Loads a saved game.
	 * 
	 * @param filename
	 *            the filename to use
	 * 
	 * @since 1.2
	 */

	public void loadgame(String filename) {
		// only meaningful strings will be considered
		if (filename.length() > 0) {
			// ensure correct file extension
			if (filename.indexOf(".jtg") < 1) {
				filename = filename.concat(".jtg");
			}

			try {
				FileInputStream fis = new FileInputStream(filename);
				ObjectInputStream ois = new ObjectInputStream(fis);
				gamedata = (GameData) (ois.readObject());
				ois.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// console message
			console.clearMessages();
			console.addMessage("BlackLeft", "Game loaded from file " + filename + ".\n");

			// refresh display
			refresh();
		}
	}

	/**
	 * Calls the panels' refresh functions.
	 * 
	 * @since 1.0
	 */

	public void refresh() {
		if ((panels != null) && (panels.size() > 0)) {
			for (JavaTrekPanel p : panels.values())
				p.refresh();
		}
	}

	/**
	 * Resizes the frame and centres the window on the user's desktop.
	 * 
	 * @since 1.1
	 */

	private void resizeCentre() {
		setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (scr.width >> 1) - (INITIAL_WIDTH >> 1);
		int y = (scr.height >> 1) - (INITIAL_HEIGHT >> 1);
		setLocation(x, y);
	}

	/**
	 * Saves the game to a binary file.
	 * 
	 * @param filename
	 *            the filename to use
	 * 
	 * @since 1.2
	 */

	public void saveGame(String filename) {
		// only meaningful strings will be considered
		if (filename.length() > 0) {
			// ensure correct file extension
			if (filename.indexOf(".jtg") < 1) {
				filename = filename.concat(".jtg");
			}

			try {
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(gamedata);
				oos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// console message
			console.addMessage("BlackLeft", "Game saved to file " + filename + ".\n");
		}
	}

	/**
	 * Creates the game's main interface components.
	 * 
	 * @since 1.3
	 */

	private void setupInterface() {
		// create and add the north frame panel
		FramePanel fp = new FramePanel(BorderLayout.NORTH);
		getContentPane().add(fp, BorderLayout.NORTH);
		panels.put(BorderLayout.NORTH, fp);

		// create and add the south frame panel
		fp = new FramePanel(BorderLayout.SOUTH);
		getContentPane().add(fp, BorderLayout.SOUTH);
		panels.put(BorderLayout.SOUTH, fp);

		// create and add the west frame panel
		fp = new FramePanel(BorderLayout.WEST);
		getContentPane().add(fp, BorderLayout.WEST);
		panels.put(BorderLayout.WEST, fp);

		// create and add the east frame panel
		fp = new FramePanel(BorderLayout.EAST);
		getContentPane().add(fp, BorderLayout.EAST);
		panels.put(BorderLayout.EAST, fp);

		// create the instructions panel
		p_full.add(InstructionsPanel.class.getName(), new InstructionsPanel());

		// create the assign pilot points panel
		AssignPilotPointsPanel app = new AssignPilotPointsPanel();
		p_full.add(AssignPilotPointsPanel.class.getName(), app);
		panels.put(AssignPilotPointsPanel.class.getName(), app);

		// create the console and the main game panel
		console = new ConsolePanel();
		MainScreen ms = new MainScreen(console);
		p_full.add(MainScreen.class.getName(), ms);
		panels.put(MainScreen.class.getName(), ms);

		// ensure that the base game panel is dispalyed
		cl_full.show(p_full, MainScreen.class.getName());
	}

	/**
	 * Displays a specified panel.
	 * 
	 * @param panel
	 *            the panel to display
	 * 
	 * @since 2.0
	 */

	public void showPanel(String panel) {
		cl_full.show(p_full, panel);
	}

}