package org.petschko.facegen.classes;

import java.util.Properties;

/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015.
 */
public class UserPrefs extends FileOperations {
	private static Properties props = null;

	/**
	 * load Properties from user-file into global var if empty
	 */
	public static void setupProps() {
		// Load User settings only if empty
		if (props == null)
			setProps(loadProperties(Config.defaultUserConfigFile));
	}

	/**
	 *
	 * @return Properties object
	 */
	public static Properties getProps() {
		if (props == null)
			setupProps();

		return props;
	}

	/**
	 *
	 * @param props Properties object
	 */
	private static void setProps(Properties props) {
		UserPrefs.props = props;
	}

	/**
	 *
	 * @return default Properties object
	 */
	protected static Properties defaultProps() {
		Properties defaultP = new Properties();

		// Set Properties
		defaultP.setProperty("AskOnExit", "1");
		defaultP.setProperty("Language", "en"); // Not implement jet
		defaultP.setProperty("defaultOutputImageHeight", "0");
		defaultP.setProperty("defaultOutputImageWidth", "0");
		defaultP.setProperty("mySaveDir", "0");
		defaultP.setProperty("myBackgroundImg", "0");
		defaultP.setProperty("browseOpenDir", "");
		defaultP.setProperty("browseSaveDir", Config.defaultSaveFolder);
		defaultP.setProperty("Layer_Count", "0");
		defaultP.setProperty("buttonsRow", "8");
		defaultP.setProperty("buttonWidth", "96");
		defaultP.setProperty("buttonHeight", "96");
		defaultP.setProperty("WindowWidth", "1300");
		defaultP.setProperty("WindowHeight", "665");

		return defaultP;
	}

	/**
	 *
	 * @param all true/false on true reset default-config file too
	 */
	protected static void resetToDefault(boolean all) {
		// Reset the "default" config file to default (if you have done some changes in this file)
		if (all)
			saveProperties(defaultProps(), Config.defaultConfigFile);

		saveProperties(loadProperties(Config.defaultConfigFile), Config.defaultUserConfigFile);
	}

	/**
	 * save config on Program exit
	 */
	public static void onProgramExit() {
		// Hold Config up to date!
		saveProperties(getProps(), Config.defaultUserConfigFile);
	}

	/**
	 * on click "cancel" in setup-config you will restore previous values
	 */
	public static void notChangeProperties() {
		setProps(loadProperties(Config.defaultUserConfigFile));
	}
}
