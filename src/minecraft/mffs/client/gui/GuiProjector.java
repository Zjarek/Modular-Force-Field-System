package mffs.client.gui;

import mffs.client.GraphicButton;
import mffs.common.ModularForceFieldSystem;
import mffs.common.ProjectorTypes;
import mffs.common.container.ContainerProjector;
import mffs.common.tileentity.TileEntityProjector;
import mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class GuiProjector extends GuiContainer
{

	private TileEntityProjector projector;
	private boolean editMode = false;

	public GuiProjector(EntityPlayer player, TileEntityProjector tileentity)
	{
		super(new ContainerProjector(player, tileentity));
		this.projector = tileentity;
		this.xSize = 176;
		this.ySize = 186;
	}

	@Override
	protected void keyTyped(char c, int i)
	{
		if ((i != 1) && (this.editMode))
		{
			if (c == '\r')
			{
				this.editMode = false;
				return;
			}

			if (i == 14)
			{
				NetworkHandlerClient.fireTileEntityEvent(this.projector, 12, "");
			}
			if ((i != 54) && (i != 42) && (i != 58) && (i != 14))
			{
				NetworkHandlerClient.fireTileEntityEvent(this.projector, 11, String.valueOf(c));
			}
		}
		else
		{
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k)
	{
		super.mouseClicked(i, j, k);

		int xMin = (this.width - this.xSize) / 2;
		int yMin = (this.height - this.ySize) / 2;

		int x = i - xMin;
		int y = j - yMin;

		if (this.editMode)
		{
			this.editMode = false;
		}
		else if ((x >= 10) && (y >= 5) && (x <= 141) && (y <= 19))
		{
			NetworkHandlerClient.fireTileEntityEvent(this.projector, 10, "null");
			this.editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		int textur = this.mc.renderEngine.getTexture(ModularForceFieldSystem.TEXTURE_DIRECTORY + "GuiProjector.png");

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textur);

		int w = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;

		drawTexturedModalRect(w, k, 0, 0, this.xSize, this.ySize);
		int i1 = 79 * this.projector.getCapacity() / 100;
		drawTexturedModalRect(w + 8, k + 91, 176, 0, i1 + 1, 79);

		if (this.projector.hasValidTypeMod())
		{
			if (ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp != 7)
			{
				drawTexturedModalRect(w + 119, k + 63, 177, 143, 16, 16);
			}

			if ((ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp != 4) && (ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp != 2))
			{
				drawTexturedModalRect(w + 155, k + 63, 177, 143, 16, 16);
			}

			if ((ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp == 1) || (ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp == 2) || (ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp == 6) || (ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp == 7) || (ProjectorTypes.typeFromItem(this.projector.getType()).ProTyp == 8))
			{
				drawTexturedModalRect(w + 137, k + 28, 177, 143, 16, 16);

				drawTexturedModalRect(w + 137, k + 62, 177, 143, 16, 16);

				drawTexturedModalRect(w + 154, k + 45, 177, 143, 16, 16);

				drawTexturedModalRect(w + 120, k + 45, 177, 143, 16, 16);
			}

			if (this.projector.hasOption(ModularForceFieldSystem.itemOptionCamouflage, true))
			{
				drawTexturedModalRect(w + 137, k + 45, 177, 143, 16, 16);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		NetworkHandlerClient.fireTileEntityEvent(this.projector, guibutton.id, "");
	}

	@Override
	public void initGui()
	{
		this.controlList.add(new GraphicButton(1, this.width / 2 + 4, this.height / 2 - 37, this.projector, 1));
		this.controlList.add(new GraphicButton(0, this.width / 2 + 67, this.height / 2 - 88, this.projector, 0));

		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
	//	this.fontRenderer.drawString(this.projector.getDeviceName(), 12, 9, 4210752);
		this.fontRenderer.drawString("MFFS Projector", 12, 24, 4210752);
		this.fontRenderer.drawString("Typ-Mode", 34, 44, 4210752);
		this.fontRenderer.drawString("PowerLink", 34, 66, 4210752);
		if (this.projector.hasPowerSource())
		{
			this.fontRenderer.drawString(String.valueOf(this.projector.getLinkPower()), 30, 80, 4210752);
		}
		else
		{
			this.fontRenderer.drawString("No Link/OOR", 30, 80, 4210752);
		}
	}
}