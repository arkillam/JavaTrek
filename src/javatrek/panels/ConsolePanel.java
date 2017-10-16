package javatrek.panels;

import java.awt.Color;
import java.awt.GridLayout;

import javatrek.JavaTrek;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import ca.thekillams.widgets.quotes.quotes_module_murphyslaws;
import ca.thekillams.widgets.quotes.quotes_module_murphysmilitarylaws;
import ca.thekillams.widgets.quotes.quotes_module_murphysmisclaws;
import ca.thekillams.widgets.quotes.quotes_module_murphystechnologylaws;
import ca.thekillams.widgets.quotes.quotes_server;

/**
 * The control panel provides a general-purpose method for communicating with the
 * player.
 * 
 * <UL>
 * <LI>Version 1.0 - 03/01/2002 - the original class (used a JTextArea object to display information)
 * <LI>Version 1.1 - 07/01/2003 - replaced the JTextArea class with a JTextPane class
 * <LI>Version 1.2 - 10/21/2004 - removed the command line text field, as the game will no longer use console input
 * <LI>Version 1.3 - 10/29/2004 - moved the quotes server inside of this class
 * <LI>Version 2.0 - 11/13/2004 - consolidated the various add message functions to a single function, set a constant font size, fixed the annoying bug that caused the text to not scroll to show a new message, and cleaned up the code
 * </UL>
 * 
 * @author	Andrew Killam
 * @version	Version 2.0 - 11/13/2004
 */

public class ConsolePanel extends JPanel
{
	
//////////////////////////////////////////////////////////////////////////////
//  private constants
//////////////////////////////////////////////////////////////////////////////

/** dictates the font size used in the display */
private static final int FONT_SIZE = 14;

/** dictates the space above each line of text */	
private static final int SPACE_ABOVE = 3;

/** dictates the space below each line of text */
private static final int SPACE_BELOW = 3;
	
//////////////////////////////////////////////////////////////////////////////
//  private fields
//////////////////////////////////////////////////////////////////////////////

/** lets the messages scroll */
private JScrollPane sp_messages;

/** displays the game messages */
private JTextPane tp_messages;

/** provides quotes for the console */
private quotes_server quotes;

/** the style context for the JTextPane's document */
private StyleContext context;

/** the JTextPane's document */
private StyledDocument document;

//////////////////////////////////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////////////////////////////////

/**		Creates a console panel.
 * 
 *		@since		1.0
 */

public ConsolePanel ()
{
	// create the quotes server
	quotes = new quotes_server ();
	quotes.add (quotes_module_murphyslaws.add (null));
	quotes.add (quotes_module_murphysmisclaws.add (null));
	quotes.add (quotes_module_murphysmilitarylaws.add (null));
	quotes.add (quotes_module_murphystechnologylaws.add (null));

	// set the border layout
	setLayout (new GridLayout (1, 1));
	
	// create the style context and the styled document
	context = new StyleContext ();
	document = new DefaultStyledDocument (context);
	tp_messages = new JTextPane (document);
	tp_messages.setEditable (false);
	sp_messages = new JScrollPane (tp_messages);
	add (sp_messages);
	
	// create the styles used to display text
	createStyle ("BlackLeft", Color.black, StyleConstants.ALIGN_LEFT);
	createStyle ("BlueLeft", Color.blue, StyleConstants.ALIGN_LEFT);
	createStyle ("GreenLeft", Color.green, StyleConstants.ALIGN_LEFT);
	createStyle ("RedLeft", Color.red, StyleConstants.ALIGN_LEFT);
	
	// display the vanity bit of data (title and author)
	addMessage ("BlueLeft", JavaTrek.GAME_TITLE);
	addMessage ("BlackLeft", "\nwritten by Andrew Killam (outlander78@hotmail.com)\n\n");
	
	// display a fun quote
	displayQuote ();
}

//////////////////////////////////////////////////////////////////////////////
//  functions
//////////////////////////////////////////////////////////////////////////////

/**		Adds a message to the message display.
 * 
 * 		@param		style		the name of the
 *		@param		message		the message string to be displayed
 * 
 *		@since		1.0
 */

public void addMessage (String style, String message)
{
	// add the message
	try
	{
		document.insertString (document.getLength (), message, context.getStyle (style));
	}
	catch (BadLocationException ble)
	{
		ble.printStackTrace ();
	}
	
	// scroll to the bottom
	tp_messages.setCaretPosition (document.getLength ());
}

/**		Clears the message display.
 * 
 *		@since		1.0
 */

public void clearMessages ()
{
	try
	{
		document.remove (0, document.getLength ());
	}
	catch (BadLocationException ble)
	{
		ble.printStackTrace ();
	}		
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
	//StyleConstants.setFontFamily (style, "Courier");
	StyleConstants.setFontSize (style, FONT_SIZE);
	
	// set the spacing
	StyleConstants.setSpaceAbove (style, SPACE_ABOVE);
	StyleConstants.setSpaceBelow (style, SPACE_BELOW);
	
	// set the colour
	StyleConstants.setForeground (style, colour);	
	StyleConstants.setBackground (style, Color.WHITE);	
}

/**  	Displays a quote.
 * 
 * 		@since		1.0
 */

public void displayQuote ()
{
	addMessage ("BlackLeft", quotes.getQuote () + "\n\n");	
}

}