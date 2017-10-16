package javatrek.panels;

/**
 * <P>This panel displays the contents of a region.
 * 
 * <UL>
 * <LI>Version 1.0 - 07/04/2003 - the original class, uses a two-dimensional array of buttons
 * <LI>Version 1.1 - 07/12/2003 - changed to extend the new panel_map_base class and a proper graphical display
 * <LI>Version 1.2 - 10/18/2004 - updated drawEnergyWeaponShot () to no longer return a value (was not checked)
 * <LI>Version 2.0 - 10/29/2004 - implements JavaTrekPanel interface, removed x/y axis numbering
 * </UL>
 * 
 * @author	Andrew Killam
 * @version 2.0 - 10/29/2004
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import javatrek.JavaTrek;
import javatrek.Space;
import javatrek.controls.RegionMapActionPopupMenu;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.SpaceObject;
import javatrek.systems.Shields;

public class RegionMap extends JPanel implements JavaTrekPanel, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	/** the buffer for double-buffered graphics */
	private BufferedImage buffer = null;

	/** used for large characters such as the grid data */
	private Font font;

	/** the graphics object for the buffer */
	private Graphics2D g2d = null;

	/** the mouse's "regional" location */
	private Point mouse;

	/**
	 * Creates a region map panel.
	 * 
	 * @since 1.0
	 */

	public RegionMap() {
		super();
		setOpaque(true);

		// set the minimum size
		Dimension d = new Dimension(Space.REGION_WIDTH * 25, Space.REGION_HEIGHT * 25);
		setMinimumSize(d);
		setPreferredSize(d);

		// create the font
		font = new Font("SansSerif", Font.BOLD, 14);

		// initially, the mouse is not inside the square (or at least we
		// pretend it is not)
		mouse = new Point(-1, -1);

		// add a mouse listener
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Calculates the mouse's regional location from it's pixel location.
	 * 
	 * @param p
	 *            the mouse-location point
	 * @param e
	 *            the mouse event to work with
	 * 
	 * @return a point giving the mouse's regional location
	 * 
	 * @since 2.0
	 */

	private Point calculateMouseLocation(Point p, MouseEvent e) {
		// input checking
		if (p == null)
			p = new Point(-1, -1);
		if (e == null) {
			p.x = -1;
			p.y = -1;
		} else {
			// get the position at which the mouse was clicked
			int mx = e.getX();
			int my = e.getY();

			// get the panel's size
			int width = getWidth();
			int height = getHeight();

			// calculate the width and height of a square
			int w = width / Space.REGION_WIDTH;
			int h = height / Space.REGION_HEIGHT;

			// modify mx and my to ignore the wasted black space at the top and
			// left sides
			mx -= width - (w * Space.REGION_WIDTH);
			my -= height - (h * Space.REGION_HEIGHT);

			// determine which section was clicked
			p.x = (int) ((float) (mx - Math.abs(mx % w)) / (float) w);
			p.y = (int) ((float) (my - Math.abs(my % h)) / (float) h);
		}

		// return the result
		return p;
	}

	/**
	 * Invoked when the mouse button has been clicked (pressed and released) on a component.
	 * 
	 * @param e
	 *            the incoming event
	 * 
	 * @since 1.0
	 */

	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Invokes when the mouse is dragged within the panel.
	 * 
	 * @paramee e the incoming event
	 * 
	 * @since 2.0
	 */

	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse enters a component.
	 * 
	 * @param e
	 *            the incoming event
	 * 
	 * @since 1.0
	 */

	public void mouseEntered(MouseEvent e) {
		mouse = calculateMouseLocation(mouse, e);
	}

	/**
	 * Invoked when the mouse exits a component.
	 * 
	 * @param e
	 *            the incoming event
	 * 
	 * @since 1.0
	 */

	public void mouseExited(MouseEvent e) {
		mouse.x = -1;
		mouse.y = -1;
		repaint();
	}

	/**
	 * Invokes when the mouse is moved within the panel.
	 * 
	 * @paramee e the incoming event
	 * 
	 * @since 2.0
	 */

	public void mouseMoved(MouseEvent e) {
		mouse = calculateMouseLocation(mouse, e);
		repaint();
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 * 
	 * @param e
	 *            the incoming event
	 * 
	 * @since 1.0
	 */

	public void mousePressed(MouseEvent e) {
		// get the mouse's "regional" location
		mouse = calculateMouseLocation(mouse, e);

		// left-clicks handle movement, while right-clicks handle scans
		if (e.getButton() == MouseEvent.BUTTON1) {
			// attempt to move the ship
			JavaTrek.game.gamedata.space.getPlayersShip().localMove(mouse.x, mouse.y);
		}
		// check for both buttons 2 and 3, as the right button appears to sometimes
		// use each of these modifiers
		else if ((e.getButton() == MouseEvent.BUTTON2) || (e.getButton() == MouseEvent.BUTTON3)) {
			// get a handle to the player's ship
			Ship ship = JavaTrek.game.gamedata.space.getPlayersShip();

			// get a handle to the space object
			SpaceObject target = JavaTrek.game.gamedata.space.getSpaceObject(ship.getQuadrant(),
					ship.getQuadrantLocation(), new Point(mouse.x, mouse.y));

			// check to see if a ship has been clicked on
			if (target != null) {
				// call the right-click function to display a menu with options
				rightClicked(e.getX(), e.getY(), target);
			}
		}

		// update the game's display
		JavaTrek.game.refresh();
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 * 
	 * @param e
	 *            the incoming event
	 * 
	 * @since 1.0
	 */

	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Overrides the JPanel's paint function.
	 * 
	 * @param g
	 *            the panel's associated graphics object
	 * 
	 * @since 1.0
	 */

	public void paint(Graphics g) {
		// if the java game handle has not yet been set (only during game startup),
		// do not draw anything
		if (JavaTrek.game == null)
			return;

		// get required information
		Ship ship = JavaTrek.game.gamedata.space.getPlayersShip();
		int quad = ship.getQuadrant();
		Point qloc = ship.getQuadrantLocation();

		// get the panel's size
		int width = getWidth();
		int height = getHeight();

		// calculate the width and height of a square
		int w = width / Space.REGION_WIDTH;
		int h = height / Space.REGION_HEIGHT;

		// if necessary, re-create the graphics images
		if ((buffer == null) || ((buffer.getWidth() != width) || (buffer.getHeight() != height))) {
			// create the image buffer and it's Graphics2D instance
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			g2d = (Graphics2D) buffer.getGraphics();
			g2d.setFont(font);

			// calculate the offsets to push the dispaly to the lower right
			// (all wasted space is upper left)
			int hor = width - (w * Space.REGION_WIDTH);
			int ver = height - (h * Space.REGION_HEIGHT);
			g2d.translate(hor, ver);

			// suggest:
			// anti-aliasing, text anti-aliasing, rendering and colour rendering all be
			// performed for optimal quality (as opposed to speed)
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}

		// modify width and height to be the width and height of the utilized area
		width = w * Space.REGION_WIDTH;
		height = h * Space.REGION_HEIGHT;

		// black background
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);

		// get and display any space objects in the region
		SpaceObject obj[] = JavaTrek.game.gamedata.space.getInRegion(quad, qloc, 0, null);
		if (obj != null) {
			int howmany = obj.length;
			for (int i = 0; i < howmany; i++) {
				Point p = obj[i].getRegionLocation();
				Image objimg = JavaTrek.imgMgr.loadImage(obj[i].getImageName());

				int centre_x = (w - objimg.getWidth(null)) / 2;
				int centre_y = (h - objimg.getHeight(null)) / 2;
				if (centre_y < 0)
					centre_y = 0;

				g2d.drawImage(objimg, (p.x * w) + centre_x, (p.y * h) + centre_y, null);

				// draw a yellow elipse around ships if they have their shields up
				if (obj[i].getClass().getName() == Ship.class.getName()) {
					Ship s = (Ship) obj[i];
					Shields shields = (Shields) s.getSystem(Shields.class.getName());
					if (shields.getShieldsOn() == true) {
						g2d.setColor(Color.YELLOW);
						g2d.drawArc((p.x * w) + 2, (p.y * h) + 2, w - 4, h - 4, 0, 360);
					}
				}
			}
		}

		// draw grid lines
		g2d.setColor(getBackground());
		g2d.drawRect(0, 0, width - 1, height - 1);
		for (int i = 1; i < Space.REGION_HEIGHT; i++) {
			// horizontal lines
			int y = i * h;
			g2d.drawLine(0, y, width, y);
		}
		for (int i = 1; i < Space.REGION_WIDTH; i++) {
			// vertical lines
			int x = (i * w);
			g2d.drawLine(x, 0, x, height);
		}

		// draw the mouse's "regional" location
		/*
		 * if ((mouse.x > -1) && (mouse.y > -1)) { FontMetrics fm = g2d.getFontMetrics ();
		 * 
		 * String mouse_loc = "(" + mouse.x + "," + mouse.y + ")";
		 * 
		 * g2d.setColor (Color.WHITE);
		 * 
		 * g2d.drawString (mouse_loc, width - fm.stringWidth (mouse_loc) - 2, height - 4); }
		 */

		// draw the buffered image to the screen
		// black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(buffer, 0, 0, null);
	}

	/**
	 * Requests a repainting of the panel.
	 * 
	 * @since 2.0
	 */

	public void refresh() {
		repaint();
	}

	/**
	 * Displays a pop-up window if the mouse was right-clicked on an object. The menu contains actions the user can
	 * perform on the object.
	 * 
	 * @param the
	 *            mouse's x-location
	 * @param the
	 *            mouse's y-location
	 * @param the
	 *            space object in question
	 * 
	 * @since 2.0
	 */

	private void rightClicked(int x, int y, SpaceObject obj) {
		// RegionMapActionPopupMenu menu = new RegionMapActionPopupMenu (this, x, y, obj);
		new RegionMapActionPopupMenu(this, x, y, obj);
	}

	/**
	 * Overrides the JPanel's update function.
	 * 
	 * @param g
	 *            the panel's associated graphics object
	 * 
	 * @since 1.0
	 */

	public void update(Graphics g) {
		paint(g);
	}

}
