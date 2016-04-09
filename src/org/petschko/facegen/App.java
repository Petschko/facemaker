package org.petschko.facegen;

import org.petschko.facegen.classes.FileOperations;
import org.petschko.facegen.classes.GUI;
import org.petschko.facegen.classes.ImageConstruct;
import org.petschko.facegen.classes.UserPrefs;

import javax.swing.JDialog;

/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015
 */
class App {

	/**
	 *
	 * @param args void - not used
	 */
	public static void main( String args[] ) {

		// Hello-Window
		JDialog hello = GUI.showHelloWindow();

		// Check if all Folders and files are available and if not try to create them
		FileOperations.checkFileSystem();

		// load config into memory
		UserPrefs.setupProps();

		// Fill vars for faceSet
		ImageConstruct.fillVars(false);

		// Show if something was happen while startup
		GUI.showProgramStartInfo();

		// End of startup -----------------------
		// Build & Show GUI
		new GUI();
		hello.dispose();
	}
}
