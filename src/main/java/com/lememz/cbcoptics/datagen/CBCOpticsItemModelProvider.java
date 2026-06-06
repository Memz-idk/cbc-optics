package com.lememz.cbcoptics.datagen;

import com.lememz.cbcoptics.CBCOptics;
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
        this.handheldItem(CBCOpticsItems.SIGHT_VIEWER.get());
    }
}
