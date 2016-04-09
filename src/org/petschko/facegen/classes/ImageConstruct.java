package org.petschko.facegen.classes;

import com.sun.istack.internal.Nullable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015
 */
public class ImageConstruct {
	private static Graphics faceSet;
	private static BufferedImage bgImg;
	private static ArrayList<BufferedImage> faceGraphic = new ArrayList<BufferedImage>();

	/**
	 *
	 * @param obj array list of layers to refactor the image
	 */
	public static void refactorImg(ArrayList<LayerImg> obj) {
		// Draw all Layers new
		for(LayerImg layer : obj) {
			layer.drawLayer();
		}
	}

	/**
	 *
	 * @return faceSet Graphic
	 */
	private static Graphics getFaceSet() {
		return faceSet;
	}

	/**
	 *
	 * @param faceSet faceSet Graphic
	 */
	private static void setFaceSet(Graphics faceSet) {
		ImageConstruct.faceSet = faceSet;
	}

	/**
	 *
	 * @param index the index number for the Graphic you want
	 * @return the faceGraphic of your number
	 */
	private static BufferedImage getFaceGraphic(int index) {
		return faceGraphic.get(index);
	}

	/**
	 *
	 * @param faceGraphic a single faceGraphic
	 * @param index number 0 - 7 where you want to save the faceGraphic
	 */
	public static void setFaceGraphic(BufferedImage faceGraphic, int index) {
		ImageConstruct.faceGraphic.set(index, faceGraphic);
	}

	/**
	 *
	 * @param faceGraphic a blank image
	 */
	private static void setFaceGraphic(BufferedImage faceGraphic) {
		ImageConstruct.faceGraphic.add(faceGraphic);
	}

	/**
	 *
	 * @param index index number for the Graphic you want reset
	 */
	public static void eraseFaceGraphic(int index) {
		// It is not really deleted but set to default
		setFaceGraphic(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY), index);
	}

	/**
	 * calls drawFaceSet with 0, 0 size (default size)
	 */
	public static void drawFaceSet() {
		drawFaceSet(0, 0, true); // Use default faceGraphic size
	}

	/**
	 *
	 * @param faceHeight height of every faceGraphic
	 * @param faceWidth width of every faceGraphic
	 */
	public static void drawFaceSet(@Nullable int faceHeight, @Nullable int faceWidth, boolean gui) {
		if (faceHeight == 0)
			faceHeight = LayerImg.getBgImgHeight();
		if (faceWidth == 0)
			faceWidth = LayerImg.getBgImgWidth();

		// Set vars for the height/width of every picture
		final int imgHeight = faceHeight;
		final int imgWidth = faceWidth;

		// Set math vars
		int i = 0; // loop counter (gets pictures as well)
		int n = 0; // number of pic, begins with 0 because we draw on the left side on px 0
		int line = 0; // 0 because we draw picture on the top px 0
		int height;
		int width;

		// Reset Background before drawing
		setFaceSet(null);
		setBgImg(drawNewPicture(4 * imgWidth, 2 * imgHeight));
		setFaceSet(getBgImg().getGraphics());

		// draw GUI Background when it is showed on gui
		if (gui)
			getFaceSet().drawImage(FileOperations.loadBImg(Config.systemFolder + Config.ds + "backgroundFaceSet.png"), 0, 0, 4 * imgWidth, 2 * imgHeight, null);

		while (i < 8) {
			// calc picture positions
			height = imgHeight * line;
			width = imgWidth * n;

			// Draw Picture if one is set
			if (! (getFaceGraphic(i).getWidth() == 1 && getFaceGraphic(i).getHeight() == 1)) {
				try {
					getFaceSet().drawImage(getFaceGraphic(i), width, height, imgWidth, imgHeight, null);
				} catch (Exception e) {
					GUI.showFatalError(e);
				}
			}

			// new line after 4 pics
			if(n >= 3) {
				n = -1; // -1 because after the statement it will increased by 1 (equals = 0 on the next loop start)
				line++;
			}
			// classic^^ don't forget!
			n++;
			i++;
		}

		if (gui) {// save and refresh tmp file when its gui
			FileOperations.saveGraphic(getBgImg(), Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFaceSet.png");
			GUI.getFaceSetPicture().setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFaceSet.png", 384, 192));
		}
	}

	/**
	 * fills the faceSet image List with default Graphics
	 * @param reset use this function to reset
	 */
	public static void fillVars(boolean reset) {
		int i = 0;
		while(i < 8) {
			if (reset)
				setFaceGraphic(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY), i);
			else
				setFaceGraphic(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY));

			i++;
		}
	}

	/**
	 *
	 * @return image of the faceSet
	 */
	public static BufferedImage getBgImg() {
		return bgImg;
	}

	/**
	 *
	 * @param bgImg image of the faceSet
	 */
	private static void setBgImg(BufferedImage bgImg) {
		ImageConstruct.bgImg = bgImg;
	}

	/**
	 *
	 * @param x width of the new picture
	 * @param y height of the new picture
	 * @return new picture (transparency)
	 */
	protected static BufferedImage drawNewPicture(int x, int y) {
		if (x <= 0 || y <= 0)
			GUI.showFatalError(new Exception("drawNewPicture: x and y can't be <= 0!"));

		BufferedImage bi = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();

		Color transparency = new Color(0, 0, 0, 0);
		g.setColor(transparency);
		g.fillRect(0, 0, x, y);

		return bi;
	}

	/**
	 *
	 * @param obj array list of layers to refactor the image
	 */
	public static void drawGUIbg(ArrayList<LayerImg> obj) {
		LayerImg.resetBgImg();
		LayerImg.getFaceGraphic().drawImage(FileOperations.loadBImg(Config.systemFolder + Config.ds + "backgroundFace.png"), 0, 0, LayerImg.getBgImgWidth(), LayerImg.getBgImgHeight(), null);
		LayerImg.getFaceGraphic().drawImage(FileOperations.loadBImg(Config.resourceFolder + Config.ds + Config.defaultBGImg), 0, 0, LayerImg.getBgImgWidth(), LayerImg.getBgImgHeight(), null);
		ImageConstruct.refactorImg(obj);
		// save tmp file
		FileOperations.saveGraphic(LayerImg.getBgImg(), Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png");
		//set gui element up to date
		GUI.getFacePicture().setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png", 96, 96));
	}

	/**
	 *
	 * @param newWidth width of the new image
	 * @param newHeight height of the new image
	 * @param img the old picture
	 * @return new picture
	 */
	public static BufferedImage resizeImg(int newWidth, int newHeight, BufferedImage img) {
		BufferedImage newImage = drawNewPicture(newWidth, newHeight);
		Graphics g = newImage.getGraphics();

		g.drawImage(img, 0, 0, newWidth, newHeight, null);

		return newImage;
	}

	/**
	 *
	 * @param height is size height?
	 * @return height/width value
	 */
	public static int getSize(boolean height) {
		int size = 0;

		try {
			if (height)
				size = Integer.parseInt(UserPrefs.getProps().getProperty("defaultOutputImageHeight", "0"));
			else
				size = Integer.parseInt(UserPrefs.getProps().getProperty("defaultOutputImageWidth", "0"));
		} catch (Exception e) {
			GUI.showWarning(e);
		}

		if (size != 0)
			return size;

		if (height)
			return LayerImg.getBgImgHeight();
		else
			return LayerImg.getBgImgWidth();
	}

	/**
	 *
	 * @param file file that you want to have on a background
	 * @param tmpName name for the tmp file
	 * @return path to the tmp file
	 */
	public static String GUITransparentBgImg(String file, String tmpName) {
		BufferedImage bg = drawNewPicture(96, 96);
		Graphics g = bg.getGraphics();
		g.drawImage(FileOperations.loadBImg(Config.systemFolder + Config.ds + "backgroundFace.png"), 0, 0, 96, 96, null);
		g.drawImage(FileOperations.loadBImg(file), 0, 0, 96, 96, null);
		FileOperations.saveGraphic(bg, Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + tmpName + ".png");

		return Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + tmpName + ".png";
	}
}
