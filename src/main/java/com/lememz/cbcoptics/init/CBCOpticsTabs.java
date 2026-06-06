package com.lememz.cbcoptics.init;

import com.lememz.cbcoptics.CBCOptics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Collectors;

public final class CBCOpticsTabs {

    private CBCOpticsTabs () {}

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CBCOptics.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CBC_OPTICS_TAB =
            CREATIVE_MODE_TABS.register("main_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cbcoptics"))
                    .icon(CBCOpticsBlocks.CANNON_SIGHT::toStack)
                    .displayItems(((parameters, output) ->
                            output.acceptAll(
                                    CBCOpticsItems.ITEMS.getEntries().stream().map(
                                            item -> item.get().getDefaultInstance()
                                    ).toList())))
                    .build());
}
