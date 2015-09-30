package in.eona.petschko.facegen.classes;

import com.sun.istack.internal.Nullable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @autor: Petschko [peter-91@hotmail.de]
 * @date: 03.05.2015.
 */
public class GUI_Action extends GUI {

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener saveFace() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = fileWindow("Save Face-Graphic...", true);
				if (! file.equals("")) {
					int height = 0;
					int width = 0;
					BufferedImage img;

					// Parse User values to int
					try {
						height = Integer.parseInt(UserPrefs.getProps().getProperty("defaultOutputImageHeight", "0"));
						width = Integer.parseInt(UserPrefs.getProps().getProperty("defaultOutputImageWidth", "0"));
					} catch (Exception ex) {
						showWarning(ex);
					}

					// Make img
					LayerImg.resetBgImg();
					ImageConstruct.refactorImg(getFaceLayerImg());
					// Set correct size
					if (height != 0 || width != 0) {
						if (height == 0)
							height = LayerImg.getBgImgHeight();
						if (width == 0)
							width = LayerImg.getBgImgWidth();

						img = ImageConstruct.resizeImg(width, height, LayerImg.getBgImg());
					}
					else
						img = LayerImg.getBgImg();

					// save
					if (! FileOperations.saveGraphic(img, file))
						showWarning("File \"" + file + "\" is not saved...");
				}
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener saveFaceSet() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = fileWindow("Save FaceSet...", true);
				if (! file.equals("")) {
					int height = 0;
					int width = 0;

					// Parse User values to int
					try {
						height = Integer.parseInt(UserPrefs.getProps().getProperty("defaultOutputImageHeight", "0"));
						width = Integer.parseInt(UserPrefs.getProps().getProperty("defaultOutputImageWidth", "0"));
					} catch (Exception ex) {
						showWarning(ex);
					}

					// Make img
					ImageConstruct.drawFaceSet(height, width, false);

					// Save
					if (! FileOperations.saveGraphic(ImageConstruct.getBgImg(), file))
						showWarning("File \"" + file + "\" was not saved...");
				}
			}
		};
	}

	/**
	 *
	 * @param maxLayers layers of the face img
	 * @return ActionListener
	 */
	protected static ActionListener resetFace(final int maxLayers) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(yesNoPopupQuestion("Reset Face-Graphic?", "Confirmation", JOptionPane.QUESTION_MESSAGE, false) == JOptionPane.YES_OPTION) {
					fillDefaultImg(maxLayers);
					ImageConstruct.drawGUIbg(getFaceLayerImg());
				}
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener resetFaceSet() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(yesNoPopupQuestion("Reset FaceSet-Graphic?", "Confirmation", JOptionPane.QUESTION_MESSAGE, false) == JOptionPane.YES_OPTION) {
					ImageConstruct.fillVars(true);
					ImageConstruct.drawFaceSet();
				}
			}
		};
	}

	/**
	 *
	 * @param maxLayers layers of the face img
	 * @return ActionListener
	 */
	protected static ActionListener resetAll(final int maxLayers) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(yesNoPopupQuestion("Reset ALL Graphics?", "Confirmation", JOptionPane.QUESTION_MESSAGE, false) == JOptionPane.YES_OPTION) {
					// Reset Face
					fillDefaultImg(maxLayers);
					ImageConstruct.drawGUIbg(getFaceLayerImg());
					// Reset faceSet
					ImageConstruct.fillVars(true);
					ImageConstruct.drawFaceSet();
				}
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener about() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener close() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeQ();
			}
		};
	}

	/**
	 * ask on closing and exit the program
	 */
	private static void closeQ() {
		int selectedOption;

		if (UserPrefs.getProps().getProperty("AskOnExit", "1").equals("1")) {
			String buttons[] = {"Yes", "Yes, never ask me again!", "No"};
			selectedOption = JOptionPane.showOptionDialog(null, "Do you want to close the program ? (Unsaved Pictures will be lost!)", "Exit - Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons, buttons[2]);
		} else
			selectedOption = 0;

		if (selectedOption != 2 && selectedOption != JOptionPane.CLOSED_OPTION) {
			if (selectedOption == 1)
				UserPrefs.getProps().setProperty("AskOnExit", "0");

			// Remove tmp files before closing and save config
			FileOperations.cleanup();
			UserPrefs.onProgramExit();
			System.exit(0);
		}
	}

	/**
	 *
	 * @return WindowAdapter
	 */
	protected static WindowAdapter closeWindow() {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				closeQ();
			}
		};
	}

	/**
	 *
	 * @param jDialog dialog to close
	 * @return ActionListener
	 */
	protected static ActionListener closeThis(final JDialog jDialog) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Close window and remove it from memory
				jDialog.dispose();
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener settings() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// todo
			}
		};
	}

	/**
	 *
	 * @param layerId layerId of the button
	 * @return ActionListener
	 */
	protected static ActionListener removeButton(final int layerId) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					getFaceLayerImg().get(layerId).setVisible(false);
				} catch (Exception ex) {
					showWarning(ex);
				}

				// Refactor img
				ImageConstruct.drawGUIbg(getFaceLayerImg());
			}
		};
	}

	/**
	 *
	 * @param allowEmpty true/false allow to empty this layer
	 * @param files files of this layer
	 * @param layerId id of the current layer
	 * @param path path to the files
	 * @return ActionListener
	 */
	protected static ActionListener randomButton(final boolean allowEmpty, final ArrayList<String> files, final int layerId, final String path) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get random file number
				int random = generateRandom(files, path, allowEmpty, false);

				// Set random image
				try {
					if (random >= files.size())
						getFaceLayerImg().get(layerId).setVisible(false);
					else
						getFaceLayerImg().set(layerId, new LayerImg(path + Config.ds + files.get(random))).setVisible(true);
				} catch (Exception ex) {
					showWarning(ex);
				}

				// Refactor img
				ImageConstruct.drawGUIbg(getFaceLayerImg());
			}
		};
	}

	/**
	 *
	 * @param layerId id of the current layer
	 * @param file file of the current button
	 * @param path path to the file
	 * @return ActionListener
	 */
	protected static ActionListener layerButton(final int layerId, final String file, final String path) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					getFaceLayerImg().set(layerId, new LayerImg(path + Config.ds + file)).setVisible(true);
				} catch (Exception ex) {
					showWarning(ex);
				}

				// Refactor img
				ImageConstruct.drawGUIbg(getFaceLayerImg());
			}
		};
	}

	/**
	 *
	 * @param maxLayer layers of the picture
	 * @return ActionListener
	 */
	protected static ActionListener saveConfigDefaultImg(final int maxLayer) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (yesNoPopupQuestion("Set current Face to default-Face?", "Confirmation", JOptionPane.QUESTION_MESSAGE, false) == JOptionPane.YES_OPTION)
					propSetDefaultImg(maxLayer);
			}
		};
	}

	/**
	 *
	 * @param maxLayers layers of the picture
	 * @return ActionListener
	 */
	protected static ActionListener clearFace(final int maxLayers) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (yesNoPopupQuestion("Delete ALL Layers on the Face?", "Confirmation", JOptionPane.QUESTION_MESSAGE, false) == JOptionPane.YES_OPTION) {
					fillFaceArr(maxLayers);
					ImageConstruct.drawGUIbg(getFaceLayerImg());
				}
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener randomAll() {
		return  new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> typeFolders = FileOperations.getDirList(Config.resourceFolder);

				// Get and set random image for every layer
				for (String typeFolder : typeFolders) {
					int layerId = 0;
					boolean allowEmpty = true;
					boolean allowRandom = true;
					String path = Config.resourceFolder + Config.ds + typeFolder;
					// Get User-Config
					try {
						layerId = Integer.parseInt(UserPrefs.getProps().getProperty("Layer_ResSource_" + typeFolder, "0"));
						allowEmpty = Boolean.parseBoolean(UserPrefs.getProps().getProperty("AllowEmpty_Layer_ResSource_" + typeFolder, "true"));
						allowRandom = Boolean.parseBoolean(UserPrefs.getProps().getProperty("AllowRandom_Layer_ResSource_" + typeFolder, "true"));
					} catch (Exception ex) {
						showWarning(ex);
					}

					// Only set random image if its allowed and the directory is linked to a layer
					if (layerId > 0 && allowRandom) {
						ArrayList<String> files = FileOperations.getFileList(path);
						ArrayList<String> subTypeFolders = FileOperations.getDirList(path);

						// Get all files from subFolders
						for (String subTypeFolder : subTypeFolders) {
							ArrayList<String> subFiles = FileOperations.getFileList(path + Config.ds + subTypeFolder);
							for (String subFile : subFiles)
								files.add(subTypeFolder + Config.ds + subFile);
						}

						int random = generateRandom(files, path, allowEmpty, false);
						try {
							if (random >= files.size())
								getFaceLayerImg().get(layerId).setVisible(false);
							else
								getFaceLayerImg().set(layerId, new LayerImg(path + Config.ds + files.get(random)));
						} catch (Exception ex) {
							showWarning(ex);
						}
					}
				}

				// Refactor img after set all layers new
				ImageConstruct.drawGUIbg(getFaceLayerImg());
			}
		};
	}

	/**
	 *
	 * @param fileList list of files for this layer
	 * @param path path to the files of this layer
	 * @param allowEmpty allow to set the layer invisible
	 * @param all allow all fileTypes if false allow only images
	 * @return random integer value between 0 and (max fileIndex) (+1)
	 */
	private static int generateRandom(ArrayList<String> fileList, String path, boolean allowEmpty, boolean all) {
		int empty = 0;
		int random = 0;
		boolean done = false;

		if (allowEmpty)
			empty = 1;

		while (! done) {
			random = (int) (Math.random() * (fileList.size() + empty));

			if (random >= fileList.size())
				done = true;
			else {
				String fileType = FileOperations.getFileTyp(path + Config.ds + fileList.get(random));

				if (fileType.equals(".png") || fileType.equals(".bmp") || fileType.equals(".jpg") || fileType.equals(".jpeg") || all)
					done = true;
			}
		}

		return random;
	}

	/**
	 *
	 * @param count amount of layers that should be filled with empty pictures
	 */
	protected static void fillFaceArr(int count) {
		ArrayList<LayerImg> tmpArr = new ArrayList<LayerImg>();

		// Fill array with empty pictures
		for (int i = 0; i < count; i++) {
			tmpArr.add(new LayerImg(Config.systemFolder + Config.ds + Config.defaultEmpty));
		}

		setFaceLayerImg(tmpArr);
	}

	/**
	 *
	 * @param maxLayer layers of the picture
	 */
	protected static void fillDefaultImg(int maxLayer) {
		for (int i = 1; i < maxLayer; i++) {
			String picture = UserPrefs.getProps().getProperty("defaultImg_Layer_" + i, "");
			if (! picture.equals("") && FileOperations.checkFile(picture, false)) {
				getFaceLayerImg().get(i).setLayerImg(FileOperations.loadBImg(picture));
				getFaceLayerImg().get(i).setLayerPath(picture);
				getFaceLayerImg().get(i).setVisible(true);
			} else {
				getFaceLayerImg().get(i).setVisible(false);
				UserPrefs.getProps().setProperty("defaultImg_Layer_" + i, "");
			}
		}
	}

	/**
	 * sets the current picture to  the config as default
	 * @param maxLayer layers of the picture
	 */
	private static void propSetDefaultImg(int maxLayer) {

		for (int i = 1; i < maxLayer; i++) {
			if (getFaceLayerImg().get(i).isVisible())
				UserPrefs.getProps().setProperty("defaultImg_Layer_" + i, getFaceLayerImg().get(i).getLayerPath());
			else
				UserPrefs.getProps().setProperty("defaultImg_Layer_" + i, "");
		}
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener reloadResources() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//setButtonArea(generateButtonContainers(true)); todo
			}
		};
	}

	/**
	 *
	 * @param jDialog selected dialog
	 * @return WindowAdapter
	 */
	protected static WindowListener hideWindow(final JDialog jDialog) {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				jDialog.setVisible(false);
			}
		};
	}

	/**
	 *
	 * @param jDialog selected dialog
	 * @param file file for FaceSet window, can be null if not needed
	 * @return ActionListener
	 */
	protected static ActionListener showWindow(final JDialog jDialog, @Nullable final String file) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (file != null)
					setSelectedImg(file);

				if (! jDialog.isVisible())
					jDialog.setVisible(true);
			}
		};
	}

	/**
	 *
	 * @param file target file
	 */
	private static void setSelectedImg(String file) {
		setTmpSelectedImg(file);

		if (file.equals(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png")) {
			getSelectedPicture().setIcon(FileOperations.getIcon(file, 96, 96));
			return;
		}

		if (file.equals("")) {
			setTmpSelectedImg(Config.systemFolder + Config.ds + Config.defaultEmpty);
			getSelectedPicture().setIcon(FileOperations.getIcon(Config.systemFolder + Config.ds + "backgroundFace.png", 96, 96));
			return;
		}
		getSelectedPicture().setIcon(FileOperations.getIcon(ImageConstruct.GUITransparentBgImg(file, "tmpSelectedImg"), 96, 96));
	}

	/**
	 *
	 * @param file target file
	 * @return ActionListener
	 */
	protected static ActionListener setSelectedImgButton(final String file) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSelectedImg(file);
			}
		};
	}

	/**
	 *
	 * @param i index of the faceSet
	 * @return ActionListener
	 */
	protected static ActionListener addToSet(final int i) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getTmpSelectedImg().equals(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + "tmpFace.png")) {
					LayerImg.resetBgImg();
					ImageConstruct.refactorImg(getFaceLayerImg());
					FileOperations.saveGraphic(LayerImg.getBgImg(), Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + i + ".png");
					ImageConstruct.setFaceGraphic(FileOperations.loadBImg(Config.systemFolder + Config.ds + Config.tmpFolder + Config.ds + i + ".png"), i - 1);
				} else
					ImageConstruct.setFaceGraphic(FileOperations.loadBImg(getTmpSelectedImg()), i - 1);

				ImageConstruct.drawFaceSet();
			}
		};
	}

	/**
	 *
	 * @param i index of the faceSet
	 * @return ActionListener
	 */
	protected static ActionListener delFromSet(final int i) {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageConstruct.eraseFaceGraphic(i - 1);
				ImageConstruct.drawFaceSet();
			}
		};
	}

	/**
	 *
	 * @return ActionListener
	 */
	protected static ActionListener openTmpImage() {
		return new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = fileWindow("Open Image...", false);

				if (! file.equals(""))
					setSelectedImg(file);
			}
		};
	}
}
