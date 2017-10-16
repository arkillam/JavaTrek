package javatrek.panels;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * This is the commonly-used 2x2 grid panel.
 * 
 * <UL>
 * <LI>Version 2.0 - 10/30/2004 - the original class
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 10/30/2004
 */

public class MainScreen extends JPanel implements JavaTrekPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the number of JavaTrekPanels the panel holds */
private static final int PANELS = 4;
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** holds the panels so that they can be refreshed */
private JavaTrekPanel panels[];
	
//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates the main screen panel.
 * 
 * 		@param		cp		the console panel to display
 * 
 * 		@since		2.0
 */

public MainScreen (ConsolePanel cp)
{
	super (new GridLayout (2, 2));
	
	// create the panels array
	panels = new JavaTrekPanel[PANELS];
	int count = 0;
	
	// upper left gets the quadrant map
	QuadrantMap qm = new QuadrantMap ();
	panels[count++] = qm;
	add (new JScrollPane (qm));

	// upper right gets the region map
	RegionMap rm = new RegionMap ();
	panels[count++] = rm;
	add (new JScrollPane (rm));
	
	// lower left gets a tabbed pane
	JTabbedPane tp = new JTabbedPane ();
	ShipState ssp = new ShipState ();
	panels[count++] = ssp;
	tp.add ("Ship", ssp);
	PilotInformation pi = new PilotInformation ();
	panels[count++] = pi;
	tp.add ("Pilot", pi);
	add (tp);
	
	// lower right gets the console
	add (cp);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Refreshes the panel's display, and calls refresh for contained panels.
 * 
 * 		@since		2.0
 */

public void refresh()
{
	for (int i = 0; i < PANELS; i++)
	{
		panels[i].refresh ();
	}
}

}
