package javatrek.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import javatrek.ImageManager.ImagesEnum;
import javatrek.JavaTrek;
import javatrek.controls.EnergyTransferButton;
import javatrek.controls.LightDriveSpeedPanel;
import javatrek.controls.PilotImprovementButton;
import javatrek.controls.ShieldsToggleButton;
import javatrek.controls.TransferEnergyPanel;
import javatrek.handlers.Rest;

/**
 * <P>
 * Displays icon and game controls for one of the four edges of the game.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/31/2004 - the original instance
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/31/2004
 */

public class FramePanel extends JPanel implements JavaTrekPanel {

	private static final long serialVersionUID = 1L;

	/** grid bag layout manager for the panel */
	private GridBagLayout gb;

	/** constaints for the grid bag layout manager */
	private GridBagConstraints gbc;

	/** holds JavaTrekPanels so that they can all be refreshed at once */
	private List<JavaTrekPanel> panels = new ArrayList<>();

	/**
	 * Creates a frame panel.
	 * 
	 * @param side
	 *            specifies which frame panel to create
	 * 
	 * @since 2.0
	 */

	public FramePanel(String side) {
		super();
		gb = new GridBagLayout();
		setLayout(gb);

		// used to add items to the layout
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.75;
		gbc.weighty = 0.75;

		// call the appropriate setup function
		if (side == BorderLayout.EAST)
			setupEast();
		else if (side == BorderLayout.NORTH)
			setupNorth();
		else if (side == BorderLayout.SOUTH)
			setupSouth();
		else if (side == BorderLayout.WEST)
			setupWest();
	}

	/**
	 * Adds a component to the display, at a specified grid location.
	 * 
	 * @param item
	 *            the component to add
	 * @param x
	 *            the x-location in the grid
	 * @param y
	 *            the y-location in the grid
	 * 
	 * @since 1.2
	 */

	private void add(JComponent item, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;

		gb.setConstraints(item, gbc);

		add(item);
	}

	/**
	 * Calls the refresh function for items which require it.
	 * 
	 * @since 2.0
	 */

	public void refresh() {
		if ((panels != null) && (panels.size() > 0)) {
			for (JavaTrekPanel p : panels)
				p.refresh();
		}
	}

	/**
	 * Creates a east frame panel.
	 * 
	 * @since 2.0
	 */

	private void setupEast() {
	}

	/**
	 * Creates a north frame panel.
	 * 
	 * @since 2.0
	 */

	private void setupNorth() {
		// used tp specify the size of buttons
		// Dimension preferred = new Dimension (32, 32);

		// tracks which column to place the next control on
		// int col = 0;
	}

	/**
	 * Creates a south frame panel.
	 * 
	 * @since 2.0
	 */

	private void setupSouth() {
		// tracks which column to place the next control on
		int col = 0;

		// add the main energy transfer button
		EnergyTransferButton energy = new EnergyTransferButton(EnergyTransferButton.MAIN_ENERGY);
		add(energy, col++, 0);
		panels.add(energy);

		// add the shield energy transfer button
		EnergyTransferButton shield = new EnergyTransferButton(EnergyTransferButton.SHIELD_ENERGY);
		add(shield, col++, 0);
		panels.add(shield);

		// add the light drive speed setting panel
		LightDriveSpeedPanel ld = new LightDriveSpeedPanel();
		panels.add(ld);
		add(ld, col++, 0);

		// add the energy transfer panel
		TransferEnergyPanel tp = new TransferEnergyPanel();
		panels.add(tp);
		add(tp, col++, 0);
	}

	/**
	 * Creates a west frame panel.
	 * 
	 * @since 2.0
	 */

	private void setupWest() {
		// tracks which row to place the next control on
		int row = 0;

		// used tp specify the size of buttons
		Dimension preferred = new Dimension(32, 32);

		// add the main display button
		JButton b_main = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SUN_ICON_WEB_COMPONENT));
		b_main.setToolTipText("display the main game interface");
		b_main.addActionListener(e -> JavaTrek.game.showPanel(MainScreen.class.getName()));
		b_main.setPreferredSize(preferred);
		b_main.setMinimumSize(preferred);
		b_main.setMaximumSize(preferred);
		add(b_main, 0, row++);

		// add the documentation button
		JButton b_doc = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.HELP));
		b_doc.setToolTipText("display the game's documentation");
		b_doc.addActionListener(e -> JavaTrek.game.showPanel(InstructionsPanel.class.getName()));
		b_doc.setPreferredSize(preferred);
		b_doc.setMinimumSize(preferred);
		b_doc.setMaximumSize(preferred);
		add(b_doc, 0, row++);

		// add the quick save button
		JButton b_quicksave = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SAVE));
		b_quicksave.setToolTipText("quickly save the game");
		b_quicksave.addActionListener(e -> JavaTrek.game.saveGame("quick"));
		b_quicksave.setPreferredSize(preferred);
		b_quicksave.setMinimumSize(preferred);
		b_quicksave.setMaximumSize(preferred);
		add(b_quicksave, 0, row++);

		// add the quick load button
		JButton b_quickload = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SAVE_ALL));
		b_quickload.setToolTipText("quickly load the game");
		b_quickload.addActionListener(e -> JavaTrek.game.loadgame("quick"));
		b_quickload.setPreferredSize(preferred);
		b_quickload.setMinimumSize(preferred);
		b_quickload.setMaximumSize(preferred);
		add(b_quickload, 0, row++);

		// add the rest for an hour button
		JButton b_rest_hour = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.REDO));
		b_rest_hour.setToolTipText("rest for one hour");
		b_rest_hour.addActionListener(new Rest(1));
		b_rest_hour.setPreferredSize(preferred);
		b_rest_hour.setMinimumSize(preferred);
		b_rest_hour.setMaximumSize(preferred);
		add(b_rest_hour, 0, row++);

		// add the rest for twenty-four hours button
		JButton b_rest_24 = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.REFRESH));
		b_rest_24.setToolTipText("rest for eight hours");
		b_rest_24.addActionListener(new Rest(8));
		b_rest_24.setPreferredSize(preferred);
		b_rest_24.setMinimumSize(preferred);
		b_rest_24.setMaximumSize(preferred);
		add(b_rest_24, 0, row++);

		// add the shields button
		ShieldsToggleButton shields_button = new ShieldsToggleButton();
		shields_button.setPreferredSize(preferred);
		shields_button.setMinimumSize(preferred);
		shields_button.setMaximumSize(preferred);
		panels.add(shields_button);
		add(shields_button, 0, row++);

		// add pilot improvement button
		PilotImprovementButton pib = new PilotImprovementButton();
		pib.setPreferredSize(preferred);
		pib.setMinimumSize(preferred);
		pib.setMaximumSize(preferred);
		panels.add(pib);
		add(pib, 0, row++);
	}

}
