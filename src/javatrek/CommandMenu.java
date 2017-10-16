package javatrek;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javatrek.handlers.ExitGame;
import javatrek.handlers.QuickLoad;
import javatrek.handlers.QuickSave;
import javatrek.handlers.RefreshDisplay;
import javatrek.handlers.debug.DebugEnergyDamage;
import javatrek.handlers.debug.DebugEnergyDamageSystem;
import javatrek.handlers.debug.DebugGivePlayerExperience;
import javatrek.handlers.debug.DebugIONDamage;
import javatrek.handlers.debug.DebugProjectileDamage;
import javatrek.handlers.debug.DebugProjectileDamageSystem;
import javatrek.handlers.debug.DebugRevealRegions;
import javatrek.panels.InstructionsPanel;

/**
 * The application's menu bar.
 * 
 * <UL>
 * <LI>Version 1.1 - 04/06/2002 - the original class
 * <LI>Version 1.0 - 03/09/2002 - the original was a pop-up menu
 * <LI>Version 2.0 - 10/21/2004 - now uses seperate action handlers and menu- and item-creating functions
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/21/2004
 */

public class CommandMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the command menu.
	 * 
	 * @param dbg
	 *            sets debugging mode (or not :)
	 * 
	 * @since 1.0
	 */

	public CommandMenu(boolean dbg) {
		super();

		// file menu
		JMenu m_file = createMenu("File", KeyEvent.VK_F);
		createMenuItem(m_file, "Quick Load", "loads the quick-save game", new QuickLoad());
		createMenuItem(m_file, "Quick Save", "quick-saves your game", new QuickSave());
		m_file.addSeparator();
		createMenuItem(m_file, "Exit", "exits the game", KeyEvent.VK_X, new ExitGame());

		// view menu
		JMenu m_view = createMenu("View", KeyEvent.VK_V);
		createMenuItem(m_view, "Clear Console", "clears the console", KeyEvent.VK_C,
				e -> JavaTrek.game.console.clearMessages());
		createMenuItem(m_view, "Quote", "displays a quote", KeyEvent.VK_Q, e -> JavaTrek.game.console.displayQuote());
		createMenuItem(m_view, "Refresh Display", "refreshes the displays", KeyEvent.VK_R, new RefreshDisplay());

		// debug menu
		if (dbg == true) {
			JMenu m_debug = createMenu("Debug", KeyEvent.VK_D);
			createMenuItem(m_debug, "Damage - Energy",
					"applies 101 points of energy-weapon damage to the player's ship", KeyEvent.VK_E,
					new DebugEnergyDamage());
			createMenuItem(m_debug, "Damage - ION", "applies 101 points of energy-weapon damage to the player's ship",
					KeyEvent.VK_E, new DebugIONDamage());
			createMenuItem(m_debug, "Damage - Projectile",
					"applies 101 points of energy-weapon damage to the player's ship", KeyEvent.VK_P,
					new DebugProjectileDamage());
			createMenuItem(m_debug, "Damage Systems - Energy",
					"applies 101 points of energy-weapon damage to the player's ship's systems", KeyEvent.VK_E,
					new DebugEnergyDamageSystem());
			createMenuItem(m_debug, "Damage Systems - Projectile",
					"applies 101 points of energy-weapon damage to the player's ship's systems", KeyEvent.VK_P,
					new DebugProjectileDamageSystem());
			createMenuItem(m_debug, "Give Player XP", "gives the player 5000 experience points", KeyEvent.VK_G,
					new DebugGivePlayerExperience());
			createMenuItem(m_debug, "Reveal Regions", "displays all regions", KeyEvent.VK_R, new DebugRevealRegions());
		}

		// create and add the menu items for the help menu
		JMenu m_help = createMenu("Help", KeyEvent.VK_H);
		createMenuItem(m_help, "Instructions", "displays instructions for the game", KeyEvent.VK_I,
				e -> JavaTrek.game.showPanel(InstructionsPanel.class.getName()));
		m_help.addSeparator();
		createMenuItem(m_help, "Version", "displays version information in the console", KeyEvent.VK_V,
				e -> JavaTrek.game.console.addMessage("BlackLeft", JavaTrek.GAME_TITLE + "\n"));
	}

	/**
	 * Creates a menu, adds it to the menu bar, and returns it.
	 * 
	 * @param name
	 *            the name of the menu
	 * @param mnemonic
	 *            mnemonic for the menu item
	 * 
	 * @since 2.0
	 */

	private JMenu createMenu(String name, int mnemonic) {
		JMenu menu = new JMenu(name);
		menu.setMnemonic(mnemonic);

		add(menu);

		return menu;
	}

	/**
	 * Creates a menu item, adds it to a menu, specifies this class as the menu item's action handler, sets the tool tip
	 * text, adds a mnemonic and returns it.
	 * 
	 * @param menu
	 *            the menu to add the item to
	 * @param name
	 *            the name of the menu item
	 * @param tip
	 *            toop tip text
	 * @param ah
	 *            the item's action listener
	 * 
	 * @since 2.0
	 */

	private JMenuItem createMenuItem(JMenu menu, String name, String tip, ActionListener ah) {
		JMenuItem item = new JMenuItem(name);
		item.setToolTipText(tip);
		item.addActionListener(ah);

		menu.add(item);

		return item;
	}

	/**
	 * Creates a menu item, adds it to a menu, specifies this class as the menu item's action handler, sets the tool tip
	 * text, adds a mnemonic and returns it.
	 * 
	 * @param menu
	 *            the menu to add the item to
	 * @param name
	 *            the name of the menu item
	 * @param tip
	 *            toop tip text
	 * @param mnemonic
	 *            mnemonic for the menu item
	 * @param ah
	 *            the item's action listener
	 * 
	 * @since 2.0
	 */

	private JMenuItem createMenuItem(JMenu menu, String name, String tip, int mnemonic, ActionListener ah) {
		JMenuItem item = new JMenuItem(name);
		item.setToolTipText(tip);
		item.setMnemonic(mnemonic);
		item.addActionListener(ah);

		menu.add(item);

		return item;
	}

}