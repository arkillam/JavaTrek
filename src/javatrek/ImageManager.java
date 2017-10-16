package javatrek;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * <UL>
 * <LI>Version 3.0 - 09/16/2017 - modernizing the code
 * </UL>
 * 
 * @author Andrew Killam
 * @version 3.0 - 09/16/2017
 */

public class ImageManager {

	// TODO: add buffering

	public static enum ImagesEnum {

		HELP("/images/sun_icons/Help24.gif"),
		IMPORT("/images/sun_icons/Import24.gif"),
		REDO("/images/sun_icons/Redo24.gif"),
		REFRESH("/images/sun_icons/Refresh24.gif"),
		SAVE("/images/sun_icons/Save24.gif"),
		SAVE_ALL("/images/sun_icons/SaveAll24.gif"),
		SHIELDS("/images/items/shields.gif"),
		STEP_BACK_16("/images/sun_icons/StepBack16.gif"),
		STEP_BACK_24("/images/sun_icons/StepBack24.gif"),
		STEP_FORWARD("/images/sun_icons/StepForward24.gif"),
		SUN_ICON_BACK_24("/images/sun_icons/Back24.gif"),
		SUN_ICON_EXPORT_24("/images/sun_icons/Export24.gif"),
		SUN_ICON_FORWARD_24("/images/sun_icons/Forward24.gif"),
		SUN_ICON_WEB_COMPONENT("/images/sun_icons/WebComponent24.gif"),
		TIP_OF_THE_DAY("/images/sun_icons/TipOfTheDay24.gif");

		private final String filename;

		private ImagesEnum(String filename) {
			this.filename = filename;
		}

		public String getFilename() {
			return filename;
		}
	}

	/**
	 * @param name
	 * 
	 * @return
	 * 
	 * @since 3.0
	 */
	public Image loadImage(String name) {
		try {
			URL url = this.getClass().getResource(name);
			if (url == null) {
				System.out.println(String.format("Image '%s' not found", name));
				throw new Exception();
			}
			BufferedImage image = ImageIO.read(url);
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	/**
	 * @param image
	 *            image enum
	 * 
	 * @return the request image icon
	 * 
	 * @since 3.0
	 */
	public ImageIcon loadImageIcon(ImagesEnum image) {
		try {
			URL url = getClass().getResource(image.getFilename());
			if (url == null) {
				System.out.println(String.format("Image '%s' not found", image.getFilename()));
				throw new Exception();
			}
			return new ImageIcon(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param filename
	 *            image name
	 * 
	 * @return the request image icon
	 * 
	 * @since 3.0
	 */
	public ImageIcon loadImageIcon(String filename) {
		try {
			return new ImageIcon(getClass().getResource(filename));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
