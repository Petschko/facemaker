package in.eona.petschko.facegen.classes;

/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015
 */
class Config {
	// Info about the Program
	public static final String programName = "Character-Face-Generator";
	public static final String programmer = "Petschko";
	public static final int version = 2;
	public static final int subRevision = 1;
	public static final String programState = "Beta";

	// Info about file-paths etc
	public static final String resourceFolder = "images";
	public static final String systemFolder = "system";
	public static final String defaultSaveFolder = "save";
	public static final String tmpFolder = "tmp";
	public static final String defaultBGImg = "faces.png";
	public static final String defaultEmpty = "none.png";
	public static final String removeIcon = "remove.png";
	public static final String randomIcon = "random.png";
	public static final String defaultConfigFile = "defaultConf.conf";
	public static final String defaultUserConfigFile = "userConf.conf";
	public static final String logoAbout = "avatar_petschko.png";
	public static final String arrowLeft = "arrowLeft.png";

	// Other
	public static final String ds = System.getProperty("file.separator");
	public static final String[] programmerHp = { "http://petschko.eona.in/?in=facegen", "http://petschko.deviantart.com/" };
	public static final String graphicsBy = "Petschko (Logo); Darkmoon-Network (GUI); Moromaga, Morokyu, Margaret (Face-Images)";
	public static final String[] otherHp = {"darkmoon@smtp.ru", "http://lovelymoro.web.fc2.com/"};
	public static final String programDescr = "This Program is Freeware! Please use it as much as you want.";
	//public static final String defaultLang = "en";
}
