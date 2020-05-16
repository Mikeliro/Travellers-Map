package net.dark_roleplay.travellers_map.objects.screens.full_map;

import net.dark_roleplay.travellers_map.features.waypoints.Waypoint;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.SliderMultiplierOption;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class WayPointCreationScreen extends Screen {

	private Screen parent;
	private TextFieldWidget nameTextField;
	private Waypoint editedWaypoint;

	protected WayPointCreationScreen(Screen parent, Waypoint editedWaypoint) {
		super(new TranslationTextComponent("screen.travellers_map.waypoint_creation"));
		this.parent = parent;
		this.editedWaypoint = editedWaypoint;
	}

	@Override
	public void onClose() {
		this.minecraft.displayGuiScreen(parent);
	}

	@Override
	protected void init() {
		//FontRenderer fontIn, int xIn, int yIn, int widthIn, int heightIn, String msg
		this.nameTextField = new TextFieldWidget(this.font, this.width / 2 - 64, this.height / 2 - 24, 128, 12, "Waypoint");
		TextFieldWidget x = new TextFieldWidget(this.font, this.width / 2 - 64, this.height / 2 - 9, 40, 12, "x");
		TextFieldWidget y = new TextFieldWidget(this.font, this.width / 2 - 20, this.height / 2 - 9, 40, 12, "y");
		TextFieldWidget z = new TextFieldWidget(this.font, this.width / 2 + 24, this.height / 2 - 9, 40, 12, "z");


		this.addButton(this.nameTextField);
		this.addButton(x);
		this.addButton(y);
		this.addButton(z);

		x.setValidator(str -> str.matches("^-?[0-9]*$") || str.isEmpty());
		y.setValidator(str -> str.matches("^[0-9]*$") || str.isEmpty());
		z.setValidator(str -> str.matches("^-?[0-9]*$") || str.isEmpty());

		BlockPos playerPos = Minecraft.getInstance().player.getPosition();

		if (this.editedWaypoint == null) {
			nameTextField.setText("Waypoint");
			x.setText(playerPos.getX() + "");
			y.setText(playerPos.getY() + "");
			z.setText(playerPos.getZ() + "");
			this.addButton(new Button(this.width / 2 - 65, this.height / 2 + 17, 130, 20, "Create New", button -> {
				MapManager.saveWaypoint(
						new Waypoint(
								UUID.randomUUID(),
								nameTextField.getText(),
								new BlockPos(
										Integer.parseInt(x.getText()),
										Integer.parseInt(y.getText()),
										Integer.parseInt(z.getText())
								),
								0xFFFFFFFF,
								true
						),
						this.editedWaypoint == null
				);
				this.onClose();
			}));
		} else {
			nameTextField.setText(editedWaypoint.getName());
			x.setText(editedWaypoint.getPos().getX() + "");
			y.setText(editedWaypoint.getPos().getY() + "");
			z.setText(editedWaypoint.getPos().getZ() + "");

			this.addButton(new Button(this.width / 2 - 65, this.height / 2 + 6, 65, 20, "Save Changes", button -> {
				this.editedWaypoint.update(
						nameTextField.getText(),
						new BlockPos(
								Integer.parseInt(x.getText()),
								Integer.parseInt(y.getText()),
								Integer.parseInt(z.getText())
						),
						0xFFFFFFFF
				);
				MapManager.saveWaypoint(
						this.editedWaypoint,
						this.editedWaypoint == null
				);
				this.onClose();
			}));

			this.addButton(new Button(this.width / 2 + 1, this.height / 2 + 6, 65, 20, "Delete Waypoint", button -> {
				MapManager.deleteWaypoint(this.editedWaypoint);
				this.onClose();
			}));
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		parent.render(-500, -500, delta);
		renderBackground();
		super.render(mouseX, mouseY, delta);
	}
}
