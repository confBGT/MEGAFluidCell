package com.pazaro.megafluidcell.definition;

import com.pazaro.megafluidcell.MEGAFluidCell;

import appeng.core.localization.LocalizationEnum;

public enum ModTranslations implements LocalizationEnum {
    Empty("Empty", "gui.tooltips"),
    NotPartitioned("Not Partitioned", "gui.tooltips"),
    PartitionedFor("Partitioned for: %s", "gui.tooltips"),
    Contains("Contains: %s", "gui.tooltips"),
    Quantity("Quantity: %s", "gui.tooltips"),
    MismatchedFilter("Mismatched Filter!", "gui.tooltips");

    private final String englishText;
    private final String root;

    ModTranslations(String englishText, String root) {
        this.englishText = englishText;
        this.root = root;
    }

    @Override
    public String getEnglishText() {
        return englishText;
    }

    @Override
    public String getTranslationKey() {
        return String.format("%s.%s.%s", root, MEGAFluidCell.MODID, name());
    }
}
