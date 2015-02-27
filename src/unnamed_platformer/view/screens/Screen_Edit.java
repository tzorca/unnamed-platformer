package unnamed_platformer.view.screens;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.Settings;
import unnamed_platformer.app.Settings.SettingName;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.content_management.ContentManager;
import unnamed_platformer.game.editor.CategoryLookup;
import unnamed_platformer.game.editor.Editor;
import unnamed_platformer.game.editor.EntityCreator;
import unnamed_platformer.game.zones.Level;
import unnamed_platformer.game.zones.World;
import unnamed_platformer.globals.FileGlobals;
import unnamed_platformer.globals.StyleGlobals;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.MouseInputManager;
import unnamed_platformer.input.MouseInputManager.MouseEventType;
import unnamed_platformer.view.GUIHelper.ParamRunnable;
import unnamed_platformer.view.GUIManager;
import unnamed_platformer.view.GUIManager.ScreenType;
import unnamed_platformer.view.Graphic;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.dialogs.Dialog_EditMenu;
import unnamed_platformer.view.dialogs.Dialog_OptionSelection;
import unnamed_platformer.view.objects.ImageListEntry;
import unnamed_platformer.view.objects.ListCellRenderer_ImageListEntry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Screen_Edit extends BaseScreen_Hybrid
{
	// TODO: Make it more obvious the user can scroll past the edges of the
	// current screen

	public static final int LEFT_TOOLBAR_SIZE = 212;
	public static final int ENTITY_ICON_SIZE = 36;
	public static final int CURSOR_SIZE = 48;
	private static final String NAV_TIME_PERIOD_STRING = "EditorLevelNavigation";
	private static final float NAV_RATE = 0.01f;
	private static final int NAV_SPEED = 6;
	private static final float NAV_EDGE_SPEED_MULTIPLIER = 2f;

	private final Editor editor = new Editor(0);

	private final Map<String, Graphic> entityGraphics = new HashMap<String, Graphic>();

	private final Map<String, DefaultListModel<ImageListEntry>> categoryMap = Maps
			.newHashMap();

	private final DefaultListModel<String>
	/* */lstCategoriesModel = new DefaultListModel<String>();

	private JList<String> lstCategories = new JList<String>();
	private JList<ImageListEntry> lstEntities = new JList<ImageListEntry>();
	JScrollPane scrlLstCategories, scrlLstEntities;
	private final List<Component> editModeComponents = new ArrayList<Component>();

	private transient boolean lastMultiselectState;
	private Rectangle cursorRect;
	private Graphic cursorGraphic = new Graphic("crosshair");

	public Screen_Edit() {
		super();

		// SETUP LEFT TOOLBAR
		setToolbarSizes(LEFT_TOOLBAR_SIZE, 0, 0, 0);
		setupLeftToolbarComponents();
		Panel leftToolbar = toolbars.get(Side.left);
		leftToolbar.setLayout(new GridLayout(1, 2));
		leftToolbar.add(scrlLstCategories);
		leftToolbar.add(scrlLstEntities);

		// ADD CANVAS LISTENERS
		MouseInputManager.setEventHandler(MouseEventType.leftClick,
				new RenderCanvas_LeftClick());
		MouseInputManager.setEventHandler(MouseEventType.rightClick,
				new RenderCanvas_RightClick());

		// INITIALIZE CURSOR
		initCursor();

		// FOCUS ON PLAYER INITIALLY
		editor.focusOnPlayer();

		// ADD EDIT MODE COMPONENTS
		editModeComponents.addAll(Lists.newArrayList(scrlLstCategories,
				scrlLstEntities));
	}

	private void setupLeftToolbarComponents() {
		// SETUP LIST APPEARANCE
		lstEntities.setCellRenderer(new ListCellRenderer_ImageListEntry(
				ENTITY_ICON_SIZE));
		StyleGlobals.STYLE_ENTITY_LIST.apply(lstEntities);
		StyleGlobals.STYLE_ENTITY_LIST.apply(lstCategories);

		// SETUP LIST MODELS
		for (String category : CategoryLookup.listCategories()) {
			lstCategoriesModel.addElement(category);
		}
		lstCategories.setModel(lstCategoriesModel);
		loadCategoryTextures(lstCategoriesModel);
		lstEntities.setModel(categoryMap.get(lstCategoriesModel.get(0)));

		// SETUP SCROLLBARS
		scrlLstCategories = new JScrollPane(lstCategories,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrlLstEntities = new JScrollPane(lstEntities,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		lstEntities.addListSelectionListener(new EntityList_SelectionChanged());
		lstCategories
				.addListSelectionListener(new CategoryList_SelectionChanged());
	}

	private void loadCategoryTextures(DefaultListModel<String> categories) {
		Collection<String> textureNames = EntityCreator.listTextureNames();
		List<ImageListEntry> tempImageListEntryList = new ArrayList<ImageListEntry>();
		for (final String textureName : textureNames) {
			tempImageListEntryList.add(getImageListEntry(textureName));
			entityGraphics.put(textureName, /* */
					new Graphic(textureName, new Color(1, 1, 1, 0.75f)));
		}

		Collections.sort(tempImageListEntryList);

		for (ImageListEntry imageListEntry : tempImageListEntryList) {
			String category = CategoryLookup.getCategory(imageListEntry
					.getInternalName());

			if (!categoryMap.containsKey(category)) {
				categoryMap.put(category,
						new DefaultListModel<ImageListEntry>());
			}
			categoryMap.get(category).addElement(imageListEntry);
		}
	}

	private ImageListEntry getImageListEntry(String textureName) {
		String displayName = ContentManager.humanizeName(textureName);
		BufferedImage originalImage = ContentManager.get(BufferedImage.class,
				FileGlobals.IMG_OBJ_DIR, textureName);

		ImageIcon imageIcon = new ImageIcon(ImageHelper.scaleWidth(
				originalImage, ENTITY_ICON_SIZE, BufferedImage.SCALE_SMOOTH));

		return new ImageListEntry(imageIcon, displayName, textureName);
	}

	public Graphic getCurrentGraphic() {
		if (getSelectedEntityEntry() == null) {
			return null;
		}

		String textureName = getSelectedEntityEntry().getInternalName();
		if (textureName == null) {
			return null;
		}
		return entityGraphics.get(textureName);
	}

	public void update() {
		final boolean currentlyEditing = !World.playing();

		if (currentlyEditing) {
			editor.update();
			handleUserInput();
			keepCursorInView();
		}

		// Can display edit mode dialog while playtesting too
		if (InputManager.keyPressOccurred(GameKey.START, 1)) {
			Dialog_EditMenu dialogEditMenu = new Dialog_EditMenu(
					ViewManager.getFrame(), editor, this);

			GUIManager.showDialog(dialogEditMenu);
		}

		for (final Component editModeComponent : editModeComponents) {
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

	private void handleUserInput() {
		handleNavigationControls();
		handleGridControls();
		handleEntitySelectionControls();
		handlePaintControls();
	}

	private boolean categoryListIsCurrent = false;

	private void handleEntitySelectionControls() {
		// Check which of category list and entity list is current
		// to determine which list to operate on
		JList<?> currentList = categoryListIsCurrent ? lstCategories
				: lstEntities;
		JList<?> otherList = categoryListIsCurrent ? lstEntities
				: lstCategories;

		int currentIndex = currentList.getSelectedIndex();
		int nextIndex = Math.max(currentIndex, 0);

		if (InputManager.keyPressOccurredOrRepeating(GameKey.SECONDARY_LEFT, 1)
				|| InputManager.keyPressOccurredOrRepeating(
						GameKey.SECONDARY_RIGHT, 1)) {
			categoryListIsCurrent = !categoryListIsCurrent;
		} else if (InputManager.keyPressOccurredOrRepeating(
				GameKey.SECONDARY_UP, 1)) {
			nextIndex = currentIndex - 1;
		} else if (InputManager.keyPressOccurredOrRepeating(
				GameKey.SECONDARY_DOWN, 1)) {
			nextIndex = currentIndex + 1;
		}

		// Wrap around
		if (nextIndex >= currentList.getModel().getSize()) {
			nextIndex = 0;
		} else if (nextIndex < 0) {
			nextIndex = currentList.getModel().getSize() - 1;
		}

		currentList.setBackground(StyleGlobals.COLOR_MAIN);
		otherList.setBackground(StyleGlobals.COLOR_MAIN_HIGHLIGHT.darker());
		currentList.ensureIndexIsVisible(nextIndex);
		currentList.setSelectedIndex(nextIndex);
	}

	private void handlePaintControls() {
		// Multi-select
		final boolean multiselectState = InputManager.keyIsPressed(
				GameKey.MULTI_SELECT, 1);
		if (multiselectState && !lastMultiselectState) {
			editor.startMultiselect(cursorRect.getLocation());
		} else if (!multiselectState && lastMultiselectState) {
			editor.exitMultiselect();
		}
		lastMultiselectState = multiselectState;

		// Place or remove objects
		if (InputManager.keyPressOccurred(GameKey.A, 1)) {
			editor.placeObject(cursorRect.getLocation(),
					getSelectedEntityEntry());

		} else if (InputManager.keyPressOccurred(GameKey.B, 1)) {
			editor.removeObject(cursorRect.getLocation());
		}

	}

	private void handleGridControls() {
		int newGridSize = editor.gridSize;
		if (InputManager.keyPressOccurred(GameKey.SCROLL_IN, 1)) {
			newGridSize /= 2;

		} else if (InputManager.keyPressOccurred(GameKey.SCROLL_OUT, 1)) {
			newGridSize *= 2;
		}

		if (newGridSize >= 8 && newGridSize <= 128) {
			editor.gridSize = newGridSize;
		}
	}

	// ===============================================================================
	// DRAWING
	// ===============================================================================

	@Override
	public void drawBackground() {
		if (World.playing()) {
			return;
		}

		ViewManager.drawGrid(editor.gridSize, Color.lightGray);
	}

	@Override
	public void drawForeground() {
		if (World.playing()) {
			return;
		}

		drawEntityPlaceHolder();
		drawCursor();
	}

	private void drawEntityPlaceHolder() {
		final Graphic entityPlaceholderGraphic = getCurrentGraphic();

		if (entityPlaceholderGraphic == null) {
			return;
		}

		final Texture texture = entityPlaceholderGraphic.getTexture();
		final Level currentLevel = World.getCurrentLevel();

		final Rectangle2D levelRect = new Rectangle2D.Float(currentLevel
				.getRect().getX(), currentLevel.getRect().getY(), currentLevel
				.getRect().getWidth(), currentLevel.getRect().getHeight());

		Vector2f startPos = MathHelper.snapToGrid(cursorRect.getLocation(),
				editor.gridSize);

		final List<Vector2f> drawLocations = editor.getPotentialPaintLocations(
				startPos, texture.getImageWidth(), texture.getImageHeight());

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
				ViewManager.setColor(new Color(1, 1, 1, 0.75f));
			}
		}
	}

	private void drawCursor() {
		Rectangle cursorDrawRect = new Rectangle(cursorRect.getX()
				- cursorRect.getWidth() / 2, cursorRect.getY()
				- cursorRect.getHeight() / 2, cursorRect.getWidth(),
				cursorRect.getHeight());

		ViewManager.drawGraphic(cursorGraphic, cursorDrawRect);
	}

	// ===============================================================================
	// Level Navigation
	// ===============================================================================

	private void initCursor() {
		int x = (int) (editor.getCameraPos().x - LEFT_TOOLBAR_SIZE);
		int y = (int) (editor.getCameraPos().y);
		cursorRect = new Rectangle(x, y, CURSOR_SIZE, CURSOR_SIZE);
	}

	private void keepCursorInView() {
		// if cursor got lost, put it back in the center
		if (!cursorRect.intersects(getNavigationBounds())) {
			cursorRect.setCenterX(ViewManager.getViewport().getCenterX());
			cursorRect.setCenterY(ViewManager.getViewport().getCenterY());
		}

	}

	private Rectangle getNavigationBounds() {
		Rectangle rect = ViewManager.getViewport();
		rect.setWidth(rect.getWidth() - LEFT_TOOLBAR_SIZE);

		return rect;
	}

	private void handleNavigationControls() {
		if (!TimeManager.periodElapsed(this, NAV_TIME_PERIOD_STRING, NAV_RATE)) {
			return;
		}

		Vector2f cursorDelta = new Vector2f(0, 0);
		if (InputManager.keyIsPressed(GameKey.LEFT, 1)) {
			cursorDelta.x -= NAV_SPEED;
		}

		if (InputManager.keyIsPressed(GameKey.RIGHT, 1)) {
			cursorDelta.x += NAV_SPEED;
		}

		if (InputManager.keyIsPressed(GameKey.UP, 1)) {
			cursorDelta.y -= NAV_SPEED;
		}

		if (InputManager.keyIsPressed(GameKey.DOWN, 1)) {
			cursorDelta.y += NAV_SPEED;
		}

		Vector2f originalPos = cursorRect.getLocation();
		Vector2f newPos = cursorRect.getLocation();
		newPos.add(cursorDelta);
		newPos = MathHelper.snapToGrid(newPos, NAV_SPEED);

		cursorRect.setLocation(newPos);

		if (MathHelper.rectExitingOrOutsideRect(cursorRect,
				getNavigationBounds())) {
			// Scroll across level faster than inside frame
			Vector2f cursorEdgeDelta = cursorDelta
					.scale(NAV_EDGE_SPEED_MULTIPLIER);

			newPos = originalPos.add(cursorEdgeDelta);
			newPos = MathHelper.snapToGrid(newPos, NAV_SPEED);
			cursorRect.setLocation(newPos);

			editor.moveCamera(cursorEdgeDelta);
			editor.update();
		}
	}

	// ===============================================================================
	// Event Handlers
	// ===============================================================================

	public boolean onFinish(final ScreenType plannedNextScreen) {
		if (Settings.getBoolean(SettingName.AUTO_SAVE_ON_EXIT)) {
			editor.save();
			return true;
		}

		if (!editor.unsavedChangesExist()) {
			return true;
		}

		if (plannedNextScreen == ScreenType.Transition) {
			return true;
		}

		showExitQuestionDialog(plannedNextScreen);

		return false;
	}

	private void showExitQuestionDialog(final ScreenType plannedNextScreen) {
		String message = "Really exit? You haven't saved your last changes.";
		final String cancelText = "Cancel";
		final List<String> choices = Lists.newArrayList("Exit", cancelText);

		// This has to be done with a callback
		// (A deadlock occurs if not invoked with SwingUtils.invokeLater)
		final ParamRunnable afterChoice = new ParamRunnable() {
			public void run(Object param) {
				final String choice = (String) param;

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (choice.equals(cancelText)) {
							return;
						}
						editor.overrideSavedChanges();
						GUIManager.changeScreen(plannedNextScreen);
					}
				});

			}
		};

		Dialog_OptionSelection<String> dlgConfirmExit = new Dialog_OptionSelection<String>(
				ViewManager.getFrame(), message, choices, cancelText,
				afterChoice);

		dlgConfirmExit.setVisible(true);
	}

	private ImageListEntry getSelectedEntityEntry() {
		if (lstEntities.isSelectionEmpty()) {
			return null;
		}

		return lstEntities.getSelectedValue();
	}

	private class RenderCanvas_LeftClick implements Runnable
	{
		public void run() {
			editor.placeObject(MouseInputManager.getGameMousePos(),
					getSelectedEntityEntry());
		}
	}

	private class RenderCanvas_RightClick implements Runnable
	{
		public void run() {
			editor.removeObject(MouseInputManager.getGameMousePos());
		}
	}

	private class EntityList_SelectionChanged implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			ViewManager.focusRenderCanvas();
		}
	}

	public class CategoryList_SelectionChanged implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			ViewManager.focusRenderCanvas();

			lstEntities.setModel(categoryMap.get(lstCategories
					.getSelectedValue()));
		}
	}

	public void toggleEditMode() {
		if (World.playing()) {
			editor.switchToEditMode();
			setToolbarSize(Side.left, LEFT_TOOLBAR_SIZE);
		} else {
			editor.switchToPlayMode();
			setToolbarSize(Side.left, 0);
		}

		ViewManager.focusRenderCanvas();
	}

}
