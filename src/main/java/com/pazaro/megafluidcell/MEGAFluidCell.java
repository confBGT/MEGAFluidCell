package com.pazaro.megafluidcell;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import appeng.api.storage.StorageCells;

import com.pazaro.megafluidcell.definition.ModItems;

@Mod(MEGAFluidCell.MODID)
public class MEGAFluidCell {
    public static final String MODID = "megafluidcell";

    public MEGAFluidCell(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();

        ModItems.init();

        modEventBus.addListener(this::initStorageCells);
        modEventBus.addListener(this::register);
    }

    public static ResourceLocation makeId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void initStorageCells(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StorageCells.addCellHandler(BulkCellFluidHandler.INSTANCE);
        });
    }

    private void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            for (var itemDefinition : ModItems.getItems()) {
                ForgeRegistries.ITEMS.register(itemDefinition.id(), itemDefinition.asItem());
            }

        }
    }
}