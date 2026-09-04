package com.pazaro.megafluidcell;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.core.AEConfig;
import appeng.core.localization.Tooltips;
import appeng.items.AEBaseItem;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;

import com.pazaro.megafluidcell.definition.ModTranslations;

public class BulkCellFluid extends AEBaseItem implements ICellWorkbenchItem {
    public BulkCellFluid(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEKeyType.fluids().filter(), is, 1);
    }

    @Override
    public void appendHoverText(ItemStack is, Level level, List<Component> lines, TooltipFlag flag) {
        var inventory = (BulkCellInventory) BulkCellFluidHandler.INSTANCE.getCellInventory(is, null);
        if (inventory == null) {
            return;
        }

        var storedFluid = inventory.getStoredFluid();
        var filterFluid = inventory.getFilterFluid();

        if (storedFluid == null) {
            lines.add(Tooltips.of(ModTranslations.Empty.text()));

            if (filterFluid == null) {
                lines.add(Tooltips.of(ModTranslations.NotPartitioned.text()));
            } else {
                lines.add(Tooltips.of(ModTranslations.PartitionedFor.text(filterFluid.getDisplayName())));
            }
        } else {
            lines.add(Tooltips.of(ModTranslations.Contains.text(storedFluid.getDisplayName())));

            var unitCount = inventory.getUnitCount();
            var unitCountBuckets = unitCount.doubleValue() / 1000;
            lines.add(Tooltips.of(ModTranslations.Quantity.text(Tooltips.ofNumber(unitCountBuckets))));

            if (inventory.isFilterMismatched()) {
                lines.add(ModTranslations.MismatchedFilter.text().withStyle(ChatFormatting.DARK_RED));
            }
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack is) {
        var inventory = (BulkCellInventory) BulkCellFluidHandler.INSTANCE.getCellInventory(is, null);
        if (inventory == null) {
            return Optional.empty();
        }

        var upgrades = new ArrayList<ItemStack>();
        var contents = new ArrayList<GenericStack>();

        if (AEConfig.instance().isTooltipShowCellContent()) {
            if (inventory.getStoredFluid() != null) {
                contents.add(new GenericStack(inventory.getStoredFluid(), inventory.getUnitCountClamped()));
            } else if (inventory.getFilterFluid() != null) {
                contents.add(new GenericStack(inventory.getFilterFluid(), 0));
            }
        }

        return Optional.of(new StorageCellTooltipComponent(upgrades, contents, false, true));
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack is) {
        return null;
    }

    @Override
    public void setFuzzyMode(ItemStack is, FuzzyMode fzMode) {}
}