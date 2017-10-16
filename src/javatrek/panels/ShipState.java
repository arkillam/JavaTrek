package javatrek.panels;

import java.awt.Color;
import java.awt.GridLayout;

import javatrek.JavaTrek;
import javatrek.spaceobjects.Ship;
import javatrek.systems.MachineSystem;
import javatrek.systems.Shields;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * This panel displays the status of the player's ship's systems.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/01/2002 - the original class
 * <LI>Version 1.1 - 04/18/2003 - changed the label/progress bar/label display to a display using the panel_status class
 * <LI>Version 1.2 - 11/14/2003 - changed to a gridbag layout, optional level of detail and choice of vertical or horizontal layouts
 * <LI>Version 2.0 - 11/11/2004 - changed from a layered panel display to a text display, and altered the code to work with the new machine systems scheme
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/11/2004
 */

public class ShipState extends JPanel implements JavaTrekPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** the length of a dotted string */
private static final int DOT_WIDTH = 44;

/** dictates the font size used in the display */
private static final int FONT_SIZE = 14;

/** dictates the space above each line of text */	
private static final int SPACE_ABOVE = 3;

/** dictates the space below each line of text */
private static final int SPACE_BELOW = 3;

//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** lets the display scroll */
private JScrollPane scrollpane;

/** displays the text */
private JTextPane textarea;

/** the style context for the JTextPane's document */
private StyleContext context;

/** the JTextPane's document */
private StyledDocument document;


//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////
	
/** 	Creates the ship state panel.
 * 
 *		@since		1.0
 */

public ShipState ()
{
	super (new GridLayout (1, 1));
	
	// create the text area and its scroll pane, and add them to the panel
	// create the text area, the style context and the styled document
	context = new StyleContext ();
	document = new DefaultStyledDocument (context);
	textarea = new JTextPane (document);
	textarea.setEditable (false);
	scrollpane = new JScrollPane (textarea);
	add (scrollpane);
	
	// create the styles used to display text
	createStyle ("BlackLeft", Color.black, StyleConstants.ALIGN_LEFT);
	createStyle ("BlueLeft", Color.blue, StyleConstants.ALIGN_LEFT);
	createStyle ("RedLeft", Color.red, StyleConstants.ALIGN_LEFT);
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Creates a dotted line with a string at either end.
 * 
 * 		@param		left		the left side of the string
 * 		@param		right		the right side of the string
 * 		@param		width		the width of the dotted string
 * 
 * 		@return		the dotted string
 * 
 * 		@since		2.0
 */

private String addDots (String left, String right, int width)
{
	StringBuffer buffer = new StringBuffer (width + 1);
	
	int dots = width - left.length () - right.length ();
	buffer.append (left);
	for (int j = 0; j < dots; j++)
	{
		buffer.append (".");
	}
	buffer.append (right);
	
	return buffer.toString ();
}

/**		Creates a style with a specified alignment and colour.
 * 
 * 		@param		name		the name for the new style
 * 		@param		colour		the colour for the font
 * 		@param		align		the StyleContext alignment constant for the font
 * 
 * 		@since		2.0
 */

private void createStyle (String name, Color colour, int align)
{
	Style style = context.addStyle (name, context.getStyle (StyleContext.DEFAULT_STYLE));
	
	// set the alignment
	StyleConstants.setAlignment (style, align);
	
	// use the constant font size
	StyleConstants.setFontFamily (style, "Courier");
	StyleConstants.setFontSize (style, FONT_SIZE);
	
	// set the spacing
	StyleConstants.setSpaceAbove (style, SPACE_ABOVE);
	StyleConstants.setSpaceBelow (style, SPACE_BELOW);
	
	// set the colour
	StyleConstants.setForeground (style, colour);	
	StyleConstants.setBackground (style, Color.WHITE);	
}

/**		Updates displayed components.
 * 
 * 		@since		1.0
 */

public void refresh ()
{
	// to be safe, make sure the handles exist
	if (JavaTrek.game == null) return;
	if (JavaTrek.game.gamedata == null) return;
	if (JavaTrek.game.gamedata.space == null) return;
	
	try
	{
		// clear the current display
		document.remove (0, document.getLength ());
		
		// get the player's ship
		Ship ship = JavaTrek.game.gamedata.space.getPlayersShip ();
		
		// beginning of the resource section
		document.insertString (document.getLength (), "Resources\n", context.getStyle ("BlackLeft"));
		
		// display ship's hit points
		int hp = ship.getHP ();
		int mhp = ship.getHPMax ();
		int p = 100 * hp / mhp;
		String left = "Hit Points (" + hp + " / " + mhp + (")");
		String right = String.valueOf (p) + "%\n";
		if (mhp > 80)
		{
			document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("BlueLeft"));
		}
		else if (mhp > 50)
		{
			document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("BlackLeft"));
		}
		else
		{
			document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("RedLeft"));
		}

		// display ship's main energy
		int e = ship.getEnergyRemaining ();
		int me = ship.getEnergyMax ();
		p = 100 * e / me;
		left = "Main Energy (" + e + " / " + me + (")");
		right = String.valueOf (p) + "%\n";
		if (mhp > 80)
		{
			document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("BlueLeft"));
		}
		else if (mhp > 50)
		{
			document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("BlackLeft"));
		}
		else
		{
			document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("RedLeft"));
		}

		// display ship's shield energy
		Shields shields = (Shields) ship.getSystem (Shields.class.getName ());
		if (shields != null)
		{
			e = shields.getRemaining ();
			me = shields.getCapacity ();
			p = 100 * e / me;
			left = "Shield Energy (" + e + " / " + me + (")");
			right = String.valueOf (p) + "%\n";
			if (mhp > 80)
			{
				document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("BlueLeft"));
			}
			else if (mhp > 50)
			{
				document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("BlackLeft"));
			}
			else
			{
				document.insertString (document.getLength (), addDots (left, right, DOT_WIDTH), context.getStyle ("RedLeft"));
			}
		}
		
		// display the ship's repair points
		//document.insertString (document.getLength (), addDots ("Repair Points (per hour)", String.valueOf (ship.getRepairPoints ()), DOT_WIDTH), context.getStyle ("BlueLeft"));
		
		// end of the resource section
		document.insertString (document.getLength (), "\n", context.getStyle ("BlackLeft"));
				
		// get an array of the player's ship's systems
		if (ship != null)
		{
			MachineSystem sys[] = ship.getSystems ();
			
			if (sys != null)
			{
				// section title
				document.insertString (document.getLength (), "Systems\n", context.getStyle ("BlackLeft"));
				
				// sort alphabetically
				for (int i = 0; i < (sys.length - 1); i++)
				{
					for (int j = i + 1; j < sys.length; j++)
					{
						if (sys[i].getName ().compareTo (sys[j].getName ()) > 0)
						{
							MachineSystem s = sys[i];
							sys[i] = sys[j];
							sys[j] = s;
						}
					}
				}

				for (int i = 0; i < sys.length; i++)
				{
					String name = sys[i].getName ();
					int r = (int)(sys[i].getRepair () * 100);
					String repair = String.valueOf (r) + "%\n";
					String result = addDots (name, repair, DOT_WIDTH);
										
					if (r > 80)
					{
						document.insertString (document.getLength (), result, context.getStyle ("BlueLeft"));
					}
					else if (r > 50)
					{
						document.insertString (document.getLength (), result, context.getStyle ("BlackLeft"));
					}
					else
					{
						document.insertString (document.getLength (), result, context.getStyle ("RedLeft"));
					}
				}
			}
		}		
	}
	catch (BadLocationException e)
	{
		e.printStackTrace ();
		System.exit (1);
	}	
	
	// scroll back to the top
	textarea.setCaretPosition (0);
}

}