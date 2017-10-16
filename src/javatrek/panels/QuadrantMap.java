package javatrek.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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
import javatrek.spaceobjects.Asteroid;
import javatrek.spaceobjects.Base;
import javatrek.spaceobjects.Ship;
import javatrek.spaceobjects.SpaceObject;
import javatrek.spaceobjects.Star;
import javatrek.systems.Computer;

/**
 * <P>
 * This panel displays the contents of the quadrant. The original version used an two-dimensional array of buttons,
 * while this one uses a proper graphical representation.
 * 
 * <UL>
 * <LI>Version 1.0 - 07/05/2003 - the original class
 * <LI>Version 1.1 - 07/12/2003 - changed to extend the new panel_map_base class
 * <LI>Version 2.0 - 10/29/2004 - implements JavaTrekPanel interface, removed x/y axis numbering
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 10/29/2004
 */

public class QuadrantMap extends JPanel implements JavaTrekPanel, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	/** the buffer for double-buffered graphics */
	private BufferedImage buffer = null;

	/** used for large characters such as the grid data */
	private Font font;

	/** the graphics object for the buffer */
	private Graphics2D g2d = null;

	/** tracks the mouse's quadrant location */
	private Point mouse;

	/** vertical offset for the font */
	private int voff;

	/**
	 * Creates a quadrant map panel
	 * 
	 * @since 1.0
	 */

	public QuadrantMap() {
		super();
		setOpaque(true);

		// set the minimum size
		Dimension d = new Dimension(Space.QUADRANT_WIDTH * 25, Space.QUADRANT_HEIGHT * 25);
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
			int w = width / Space.QUADRANT_WIDTH;
			int h = height / Space.QUADRANT_HEIGHT;

			// modify my to ignore the wasted black space at the top
			my -= height - (h * Space.QUADRANT_HEIGHT);

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
		mouse = calculateMouseLocation(mouse, e);

		// send the light drive move command
		Ship p = JavaTrek.game.gamedata.space.getPlayersShip();
		Point r = p.getRegionLocation();
		p.lightDriveMove(mouse, r);

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
		Image shipimg = JavaTrek.imgMgr.loadImage(ship.getImageName());
		int quad = ship.getQuadrant();
		Point qloc = ship.getQuadrantLocation();

		// get the panel's size
		int width = getWidth();
		int height = getHeight();

		// calculate the width and height of a square
		int w = width / Space.QUADRANT_WIDTH;
		int h = height / Space.QUADRANT_HEIGHT;

		// if necessary, re-create the graphics images
		if ((buffer == null) || ((buffer.getWidth() != getWidth()) || (buffer.getHeight() != getHeight()))) {
			// create the image buffer, it's Graphics2D instance and a larger font
			buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			g2d = (Graphics2D) buffer.getGraphics();
			g2d.setFont(font);

			// get the offset sizes for the large font
			FontMetrics fm = g2d.getFontMetrics();
			voff = fm.getHeight() + 1;

			// calculate the offsets to push the dispaly to the lower right
			// (all wasted space is upper left)
			int ver = height - (h * Space.QUADRANT_HEIGHT);
			g2d.translate(0, ver);

			// suggest:
			// anti-aliasing, text anti-aliasing, rendering and colour rendering all be
			// performed for optimal quality (as opposed to speed)
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}

		// modify width and height to be the width and height of the utilized area
		width = w * Space.QUADRANT_WIDTH;
		height = h * Space.QUADRANT_HEIGHT;

		// remove the cliping area for the backdrop draw stage
		g2d.setClip(null);

		// black background
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);

		// draw an image of the ship to indicate which region the ship is currently in
		int centre_x = (w - shipimg.getWidth(null)) / 2;
		int centre_y = (h - shipimg.getHeight(null)) / 2;
		if (centre_y < 0)
			centre_y = 0;
		g2d.drawImage(shipimg, (qloc.x * w) + centre_x, (qloc.y * h) + centre_y, null);

		// work through the player's ship's memory and display the data it contains
		Computer computer = (Computer) ship.getSystem(Computer.class.getName());
		if (computer != null) {
			String asteroid_classname = Asteroid.class.getName();
			String base_classname = Base.class.getName();
			String ship_classname = Ship.class.getName();
			String star_classname = Star.class.getName();
			Point point = new Point(0, 0);
			for (int i = 0; i < Space.QUADRANT_WIDTH; i++) {
				for (int j = 0; j < Space.QUADRANT_HEIGHT; j++) {
					if (computer.getKnown(i, j) == true) {
						// aim the point object at the current quadrant
						point.x = i;
						point.y = j;

						// displays a base image, if one exists in the region; in the case
						// of multiple bases, the first one found is displayed
						SpaceObject so[] = JavaTrek.game.gamedata.space.getInRegion(quad, point, 0, base_classname);
						if (so != null) {
							Image img_base = JavaTrek.imgMgr.loadImage(so[0].getImageName());
							centre_x = (w - img_base.getWidth(null)) / 2;
							centre_y = (h - img_base.getHeight(null)) / 2;
							if (centre_y < 0)
								centre_y = 0;
							g2d.drawImage(img_base, (i * w) + centre_x, (j * h) + centre_y, null);
						}

						// list the number of enemy ships in the region (if they are present)
						int enemies = 0;
						so = JavaTrek.game.gamedata.space.getInRegion(quad, point, ship.getTeam(), ship_classname);
						if (so != null)
							enemies = so.length;
						if (enemies > 0) {
							// use red to display the raiders
							g2d.setColor(Color.red);

							// get the number of raiders present as a string
							String nr = String.valueOf(enemies);

							// calculations for locating the string (left side of the segment)
							int s_h_loc = (j * h) + voff;
							int s_w_loc = (i * w) + 4;

							// display the string
							g2d.drawString(nr, s_w_loc, s_h_loc);
						}

						// list the number of stars and asteroids in the region (if they are present)
						// TODO: improve this to count non-machine space objects
						int objects = 0;
						so = JavaTrek.game.gamedata.space.getInRegion(quad, point, 0, star_classname);
						if (so != null)
							objects += so.length;
						so = JavaTrek.game.gamedata.space.getInRegion(quad, point, 0, asteroid_classname);
						if (so != null)
							objects += so.length;
						if (objects >= 0) {
							// use green to display the stars
							g2d.setColor(Color.green);

							// get the number of stars present as a string
							String ns = String.valueOf(objects);

							// calculations for locating the string (right side of the segment)
							int s_h_loc = (j * h) + voff;
							int s_width = (int) g2d.getFontMetrics().getStringBounds(ns, g2d).getWidth();
							int s_w_loc = ((i + 1) * w) - s_width - 2;

							// display the string
							g2d.drawString(ns, s_w_loc, s_h_loc);
						}
					}
					// nothing to display - the player knows nothing about this region
					else {
					}
				}
			}
		}

		// draw grid lines
		g2d.setColor(getBackground());
		g2d.drawRect(0, 0, width - 1, height - 1);
		for (int i = 1; i < Space.QUADRANT_HEIGHT; i++) {
			// horizontal lines
			int y = i * h;
			g2d.drawLine(0, y, width - 1, y);
		}
		for (int i = 1; i < Space.QUADRANT_WIDTH; i++) {
			// vertical lines
			int x = (i * w);
			g2d.drawLine(x, 0, x, height);
		}

		// outline the ship's grid location in red
		g2d.setColor(Color.red);
		g2d.drawRect((qloc.x * w) + 1, (qloc.y * h) + 1, (w - 2), (h - 2));
		g2d.drawRect((qloc.x * w) + 2, (qloc.y * h) + 2, (w - 4), (h - 4));

		// draw the mouse's quadrant location
		/*
		 * if ((mouse.x > -1) && (mouse.y > -1)) { String mouse_loc = "(" + mouse.x + "," + mouse.y + ")";
		 * 
		 * g2d.setColor (Color.WHITE);
		 * 
		 * g2d.drawString (mouse_loc, 2, height - 4); }
		 */

		// draw the buffered image to the screen
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
