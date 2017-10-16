package javatrek.controls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javatrek.ImageManager.ImagesEnum;
import javatrek.JavaTrek;
import javatrek.panels.JavaTrekPanel;
import javatrek.spaceobjects.Ship;
import javatrek.systems.Shields;

/**
 * <P>
 * This panel allows the user to transfer specific amounts of energy between shields and the main supply of their ship.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/24/2002 - the original TransferEnergyRow class
 * <LI>Version 1.1 - 04/19/2003 - changed from a multi-row panel to a single-row panel suitable for inclusion in other
 * control panels
 * <LI>Version 2.0 - 10/31/2004 - renamed to TransferEnergyPanel, enlarged the icons, reduced the buttons' sizes and
 * cleaned up the code ever so slightly
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/31/2004 - renamed to TransferEnergyPanel, enlarged the icons, reduced the buttons' sizes and
 *          cleaned up the code ever so slightly
 */

public class TransferEnergyPanel extends JPanel implements ActionListener, JavaTrekPanel {

	private static final long serialVersionUID = 1L;

	/** transfer energy to the main supply */
	private JButton b_main;

	/** transfer energy to the shields */
	private JButton b_shields;

	/** specifies the amount of energy to transfer */
	private JTextField tf_energy;

	/**
	 * Creates a panel with the required data access.
	 * 
	 * @since 1.0
	 */

	public TransferEnergyPanel() {
		// set the layout manager
		setLayout(new BorderLayout());

		// used tp specify the size of buttons
		Dimension preferred = new Dimension(32, 32);

		// the label for the panel
		add(new JLabel(" Energy "), BorderLayout.WEST);

		// add the text field energy values are entered into
		tf_energy = new JTextField(5);
		add(tf_energy, BorderLayout.CENTER);

		// create the buttons
		JPanel p_buttons = new JPanel(new GridLayout(1, 2));
		b_shields = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SUN_ICON_EXPORT_24));
		b_shields.setToolTipText("transfer energy from the main supply to shields");
		b_shields.addActionListener(this);
		b_shields.setPreferredSize(preferred);
		b_shields.setMinimumSize(preferred);
		b_shields.setMaximumSize(preferred);
		p_buttons.add(b_shields);
		b_main = new JButton(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.IMPORT));
		b_main.setToolTipText("transfer energy from shields to the main supply");
		b_main.addActionListener(this);
		b_main.setPreferredSize(preferred);
		b_main.setMinimumSize(preferred);
		b_main.setMaximumSize(preferred);
		p_buttons.add(b_main);

		add(p_buttons, BorderLayout.EAST);
	}

	/**
	 * Handles action events.
	 * 
	 * @param e
	 *            the incoming action event
	 * 
	 * @since 1.0
	 */

	public void actionPerformed(ActionEvent e) {
		// retrieve the player's ship
		Ship ship = JavaTrek.game.gamedata.space.getPlayersShip();

		// get the number entered in the text field
		int amount = 0;
		try {
			// convert the string to an integer value
			amount = Integer.valueOf(tf_energy.getText()).intValue();
		} catch (NumberFormatException nfe) {
			// if an invalid value was entered, use 0
			amount = 0;
		} finally {
			// if the amount is incorrect, correct it
			if (amount < 0)
				amount = 0;
		}

		// clear the text field
		tf_energy.setText("");

		// transfer energy to the main supply
		if (e.getSource() == b_main) {
			// is the amount available?
			Shields shields = (Shields) ship.getSystem(Shields.class.getName());
			if (shields.removeEnergy(amount) == true) {
				// add the energy to main, catching overflow
				int overflow = ship.addEnergy(amount);

				// put the overflow back
				shields.addEnergy(overflow);

				// notify the player
				JavaTrek.game.console.addMessage("GreenLeft",
						(amount - overflow) + " energy was added to main power and " + overflow
								+ " energy overflowed back into the shield system.\n");
			} else {
				// notify the player
				JavaTrek.game.console.addMessage("RedLeft", "You do not have enough shield energy to do that.\n");
			}
		}

		// transfer energy to shields
		else if (e.getSource() == b_shields) {
			Shields shields = (Shields) ship.getSystem(Shields.class.getName());
			if (shields == null)
				return;

			// is that amount available?
			if (ship.removeEnergy(amount) == true) {
				// add energy to shields, catching any overflow
				int overflow = shields.addEnergy(amount);

				// replace any overflow in the main banks
				ship.addEnergy(overflow);

				// notify the player
				JavaTrek.game.console.addMessage("GreenLeft", (amount - overflow) + " energy was added to shields and "
						+ overflow + " energy overflowed back into the main banks.\n");
			} else {
				JavaTrek.game.console.addMessage("RedLeft", "You do not have enough main energy to do that.\n");
			}
		}

		// update the display
		JavaTrek.game.refresh();
	}

	/**
	 * Refreshes the display.
	 * 
	 * @since 2.0
	 */

	public void refresh() {
		tf_energy.setText("");
	}

}