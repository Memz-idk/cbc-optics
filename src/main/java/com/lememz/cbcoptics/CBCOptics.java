package com.lememz.cbcoptics;

import com.lememz.cbcoptics.init.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CBCOptics.MOD_ID)
public class CBCOptics {

    public static final String MOD_ID = "cbcoptics";

    public CBCOptics(IEventBus bus, ModContainer container) {
        CBCOpticsBlocks.BLOCKS.register(bus);
        CBCOpticsItems.ITEMS.register(bus);
        CBCOpticsTabs.CREATIVE_MODE_TABS.register(bus);
        CBCOpticsAttachments.ATTACHMENT_TYPES.register(bus);
        CBCOpticsBlockEntityTypes.BLOCK_ENTITY_TYPES.register(bus);
        CBCOpticsDataComponentTypes.DATA_COMPONENT_TYPES.register(bus);
    }
}
