package javatrek.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;
import javatrek.ImageManager.ImagesEnum;
import javatrek.panels.AssignPilotPointsPanel;
import javatrek.panels.JavaTrekPanel;

import javax.swing.JButton;

/**
 * <P>
 * Provides a control to open the pilot improvement panel when unused pilot skill points are available
 * 
 * <UL>
 * <LI>Version 2.0 - 11/26/2004 - the original instance
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 11/26/2004
 */

public class PilotImprovementButton extends JButton implements ActionListener, JavaTrekPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the button.
	 * 
	 * @since 2.0
	 */

	public PilotImprovementButton() {
		super(JavaTrek.imgMgr.loadImageIcon(ImagesEnum.TIP_OF_THE_DAY));

		addActionListener(this);

		setToolTipText("opens the pilot improvement panel");
	}

	/**
	 * Implements the action listener function/interface.
	 * 
	 * @param e
	 *            The incoming event.
	 * 
	 * @since 2.0
	 */

	public void actionPerformed(ActionEvent e) {
		JavaTrek.game.showPanel(AssignPilotPointsPanel.class.getName());
	}

	/**
	 * Refreshes the button.
	 * 
	 * @since 2.0
	 */

	public void refresh() {
		if (JavaTrek.game.gamedata.player.getUnassigend() > 0) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}

}
