package com.pazaro.megafluidcell.datagen;

import java.util.function.Consumer;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import com.pazaro.megafluidcell.MEGAFluidCell;
import com.pazaro.megafluidcell.definition.ModItems;
import com.pazaro.megafluidcell.definition.ModTranslations;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;

@EventBusSubscriber(modid = MEGAFluidCell.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();

        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
    }

    private static class ModLanguageProvider extends LanguageProvider {
        public ModLanguageProvider(PackOutput output) {
            super(output, MEGAFluidCell.MODID, "en_us");
        }

        @Override
        protected void addTranslations() {
            for (var itemDefinition : ModItems.getItems()) {
                add(itemDefinition.asItem(), itemDefinition.getEnglishName());
            }

            for (var translation : ModTranslations.values()) {
                add(translation.getTranslationKey(), translation.getEnglishText());
            }
        }
    }

    private static class ModRecipeProvider extends RecipeProvider {
        public ModRecipeProvider(PackOutput output) {
            super(output);
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BULK_CELL_FLUID)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.SKY_DUST)
                .define('c', ModItems.BULK_CELL_FLUID_COMPONENT)
                .define('d', Items.NETHERITE_INGOT)
                .unlockedBy("has_material", has(ModItems.BULK_CELL_FLUID_COMPONENT))
                .save(consumer);
        }
    }
}
