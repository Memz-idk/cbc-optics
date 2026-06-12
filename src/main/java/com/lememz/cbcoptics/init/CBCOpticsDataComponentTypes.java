package com.lememz.cbcoptics.init;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.data.SightLinkerData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CBCOpticsDataComponentTypes {

    private CBCOpticsDataComponentTypes() {}

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CBCOptics.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SightLinkerData>> SIGHT_LINKER =
            DATA_COMPONENT_TYPES.register("sight_linker", () -> DataComponentType.<SightLinkerData>builder()
                    .persistent(SightLinkerData.CODEC).networkSynchronized(SightLinkerData.STREAM_CODEC).build()
            );
}
