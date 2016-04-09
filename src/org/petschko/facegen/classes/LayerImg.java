package org.petschko.facegen.classes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015
 */
public class LayerImg extends FileOperations {
	private static LayerImg instance;
	private static BufferedImage bgImg;
	private static Graphics faceGraphic;
	private static int bgImgHeight;
	private static int bgImgWidth;

	private BufferedImage layerImg;
	private String layerPath;
	private boolean visible;

	/**
	 *
	 * @param path path to the layer img
	 */
	public LayerImg(String path) {
		// Check if there is a background - if not create it
		if (instance == null)
			instance = new LayerImg();

		// set the layers picture
		this.setVisible(true);
		this.setLayerPath(path);
		this.setLayerImg(FileOperations.loadBImg(path));
	}

	/**
	 * constructor - set background graphic
	 */
	private LayerImg() {
		// Set Graphic Background
		setBgImg(FileOperations.loadBImg(Config.resourceFolder + Config.ds + Config.defaultBGImg));

		setFaceGraphic(getBgImg().getGraphics());
		// Set height and width - to check the other pictures
		setBgImgWidth(getBgImg().getWidth());
		setBgImgHeight(getBgImg().getHeight());
	}

	/**
	 *
	 * @return the base graphic
	 */
	public static BufferedImage getBgImg() {
		return bgImg;
	}

	/**
	 *
	 * @param bgImg the base graphic
	 */
	private static void setBgImg(BufferedImage bgImg) {
			LayerImg.bgImg = bgImg;
	}

	/**
	 *
	 * @return the face-graphic
	 */
	public static Graphics getFaceGraphic() {
		return faceGraphic;
	}

	/**
	 *
	 * @param faceGraphic the face-graphic
	 */
	private static void setFaceGraphic(Graphics faceGraphic) {
		LayerImg.faceGraphic = faceGraphic;
	}

	/**
	 *
	 * @return picture of the current layer
	 */
	public BufferedImage getLayerImg() {
		return layerImg;
	}

	/**
	 *
	 * @param layerImg picture of the current layer
	 */
	public void setLayerImg(BufferedImage layerImg) {
		this.layerImg = layerImg;
	}

	/**
	 *
	 * @return the layers visible (true/false)
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 *
	 * @param visible set the layer visible (true) or not (false)
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 *
	 * @return height of the background graphic
	 */
	public static int getBgImgHeight() {
		return bgImgHeight;
	}

	/**
	 *
	 * @param bgImgHeight height of the background graphic
	 */
	private static void setBgImgHeight(int bgImgHeight) {
		LayerImg.bgImgHeight = bgImgHeight;
	}

	/**
	 *
	 * @return width of the background graphic
	 */
	public static int getBgImgWidth() {
		return bgImgWidth;
	}

	/**
	 *
	 * @param bgImgWidth width of the background graphic
	 */
	private static void setBgImgWidth(int bgImgWidth) {
		LayerImg.bgImgWidth = bgImgWidth;
	}

	/**
	 * reset the bgImg instance
	 */
	public static void resetBgImg() {
		instance = new LayerImg();
	}

	/**
	 * Draw a layer on the background graphic
	 */
	public void drawLayer() {
		if (this.isVisible()) {
			getFaceGraphic().drawImage(this.getLayerImg(), 0, 0, getBgImgWidth(), getBgImgHeight(), null);
		}
	}

	/**
	 *
	 * @return path to the file of the current layer
	 */
	public String getLayerPath() {
		return layerPath;
	}

	/**
	 *
	 * @param layerPath path to the file of the current layer
	 */
	public void setLayerPath(String layerPath) {
		this.layerPath = layerPath;
	}
}
