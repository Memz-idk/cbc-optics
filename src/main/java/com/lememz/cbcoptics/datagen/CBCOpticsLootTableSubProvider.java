package com.lememz.cbcoptics.datagen;

import com.lememz.cbcoptics.init.CBCOpticsBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

@MethodsReturnNonnullByDefault
public class CBCOpticsLootTableSubProvider extends BlockLootSubProvider {

    public CBCOpticsLootTableSubProvider(Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        this.dropSelf(CBCOpticsBlocks.CANNON_SIGHT.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CBCOpticsBlocks.BLOCKS.getEntries().stream().map(holder -> (Block)holder.get()).toList();
    }
}
