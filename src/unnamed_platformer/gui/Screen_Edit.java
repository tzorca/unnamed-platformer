package unnamed_platformer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
import unnamed_platformer.app.MathHelper;
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

	// TODO: Fix level bounds bug (probably related to that other bug with
	// entity placeholder positioning)

	Editor editor = new Editor(0);

	List<Graphic> entityPlaceholderGraphics = new ArrayList<Graphic>();
	ImageIcon imgEditMode = ImageHelper.getImageIconContent("gui_modeEdit"),
			imgPlayMode = ImageHelper.getImageIconContent("gui_modePlay"),
			imgAdd = ImageHelper.getImageIconContent("gui_add"),
			imgNext = ImageHelper.getImageIconContent("gui_next"),
			imgPrev = ImageHelper.getImageIconContent("gui_prev"),
			imgRemove = ImageHelper.getImageIconContent("gui_remove"),
			imgSave = ImageHelper.getImageIconContent("gui_save");

	DefaultListModel<ImageListEntry> mdlTextureNames = new DefaultListModel<ImageListEntry>();
	JList<ImageListEntry> lstTextureNames = new JList<ImageListEntry>();
	JScrollPane scrlTextureNames = new JScrollPane(lstTextureNames,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JLabel lblCurrentLevel = new JLabel();
	JButton btnModeSwitch = new JButton(imgPlayMode),
			btnPrevLevel = new JButton(imgPrev), btnAddLevel = new JButton(
					imgAdd), btnNextLevel = new JButton(imgNext),
			btnRemoveLevel = new JButton(imgRemove),
			btnSaveLevel = new JButton(imgSave);
	List<Component> ctlList_EditMode = new ArrayList<Component>();

	// Instance Initializer
	{
		// Misc
		Ref.multiadd(ctlList_EditMode, new Component[] { lstTextureNames,
				lblCurrentLevel, btnPrevLevel, btnAddLevel, btnNextLevel,
				btnRemoveLevel, btnSaveLevel });
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
		lstTextureNames
				.addListSelectionListener(new lstTextureNames_ListSelection());
		lstTextureNames.setSelectedIndex(0);

		leftToolbar.add(scrlTextureNames, BorderLayout.CENTER);

		// Canvas : Listeners
		InputManager.setEventHandler(InputEventType.leftClick,
				new RenderCanvas_LeftClick());

		InputManager.setEventHandler(InputEventType.rightClick,
				new RenderCanvas_RightClick());

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
		return entityPlaceholderGraphics
				.get(lstTextureNames.getSelectedIndex());
	}

	private void loadEntityPlaceholderGraphics() {
		mdlTextureNames.clear();
		for (String internalTextureName : EntityCreator.listTextureNames()) {

			String displayName = ResManager
					.getDisplayName(internalTextureName);
			ImageIcon imageIcon = ImageHelper.getImageIconContentScaleDown(
					internalTextureName, 48);

			mdlTextureNames.addElement(new ImageListEntry(imageIcon,
					displayName, internalTextureName));
			entityPlaceholderGraphics.add(new Graphic(internalTextureName,
					Ref.COLOR_75_PERCENT_TRANS));
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
	}

	private void processGridControls() {

		int newGridSize = Editor.gridSize;
		if (InputManager.getGameKeyState(GameKey.scrollIn, 1)) {
			newGridSize /= 2;
			InputManager.resetGameKey(GameKey.scrollIn, 1);
			if (newGridSize >= 8 && newGridSize <= 128) {
				Editor.gridSize = newGridSize;
			}
		} else if (InputManager.getGameKeyState(GameKey.scrollOut, 1)) {
			newGridSize *= 2;
			InputManager.resetGameKey(GameKey.scrollOut, 1);
			if (newGridSize >= 8 && newGridSize <= 128) {
				Editor.gridSize = newGridSize;
			}
		}

	}

	private void processCameraControls() {
		Vector2f cameraDelta = new Vector2f(0, 0);

		if (InputManager.getKeyState(Keyboard.KEY_RIGHT)) {
			cameraDelta.x += 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			cameraDelta.x -= 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_UP)) {
			cameraDelta.y -= 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_DOWN)) {
			cameraDelta.y += 8;
		}

		editor.tryMoveCamera(cameraDelta);
	}


	public void drawEntityPlaceholder() {

		if (Main.state != State.Edit) {
			return;
		}

		Graphic entityPlaceholderGraphic = getCurrentGraphic();

		if (entityPlaceholderGraphic == null) {
			return;
		}

		Level currentLevel = GameManager.getCurrentLevel();

		Vector2f loc = MathHelper.snapToGrid(InputManager.getGameMousePos(),
				Editor.gridSize);

		Texture t = entityPlaceholderGraphic.getTexture();

		Rectangle2D entityPlaceholderRect = new Rectangle2D.Float(loc.x, loc.y,
				t.getImageWidth(), t.getImageHeight());

		Rectangle2D levelRect = new Rectangle2D.Float(currentLevel.getRect()
				.getX(), currentLevel.getRect().getY(), currentLevel.getRect()
				.getWidth(), currentLevel.getRect().getHeight());

		if (levelRect.contains(entityPlaceholderRect)) {
			ViewManager.drawGraphic(entityPlaceholderGraphic,
					entityPlaceholderRect);
		} else {
			ViewManager.setColor(Ref.COLOR_75_PERCENT_TRANS);
		}
	}

	private void updateCurrentLevelLabel() {
		lblCurrentLevel.setText(GameManager.getCurrentLevelNumber() + "");
	}

	// ===============================================================================
	// Event Handlers
	// ===============================================================================

	public class RenderCanvas_LeftClick implements Runnable {
		public void run() {
			editor.placeObject(InputManager.getGameMousePos(),
					lstTextureNames.getSelectedValue());
		}
	}

	public class RenderCanvas_RightClick implements Runnable {
		public void run() {
			editor.removeObject(InputManager.getGameMousePos());
		}
	}

	public class lstTextureNames_ListSelection implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			ViewManager.focusRenderCanvas();
		}
	}

	public class Edit_FocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			ViewManager.focusRenderCanvas();
		}

		public void focusLost(FocusEvent e) {
			ViewManager.focusRenderCanvas();
		}
	}

	public class btnModeSwitch_Click implements ActionListener {
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

	public class btnAddLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			GameManager.addBlankLevel();
			editor.changeLevel(GameManager.getLevelCount() - 1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	public class btnNextLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.levelInc(1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	public class btnPrevLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.levelInc(-1);
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	public class btnRemoveLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.removeLevel();
			updateCurrentLevelLabel();
			ViewManager.focusRenderCanvas();
		}
	}

	public class btnSaveLevel_Click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editor.resetToEditPlacement();
			GameManager.saveCurrentGame();
			ViewManager.focusRenderCanvas();
		}
	}

}
