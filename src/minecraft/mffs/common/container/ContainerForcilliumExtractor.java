package mffs.common.container;

import mffs.common.SlotHelper;
import mffs.common.tileentity.TileEntityExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerForcilliumExtractor extends ContainerMFFS
{
	private TileEntityExtractor tileEntity;
	private int workCylce;
	private int workdone;
	private int forceEnergybuffer;

	public ContainerForcilliumExtractor(EntityPlayer player, TileEntityExtractor tileentity)
	{
		super(tileentity);
		this.tileEntity = tileentity;
		this.workCylce = -1;
		this.workdone = -1;
		this.forceEnergybuffer = -1;

		this.addSlotToContainer(new SlotHelper(this.tileEntity, 0, 82, 26));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 1, 145, 40));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 2, 20, 66));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 3, 39, 66));
		this.addSlotToContainer(new SlotHelper(this.tileEntity, 4, 112, 26));

		this.addPlayerInventory(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(i);
		if ((slot != null) && (slot.getHasStack()))
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize)
			{
				slot.onSlotChanged();
			}
			else
			{
				return null;
			}
		}
		return itemstack;
	}

	@Override
	public void updateProgressBar(int i, int j)
	{
		switch (i)
		{
			case 0:
				this.tileEntity.setWorkDone(j);
				break;
			case 1:
				this.tileEntity.setWorkCylce(j);
				break;
			case 2:
				this.tileEntity.setForceEnergyBuffer(this.tileEntity.getForceEnergybuffer() & 0xFFFF0000 | j);

				break;
			case 3:
				this.tileEntity.setForceEnergyBuffer(this.tileEntity.getForceEnergybuffer() & 0xFFFF | j << 16);
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); i++)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.workdone != this.tileEntity.getWorkDone())
			{
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getWorkDone());
			}

			if (this.workCylce != this.tileEntity.getWorkCycle())
			{
				icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.getWorkCycle());
			}

			if (this.forceEnergybuffer != this.tileEntity.getForceEnergybuffer())
			{
				icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.getForceEnergybuffer() & 0xFFFF);

				icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.getForceEnergybuffer() >>> 16);
			}

		}

		this.workdone = this.tileEntity.getWorkDone();
		this.workCylce = this.tileEntity.getWorkCycle();
		this.forceEnergybuffer = this.tileEntity.getForceEnergybuffer();
	}
}