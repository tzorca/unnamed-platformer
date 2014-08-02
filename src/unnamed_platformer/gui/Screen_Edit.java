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
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.InputEventType;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.Main.State;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.Editor;
import unnamed_platformer.game.EntityCreator;
import unnamed_platformer.game.Level;
import unnamed_platformer.globals.InputRef.GameKey;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.objects.ImageListEntry;
import unnamed_platformer.gui.objects.ImageListEntryRenderer;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.Graphic;

public class Screen_Edit extends BaseScreen_Hybrid {
	Editor editor = new Editor(0);

	List<Graphic> entityPlaceholderGraphics = new ArrayList<Graphic>();
	ImageIcon imgEditMode = ImageHelper.getImageIconContent("gui_modeEdit"), imgPlayMode = ImageHelper
			.getImageIconContent("gui_modePlay"), imgAdd = ImageHelper.getImageIconContent("gui_add"),
			imgNext = ImageHelper.getImageIconContent("gui_next"), imgPrev = ImageHelper
					.getImageIconContent("gui_prev"), imgRemove = ImageHelper.getImageIconContent("gui_remove"),
			imgSave = ImageHelper.getImageIconContent("gui_save");

	DefaultListModel<ImageListEntry> mdlTextureNames = new DefaultListModel<ImageListEntry>();
	JList<ImageListEntry> lstTextureNames = new JList<ImageListEntry>();
	JScrollPane scrlTextureNames = new JScrollPane(lstTextureNames, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JLabel lblCurrentLevel = new JLabel();
	JButton btnModeSwitch = new JButton(imgPlayMode), btnPrevLevel = new JButton(imgPrev), btnAddLevel = new JButton(
			imgAdd), btnNextLevel = new JButton(imgNext), btnRemoveLevel = new JButton(imgRemove),
			btnSaveLevel = new JButton(imgSave);
	List<Component> ctlList_EditMode = new ArrayList<Component>();

	// Instance Initializer
	{
		// Misc
		Ref.multiadd(ctlList_EditMode, new Component[] {
				lstTextureNames, lblCurrentLevel, btnPrevLevel, btnAddLevel, btnNextLevel, btnRemoveLevel, btnSaveLevel
		});
		loadEntityPlaceholderGraphics();

		// Toolbar Setup
		setToolbarSize(Side.left, 96);
		setToolbarSize(Side.top, 36);
		setToolbarSize(Side.bottom, 0);
		setToolbarSize(Side.right, 0);
		Panel leftToolbar = toolbars.get(Side.left);
		leftToolbar.setLayout(new BorderLayout());
		Panel topToolbar = toolbars.get(Side.top);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		topToolbar.setLayout(flowLayout);

		// Left Toolbar : Components
		lstTextureNames.setBackground(leftToolbar.getBackground());
		lstTextureNames.setCellRenderer(new ImageListEntryRenderer());
		lstTextureNames.setModel(mdlTextureNames);
		lstTextureNames.addListSelectionListener(new lstTextureNames_ListSelection());
		lstTextureNames.setSelectedIndex(0);

		leftToolbar.add(scrlTextureNames, BorderLayout.CENTER);

		// Canvas : Listeners
		InputManager.setEventHandler(InputEventType.leftClick, new RenderCanvas_LeftClick());
		InputManager.setEventHandler(InputEventType.rightClick, new RenderCanvas_RightClick());

		InputManager.setEventHandler(InputEventType.leftMouseDown, new RenderCanvas_LeftMouseDown());
		InputManager.setEventHandler(InputEventType.rightMouseDown, new RenderCanvas_RightMouseDown());

		// Top Toolbar : Components
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

	public Graphic getCurrentGraphic() {
		return entityPlaceholderGraphics.get(lstTextureNames.getSelectedIndex());
	}

	private void loadEntityPlaceholderGraphics() {
		mdlTextureNames.clear();

		List<String> internalTextureNames = new ArrayList<String>(EntityCreator.listTextureNames());
		Collections.sort(internalTextureNames);
		for (String internalTextureName : internalTextureNames) {

			String displayName = ResManager.humanizeName(internalTextureName);
			ImageIcon imageIcon = ImageHelper.getImageIconContentScaleDown(internalTextureName, 48);

			mdlTextureNames.addElement(new ImageListEntry(imageIcon, displayName, internalTextureName));
			entityPlaceholderGraphics.add(new Graphic(internalTextureName, Ref.COLOR_75_PERCENT_TRANS));
		}
	}

	public void update() {
		boolean isEditMode = Main.state == State.Edit;

		if (isEditMode) {
			editor.update();
			processControls();
		}

		for (Component editModeComponent : ctlList_EditMode) {
			if (editModeComponent.isVisible() != isEditMode) {
				editModeComponent.setVisible(isEditMode);
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

		int newGridSize = Editor.gridSize;
		if (InputManager.getGameKeyState(GameKey.scrollIn, 1)) {
			newGridSize /= 2;
			InputManager.resetGameKey(GameKey.scrollIn, 1);

		} else if (InputManager.getGameKeyState(GameKey.scrollOut, 1)) {
			newGridSize *= 2;
			InputManager.resetGameKey(GameKey.scrollOut, 1);
		}

		if (newGridSize >= 8 && newGridSize <= 128) {
			Editor.gridSize = newGridSize;
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

	public void draw() {

		if (Main.state != State.Edit) {
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

	private class RenderCanvas_LeftClick implements Runnable {
		public void run() {
			editor.placeObject(InputManager.getGameMousePos(), lstTextureNames.getSelectedValue());
		}
	}

	private class RenderCanvas_RightClick implements Runnable {
		public void run() {
			editor.removeObject(InputManager.getGameMousePos());
		}
	}

	private class RenderCanvas_LeftMouseDown implements Runnable {
		public void run() {

		}

	}

	private class RenderCanvas_RightMouseDown implements Runnable {
		public void run() {

		}

	}

	private class lstTextureNames_ListSelection implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			ViewManager.focusRenderCanvas();
		}
	}

	private class btnModeSwitch_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (Main.state == State.Edit) {
				editor.switchToPlayMode();
				btnModeSwitch.setIcon(imgEditMode);
				setToolbarSize(Side.left, 0);
			} else {
				editor.switchToEditMode();
				btnModeSwitch.setIcon(imgPlayMode);
				setToolbarSize(Side.left, 96);
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
