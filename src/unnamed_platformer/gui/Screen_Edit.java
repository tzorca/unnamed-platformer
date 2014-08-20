package unnamed_platformer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.InputEventType;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.Editor;
import unnamed_platformer.game.EntityCreator;
import unnamed_platformer.game.Level;
import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.InputRef.GameKey;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIManager.ScreenType;
import unnamed_platformer.gui.objects.ImageListEntry;
import unnamed_platformer.gui.objects.TreeCellImageRenderer;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.Graphic;
import unnamed_platformer.structures.ParamRunnable;

public class Screen_Edit extends BaseScreen_Hybrid {
	public static final int LEFT_TOOLBAR_SIZE = 160;

	Editor editor = new Editor(0);

	Map<ImageListEntry, Graphic> entityPlaceholderGraphics = new HashMap<ImageListEntry, Graphic>();
	ImageIcon imgEditMode = ImageHelper.getImageIconContent("gui_modeEdit"), imgPlayMode = ImageHelper
			.getImageIconContent("gui_modePlay"), imgAdd = ImageHelper.getImageIconContent("gui_add"),
			imgNext = ImageHelper.getImageIconContent("gui_next"), imgPrev = ImageHelper
					.getImageIconContent("gui_prev"), imgRemove = ImageHelper.getImageIconContent("gui_remove"),
			imgSave = ImageHelper.getImageIconContent("gui_save");

	List<ImageListEntry> imageListEntries = new ArrayList<ImageListEntry>();
	JTree treeEntities = new JTree(new DefaultMutableTreeNode());

	JLabel lblCurrentLevel = new JLabel();
	JButton btnModeSwitch = new JButton(imgPlayMode), btnPrevLevel = new JButton(imgPrev), btnAddLevel = new JButton(
			imgAdd), btnNextLevel = new JButton(imgNext), btnRemoveLevel = new JButton(imgRemove),
			btnSaveLevel = new JButton(imgSave);
	List<Component> ctlList_EditMode = new ArrayList<Component>();

	// Instance Initializer
	{
		// Misc
		Ref.multiadd(ctlList_EditMode, new Component[] {
				treeEntities, lblCurrentLevel, btnPrevLevel, btnAddLevel, btnNextLevel, btnRemoveLevel, btnSaveLevel
		});
		loadEntityPlaceholderGraphics();

		setToolbarSizes();

		// Left Toolbar : Setup
		setupLeftToolbar();

		addCanvasListeners();
		setupTopToolbar();
	}

	private void setupTopToolbar() {
		Panel topToolbar = toolbars.get(Side.top);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		topToolbar.setLayout(flowLayout);
		GUIHelper.removeButtonPadding(btnPrevLevel);
		btnPrevLevel.addActionListener(new btnPrevLevel_Click());
		topToolbar.add(btnPrevLevel);
		lblCurrentLevel.setText("0");
		lblCurrentLevel.setBorder(new EmptyBorder(8, 8, 8, 8));
		topToolbar.add(lblCurrentLevel);
		GUIHelper.removeButtonPadding(btnNextLevel);
		btnNextLevel.addActionListener(new btnNextLevel_Click());
		topToolbar.add(btnNextLevel);
		GUIHelper.removeButtonPadding(btnAddLevel);
		btnAddLevel.addActionListener(new btnAddLevel_Click());
		topToolbar.add(btnAddLevel);
		GUIHelper.removeButtonPadding(btnRemoveLevel);
		btnRemoveLevel.addActionListener(new btnRemoveLevel_Click());
		topToolbar.add(btnRemoveLevel);
		GUIHelper.removeButtonPadding(btnSaveLevel);
		btnSaveLevel.addActionListener(new btnSaveLevel_Click());
		topToolbar.add(btnSaveLevel);
		GUIHelper.removeButtonPadding(btnModeSwitch);
		btnModeSwitch.addActionListener(new btnModeSwitch_Click());
		topToolbar.add(btnModeSwitch);
	}

	private void setToolbarSizes() {
		setToolbarSize(Side.left, LEFT_TOOLBAR_SIZE);
		setToolbarSize(Side.right, 0);
		setToolbarSize(Side.top, 36);
		setToolbarSize(Side.bottom, 0);
	}

	private void setupLeftToolbar() {

		Panel leftToolbar = toolbars.get(Side.left);
		leftToolbar.setLayout(new BorderLayout());
		setupEntityJTree(leftToolbar);

		treeEntities.addTreeSelectionListener(new TreeEntities_SelectionChanged());
	}

	private void setupEntityJTree(Panel toolbar) {
		try {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeEntities.getModel().getRoot();

			treeEntities.setCellRenderer(new TreeCellImageRenderer());
			treeEntities.setBackground(toolbar.getBackground());

			Map<String, DefaultMutableTreeNode> createdCategories = new HashMap<String, DefaultMutableTreeNode>();

			for (ImageListEntry entry : imageListEntries) {
				String internalName = entry.getInternalName();

				// get class name for top-level category
				Class<?> clazz = EntityRef.getEntityClassFromTextureName(internalName);
				String categoryName = clazz != null ? clazz.getSimpleName() : "misc";

				// add category node to top-level (if not already there)
				DefaultMutableTreeNode categoryNode;
				if (!createdCategories.containsKey(categoryName)) {
					categoryNode = new DefaultMutableTreeNode(new ImageListEntry(imgAdd, categoryName));
					root.add(categoryNode);
					createdCategories.put(categoryName, categoryNode);
				} else {
					categoryNode = createdCategories.get(categoryName);
				}

				// add entry to categoryNode
				categoryNode.add(new DefaultMutableTreeNode(entry));
			}

			treeEntities.expandPath(new TreePath(root.getPath()));
			treeEntities.setRootVisible(false);

			JScrollPane treeScroller = new JScrollPane(treeEntities, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			toolbar.add(treeScroller, BorderLayout.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addCanvasListeners() {
		InputManager.setEventHandler(InputEventType.leftClick, new RenderCanvas_LeftClick());
		InputManager.setEventHandler(InputEventType.rightClick, new RenderCanvas_RightClick());
	}

	public Graphic getCurrentGraphic() {
		return entityPlaceholderGraphics.get(getSelectedEntry());
	}

	private void loadEntityPlaceholderGraphics() {

		List<String> internalTextureNames = new ArrayList<String>(EntityCreator.listTextureNames());
		Collections.sort(internalTextureNames);
		for (String internalTextureName : internalTextureNames) {

			String displayName = ResManager.humanizeName(internalTextureName);
			ImageIcon imageIcon = ImageHelper.getImageIconContentScaleDown(internalTextureName, 48);

			ImageListEntry entry = new ImageListEntry(imageIcon, displayName, internalTextureName);
			imageListEntries.add(entry);
			entityPlaceholderGraphics.put(entry, new Graphic(internalTextureName, Ref.COLOR_75_PERCENT_TRANS));
		}
	}

	public void update() {
		boolean editing = !GameManager.playing();

		if (editing) {
			editor.update();
			processControls();
		}

		for (Component editModeComponent : ctlList_EditMode) {
			try {
				if (editModeComponent.isVisible() != editing) {
					editModeComponent.setVisible(editing);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(editModeComponent);
			}
		}
	}

	private void processControls() {
		processCameraControls();
		processGridControls();
		processMultipaintControls();
	}

	private boolean lastShiftState = false;

	private void processMultipaintControls() {
		boolean shiftState = InputManager.isShiftHeld();
		if (shiftState && !lastShiftState) {
			editor.startMultiselect(InputManager.getGameMousePos());
		} else if (!shiftState && lastShiftState) {
			editor.exitMultiselect();
		}
		lastShiftState = shiftState;
	}

	private void processGridControls() {

		int newGridSize = editor.gridSize;
		if (InputManager.getGameKeyState(GameKey.scrollIn, 1)) {
			newGridSize /= 2;
			InputManager.resetGameKey(GameKey.scrollIn, 1);

		} else if (InputManager.getGameKeyState(GameKey.scrollOut, 1)) {
			newGridSize *= 2;
			InputManager.resetGameKey(GameKey.scrollOut, 1);
		}

		if (newGridSize >= 8 && newGridSize <= 128) {
			editor.gridSize = newGridSize;
		}
	}

	private void processCameraControls() {
		Vector2f cameraDelta = new Vector2f(0, 0);

		cameraDelta.x -= InputManager.getKeyState(Keyboard.KEY_LEFT) ? 8 : 0;
		cameraDelta.x += InputManager.getKeyState(Keyboard.KEY_RIGHT) ? 8 : 0;
		cameraDelta.y -= InputManager.getKeyState(Keyboard.KEY_UP) ? 8 : 0;
		cameraDelta.y += InputManager.getKeyState(Keyboard.KEY_DOWN) ? 8 : 0;

		editor.tryMoveCamera(cameraDelta);
	}

	public void drawBackground() {
		if (GameManager.playing()) {
			return;
		}
		ViewManager.drawEditorGrid(editor.gridSize);
	}

	public void drawForeground() {
		if (GameManager.playing()) {
			return;
		}

		Graphic entityPlaceholderGraphic = getCurrentGraphic();

		if (entityPlaceholderGraphic == null) {
			return;
		}

		Texture t = entityPlaceholderGraphic.getTexture();
		Level currentLevel = GameManager.getCurrentLevel();

		Rectangle2D levelRect = new Rectangle2D.Float(currentLevel.getRect().getX(), currentLevel.getRect().getY(),
				currentLevel.getRect().getWidth(), currentLevel.getRect().getHeight());

		List<Vector2f> drawLocations = editor.getPaintDrawLocations(t.getImageWidth(), t.getImageHeight());

		for (Vector2f drawLocation : drawLocations) {

			Rectangle2D drawRect = new Rectangle2D.Float(drawLocation.x, drawLocation.y, t.getImageWidth(),
					t.getImageHeight());

			if (levelRect.contains(drawRect)) {
				ViewManager.drawGraphic(entityPlaceholderGraphic, drawRect);
			} else {
				ViewManager.setColor(Ref.COLOR_75_PERCENT_TRANS);
			}
		}
	}

	private void updateCurrentLevelLabel() {
		lblCurrentLevel.setText(GameManager.getCurrentLevelNumber() + "");
	}

	// ===============================================================================
	// Event Handlers
	// ===============================================================================

	public boolean onFinish(final ScreenType plannedNextScreen) {
		if (editor.unsavedChangesExist()) {
			// this has to be done with a callback
			// (a deadlock occurs if not invoked with SwingUtils.invokeLater)
			GUIHelper.confirmDangerousWithCallback("Are you sure you want to exit? Your game has unsaved changes.",
					new ParamRunnable() {
						public void run(Object param) {
							if ((boolean) param) {
								editor.overrideSavedChanges();
								GUIManager.changeScreen(plannedNextScreen);
							}
						}
					});
			return false;
		}
		return true;
	}

	private ImageListEntry getSelectedEntry() {
		if (treeEntities.isSelectionEmpty())
			return null;

		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeEntities.getSelectionPath()
				.getLastPathComponent();
		ImageListEntry entry = (ImageListEntry) selectedNode.getUserObject();

		return entry;
	}

	private String getSelectedTexture() {
		ImageListEntry entry = getSelectedEntry();

		String textureName = entry.getInternalName();

		return EntityRef.textureMapped(textureName) ? textureName : null;
	}

	private class RenderCanvas_LeftClick implements Runnable {
		public void run() {
			editor.placeObject(InputManager.getGameMousePos(), getSelectedEntry());
		}
	}

	private class RenderCanvas_RightClick implements Runnable {
		public void run() {
			editor.removeObject(InputManager.getGameMousePos());
		}
	}

	private class TreeEntities_SelectionChanged implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnModeSwitch_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!GameManager.playing()) {
				editor.switchToPlayMode();
				btnModeSwitch.setIcon(imgEditMode);
				setToolbarSize(Side.left, 0);
			} else {
				editor.switchToEditMode();
				btnModeSwitch.setIcon(imgPlayMode);
				setToolbarSize(Side.left, LEFT_TOOLBAR_SIZE);
			}
			
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnAddLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			GameManager.addBlankLevel();
			editor.changeLevel(GameManager.getLevelCount() - 1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnNextLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.levelInc(1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnPrevLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.levelInc(-1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnRemoveLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.removeLevel();
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnSaveLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.resetToEditPlacement();
			GameManager.saveCurrentGame();
			ViewManager.focusRenderCanvas();
		}
	}

}
