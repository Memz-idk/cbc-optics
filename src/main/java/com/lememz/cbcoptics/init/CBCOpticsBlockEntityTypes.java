package com.lememz.cbcoptics.init;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.block.entity.OpticsControlUnitBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("DataFlowIssue")
public final class CBCOpticsBlockEntityTypes {

    private CBCOpticsBlockEntityTypes() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CBCOptics.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OpticsControlUnitBlockEntity>> OPTICS_CONTROL_UNIT =
            BLOCK_ENTITY_TYPES.register("optics_control_unit", () -> BlockEntityType.Builder.of(
                    OpticsControlUnitBlockEntity::new, CBCOpticsBlocks.OPTICS_CONTROL_UNIT.get()
            ).build(null));
}
