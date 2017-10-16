package javatrek.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javatrek.ImageManager.ImagesEnum;
import javatrek.JavaTrek;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.SpaceObject;
import javatrek.systems.Generator;
import javatrek.systems.LightDrive;
import javatrek.systems.LongRangeScanner;
import javatrek.systems.Shields;
import javatrek.systems.ShortRangeScanner;

/**
 * This panel displays JavaTrek's ships' details.
 * 
 * <UL>
 * <LI>Version 1.0 - 11/22/2002 - the original class
 * <LI>Version 1.1 - 10/19/2004 - updated to use the new ShipFactory class to create ships
 * <LI>Version 2.0 - 11/22/2004 - renamed to ShipsDatabasePanel, changed the button icons and made small changes to
 * neaten up the code
 * </UL>
 * 
 * @author Andrew Killam
 * @version Version 2.0 - 11/22/2004
 */

public class ShipsDatabasePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** the back button */
	private JButton b_back;

	/** the beginning button */
	private JButton b_beginning;

	/** the end button */
	private JButton b_end;

	/** the forward button */
	private JButton b_forward;

	/** the card layout manager for the display */
	private CardLayout card_layout;

	/** holds the controls for changing the viewed ship */
	private JPanel p_controls;

	/** the main display panel */
	private JPanel p_main;

	/**
	 * The default constructor.
	 * 
	 * @since 1.0
	 */

	public ShipsDatabasePanel() {
		setLayout(new BorderLayout());

		// create the main panel
		card_layout = new CardLayout();
		p_main = new JPanel(card_layout);
		add(p_main, BorderLayout.CENTER);

		// set up the ship panels
		setupShips();

		// set up the control panel
		setupControls();
	}

	/**
	 * Handles the buttons being clicked.
	 * 
	 * @param e
	 *            the incoming event
	 * 
	 * @since 1.0
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b_back) {
			card_layout.previous(p_main);
		}

		else if (e.getSource() == b_beginning) {
			card_layout.first(p_main);
		}

		else if (e.getSource() == b_forward) {
			card_layout.next(p_main);
		}

		else if (e.getSource() == b_end) {
			card_layout.last(p_main);
		}
	}

	/**
	 * Creates the control panel.
	 * 
	 * @since 1.0
	 */

	private void setupControls() {
		p_controls = new JPanel(new GridLayout(1, 4));

		b_beginning = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.STEP_BACK_24));
		b_beginning.addActionListener(this);
		b_beginning.setToolTipText("return to first entry");
		p_controls.add(b_beginning);

		b_back = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SUN_ICON_BACK_24));
		b_back.addActionListener(this);
		b_back.setToolTipText("previous entry");
		p_controls.add(b_back);

		b_forward = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SUN_ICON_FORWARD_24));
		b_forward.addActionListener(this);
		b_forward.setToolTipText("next entry");
		p_controls.add(b_forward, BorderLayout.EAST);

		b_end = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.STEP_FORWARD));
		b_end.addActionListener(this);
		b_end.setToolTipText("skip to last entry");
		p_controls.add(b_end, BorderLayout.EAST);

		add(p_controls, BorderLayout.SOUTH);
	}

	/**
	 * Creates the ship panels.
	 * 
	 * @since 1.0
	 */

	private void setupShips() {
		// get a list of the classes of ships in the game
		List<String> ships = JavaTrek.ship_factory.getShipClasses();

		// create the array of panels and an array of strings
		JPanel[] p_ships = new JPanel[ships.size()];

		// iterate through the array of constants
		for (int i = 0; i < ships.size(); i++) {
			// create the panel
			p_ships[i] = new JPanel(new GridLayout(6, 4));
			p_ships[i].setBackground(Color.WHITE);

			// create an instance of the ship type
			Ship ship = JavaTrek.ship_factory.createShip((String) ships.get(i), "demo", SpaceObject.FEDERATION, 1,
					false, 0, 0, 0, 0, 0);

			// ship information begins here
			if (ship != null) {
				p_ships[i].add(new JLabel("Icon:  ", JLabel.LEFT));
				p_ships[i].add(
						new JLabel(JavaTrek.imgMgr.loadImageIcon("/images/ships/" + ship.getImageName()), JLabel.LEFT));

				p_ships[i].add(new JLabel("Type:  ", JLabel.LEFT));
				p_ships[i].add(new JLabel(ship.getShipType(), JLabel.LEFT));

				p_ships[i].add(new JLabel("Hull Strength:  ", JLabel.LEFT));
				p_ships[i].add(new JLabel(String.valueOf(ship.getHPMax()), JLabel.LEFT));

				p_ships[i].add(new JLabel("Shield Strength:  ", JLabel.LEFT));
				Shields shields = (Shields) ship.getSystem(Shields.class.getName());
				if (shields == null) {
					p_ships[i].add(new JLabel("No Shields", JLabel.LEFT));
				} else {
					p_ships[i].add(new JLabel(String.valueOf(shields.getCapacity()) + " (" + shields.getName() + ")",
							JLabel.LEFT));
				}

				p_ships[i].add(new JLabel("Energy Capacity:  ", JLabel.LEFT));
				p_ships[i].add(new JLabel(String.valueOf(ship.getEnergyMax()), JLabel.LEFT));

				p_ships[i].add(new JLabel("Energy Generator:  ", JLabel.LEFT));
				Generator generator = (Generator) ship.getSystem(Generator.class.getName());
				if (generator == null) {
					p_ships[i].add(new JLabel("No Generator", JLabel.LEFT));
				} else {
					p_ships[i].add(new JLabel(String.valueOf(generator.getOutput()) + " / hour", JLabel.LEFT));
				}

				p_ships[i].add(new JLabel("Hyperdrive:  ", JLabel.LEFT));
				LightDrive ld = (LightDrive) ship.getSystem(LightDrive.class.getName());
				if (ld == null) {
					p_ships[i].add(new JLabel("No Hyperdrive", JLabel.LEFT));
				} else {
					p_ships[i].add(new JLabel(String.valueOf(ld.getMax()) + " (max)", JLabel.LEFT));
				}

				p_ships[i].add(new JLabel("Short-Range Scanner:  ", JLabel.LEFT));
				ShortRangeScanner sr = (ShortRangeScanner) ship.getSystem(ShortRangeScanner.class.getName());
				if (sr == null) {
					p_ships[i].add(new JLabel("none", JLabel.LEFT));
				} else {
					p_ships[i].add(new JLabel(sr.getName(), JLabel.LEFT));
				}

				p_ships[i].add(new JLabel("Long-Range Scanner:  ", JLabel.LEFT));
				LongRangeScanner lr = (LongRangeScanner) ship.getSystem(LongRangeScanner.class.getName());
				if (lr == null) {
					p_ships[i].add(new JLabel("none", JLabel.LEFT));
				} else {
					p_ships[i].add(new JLabel(lr.getName() + " (radius:  " + String.valueOf(lr.getRadius()) + ")",
							JLabel.LEFT));
				}

				p_ships[i].add(new JLabel("Repair Ability:  ", JLabel.LEFT));
				p_ships[i]
						.add(new JLabel(String.valueOf((int) (ship.getRepairPoints() * 100)) + " / hour", JLabel.LEFT));

				p_ships[i].add(new JLabel("Dodge Ability:  ", JLabel.LEFT));
				p_ships[i].add(new JLabel(String.valueOf(ship.getDodge()), JLabel.LEFT));

				p_ships[i].add(new JLabel("Experience Value:  ", JLabel.LEFT));
				p_ships[i].add(new JLabel(String.valueOf(ship.getPointValue()), JLabel.LEFT));

				// ship information ends here

				// add the ship to the layout
				p_main.add(p_ships[i], ships.get(i));
			}
		}
	}

}