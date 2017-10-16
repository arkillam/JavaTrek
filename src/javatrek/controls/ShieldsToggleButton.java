package javatrek.controls;

import javatrek.JavaTrek;
import javatrek.ImageManager.ImagesEnum;
import javatrek.panels.JavaTrekPanel;
import javatrek.systems.Shields;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Controls the player's ship's shields.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/30/2004 - the original class
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/30/2004
 */

public class ShieldsToggleButton extends JToggleButton implements ChangeListener, JavaTrekPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the button.
	 * 
	 * @since 2.0
	 */

	public ShieldsToggleButton() {
		super(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.SHIELDS), false);

		setToolTipText("Click to toggle shields on and off.");

		setBorder(null);

		addChangeListener(this);
	}

	/**
	 * Updates the button according to the game's values.
	 * 
	 * @since 2.0
	 */

	public void refresh() {
		Shields s = (Shields) JavaTrek.game.gamedata.space.getPlayersShip().getSystem(Shields.class.getName());
		if (s != null) {
			setEnabled(true);

			if (s.getShieldsOn() == true) {
				setSelected(true);
			} else {
				setSelected(false);
			}
		} else {
			setEnabled(false);
		}
	}

	/**
	 * Invoked when the target of the listener has changed its state.
	 * 
	 * @param e
	 *            the incoming change event
	 * 
	 * @since 1.0
	 */

	public void stateChanged(ChangeEvent e) {
		// if the ship does not have a shield system, this action does matter
		Shields shields = (Shields) JavaTrek.game.gamedata.space.getPlayersShip().getSystem(Shields.class.getName());
		if (shields != null) {
			// toggle the shields
			shields.setShieldsOn(isSelected());

			// update the display
			JavaTrek.game.refresh();
		}
	}

}
