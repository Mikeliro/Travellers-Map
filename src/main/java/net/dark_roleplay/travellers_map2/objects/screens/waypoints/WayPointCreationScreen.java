package net.dark_roleplay.travellers_map2.objects.screens.waypoints;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dark_roleplay.travellers_map.mapping.waypoints.Waypoint;
import net.dark_roleplay.travellers_map.util.MapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class WayPointCreationScreen extends Screen {

	private Screen parent;
	private TextFieldWidget nameTextField;
	private Waypoint editedWaypoint;

	public WayPointCreationScreen(Screen parent, Waypoint editedWaypoint) {
		super(new TranslationTextComponent("screen.travellers_map.waypoint_creation"));
		this.parent = parent;
		this.editedWaypoint = editedWaypoint;
	}

	@Override
	public void func_231175_as__() {
		this.field_230706_i_.displayGuiScreen(parent);
	}

	@Override
	protected void func_231160_c_() {
		//FontRenderer fontIn, int xIn, int yIn, int widthIn, int heightIn, String msg
		this.nameTextField = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 - 64, this.field_230709_l_ / 2 - 24, 128, 12, new TranslationTextComponent("Waypoint"));
		TextFieldWidget x = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 - 64, this.field_230709_l_ / 2 - 9, 40, 12, new TranslationTextComponent("x"));
		TextFieldWidget y = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 - 20, this.field_230709_l_ / 2 - 9, 40, 12, new TranslationTextComponent("y"));
		TextFieldWidget z = new TextFieldWidget(this.field_230712_o_, this.field_230708_k_ / 2 + 24, this.field_230709_l_ / 2 - 9, 40, 12, new TranslationTextComponent("z"));


		this.func_230480_a_(this.nameTextField);
		this.func_230480_a_(x);
		this.func_230480_a_(y);
		this.func_230480_a_(z);

		x.setValidator(str -> str.matches("^-?[0-9]*$") || str.isEmpty());
		y.setValidator(str -> str.matches("^[0-9]*$") || str.isEmpty());
		z.setValidator(str -> str.matches("^-?[0-9]*$") || str.isEmpty());

		BlockPos playerPos = Minecraft.getInstance().player.func_233580_cy_();

		if (this.editedWaypoint == null) {
			nameTextField.setText("Waypoint");
			x.setText(playerPos.getX() + "");
			y.setText(playerPos.getY() + "");
			z.setText(playerPos.getZ() + "");
			this.func_230480_a_(new Button(this.field_230708_k_ / 2 - 65, this.field_230709_l_ / 2 + 17, 130, 20, new TranslationTextComponent("Create New"), button -> {
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
				this.func_231175_as__();
			}));
		} else {
			nameTextField.setText(editedWaypoint.getName());
			x.setText(editedWaypoint.getPos().getX() + "");
			y.setText(editedWaypoint.getPos().getY() + "");
			z.setText(editedWaypoint.getPos().getZ() + "");

			this.func_230480_a_(new Button(this.field_230708_k_ / 2 - 65, this.field_230709_l_ / 2 + 6, 65, 20, new TranslationTextComponent("Save Changes"), button -> {
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
				this.func_231175_as__();
			}));

			this.func_230480_a_(new Button(this.field_230708_k_ / 2 + 1, this.field_230709_l_ / 2 + 6, 65, 20, new TranslationTextComponent("Delete Waypoint"), button -> {
				MapManager.deleteWaypoint(this.editedWaypoint);
				this.func_231175_as__();
			}));
		}
	}

	@Override
	public void func_230430_a_(MatrixStack matrix, int mouseX, int mouseY, float delta) {
		parent.func_230430_a_(matrix, -500, -500, delta);
		func_230446_a_(matrix);
		super.func_230430_a_(matrix, mouseX, mouseY, delta);
	}
}
