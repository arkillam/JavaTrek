package javatrek.panels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javatrek.JavaTrek;
import javatrek.Pilot;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <P>Allows the player to allocate unused skill points.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/26/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/26/2004
 */

public class AssignPilotPointsPanel extends JPanel implements ActionListener, JavaTrekPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** the buttons to increase pilot skills by a point */
private JButton b_increase[];

/** the buttons to increase pilot skills by five points */
private JButton b_increase5[];

/** the buttons to increase pilot skills by ten points */
private JButton b_increase10[];

/** displays the number of pilot skill points available */
private JLabel l_points;

/** displays the assigned pilot skill points */
private JLabel l_ratings[];

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates the panel.
 * 
 * 		@since		2.0
 */

public AssignPilotPointsPanel ()
{
	super (new GridLayout (1 + Pilot.HOWMANY_SKILLS, 1));
	
	setBorder (BorderFactory.createTitledBorder ("Assign Pilot Skill Points"));
	
	// add the skill points row
	JPanel p_temp = new JPanel (new GridLayout (1, 2));
	p_temp.add (new JLabel ("Pilot Skill Points Available "));
	l_points = new JLabel ("000");
	p_temp.add (l_points);
	add (p_temp);
	
	// create and add the increase buttons and current level labels
	b_increase = new JButton [Pilot.HOWMANY_SKILLS];
	b_increase5 = new JButton [Pilot.HOWMANY_SKILLS];
	b_increase10 = new JButton [Pilot.HOWMANY_SKILLS];
	l_ratings = new JLabel [Pilot.HOWMANY_SKILLS];
	for (int i = 0; i < Pilot.HOWMANY_SKILLS; i++)
	{
		p_temp = new JPanel (new GridLayout (1, 2));
		
		JPanel p_temp2 = new JPanel (new GridLayout (1, 2));
		p_temp2.add (new JLabel (Pilot.getSkillName (i)));
		l_ratings[i] = new JLabel ("000%");
		p_temp2.add (l_ratings[i]);
		p_temp.add (p_temp2);
		
		b_increase[i] = new JButton ("+1");
		b_increase5[i] = new JButton ("+5");
		b_increase10[i] = new JButton ("+10");
		
		b_increase[i].setToolTipText ("adds one point to " + Pilot.getSkillName (i));
		b_increase5[i].setToolTipText ("adds five points to " + Pilot.getSkillName (i));
		b_increase10[i].setToolTipText ("adds ten points to " + Pilot.getSkillName (i));
		
		b_increase[i].addActionListener (this);
		b_increase5[i].addActionListener (this);
		b_increase10[i].addActionListener (this);
		
		JPanel p_temp3 = new JPanel (new GridLayout (1, 3));
		
		p_temp3.add (b_increase[i]);
		p_temp3.add (b_increase5[i]);
		p_temp3.add (b_increase10[i]);
		
		p_temp.add (p_temp3);
		
		add (p_temp);
	}
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Refreshes the display.
 * 
 * 		@since		2.0
 */

public void refresh ()
{
	// retrieve the number of unassigned points
	int points = JavaTrek.game.gamedata.player.getUnassigend ();
	
	// display the number of unassigned points
	l_points.setText (String.valueOf (points));
	
	// work through the various pilot skills
	for (int i = 0; i < Pilot.HOWMANY_SKILLS; i++)
	{
		// display the current levels
		l_ratings[i].setText (String.valueOf (JavaTrek.game.gamedata.player.getSkill (i) + "%"));
		
		// enable/disable buttons appropriately	
		if (points >= 10)
		{
			b_increase10[i].setEnabled (true);
		}
		else
		{
			b_increase10[i].setEnabled (false);
		}
		
		if (points >= 5)
		{
			b_increase5[i].setEnabled (true);
		}
		else
		{
			b_increase5[i].setEnabled (false);
		}
		
		if (points >= 1)
		{
			b_increase[i].setEnabled (true);
		}
		else
		{
			b_increase[i].setEnabled (false);
		}
	}
}

//////////////////////////////////////////////////////////////////////////////
//  action handler
//////////////////////////////////////////////////////////////////////////////

/** 	Implements the action listener function/interface.
 * 
 * 		@param		e		The incoming event.
 * 
 * 		@since		2.0
 */

public void actionPerformed (ActionEvent e)
{
	// get the source once
	Object o = e.getSource ();
	
	// check to see which button was pressed
	for (int i = 0; i < Pilot.HOWMANY_SKILLS; i++)
	{
		if (o == b_increase[i])
		{
			// assign points
			JavaTrek.game.gamedata.player.assignPoints (i, 1);
			
			// refresh the various displays
			JavaTrek.game.refresh ();
			
			// work complete
			return;
		}
		else if (o == b_increase5[i])
		{
			// assign points
			JavaTrek.game.gamedata.player.assignPoints (i, 5);

			// refresh the various displays
			JavaTrek.game.refresh ();
			
			// work complete
			return;
		}
		else if (o == b_increase10[i])
		{
			// assign points
			JavaTrek.game.gamedata.player.assignPoints (i, 10);

			// refresh the various displays
			JavaTrek.game.refresh ();
			
			// work complete
			return;
		}
	}	
}

}
