package com.lememz.cbcoptics.init;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.block.AutocannonSightBlock;
import com.lememz.cbcoptics.block.CannonSightBlock;
import com.lememz.cbcoptics.block.OpticsControlUnitBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CBCOpticsBlocks {

    private CBCOpticsBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CBCOptics.MOD_ID);
    public static final DeferredBlock<Block> CANNON_SIGHT = registerWithItem("cannon_sight", CannonSightBlock::new);
    public static final DeferredBlock<Block> AUTOCANNON_SIGHT = registerWithItem("autocannon_sight", AutocannonSightBlock::new);
    public static final DeferredBlock<Block> OPTICS_CONTROL_UNIT = registerWithItem("optics_control_unit", OpticsControlUnitBlock::new);

    private static DeferredBlock<Block> registerWithItem(String name, Supplier<Block> supplier) {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        CBCOpticsItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }
}
