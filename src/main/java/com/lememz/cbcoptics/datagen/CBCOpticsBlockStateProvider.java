package com.lememz.cbcoptics.datagen;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.init.CBCOpticsBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CBCOpticsBlockStateProvider extends BlockStateProvider {

    public CBCOpticsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CBCOptics.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(CBCOpticsBlocks.OPTICS_CONTROL_UNIT.get(), cubeAll(CBCOpticsBlocks.OPTICS_CONTROL_UNIT.get()));
    }
}
