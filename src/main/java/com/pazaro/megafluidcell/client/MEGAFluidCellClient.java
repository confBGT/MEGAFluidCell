package com.pazaro.megafluidcell;

import com.pazaro.megafluidcell.definition.ModItems;

import appeng.api.client.StorageCellModels;
import appeng.items.storage.BasicStorageCell;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = MEGAFluidCell.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MEGAFluidCellClient {
    @SubscribeEvent
    public static void initItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
            (stack, tintIndex) -> FastColor.ABGR32.opaque(BasicStorageCell.getColor(stack, tintIndex)),
            ModItems.BULK_CELL_FLUID
        );
    }

    @SubscribeEvent
    public static void initStorageCellModels(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            var modelPrefix = "block/drive/cells/";

            StorageCellModels.registerModel(
                ModItems.BULK_CELL_FLUID,
                MEGAFluidCell.makeId(modelPrefix + ModItems.BULK_CELL_FLUID.id().getPath())
            );
        });
    }
}