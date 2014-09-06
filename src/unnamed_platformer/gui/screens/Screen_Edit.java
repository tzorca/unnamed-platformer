package unnamed_platformer.gui.screens;

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
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.InputEventType;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.other.Editor;
import unnamed_platformer.game.other.EntityCreator;
import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.InputRef.GameKey;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIHelper;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.gui.GUIManager.ScreenType;
import unnamed_platformer.gui.objects.ImageListEntry;
import unnamed_platformer.gui.objects.TreeCell_ImageRenderer;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.Graphic;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
public class Screen_Edit extends BaseScreen_Hybrid
{
	public static final int LEFT_TOOLBAR_SIZE = 160;

	private final Editor editor = new Editor(0);

	private final Map<ImageListEntry, Graphic> entityGraphics = new HashMap<ImageListEntry, Graphic>();

	private static final ImageIcon IMG_EDIT_MODE = ImageHelper
			.getImageIconContent("gui_modeEdit");
	private static final ImageIcon IMG_PLAY_MODE = ImageHelper
			.getImageIconContent("gui_modePlay");
	private static final ImageIcon IMG_ADD = ImageHelper
			.getImageIconContent("gui_add");
	private static final ImageIcon IMG_NEXT = ImageHelper
			.getImageIconContent("gui_next");
	private static final ImageIcon IMG_PREV = ImageHelper
			.getImageIconContent("gui_prev");
	private static final ImageIcon IMG_REMOVE = ImageHelper
			.getImageIconContent("gui_remove");
	private static final ImageIcon IMG_SAVE = ImageHelper
			.getImageIconContent("gui_save");

	private final List<ImageListEntry> imageListEntries = new ArrayList<ImageListEntry>();
	private final JTree treeEntities = new JTree(new DefaultMutableTreeNode());

	private final JLabel lblCurrentLevel = new JLabel();
	private final JButton btnModeSwitch = new JButton(IMG_PLAY_MODE);
	private final JButton btnPrevLevel = new JButton(IMG_PREV);
	private final JButton btnAddLevel = new JButton(IMG_ADD);
	private final JButton btnNextLevel = new JButton(IMG_NEXT);
	private final JButton btnRemoveLevel = new JButton(IMG_REMOVE);
	private final JButton btnSaveLevel = new JButton(IMG_SAVE);

	private final List<Component> editModeControls = new ArrayList<Component>();

	private transient boolean lastShiftState;

	public Screen_Edit() {
		super();

		editModeControls.addAll(Lists.newArrayList(treeEntities,
				lblCurrentLevel, btnPrevLevel, btnAddLevel, btnNextLevel,
				btnRemoveLevel, btnSaveLevel));
		loadEntityPlaceholderGraphics();

		setToolbarSizes();

		setupLeftToolbar();

		addCanvasListeners();
		setupTopToolbar();
	}

	private void setupTopToolbar() {
		final Panel topToolbar = toolbars.get(Side.top);
		final FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		topToolbar.setLayout(flowLayout);

		btnPrevLevel.addActionListener(new btnPrevLevel_Click());
		btnPrevLevel.setBackground(GUIManager.GUI_BG_COLOR);
		topToolbar.add(btnPrevLevel);

		lblCurrentLevel.setText("0");
		lblCurrentLevel.setForeground(GUIManager.GUI_FG_COLOR);
		lblCurrentLevel.setBorder(new EmptyBorder(8, 8, 8, 8));
		topToolbar.add(lblCurrentLevel);

		btnNextLevel.setBackground(GUIManager.GUI_BG_COLOR);
		btnNextLevel.addActionListener(new btnNextLevel_Click());
		topToolbar.add(btnNextLevel);

		btnAddLevel.setBackground(GUIManager.GUI_BG_COLOR);
		btnAddLevel.addActionListener(new btnAddLevel_Click());
		topToolbar.add(btnAddLevel);

		btnRemoveLevel.setBackground(GUIManager.GUI_BG_COLOR);
		btnRemoveLevel.addActionListener(new btnRemoveLevel_Click());
		topToolbar.add(btnRemoveLevel);

		btnSaveLevel.setBackground(GUIManager.GUI_BG_COLOR);
		btnSaveLevel.addActionListener(new btnSaveLevel_Click());
		topToolbar.add(btnSaveLevel);

		btnModeSwitch.setBackground(GUIManager.GUI_BG_COLOR);
		btnModeSwitch.addActionListener(new btnModeSwitch_Click());
		topToolbar.add(btnModeSwitch);

		GUIHelper.removeButtonPadding(Lists.newArrayList(btnPrevLevel,
				btnNextLevel, btnAddLevel, btnRemoveLevel, btnSaveLevel,
				btnModeSwitch));
		GUIHelper.styleButtons(Lists.newArrayList(btnPrevLevel, btnNextLevel,
				btnAddLevel, btnRemoveLevel, btnSaveLevel, btnModeSwitch), 0);
	}

	private void setToolbarSizes() {
		setToolbarSize(Side.left, LEFT_TOOLBAR_SIZE);
		setToolbarSize(Side.right, 0);
		setToolbarSize(Side.top, 36);
		setToolbarSize(Side.bottom, 0);
	}

	private void setupLeftToolbar() {

		final Panel leftToolbar = toolbars.get(Side.left);
		leftToolbar.setLayout(new BorderLayout());
		setupEntityJTree(leftToolbar);

		treeEntities
				.addTreeSelectionListener(new TreeEntities_SelectionChanged());
	}

	private void setupEntityJTree(final Panel toolbar) {
		try {
			final DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeEntities
					.getModel().getRoot();

			treeEntities.setCellRenderer(new TreeCell_ImageRenderer());
			treeEntities.setBackground(toolbar.getBackground());

			final Map<String, DefaultMutableTreeNode> createdCategories = new HashMap<String, DefaultMutableTreeNode>();

			for (final ImageListEntry entry : imageListEntries) {
				final String internalName = entry.getInternalName();

				// get class name for top-level category
				final Class<?> clazz = EntityRef
						.getEntityClassFromTextureName(internalName);
				final String categoryName = clazz == null ? "misc" : clazz
						.getSimpleName();

				// add category node to top-level (if not already there)
				final DefaultMutableTreeNode categoryNode;
				if (createdCategories.containsKey(categoryName)) {
					categoryNode = createdCategories.get(categoryName);
				} else {
					categoryNode = new DefaultMutableTreeNode(
							new ImageListEntry(IMG_ADD, categoryName));
					root.add(categoryNode);
					createdCategories.put(categoryName, categoryNode);
				}

				// add entry to categoryNode
				categoryNode.add(new DefaultMutableTreeNode(entry));
			}

			treeEntities.expandPath(new TreePath(root.getPath()));
			treeEntities.setRootVisible(false);

			final JScrollPane treeScroller = new JScrollPane(treeEntities,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			toolbar.add(treeScroller, BorderLayout.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addCanvasListeners() {
		InputManager.setEventHandler(InputEventType.leftClick,
				new RenderCanvas_LeftClick());
		InputManager.setEventHandler(InputEventType.rightClick,
				new RenderCanvas_RightClick());
	}

	public Graphic getCurrentGraphic() {
		return entityGraphics.get(getSelectedEntry());
	}

	private void loadEntityPlaceholderGraphics() {

		final List<String> textureNames = new ArrayList<String>(
				EntityCreator.listTextureNames());
		Collections.sort(textureNames);
		for (final String textureName : textureNames) {

			final String displayName = ResManager.humanizeName(textureName);
			final ImageIcon imageIcon = ImageHelper
					.getImageIconContentScaleDown(textureName, 48);

			final ImageListEntry entry = new ImageListEntry(imageIcon,
					displayName, textureName);
			imageListEntries.add(entry);
			entityGraphics.put(entry, new Graphic(textureName,
					Ref.COLOR_75_PERCENT_TRANS));
		}
	}

	public void update() {
		final boolean currentlyEditing = !World.playing();

		if (currentlyEditing) {
			editor.update();
			processControls();
		}

		for (final Component editModeComponent : editModeControls) {
			try {
				if (editModeComponent.isVisible() != currentlyEditing) {
					editModeComponent.setVisible(currentlyEditing);
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

	private void processMultipaintControls() {
		final boolean shiftState = InputManager.isShiftHeld();
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
		if (World.playing()) {
			return;
		}

		ViewManager.drawGrid(editor.gridSize, Color.lightGray);
	}

	public void drawForeground() {
		if (World.playing()) {
			return;
		}

		final Graphic entityPlaceholderGraphic = getCurrentGraphic();

		if (entityPlaceholderGraphic == null) {
			return;
		}

		final Texture texture = entityPlaceholderGraphic.getTexture();
		final Level currentLevel = World.getCurrentLevel();

		final Rectangle2D levelRect = new Rectangle2D.Float(currentLevel
				.getRect().getX(), currentLevel.getRect().getY(), currentLevel
				.getRect().getWidth(), currentLevel.getRect().getHeight());

		final List<Vector2f> drawLocations = editor.getPaintDrawLocations(
				texture.getImageWidth(), texture.getImageHeight());

		for (final Vector2f drawLocation : drawLocations) {

			final Rectangle2D drawRect = new Rectangle2D.Float(drawLocation.x,
					drawLocation.y, texture.getImageWidth(),
					texture.getImageHeight());

			if (levelRect.contains(drawRect)) {
				ViewManager.drawGraphic(
						entityPlaceholderGraphic,
						new Rectangle((float) drawRect.getX(), (float) drawRect
								.getY(), (float) drawRect.getWidth(),
								(float) drawRect.getHeight()));
			} else {
				ViewManager.setColor(Ref.COLOR_75_PERCENT_TRANS);
			}
		}
	}

	private void updateCurrentLevelLabel() {
		lblCurrentLevel.setText(String.valueOf(World.getCurrentLevelIndex()));
	}

	// ===============================================================================
	// Event Handlers
	// ===============================================================================

	public boolean onFinish(final ScreenType plannedNextScreen) {

		if (!editor.unsavedChangesExist()) {
			return true;
		}

		if (plannedNextScreen == ScreenType.Transition) {
			return true;
		}

		// this has to be done with a callback
		// (a deadlock occurs if not invoked with SwingUtils.invokeLater)
		GUIHelper
				.confirmDangerousWithCallback(
						"Are you sure you want to exit? Your game has unsaved changes.",
						new ParamRunnable() {
							public void run(final Object param) {
								if (!(boolean) param) {
									return;
								}
								editor.overrideSavedChanges();
								GUIManager.changeScreen(plannedNextScreen);
							}
						});
		return false;
	}

	private ImageListEntry getSelectedEntry() {
		if (treeEntities.isSelectionEmpty()) {
			return null;
		}

		final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeEntities
				.getSelectionPath().getLastPathComponent();
		final ImageListEntry entry = (ImageListEntry) selectedNode
				.getUserObject();

		return entry;
	}

	private class RenderCanvas_LeftClick implements Runnable
	{
		public void run() {
			editor.placeObject(InputManager.getGameMousePos(),
					getSelectedEntry());
		}
	}

	private class RenderCanvas_RightClick implements Runnable
	{
		public void run() {
			editor.removeObject(InputManager.getGameMousePos());
		}
	}

	private class TreeEntities_SelectionChanged implements
			TreeSelectionListener
	{
		@Override
		public void valueChanged(final TreeSelectionEvent event) {
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnModeSwitch_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			toggleEditMode();
		}
	}

	private class btnAddLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			World.addBlankLevel();
			editor.changeLevel(World.getLevelCount() - 1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnNextLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			editor.levelInc(1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnPrevLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			editor.levelInc(-1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnRemoveLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			editor.removeLevel();
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnSaveLevel_Click implements ActionListener
	{
		public void actionPerformed(final ActionEvent event) {
			editor.save();
			ViewManager.focusRenderCanvas();
		}
	}

	public void toggleEditMode() {
		if (World.playing()) {
			editor.switchToEditMode();
			btnModeSwitch.setIcon(IMG_PLAY_MODE);
			setToolbarSize(Side.left, LEFT_TOOLBAR_SIZE);
		} else {

			editor.switchToPlayMode();
			editor.setCameraPos(World.getCurrentLevel()
					.findEntityByFlag(Flag.PLAYER).getPos());
			btnModeSwitch.setIcon(IMG_EDIT_MODE);
			setToolbarSize(Side.left, 0);
		}

		ViewManager.focusRenderCanvas();
	}

}
