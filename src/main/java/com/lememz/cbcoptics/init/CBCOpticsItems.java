package com.lememz.cbcoptics.init;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.item.SightLinkerItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CBCOpticsItems {

    private CBCOpticsItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CBCOptics.MOD_ID);
    public static final DeferredItem<Item> SIGHT_VIEWER = ITEMS.registerSimpleItem("sight_viewer");
    public static final DeferredItem<Item> INCOMPLETE_CANNON_SIGHT = ITEMS.registerSimpleItem("incomplete_cannon_sight");
    public static final DeferredItem<Item> INCOMPLETE_AUTOCANNON_SIGHT = ITEMS.registerSimpleItem("incomplete_autocannon_sight");
    public static final DeferredItem<Item> SIGHT_LINKER = ITEMS.register("sight_linker", SightLinkerItem::new);
}
