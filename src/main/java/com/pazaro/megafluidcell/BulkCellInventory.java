package com.pazaro.megafluidcell;

import java.math.BigInteger;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;

public class BulkCellInventory implements StorageCell {
    private static final String STORED_FLUID_KEY = "StoredFluidKey";
    private static final String UNIT_COUNT_KEY = "UnitCountKey";

    private final ItemStack stack;
    private final ISaveProvider container;

    private AEFluidKey storedFluid;
    private final AEFluidKey filterFluid;

    private BigInteger unitCount;

    private boolean isPersisted = true;

    public BulkCellInventory(ItemStack stack, ISaveProvider container) {
        this.stack = stack;
        this.container = container;

        var cell = (BulkCellFluid) stack.getItem();
        filterFluid = (AEFluidKey) cell.getConfigInventory(stack).getKey(0);

        var tag = stack.getOrCreateTag();
        storedFluid = tag.contains(STORED_FLUID_KEY) ? AEFluidKey.fromTag(tag.getCompound(STORED_FLUID_KEY)) : null;
        unitCount = tag.contains(UNIT_COUNT_KEY) ? new BigInteger(tag.getString(UNIT_COUNT_KEY)) : BigInteger.ZERO;
    }

    public AEFluidKey getStoredFluid() {
        return storedFluid;
    }

    public AEFluidKey getFilterFluid() {
        return filterFluid;
    }

    public BigInteger getUnitCount() {
        return unitCount;
    }

    public long getUnitCountClamped() {
        return unitCount.min(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
    }

    public boolean isFilterMismatched() {
        if (storedFluid == null) {
            return false;
        }

        if (filterFluid != null && storedFluid.equals(filterFluid)) {
            return false;
        }

        return true;
    }

    private void saveChanges() {
        isPersisted = false;

        if (container != null) {
            container.saveChanges();
        } else {
            persist();
        }
    }

    @Override
    public void persist() {
        if (isPersisted) {
            return;
        }

        if (storedFluid == null) {
            stack.getOrCreateTag().remove(STORED_FLUID_KEY);
            stack.getOrCreateTag().remove(UNIT_COUNT_KEY);
        } else {
            stack.getOrCreateTag().put(STORED_FLUID_KEY, storedFluid.toTagGeneric());
            stack.getOrCreateTag().putString(UNIT_COUNT_KEY, unitCount.toString());
        }

        isPersisted = true;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount == 0 || !(what instanceof AEFluidKey fluid)) {
            return 0;
        }

        if (filterFluid == null || !fluid.equals(filterFluid)) {
            return 0;
        }

        if (isFilterMismatched()) {
            return 0;
        }

        if (mode == Actionable.MODULATE) {
            if (storedFluid == null) {
                storedFluid = filterFluid;
            }

            unitCount = unitCount.add(BigInteger.valueOf(amount));
            saveChanges();
        }

        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (unitCount.signum() < 1 || !(what instanceof AEFluidKey fluid)) {
            return 0;
        }

        if (filterFluid == null || !fluid.equals(filterFluid)) {
            return 0;
        }

        if (isFilterMismatched()) {
            return 0;
        }

        var toExtract = unitCount.min(BigInteger.valueOf(amount));
        if (mode == Actionable.MODULATE) {
            unitCount = unitCount.subtract(toExtract);
            saveChanges();
        }
    
        return toExtract.longValue();
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        if (storedFluid == null) {
            return;
        }

        out.add(storedFluid, getUnitCountClamped());
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return what instanceof AEFluidKey fluid && fluid.equals(filterFluid);
    }

    @Override
    public boolean canFitInsideCell() {
        return filterFluid == null && storedFluid == null;
    }

    @Override
    public Component getDescription() {
        return stack.getHoverName();
    }

    @Override
    public CellState getStatus() {
        if (storedFluid == null) {
            return CellState.EMPTY;
        }

        if (isFilterMismatched()) {
            return CellState.FULL;
        }

        return CellState.NOT_EMPTY;
    }

    @Override
    public double getIdleDrain() {
        return 5.0f;
    }
}