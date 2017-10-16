package javatrek.controls;

import java.awt.GridLayout;

import javatrek.ImageManager.ImagesEnum;
import javatrek.JavaTrek;
import javatrek.handlers.MaxEnergy;
import javatrek.handlers.MaxShields;
import javatrek.panels.JavaTrekPanel;
import javatrek.spaceobjects.Ship;
import javatrek.systems.Shields;

import javax.swing.JButton;
import javax.swing.JPanel;

import ca.thekillams.widgets.panels.panel_dial;

/**
 * Controls energy transfers between main energy and the player's ship's shields.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/31/2004 - the original class
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/31/2004
 */

public class EnergyTransferButton extends JPanel implements JavaTrekPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  public constants
//////////////////////////////////////////////////////////////////////////////

/** main energy transfer panel id */
public static final int MAIN_ENERGY = 1000;

/** shield energy transfer panel id */
public static final int SHIELD_ENERGY = 1001;
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** the type of energy to handle */
private int energy_type;

/** triggers the transfer */
private JButton b_work;
	
/** displays how much energy remains */
private panel_dial pd_energy;

/** used to form tool tip texts */
private StringBuffer buffer = new StringBuffer (100);
	
//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates the button.
 * 
 * 		@param		t		the type of energy to handle
 * 
 * 		@since		2.0
 */

public EnergyTransferButton (int t)
{
	super (new GridLayout (1, 2));	
	
	// bounds checking
	if (t == MAIN_ENERGY)
	{
		energy_type = MAIN_ENERGY;
	}
	else if (t == SHIELD_ENERGY)
	{
		energy_type = SHIELD_ENERGY;
	}
	else
	{
		System.out.println ("Invalid EnergyTransferButton type:  " + t);
		Exception e = new Exception ();
		e.printStackTrace ();
		System.exit (1);
	}
	
	pd_energy = new panel_dial ("E", 1.00f);
	add (pd_energy);
	
	b_work = new JButton (JavaTrek.imgMgr.loadImageIcon(ImagesEnum.STEP_BACK_16));
	b_work.setBorder (null);
	b_work.setToolTipText ("");
	add (b_work);
	if (energy_type == MAIN_ENERGY)
	{
		b_work.addActionListener (new MaxEnergy ());
	}
	else
	{
		b_work.addActionListener (new MaxShields ());
	}
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Updates the display according to the game's values.
 * 
 * 		@since		2.0
 */

public void refresh ()
{
	// retrieve the player's ship
	Ship ship = JavaTrek.game.gamedata.space.getPlayersShip ();
	
	if (energy_type == MAIN_ENERGY)
	{
		// display the amount of energy remaining
		int remaining = ship.getEnergyRemaining ();
		int max = ship.getEnergyMax ();
		pd_energy.setPercentage ((float) remaining / (float) max);

		// set the button's tool tip text
		buffer.delete (0, buffer.length ());
		buffer.append (" Energy (");
		buffer.append (remaining);
		buffer.append (" / ");
		buffer.append (max);
		buffer.append (") ");
		b_work.setToolTipText (buffer.toString ());
	}
	else
	{
		Shields shields = (Shields) ship.getSystem (Shields.class.getName ());
		if (shields != null)
		{
			// check that the button is enabled
			b_work.setEnabled (true);
			
			// display the amount of energy remaining
			int remaining = shields.getRemaining ();
			int max = shields.getCapacity ();
			pd_energy.setPercentage ((float) remaining / (float) max);
			
			// set the button's tool tip text
			buffer.delete (0, buffer.length ());
			buffer.append (" Shields (");
			buffer.append (remaining);
			buffer.append (" / ");
			buffer.append (max);
			buffer.append (") ");
			b_work.setToolTipText (buffer.toString ());		
		}
		else
		{
			// zero the display and disable the button
			pd_energy.setPercentage (0.0f);
			b_work.setEnabled (false);
			b_work.setToolTipText ("shields not installed");
		}
	}
}

}
