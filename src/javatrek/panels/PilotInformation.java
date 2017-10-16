package javatrek.panels;

import java.awt.Color;
import java.awt.GridLayout;

import javatrek.JavaTrek;
import javatrek.Pilot;

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
 * This panel displays information about the player's pilot.
 * 
 * <UL>
 * <LI>Version 2.0 - 11/12/2004 - the original instance
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	2.0 - 11/12/2004
 */

public class PilotInformation extends JPanel implements JavaTrekPanel
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

public PilotInformation ()
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
		
		// get the player's pilot object
		Pilot pilot = JavaTrek.game.gamedata.player;
		
		// experience section (level, xp, next level at)
		document.insertString (document.getLength (), "Experience\n", context.getStyle ("BlackLeft"));
		String level = String.valueOf (pilot.getLevel ())  + "\n";
		document.insertString (document.getLength (), addDots ("Level", level, DOT_WIDTH), context.getStyle ("BlueLeft"));
		String xp = String.valueOf (pilot.getXP ())  + "\n";
		document.insertString (document.getLength (), addDots ("Experience", xp, DOT_WIDTH), context.getStyle ("BlueLeft"));
		String req = String.valueOf (Pilot.getExperienceRequired (pilot.getLevel () + 1))  + "\n";
		document.insertString (document.getLength (), addDots ("Next Level At", req, DOT_WIDTH), context.getStyle ("BlueLeft"));
		document.insertString (document.getLength (), "\n", context.getStyle ("BlackLeft"));
		
		// skills section
		document.insertString (document.getLength (), "Skills\n", context.getStyle ("BlackLeft"));
		for (int i = 0; i < Pilot.HOWMANY_SKILLS; i++)
		{
			String skill = Pilot.getSkillName (i);
			String points = String.valueOf (pilot.getSkill (i)) + "%\n";
			document.insertString (document.getLength (), addDots (skill, points, DOT_WIDTH), context.getStyle ("BlueLeft"));
		}
		int unassigned = pilot.getUnassigend ();
		if (unassigned > 0)
		{
			document.insertString (document.getLength (), addDots ("Unassigned Points", String.valueOf (unassigned) + "\n", DOT_WIDTH), context.getStyle ("RedLeft"));
		}
		document.insertString (document.getLength (), "\n", context.getStyle ("BlackLeft"));						
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