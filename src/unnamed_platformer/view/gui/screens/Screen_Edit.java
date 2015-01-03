package unnamed_platformer.view.gui.screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
import unnamed_platformer.game.other.Editor;
import unnamed_platformer.game.other.EntityCreator;
import unnamed_platformer.game.other.Level;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.StyleRef;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.input.MouseInputManager;
import unnamed_platformer.input.MouseInputManager.MouseEventType;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.Graphic;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.GUIHelper.ParamRunnable;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.GUIManager.ScreenType;
import unnamed_platformer.view.gui.dialogs.Dialog_ChoiceSelection;
import unnamed_platformer.view.gui.dialogs.Dialog_EditMenu;
import unnamed_platformer.view.gui.objects.ImageListEntry;
import unnamed_platformer.view.gui.objects.ListCellRenderer_ImageListEntry;

import com.google.common.collect.Lists;

@SuppressWarnings("unchecked")
public class Screen_Edit extends BaseScreen_Hybrid
{
	public static final int LEFT_TOOLBAR_SIZE = 160;
	public static final int ROW_HEIGHT = 56;
	public static final int ENTITY_ICON_SIZE = 48;
	public static final int CURSOR_SIZE = 48;

	private final Editor editor = new Editor(0);

	private final Map<ImageListEntry, Graphic> entityGraphics = new HashMap<ImageListEntry, Graphic>();

	// TODO: Make it more obvious the user can scroll past the edges of the
	// screens
	private final List<ImageListEntry> imageListEntries = new ArrayList<ImageListEntry>();
	private final JList<ImageListEntry> lstEntities = new JList<ImageListEntry>();

	private final List<Component> editModeComponents = new ArrayList<Component>();

	private transient boolean lastMultiselectState;

	public Screen_Edit() {
		super();

		// ADD COMPONENTS TO EDIT MODE LIST
		editModeComponents.addAll(Lists.newArrayList(lstEntities));

		// LOAD ENTITY PLACEHOLDER GRAPHICS
		loadEntityPlaceholderGraphics();

		// SET TOOLBAR SIZES
		setToolbarSize(Side.left, LEFT_TOOLBAR_SIZE);
		setToolbarSize(Side.top, 0);
		setToolbarSize(Side.right, 0);
		setToolbarSize(Side.bottom, 0);

		// SETUP ENTITY LIST
		lstEntities.setCellRenderer(new ListCellRenderer_ImageListEntry());
		StyleRef.STYLE_ENTITY_LIST.apply(lstEntities);

		DefaultListModel<ImageListEntry> lstEntitiesModel = new DefaultListModel<ImageListEntry>();
		for (final ImageListEntry entry : imageListEntries) {
			lstEntitiesModel.addElement(entry);
		}
		lstEntities.setModel(lstEntitiesModel);
		final JScrollPane treeScroller = new JScrollPane(lstEntities,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lstEntities.addListSelectionListener(new EntityList_SelectionChanged());

		// SETUP LEFT TOOLBAR
		final Panel leftToolbar = toolbars.get(Side.left);
		leftToolbar.setLayout(new BorderLayout());
		leftToolbar.add(treeScroller, BorderLayout.CENTER);

		// ADD CANVAS LISTENERS
		MouseInputManager.setEventHandler(MouseEventType.leftClick,
				new RenderCanvas_LeftClick());
		MouseInputManager.setEventHandler(MouseEventType.rightClick,
				new RenderCanvas_RightClick());

		// SETUP TOP TOOLBAR
		final Panel topToolbar = toolbars.get(Side.top);
		final FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		topToolbar.setLayout(flowLayout);

		// INITIALIZE CURSOR
		initCursor();

		// FOCUS ON PLAYER INITIALLY
		editor.focusOnPlayer();
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
					.getImageIconContentScaleDown(textureName, ENTITY_ICON_SIZE);

			final ImageListEntry entry = new ImageListEntry(imageIcon,
					displayName, textureName);
			imageListEntries.add(entry);
			entityGraphics.put(entry, new Graphic(textureName,
					Ref.COLOR_75_PERCENT_TRANS));
		}

		Collections.sort(imageListEntries);
	}

	public void update() {
		final boolean currentlyEditing = !World.playing();

		if (currentlyEditing) {
			editor.update();
			processControls();
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

	private void processControls() {
		processNavigationControls();
		processGridControls();
		processEntitySelectionControls();
		processPaintControls();
	}

	private void processEntitySelectionControls() {
		int currentIndex = lstEntities.getSelectedIndex();
		int nextIndex = currentIndex;

		if (InputManager.keyPressOccurredOrRepeating(GameKey.SECONDARY_UP, 1)) {
			nextIndex = currentIndex - 1;
		} else if (InputManager.keyPressOccurredOrRepeating(
				GameKey.SECONDARY_DOWN, 1)) {
			nextIndex = currentIndex + 1;
		}

		if (nextIndex != currentIndex) {
			if (nextIndex < 0) {
				nextIndex = imageListEntries.size() - 1;
			} else if (nextIndex >= imageListEntries.size()) {
				nextIndex = 0;
			}

			lstEntities.ensureIndexIsVisible(nextIndex);
			lstEntities.setSelectedIndex(nextIndex);
		}
	}

	private void processPaintControls() {
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
			editor.placeObject(cursorRect.getLocation(), getSelectedEntry());

		} else if (InputManager.keyPressOccurred(GameKey.B, 1)) {
			editor.removeObject(cursorRect.getLocation());
		}

	}

	private void processGridControls() {
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
				ViewManager.setColor(Ref.COLOR_75_PERCENT_TRANS);
			}
		}
	}

	private Graphic cursorGraphic = new Graphic("crosshair");

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

	private Rectangle cursorRect;

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

	private static final String NAV_TIME_PERIOD_STRING = "EditorLevelNavigation";
	private static final float NAV_RATE = 0.01f;
	private static final int NAV_SPEED = 6;
	private static final float NAV_EDGE_SPEED_MULTIPLIER = 2f;

	private void processNavigationControls() {
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

		Dialog_ChoiceSelection dlgConfirmExit = new Dialog_ChoiceSelection(
				ViewManager.getFrame(), message, choices, cancelText,
				afterChoice);

		dlgConfirmExit.setVisible(true);

		return false;
	}

	private ImageListEntry getSelectedEntry() {
		if (lstEntities.isSelectionEmpty()) {
			return null;
		}

		return lstEntities.getSelectedValue();
	}

	private class RenderCanvas_LeftClick implements Runnable
	{
		public void run() {
			editor.placeObject(MouseInputManager.getGameMousePos(),
					getSelectedEntry());
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
