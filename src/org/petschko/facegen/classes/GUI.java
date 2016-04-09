package org.petschko.facegen.classes;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 23.04.2015
 */
public class GUI {
	private static JFrame myFrame;
	private static String startupInfo;
	private static final JLabel facePicture = new JLabel();
	private static final JLabel faceSetPicture = new JLabel();
	private static final JLabel selectedPicture = new JLabel();
	private static String tmpSelectedImg;
	private static ArrayList<LayerImg> faceLayerImg = new ArrayList<LayerImg>();
	private static JTabbedPane buttonArea;
	private static JDialog faceSetDialog;

	/**
	 * Constructor - creates the GUI
	 */
	public GUI() {
		int maxLayers = 0;
		// Set name and size
		setMyFrame(new JFrame(Config.programName + " by " + Config.programmer + " v." + Config.version + "." + Config.subRevision + " (" + Config.programState + ")"));
		getMyFrame().setSize(1300, 665);
		// Set User Values
		try {
			getMyFrame().setSize(Integer.parseInt(UserPrefs.getProps().getProperty("WindowWidth", "1300")), Integer.parseInt(UserPrefs.getProps().getProperty("WindowHeight", "665")));
			maxLayers = Integer.parseInt(UserPrefs.getProps().getProperty("Layer_Count", "0")) + 1; // Layers starts with 0 but "0" cannot set as layer because its the "not set" layer in config, so start with 1
		} catch (Exception e) {
			showFatalError(e);
		}
		// Call refactorImg to load the facePicture for gui
		GUI_Action.fillFaceArr(maxLayers); // Fill Picture with empty layers
		GUI_Action.fillDefaultImg(maxLayers);
		ImageConstruct.drawGUIbg(getFaceLayerImg());
		ImageConstruct.drawFaceSet();

		// Setup menu ----------------------------------------------------------
		JMenuBar menuBar = new JMenuBar();
		// Main MenuPoints
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu toolMenu = new JMenu("Tools");
		JMenu settingMenu = new JMenu("Settings");
		JMenu helpMenu = new JMenu("Help");

		// Sub Menus
		JMenu saveMenu = new JMenu("Save");
		JMenu resetMenu = new JMenu("Reset");

		// Sub MenuPoints
		JMenuItem saveFace = new JMenuItem("Face-Graphic...");
		JMenuItem saveFaceSet = new JMenuItem("FaceSet-Graphic...");
		JMenuItem resetFace = new JMenuItem("Face-Graphic");
		JMenuItem resetFaceSet = new JMenuItem("FaceSet-Graphic");
		JMenuItem resetAll = new JMenuItem("All");
		JMenuItem close = new JMenuItem("Exit Program");
		//JMenuItem undo = new JMenuItem("Undo");
		//JMenuItem redo = new JMenuItem("Redo");
		JMenuItem outPutSize = new JMenuItem("Set output size to...");
		JMenuItem addFaceToSet = new JMenuItem("Add Face to FaceSet...") ;
		JMenuItem randomize = new JMenuItem("Random Face");
		JMenuItem clearFace = new JMenuItem("Erase Face");
		JMenuItem toolFaceSet = new JMenuItem("Create/Edit FaceSet...");
		JMenuItem toolResizeImg = new JMenuItem("Resize Image...");
		JMenuItem toolCombineImg = new JMenuItem("Combine 2 Images...");
		JMenuItem settings = new JMenuItem("Settings...");
		JMenuItem saveDefault = new JMenuItem("Save current Face as Default");
		JMenuItem reloadRes = new JMenuItem("Reload Resources");
		JMenuItem about = new JMenuItem("About...");

		// Disable MenuItems which are not done yet
		//addFaceToSet.setEnabled(false);
		outPutSize.setEnabled(false);
		//toolFaceSet.setEnabled(false);
		toolResizeImg.setEnabled(false);
		toolCombineImg.setEnabled(false);
		settings.setEnabled(false);
		reloadRes.setEnabled(false);

		// Add to File Menu
		saveMenu.add(saveFace);
		saveMenu.add(saveFaceSet);
		fileMenu.add(saveMenu);
		resetMenu.add(resetFace);
		resetMenu.add(resetFaceSet);
		resetMenu.add(resetAll);
		fileMenu.add(resetMenu);
		fileMenu.addSeparator();
		fileMenu.add(close);

		// Add to Edit Menu
		// Redo and undo
		//editMenu.addSeparator();
		editMenu.add(outPutSize);
		editMenu.addSeparator();
		editMenu.add(randomize);
		editMenu.add(clearFace);
		editMenu.addSeparator();
		editMenu.add(addFaceToSet);

		// Add to Tool Menu
		toolMenu.add(toolFaceSet);
		toolMenu.add(toolResizeImg);
		toolMenu.add(toolCombineImg);

		// Add to Setting Menu
		settingMenu.add(settings);
		settingMenu.addSeparator();
		settingMenu.add(saveDefault);
		settingMenu.add(reloadRes);

		// Add to Help Menu
		helpMenu.add(about);

		// Add Menus to MenuBar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(toolMenu);
		menuBar.add(settingMenu);
		menuBar.add(helpMenu);

		// End of Menu setup -------------------------------------------------
		// Build other Windows
		constructFaceSetDialog();

		// Start of Layout setup
		// Creating Container
		JPanel mainContainer = new JPanel();
		JPanel leftSideContainer = new JPanel();
		JPanel leftSideContainerBorder = new JPanel();
		JPanel pictureContainer = new JPanel();
		JPanel buttonCenterContainer = new JPanel();
		JPanel buttonContainer = new JPanel();
		JPanel saveButtonContainer = new JPanel();
		JPanel resetButtonContainer = new JPanel();
		JPanel addToSetButtonContainer = new JPanel();
		JPanel randomButtonContainer = new JPanel();
		JPanel rescanResFolderButtonContainer = new JPanel();
		JPanel setSizeButtonContainer = new JPanel();
		JPanel sizeTextContainer = new JPanel();

		// creating comps
		JButton saveButton = new JButton("Save...");
		JButton resetButton = new JButton("Reset");
		JButton addToSetButton = new JButton("Add to FaceSet...");
		JButton randomButton = new JButton("Random");
		JButton rescanResFolderButton = new JButton("Reload resources");
		rescanResFolderButton.setEnabled(false);
		JButton setSizeButton = new JButton("Set output size...");
		setSizeButton.setEnabled(false);
		JLabel sizeText = new JLabel("<html><div align='center'>Current size:<br>" + ImageConstruct.getSize(false) + "x" + ImageConstruct.getSize(true) + "</div></html>");

		// Link functions to buttons
		saveButton.addActionListener(GUI_Action.saveFace());
		addToSetButton.addActionListener(GUI_Action.showWindow(getFaceSetDialog(), Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png"));
		resetButton.addActionListener(GUI_Action.resetFace(maxLayers));
		randomButton.addActionListener(GUI_Action.randomAll());
		//setSizeButton.addActionListener();

		// Building left side
		mainContainer.setLayout(new BorderLayout());
		//leftSideContainer.setLayout(new GridBagLayout());
		leftSideContainer.setLayout(new BorderLayout());
		leftSideContainerBorder.setLayout(new BorderLayout());
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
		getFacePicture().setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png", 96, 96));

		// Building center side
		setButtonArea(generateButtonContainers());
		rescanResFolderButton.addActionListener(GUI_Action.reloadResources());

		// Set size and border
		getFacePicture().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftSideContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));
		leftSideContainerBorder.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		getButtonArea().setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
		buttonContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Add content to container
		saveButtonContainer.add(saveButton);
		addToSetButtonContainer.add(addToSetButton);
		resetButtonContainer.add(resetButton);
		randomButtonContainer.add(randomButton);
		rescanResFolderButtonContainer.add(rescanResFolderButton);
		setSizeButtonContainer.add(setSizeButton);
		sizeTextContainer.add(sizeText);
		buttonContainer.add(saveButtonContainer);
		buttonContainer.add(addToSetButtonContainer);
		buttonContainer.add(resetButtonContainer);
		buttonContainer.add(randomButtonContainer);
		buttonContainer.add(rescanResFolderButtonContainer);
		buttonContainer.add(setSizeButtonContainer);
		buttonContainer.add(sizeTextContainer);
		buttonCenterContainer.add(buttonContainer);
		pictureContainer.add(getFacePicture());
		leftSideContainerBorder.add(buttonCenterContainer, BorderLayout.CENTER);
		leftSideContainerBorder.add(pictureContainer, BorderLayout.NORTH);

		// Add all container together
		leftSideContainer.add(leftSideContainerBorder);
		mainContainer.add(leftSideContainer, BorderLayout.WEST);
		mainContainer.add(getButtonArea(), BorderLayout.CENTER);
		getMyFrame().add(mainContainer, BorderLayout.CENTER);
		getMyFrame().add(menuBar, BorderLayout.NORTH);

		// Set close option
		getMyFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getMyFrame().addWindowListener(GUI_Action.closeWindow());

		// Add functions to MenuItems
		saveFace.addActionListener(GUI_Action.saveFace());
		saveFaceSet.addActionListener(GUI_Action.saveFaceSet());
		resetFace.addActionListener(GUI_Action.resetFace(maxLayers));
		resetFaceSet.addActionListener(GUI_Action.resetFaceSet());
		resetAll.addActionListener(GUI_Action.resetAll(maxLayers));
		close.addActionListener(GUI_Action.close());
		addFaceToSet.addActionListener(GUI_Action.showWindow(getFaceSetDialog(), Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png"));
		randomize.addActionListener(GUI_Action.randomAll());
		clearFace.addActionListener(GUI_Action.clearFace(maxLayers));
		toolFaceSet.addActionListener(GUI_Action.showWindow(getFaceSetDialog(), null));
		saveDefault.addActionListener(GUI_Action.saveConfigDefaultImg(maxLayers));
		settings.addActionListener(GUI_Action.settings());
		about.addActionListener(GUI_Action.about());

		getMyFrame().setLocationRelativeTo(null); // Center Program
		getMyFrame().setVisible(true);
	}

	/**
	 * construct the FaceSet window
	 */
	private void constructFaceSetDialog() {
		JDialog jDialog = new JDialog(getMyFrame(), "Edit FaceSet...", true);
		JPanel graphicContainer = new JPanel();
		JLabel arrowLeft = new JLabel();
		JPanel addContainer = new JPanel();
		JPanel addSubContainer = new JPanel();
		JPanel addButtonRow1 = new JPanel();
		JPanel addButtonRow2 = new JPanel();
		JPanel delContainer = new JPanel();
		JPanel delSubContainer = new JPanel();
		JPanel delButtonRow1 = new JPanel();
		JPanel delButtonRow2 = new JPanel();
		JPanel optionContainer = new JPanel();

		// Set Layout
		graphicContainer.setLayout(new BorderLayout(0, 0));
		addContainer.setLayout(new BorderLayout(0, 0));
		addSubContainer.setLayout(new BorderLayout(0, 0));
		addButtonRow1.setLayout(new BoxLayout(addButtonRow1, BoxLayout.X_AXIS));
		addButtonRow2.setLayout(new BoxLayout(addButtonRow2, BoxLayout.X_AXIS));
		delContainer.setLayout(new BorderLayout(0, 0));
		delSubContainer.setLayout(new BorderLayout(0, 0));
		delButtonRow1.setLayout(new BoxLayout(delButtonRow1, BoxLayout.X_AXIS));
		delButtonRow2.setLayout(new BoxLayout(delButtonRow2, BoxLayout.X_AXIS));
		optionContainer.setLayout(new BoxLayout(optionContainer, BoxLayout.Y_AXIS));

		// Get Pictures
		getFaceSetPicture().setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFaceSet.png", 384, 192));
		getSelectedPicture().setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + "backgroundFace.png", 96, 96));
		setTmpSelectedImg(Config.systemFolder + Config.ds + Config.defaultEmpty);
		arrowLeft.setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.arrowLeft, 95, 81));

		// Generate button areas
		for (int i = 1; i <= 8; i++) {
			JButton button = new JButton(i + "");
			button.addActionListener(GUI_Action.addToSet(i));
			if (i <= 4)
				addButtonRow1.add(button);
			else
				addButtonRow2.add(button);
		}
		for (int i = 1; i <= 8; i++) {
			JButton button = new JButton(i + "");
			button.addActionListener(GUI_Action.delFromSet(i));
			if (i <= 4)
				delButtonRow1.add(button);
			else
				delButtonRow2.add(button);
		}

		// Generate option buttons
		JButton clearFaceSet = new JButton("Clear FaceSet");
		JButton clearSelectedImg = new JButton("Clear selected Image");
		JButton openImg = new JButton("Open Image...");
		JButton useCurrentFace = new JButton("Open Current Face");
		JButton saveFaceSet = new JButton("Save FaceSet...");
		clearFaceSet.addActionListener(GUI_Action.resetFaceSet());
		clearSelectedImg.addActionListener(GUI_Action.setSelectedImgButton(""));
		openImg.addActionListener(GUI_Action.openTmpImage());
		useCurrentFace.addActionListener(GUI_Action.setSelectedImgButton(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png"));
		saveFaceSet.addActionListener(GUI_Action.saveFaceSet());


		// Set Graphic container
		graphicContainer.add(getFaceSetPicture(), BorderLayout.WEST);
		graphicContainer.add(arrowLeft, BorderLayout.CENTER);
		graphicContainer.add(getSelectedPicture(), BorderLayout.EAST);

		// Add comps to containers
		optionContainer.add(clearFaceSet);
		optionContainer.add(clearSelectedImg);
		optionContainer.add(openImg);
		optionContainer.add(useCurrentFace);
		optionContainer.add(saveFaceSet);
		delSubContainer.add(delButtonRow1, BorderLayout.NORTH);
		delSubContainer.add(delButtonRow2, BorderLayout.CENTER);
		delContainer.add(delSubContainer, BorderLayout.CENTER);
		delContainer.add(new JLabel("<html><div align='center'>DELETE from:</div></html>"), BorderLayout.NORTH);
		addSubContainer.add(addButtonRow1, BorderLayout.NORTH);
		addSubContainer.add(addButtonRow2, BorderLayout.CENTER);
		addContainer.add(addSubContainer, BorderLayout.CENTER);
		addContainer.add(new JLabel("<html><div align='center'>ADD to:</div></html>"), BorderLayout.NORTH);

		// Add comps to dialog
		jDialog.add(graphicContainer, BorderLayout.NORTH);
		jDialog.add(optionContainer, BorderLayout.WEST);
		jDialog.add(delContainer, BorderLayout.CENTER);
		jDialog.add(addContainer, BorderLayout.EAST);

		// Set setting of the dialog
		jDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		jDialog.addWindowListener(GUI_Action.hideWindow(jDialog));
		jDialog.pack();
		jDialog.setResizable(false);
		jDialog.setLocationRelativeTo(getMyFrame());
		jDialog.setVisible(false);
		setFaceSetDialog(jDialog);
	}

	/**
	 *
	 * @return button area
	 */
	protected static JTabbedPane getButtonArea() {
		return buttonArea;
	}

	/**
	 *
	 * @param buttonArea button area
	 */
	protected static void setButtonArea(JTabbedPane buttonArea) {
		GUI.buttonArea = buttonArea;
	}

	/**
	 * show window "about"
	 */
	protected static void showAbout() {
		int height = 481;
		JDialog jDialog = new JDialog(getMyFrame(), "About", true);

		// Create container
		JPanel borderFrame = new JPanel();
		JPanel okButton = new JPanel();
		JPanel logoFrame = new JPanel();
		JPanel descriptionContainer = new JPanel();
		borderFrame.setLayout(new BorderLayout(0, 0));
		descriptionContainer.setLayout(new BorderLayout(0, 0));
		borderFrame.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Creating comps
		JLabel titleComp = new JLabel();
		JLabel logoComp = new JLabel();
		JLabel descriptionComp = new JLabel();

		// About text
		String aboutText = "<u>Programmer:</u> " + Config.programmer + "<br>" +
				"<u>Programmer Website(s):</u><br>"; // + 16px
		for (String url : Config.programmerHp) {
			aboutText += "-> <a href=\"" + url + "\">" + url + "</a><br>";
			height = height + 16;
		}
		aboutText += "<u>Version:</u> " + Config.version + "." + Config.subRevision + " <i>(" + Config.programState + ")</i><br><br>" +
				"<u>Graphics by: " + Config.graphicsBy + "</u><br>" +
				"<u>Website(s):</u><br>";
		for (String url : Config.otherHp) {
			aboutText += "-> <a href=\"" + url + "\">" + url + "</a><br>";
			height = height + 16;
		}

		// Add content to comps
		titleComp.setText("<html>" + Config.programName +  " v." + Config.version + "." + Config.subRevision + " created by " + Config.programmer + "<br>" +
				"<br><u>About this Program:</u> " + Config.programDescr + "<html>");
		logoComp.setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.logoAbout, 0, 0));
		//logoComp.setIcon(dye(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.logoAbout, 0, 0))); // todo test make dye gray
		descriptionComp.setText("<html><br>" + aboutText + "</html>");

		// Creating CloseButton
		JButton ok = new JButton("Ok");
		ok.addActionListener(GUI_Action.closeThis(jDialog));
		okButton.add(ok, BorderLayout.CENTER);

		// Add to Container
		descriptionContainer.add(descriptionComp, BorderLayout.NORTH);
		descriptionContainer.add(okButton, BorderLayout.CENTER);
		logoFrame.add(logoComp);

		// Adding to Windows
		borderFrame.add(titleComp, BorderLayout.CENTER);
		borderFrame.add(logoFrame, BorderLayout.NORTH);
		borderFrame.add(descriptionContainer, BorderLayout.SOUTH);
		jDialog.add(borderFrame, BorderLayout.NORTH);

		// Set correct size and show window
		jDialog.setSize(400, height);
		jDialog.setResizable(false);
		jDialog.setLocationRelativeTo(getMyFrame());
		jDialog.setVisible(true);
	}

	/**
	 *
	 * @param title Title of the window
	 * @param saveWindow true/false is save window?
	 * @return Sting of filePath
	 */
	protected static String fileWindow(String title, boolean saveWindow) {
		String file = "";

		// New File-chooser
		JFileChooser fileWindow = new JFileChooser();
		fileWindow.setDialogTitle(title);

		// Set fileChooser type (open or save)
		if (saveWindow) {
			fileWindow.setFileFilter(new FileNameExtensionFilter("PNG-Images (*.png)", "png")); // Export Filter
			fileWindow.setDialogType(JFileChooser.SAVE_DIALOG);

			// Set start dir
			if (UserPrefs.getProps().getProperty("mySaveDir", "0").equals("0"))
				fileWindow.setCurrentDirectory(new File(Config.defaultSaveFolder)); // Not set use default
			else if (UserPrefs.getProps().getProperty("mySaveDir", "0").equals("-1")) {
				if (new File(UserPrefs.getProps().getProperty("browseSaveDir", Config.defaultSaveFolder)).exists())
					fileWindow.setCurrentDirectory(new File(UserPrefs.getProps().getProperty("browseSaveDir", Config.defaultSaveFolder))); // Last folder
			} else if (new File(UserPrefs.getProps().getProperty("mySaveDir", "0")).exists())
				fileWindow.setCurrentDirectory(new File(UserPrefs.getProps().getProperty("mySaveDir", "0"))); // Set own start folder

		} else {
			fileWindow.setFileFilter(new FileNameExtensionFilter("Images (PNG recommend) - (*.png; *.jpg; *.jpeg; *.bmp)", "png", "jpg", "jpeg", "bmp")); // Import filter
			fileWindow.setDialogType(JFileChooser.OPEN_DIALOG);

			// Open last open dir
			if (new File(UserPrefs.getProps().getProperty("browseOpenDir", "0")).exists())
				fileWindow.setCurrentDirectory(new File(UserPrefs.getProps().getProperty("browseOpenDir", "0")));
		}

		if (saveWindow) {
			boolean done = false;
			while (! done) {
				done = true;
				if (fileWindow.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fileWindow.getSelectedFile().toString();
					// Check if the User typed ending by himself if not add it
					if (! file.toLowerCase().endsWith(".png"))
						file += ".png";

					// Write directory in last dir for user
					UserPrefs.getProps().setProperty("browseSaveDir", fileWindow.getCurrentDirectory().toString());

					// Check if the file exists and if ask the user if he want overwrite it
					if (FileOperations.checkFile(file, false)) {
						if (yesNoPopupQuestion("File already exists! Overwrite the existing file?", "Existing File", JOptionPane.WARNING_MESSAGE, false) != JOptionPane.YES_OPTION) {
							done = false; // Reopen file window on "no"
							file = ""; // Set file back to empty
						}
					}
				}
			}
		} else {
			if(fileWindow.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = fileWindow.getSelectedFile().toString();

				// Write directory in last dir for user
				UserPrefs.getProps().setProperty("browseOpenDir", fileWindow.getCurrentDirectory().toString());

				// check if file exists if not reset fileStr
				if (! FileOperations.checkFile(file, false))
					file = "";
			}
		}

		return file;
	}

	/**
	 *
	 * @return myFrame
	 */
	private static JFrame getMyFrame() {
		return myFrame;
	}

	/**
	 *
	 * @param myFrame set JFrame
	 */
	private static void setMyFrame(JFrame myFrame) {
		GUI.myFrame = myFrame;
	}

	/**
	 *
	 * @param e Exception
	 */
	public static void showFatalError(Exception e) {

		// Exception to String
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		// show Msg
		JOptionPane.showConfirmDialog(null, sw.toString(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

		// Exit Program
		System.exit(1);
	}

	/**
	 *
	 * @param errorMsg Message to display
	 * @param exit true/false close program?
	 */
	public static void showError(String errorMsg, boolean exit) {
		JOptionPane.showConfirmDialog(null, errorMsg, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

		if (exit)
			System.exit(2);
	}

	/**
	 *
	 * @param Msg Message to display
	 */
	public static void showWarning(String Msg) {
		JOptionPane.showConfirmDialog(null, Msg, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
	}

	/**
	 *
	 * @param e Exception
	 */
	public static void showWarning(Exception e) {
		// Exception to String
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		showWarning(sw.toString());
	}

	/**
	 *
	 * @param infoMsg Message to display
	 */
	private static void showInfo(String infoMsg) {
		JOptionPane.showMessageDialog(null, infoMsg);
	}

	/**
	 * display all startup information
	 */
	public static void showProgramStartInfo() {
		if (startupInfo != null) {
			showInfo(getStartupInfo());
			setStartupInfo(null);
		}
	}

	/**
	 *
	 * @return string of startup info
	 */
	private static String getStartupInfo() {
		return startupInfo;
	}

	/**
	 *
	 * @param startupInfo string to add to startup info
	 */
	public static void addStartupInfo(String startupInfo) {
		if (GUI.startupInfo == null)
			GUI.setStartupInfo(startupInfo);
		else
			setStartupInfo(getStartupInfo() + "\n" + startupInfo);
	}

	/**
	 *
	 * @param startupInfo set startup info
	 */
	private static void setStartupInfo(String startupInfo) {
		GUI.startupInfo = startupInfo;
	}

	/**
	 *
	 * @param msg Message to display
	 * @param title Title of the Popup Window
	 * @param type Message type (Error, Warning etc)
	 * @param defaultYes true/false true means that "yes" is preselected
	 * @return JOptionPane dialog result
	 */
	public static int yesNoPopupQuestion(String msg, String title, int type, boolean defaultYes) {
		String def;
		if(defaultYes)
			def = "Yes";
		else
			def = "No";

		return JOptionPane.showOptionDialog(null, msg, title, JOptionPane.YES_NO_OPTION, type, null, new String[] {"Yes", "No"}, def);
	}

	/**
	 *
	 * @return GUI-Face picture element
	 */
	public static JLabel getFacePicture() {
		return facePicture;
	}

	/**
	 *
	 * @return layer of the face as arrayList
	 */
	protected static ArrayList<LayerImg> getFaceLayerImg() {
		return faceLayerImg;
	}

	/**
	 *
	 * @param faceLayerImg faceLayerImg of the face as arrayList
	 */
	protected static void setFaceLayerImg(ArrayList<LayerImg> faceLayerImg) {
		GUI.faceLayerImg = faceLayerImg;
	}

	/**
	 *
	 * @return faceSet GUI-Picture-Label
	 */
	public static JLabel getFaceSetPicture() {
		return faceSetPicture;
	}

	/**
	 *
	 * @return Dialog Window with start info
	 */
	public static JDialog showHelloWindow() {
		JDialog jDialog = new JDialog();
		JLabel text = new JLabel();
		text.setText("Please wait, while the Program scans Resource-Folders and generates GUI...");

		jDialog.setTitle("Hello");
		jDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		jDialog.add(text);
		jDialog.pack();
		jDialog.setLocationRelativeTo(null);
		jDialog.setVisible(true);

		return jDialog;
	}

	/**
	 *
	 * @param path path to the folder
	 * @param layerId layer_id of the picture
	 * @param subDir allow to add subDir files?
	 * @param allowEmpty allow empty layer?
	 * @return JPanel with buttons
	 */
	private static JPanel createFaceButtons(String path, int layerId, boolean subDir, boolean allowEmpty, boolean allowRandom) {
		int buttonsPerLine = 8;
		int buttonHeight = 96;
		int buttonWidth = 96;
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		final ArrayList<String> files = FileOperations.getFileList(path);

		// Get user values
		try {
			buttonsPerLine = Integer.parseInt(UserPrefs.getProps().getProperty("buttonsRow", "8"));
			buttonWidth = Integer.parseInt(UserPrefs.getProps().getProperty("buttonWidth", "96"));
			buttonHeight = Integer.parseInt(UserPrefs.getProps().getProperty("buttonHeight", "96"));
		} catch (Exception e) {
			showWarning(e);
		}

		// Add sub-folder files to fileList
		if (subDir) {
			ArrayList<String> subDirs = FileOperations.getDirList(path);
			for (String subDirectory : subDirs) {
				ArrayList<String> subFiles = FileOperations.getFileList(path + Config.ds + subDirectory);
				for (String subFile : subFiles)
					files.add(subDirectory + Config.ds + subFile);
			}
		}

		int i = 0;
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		// Add "remove"-Button if its allowed
		if (allowEmpty) {
			i++;
			JButton removeButton = new JButton();
			removeButton.setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.removeIcon, buttonWidth, buttonHeight));
			removeButton.addActionListener(GUI_Action.removeButton(layerId));
			row.add(removeButton);
		}
		// Add "random"-Button if its allowed
		if (allowRandom) {
			i++;
			JButton randomButton = new JButton();
			randomButton.setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + Config.randomIcon, buttonWidth, buttonHeight));
			randomButton.addActionListener(GUI_Action.randomButton(allowEmpty, files, layerId, path));
			row.add(randomButton);
		}
		for (final String file : files) {
			String extension = FileOperations.getFileTyp(path + Config.ds + file);
			if (extension.equals(".png") || extension.equals(".bmp") || extension.equals(".jpg") || extension.equals(".jpeg")) {
				JButton jButton = new JButton();
				jButton.setIcon(FileOperations.getIcon(path + Config.ds + file, buttonWidth, buttonHeight));
				jButton.addActionListener(GUI_Action.layerButton(layerId, file, path));

				row.add(jButton);
				i++;
			}

			// Create next line after x buttons
			if (i == buttonsPerLine) {
				jPanel.add(row);
				row = new JPanel();
				row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
				i = 0;
			}
		}
		if (i > 0)
			jPanel.add(row);

		return jPanel;
	}

	/**
	 *
	 * @return JTabbed Pane area of buttons
	 */
	protected static JTabbedPane generateButtonContainers() {
		JTabbedPane selectionArea = new JTabbedPane();
		int scrollSpeed = 50;

		// Generate tabs for every type (exmpl. hair, eyes, mouth...)
		ArrayList<String> typeFolder = FileOperations.getDirList(Config.resourceFolder);
		int i = 0;
		for (String type : typeFolder) {
			Color color;
			boolean enabled;
			boolean allowEmpty = false;
			boolean allowRandom = true;
			int layerId = 0;
			int countFiles = 0;

			// Get config of the layer of this folder
			if (UserPrefs.getProps().getProperty("Layer_ResSource_" + type, "0").equals("0")) {
				color = new Color(0xFF8C36);
				enabled = false;
				// Write setting into file (easier adding for testing) todo: make settings
				UserPrefs.getProps().setProperty("Layer_ResSource_" + type, "0");
			} else {
				color = new Color(0x93FF93);
				enabled = true;
				try {
					layerId = Integer.parseInt(UserPrefs.getProps().getProperty("Layer_ResSource_" + type, "0"));
					allowEmpty = Boolean.parseBoolean(UserPrefs.getProps().getProperty("AllowEmpty_Layer_ResSource_" + type, "true"));
					allowRandom = Boolean.parseBoolean(UserPrefs.getProps().getProperty("AllowRandom_Layer_ResSource_" + type, "true"));
				} catch (Exception e) {
					showFatalError(e);
				}
			}
			JPanel typeContainer = new JPanel();
			typeContainer.setLayout(new BorderLayout());
			typeContainer.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

			if (enabled) {
				// Check for subtypes(exmpl.hair -> red, yellow, brown)
				ArrayList<String> subTypeFolder = FileOperations.getDirList(Config.resourceFolder + Config.ds + type);
				JTabbedPane typeSubTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

				// Create Tab for all Images
				JPanel allTab = new JPanel();
				allTab.setLayout(new BorderLayout());
				allTab.add(createFaceButtons(Config.resourceFolder + Config.ds + type, layerId, true, allowEmpty, allowRandom));

				// Add to Parent
				JScrollPane allScrollPane = new JScrollPane(allTab, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				allScrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
				typeSubTabs.add("ALL", allScrollPane);
				// Check amount of pictures in the type folder
				if (FileOperations.countImages(Config.resourceFolder + Config.ds + type) > 0) {
					countFiles += FileOperations.countImages(Config.resourceFolder + Config.ds + type);
				}

				int n = 1; // Counter start with 1 because there is the ALL tab with the id 0
				for (String subType : subTypeFolder) {
					JPanel subTypeContainer = new JPanel();
					String pathToFolder = Config.resourceFolder + Config.ds + type + Config.ds + subType;
					subTypeContainer.add(createFaceButtons(pathToFolder, layerId, false, allowEmpty, allowRandom));

					// Add to parent
					JScrollPane subTypeScrollPane = new JScrollPane(subTypeContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					subTypeScrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
					typeSubTabs.add(subType, subTypeScrollPane);
					// Check amount of pictures, if there are 0 disable the tab
					if (FileOperations.countImages(pathToFolder) > 0)
						countFiles += FileOperations.countImages(pathToFolder);
					else {
						typeSubTabs.setEnabledAt(n, false);
						typeSubTabs.setBackgroundAt(n, Color.RED);
					}
					n++;
				}

				if (countFiles > 0) {
					typeSubTabs.setBackgroundAt(0, Color.GREEN);
					if (n == 1)
						typeSubTabs.setEnabledAt(0, false);
				} else {
					// If its empty disable
					typeSubTabs.setBackgroundAt(0, Color.RED);
					typeSubTabs.setEnabledAt(0, false);
					color = Color.RED;
					enabled = false;
				}
				typeContainer.add(typeSubTabs, BorderLayout.CENTER);
			}

			selectionArea.add(type, typeContainer);
			selectionArea.setEnabledAt(i, enabled);
			selectionArea.setBackgroundAt(i, color);
			i++;
		}

		return selectionArea;
	}

	/**
	 *
	 * @return JDialog of the faceSet Editor
	 */
	private static JDialog getFaceSetDialog() {
		return faceSetDialog;
	}

	/**
	 *
	 * @param faceSetDialog JDialog
	 */
	private static void setFaceSetDialog(JDialog faceSetDialog) {
		GUI.faceSetDialog = faceSetDialog;
	}

	/**
	 *
	 * @return selected picture label
	 */
	protected static JLabel getSelectedPicture() {
		return selectedPicture;
	}

	/**
	 *
	 * @return the filePath of the current selected picture
	 */
	protected static String getTmpSelectedImg() {
		return tmpSelectedImg;
	}

	/**
	 *
	 * @param tmpSelectedImg the new filePath of the new selected picture
	 */
	protected static void setTmpSelectedImg(String tmpSelectedImg) {
		GUI.tmpSelectedImg = tmpSelectedImg;
	}
}
