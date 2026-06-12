package com.lememz.cbcoptics.datagen;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.init.CBCOpticsBlocks;
import com.lememz.cbcoptics.init.CBCOpticsItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CBCOpticsItemModelProvider extends ItemModelProvider {

    public CBCOpticsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CBCOptics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(CBCOpticsItems.INCOMPLETE_CANNON_SIGHT.get());
        this.basicItem(CBCOpticsItems.INCOMPLETE_AUTOCANNON_SIGHT.get());
        this.handheldItem(CBCOpticsItems.SIGHT_VIEWER.get());
        this.handheldItem(CBCOpticsItems.SIGHT_LINKER.get());
        this.basicItem(CBCOpticsBlocks.CANNON_SIGHT.asItem());
        this.basicItem(CBCOpticsBlocks.AUTOCANNON_SIGHT.asItem());
    }
}
