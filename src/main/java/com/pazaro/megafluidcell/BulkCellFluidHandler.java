package com.pazaro.megafluidcell;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;

import com.pazaro.megafluidcell.definition.ModItems;

public class BulkCellFluidHandler implements ICellHandler {
    public static BulkCellFluidHandler INSTANCE = new BulkCellFluidHandler();

    @Override
    public boolean isCell(ItemStack is) {
        return is != null && is.is(ModItems.BULK_CELL_FLUID.asItem());
    }

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack is, @Nullable ISaveProvider host) {
        return isCell(is) ? new BulkCellInventory(is, host) : null;
    }
}