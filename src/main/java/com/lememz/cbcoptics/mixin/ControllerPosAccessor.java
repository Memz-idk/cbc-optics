package com.lememz.cbcoptics.mixin;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(PitchOrientedContraptionEntity.class)
public interface ControllerPosAccessor {

    @Accessor("controllerPos")
    BlockPos getControllerPos();
}
