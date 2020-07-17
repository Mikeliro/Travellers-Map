package net.dark_roleplay.travellers_map.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;

public abstract class IMapLayer extends AbstractGui {

	protected boolean canBeDisabled;
	protected boolean isEnabled;

	public IMapLayer(boolean canBeDisabled){
		this.canBeDisabled = canBeDisabled;
		this.isEnabled = true;
	}

	public abstract void renderLayer(MatrixStack matrix, MapRenderInfo renderInfo, MapType mapType, boolean isRotated, float delta);

	public void disable(){
		if(canBeDisabled) isEnabled = false;
	}

	public void enable(){
		if(canBeDisabled) isEnabled = true;
	}

	public boolean canBeDisabled(){
		return this.canBeDisabled;
	}

	public boolean isEnabled(){
		return this.isEnabled;
	}

	public boolean canRenderIn(MapType type){
		return true;
	}
}
