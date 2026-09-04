package com.pazaro.megafluidcell.definition;

import java.util.List;
import java.util.ArrayList;

import com.google.common.base.Function;

import net.minecraft.world.item.Item;

import appeng.core.definitions.ItemDefinition;
import appeng.items.materials.MaterialItem;

import com.pazaro.megafluidcell.BulkCellFluid;
import com.pazaro.megafluidcell.MEGAFluidCell;

public class ModItems {
    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    public static ItemDefinition<BulkCellFluid> BULK_CELL_FLUID;
    public static ItemDefinition<MaterialItem> BULK_CELL_FLUID_COMPONENT;

    public static void init() {
        BULK_CELL_FLUID = item("MEGA Bulk Fluid Storage Cell", "bulk_fluid_cell", BulkCellFluid::new);
        BULK_CELL_FLUID_COMPONENT = item("MEGA Bulk Fluid Storage Component", "bulk_fluid_cell_component", MaterialItem::new);
    }

    public static List<ItemDefinition<?>> getItems() {
        return ITEMS;
    }

    private static <T extends Item> ItemDefinition<T> item(String englishName, String id, Function<Item.Properties, T> factory) {
        var definition = new ItemDefinition<T>(englishName, MEGAFluidCell.makeId(id), factory.apply(new Item.Properties()));
        ITEMS.add(definition);
        return definition;
    }
}