package com.lememz.cbcoptics.datagen;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public final class DataGenerators {

    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<Provider> registries = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        LootTableProvider.SubProviderEntry lootTableSubProvider = new LootTableProvider.SubProviderEntry(
                CBCOpticsLootTableSubProvider::new, LootContextParamSets.BLOCK
        );
        event.getGenerator().addProvider(event.includeClient(), new LootTableProvider(
                output, Collections.emptySet(), List.of(lootTableSubProvider), registries
        ));
        event.getGenerator().addProvider(event.includeClient(), new CBCOpticsItemModelProvider(output, helper));
    }
}
