package in.eona.petschko.facegen.classes;

import com.sun.istack.internal.Nullable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.Reader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015
 */
public class FileOperations {

	/**
	 *
	 * @param img graphic, that you want to save
	 * @param filePath filePath where the file will saved
	 * @return true/false is file saved
	 */
	public static boolean saveGraphic(BufferedImage img, String filePath) {
		File file = new File(filePath);
		try {
			ImageIO.write(img, "PNG", file);
		} catch (Exception e) {
			GUI.showFatalError(e);
		}

		// Is file saved?
		return file.exists();
	}

	/**
	 * this method check if there are all necessary files and directories if not it try to create them
	 */
	public static void checkFileSystem() {
		// Check folders
		checkDir(Config.resourceFolder);
		checkDir(Config.systemFolder);
		checkDir(Config.defaultSaveFolder);
		checkDir(Config.systemFolder + Config.ds + Config.tmpFolder);

		// Check files
		if (! checkFile(Config.systemFolder + Config.ds + Config.defaultConfigFile, false)) {
			saveProperties(UserPrefs.defaultProps(), Config.defaultConfigFile);
			GUI.addStartupInfo("Default Config-File doesn't exists, so it was created.");
		}
		if (! checkFile(Config.systemFolder + Config.ds + Config.defaultUserConfigFile, false)) {
			saveProperties(loadProperties(Config.defaultConfigFile), Config.defaultUserConfigFile);
			GUI.addStartupInfo("User Config-File doesn't exists, so it was created.");
		}
		if (! checkFile(Config.resourceFolder + Config.ds + Config.defaultBGImg, false)) {
			int answer = GUI.yesNoPopupQuestion("Can't find default Background-Image in \"" + Config.resourceFolder + Config.ds + Config.defaultBGImg + "\"...\n" +
					"Please add this file, its a base-file, the most Makers need 96x96px Face-Pictures!\n" +
					"\n" +
					"Should I create this file for you? (96x96px transparent)?", "Missing File", JOptionPane.WARNING_MESSAGE, true);
			if (answer == JOptionPane.YES_OPTION) {
				if(saveGraphic(ImageConstruct.drawNewPicture(96, 96), Config.resourceFolder + Config.ds + Config.defaultBGImg))
					GUI.addStartupInfo("Default Face-Background-Graphic created.");
				else
					GUI.showError("Can't create Face-Background-Graphic", true);
			} else
				GUI.showError("Can't start Program without Face-Background-Graphic! Exiting...", true);
		}
		if (! checkFile(Config.systemFolder + Config.ds + Config.defaultEmpty, false)) {
			int answer = GUI.yesNoPopupQuestion("Can't find default Empty-Image in \"" + Config.systemFolder + Config.ds + Config.defaultEmpty + "\"...\n" +
					"Please add this full transparency file, its a base-file, the most Makers need 96x96px Face-Pictures!\n" +
					"\n" +
					"Should I create this file for you? (96x96px transparent)?", "Missing File", JOptionPane.WARNING_MESSAGE, true);
			if (answer == JOptionPane.YES_OPTION) {
				if(saveGraphic(ImageConstruct.drawNewPicture(96, 96), Config.systemFolder + Config.ds + Config.defaultEmpty))
					GUI.addStartupInfo("Default Empty-Graphic created.");
				else
					GUI.showError("Can't create Empty-Graphic", true);
			} else
				GUI.showError("Can't start Program without Empty-Graphic! Exiting...", true);
		}
		if (! checkFile(Config.systemFolder + Config.ds + Config.removeIcon, false))
			GUI.showError("Can't load graphic: " + Config.systemFolder + Config.ds + Config.removeIcon, true);
		if (! checkFile(Config.systemFolder + Config.ds + Config.randomIcon, false))
			GUI.showError("Can't load graphic: " + Config.systemFolder + Config.ds + Config.randomIcon, true);
	}

	/**
	 *
	 * @param dirPath path of the dir, which will be checked
	 */
	private static void checkDir(String dirPath) {
		File directory = new File(dirPath);

		if (! directory.exists()) {
			try {
				// Create dir
				if (! directory.mkdir())
					throw new Exception("Can't create the Directory \"" + dirPath + "\"!\n");
			} catch (Exception e) {
				GUI.showFatalError(e);
			} finally {
				GUI.addStartupInfo("Directory \"" + dirPath + "\" doesn't exists, so it was created.");
			}
		}

		if (! directory.isDirectory() && directory.exists())
			GUI.showError("Can't create or access Directory \"" + dirPath + "\" there is already a File(?) with this name!", true);
	}

	/**
	 *
	 * @param filePath path of the file, what will be checked
	 * @param createMissing true/false on true this method will try to create the file if not exists
	 * @return true/false true when dir exists
	 */
	protected static boolean checkFile(String filePath, boolean createMissing) {
		File file = new File(filePath);

		if (! file.exists()) {
			if (createMissing) {
				try {
					// Write new file
					new FileWriter(file).close();
				} catch (Exception e) {
					GUI.showFatalError(e);
				} finally {
					GUI.addStartupInfo("File \"" + filePath + "\" doesn't exists, so it was generated.");
				}
				// File was created, so it exists!
				return true;
			}

			return false;
		}

		// Is this really a file?
		if (! file.isFile())
			GUI.showError("File \"" + filePath + "\" is not a file! Please check it!", true);

		return true;
	}

	/**
	 *
	 * @param p the properties that we should save
	 * @param filename name of the target save-file
	 */
	protected static void saveProperties(Properties p, String filename) {
		if (checkFile(Config.systemFolder + Config.ds + filename, true)) {
			try {
				Writer fWriter = new FileWriter(Config.systemFolder + Config.ds + filename);
				p.store(fWriter, "Face-Maker v." + Config.version + "." + Config.subRevision + " ConfigFile"); // Store Props
			} catch (Exception e) {
				GUI.showFatalError(e);
			}
		}
	}

	/**
	 *
	 * @param filename name of the config-file that we should load
	 * @return properties from the file
	 */
	protected static Properties loadProperties(String filename) {
		Properties prop = new Properties();
		String path = Config.systemFolder + Config.ds + filename;

		try {
			if (! checkFile(path, false))
				throw new Exception("Can't load Properties... File \"" + path + "\" doesn't exists!");

			Reader r = new FileReader(path);
			prop.load(r); // Load Props
		} catch (Exception e) {
			GUI.showFatalError(e);
		}

		return prop;
	}

	/**
	 *
	 * @param directoryPath the path of the directory where you want to get a list of files
	 * @return String array with file-names
	 */
	public static ArrayList<String> getFileList(String directoryPath) {
		File f = new File(directoryPath);
		String[] files = f.list();
		ArrayList<String> out = new ArrayList<String>();

		if (files != null) {
			for (String file : files) {
				File tmpFile = new File(directoryPath + Config.ds + file);
				if (tmpFile.isFile()) {
					out.add(file);
				}
			}
		}

		return out;
	}

	/**
	 *
	 * @param directoryPath the path of the directory where you want to get a list of directories
	 * @return String array with directory-names
	 */
	public static ArrayList<String> getDirList(String directoryPath) {
		File f = new File(directoryPath);
		String[] files = f.list();
		ArrayList<String> out = new ArrayList<String>();

		if (files != null) {
			for (String file : files) {
				File tmpFile = new File(directoryPath + Config.ds + file);
				if (tmpFile.isDirectory()) {
					out.add(file);
				}
			}
		}

		return out;
	}

	/**
	 * This method clears the tmp folder
	 */
	public static void cleanup() {
		String tmpPath = Config.systemFolder + Config.ds + Config.tmpFolder;
		ArrayList files = getFileList(tmpPath);

		for (Object file : files) {
			deleteFile(tmpPath + Config.ds + file);
		}
	}

	/**
	 *
	 * @param filePath path to the file that should deleted
	 */
	private static void deleteFile(String filePath) {
		File tmpFile = new File(filePath);

		if (checkFile(filePath, false)) {
			try {
				if (! tmpFile.delete())
					throw new Exception("Can't delete tmp-File \"" + filePath + "\".");
			} catch (Exception e) {
				GUI.showWarning(e);
			}
		} else
			GUI.showWarning("Can't delete a file that doesn't exist. (File: \"" + filePath + "\")");
	}

	/**
	 *
	 * @param img image that will convert into an icon
	 * @return the picture as icon
	 */
	private static ImageIcon getIcon(BufferedImage img) {
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(img);
		} catch (Exception e) {
			GUI.showFatalError(e);
		}

		return icon;
	}

	/**
	 *
	 * @param path path to the folder
	 * @param extension file extension exmpl. (png, txt)
	 * @return count of files of the selected type in the selected folder
	 */
	private static int countFileType(String path, String extension) {
		ArrayList<String> files = getFileList(path);
		int count = 0;

		for (String file : files) {
			if (file.toLowerCase().endsWith("." + extension))
				count++;
		}

		return count;
	}

	/**
	 *
	 * @param filePath path to the img
	 * @param width @Nullable width of the icon
	 * @param height @Nullable height of the icon
	 * @return the picture as icon
	 */
	public static ImageIcon getIcon(String filePath, @Nullable int width, @Nullable int height) {
		ImageIcon icon;
		BufferedImage img = loadBImg(filePath);

		// set correct size if default size (null) is selected
		if (width == 0)
			width = img.getWidth();
		if (height == 0)
			height = img.getHeight();

		// Check size
		if ( img.getWidth() == width && img.getHeight() == height)
			icon = getIcon(img);
		else {
			String tmpPath =  Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + filePath.replace(Config.ds, "");
			// check if sized image already exists, if load it
			if (! checkFile(tmpPath, false)) {
				// New picture for changed values
				BufferedImage tmpImg = ImageConstruct.drawNewPicture(width, height);
				Graphics g = tmpImg.getGraphics();
				// Redraw graphics
				g.drawImage(img, 0, 0, width, height, null);
				// Save temp
				saveGraphic(tmpImg, tmpPath);
			}

			return getIcon(tmpPath, width, height);
		}

		return icon;
	}

	/**
	 *
	 * @param filePath path to the img
	 * @return the image
	 */
	public static BufferedImage loadBImg(String filePath) {
		File file = new File(filePath);
		BufferedImage bufferedImage = null;

		if (file.exists() && file.isFile()) {
			try {
				bufferedImage = ImageIO.read(file);
			} catch (Exception e) {
				GUI.showFatalError(e);
			}
		}

		return bufferedImage;
	}

	/**
	 *
	 * @param path path to the folder
	 * @return count of image files in the directory
	 */
	public static int countImages(String path) {
		int count = 0;

		count += countFileType(path, "png") + FileOperations.countFileType(path, "bmp");
		count += countFileType(path, "jpg") + FileOperations.countFileType(path, "jpeg");

		return count;
	}

	/**
	 *
	 * @param path path to the file
	 * @return fileExtension
	 */
	public static String getFileTyp(String path) {
		String type = "";

		if (checkFile(path, false)) {
			int index = path.lastIndexOf(".");
			if (index != -1)
				type = path.substring(index);
		}

		return type.toLowerCase();
	}
}
