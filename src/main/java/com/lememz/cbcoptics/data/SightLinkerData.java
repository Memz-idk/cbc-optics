package com.lememz.cbcoptics.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SightLinkerData(BlockPos mountPos) {

    public static final Codec<SightLinkerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.fieldOf("sight_pos").forGetter(SightLinkerData::mountPos)
            ).apply(instance, SightLinkerData::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SightLinkerData> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, SightLinkerData::mountPos,
                    SightLinkerData::new
            );
}
