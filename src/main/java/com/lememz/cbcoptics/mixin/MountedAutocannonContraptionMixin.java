package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.init.CBCOpticsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;

import java.util.HashMap;
import java.util.Map;

@Mixin(MountedAutocannonContraption.class)
public class MountedAutocannonContraptionMixin {

    @Inject(method = "collectCannonBlocks", at = @At("TAIL"))
    private void cbcoptics$collectCannonBlocks(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        MountedAutocannonContraption self = (MountedAutocannonContraption)(Object)this;
        Map<BlockPos, StructureBlockInfo> toAdd = new HashMap<>();
        outer:
        for(var entry : self.getBlocks().entrySet()) {
            for(var direction : Direction.values()) {
                BlockPos relPos = entry.getKey().relative(direction);
                BlockState block = level.getBlockState(relPos.offset(self.anchor));
                if(block.is(CBCOpticsBlocks.AUTOCANNON_SIGHT)) {
                    toAdd.put(relPos, new StructureBlockInfo(relPos, block, null));
                    break outer;
                }
            }
        }
        self.getBlocks().putAll(toAdd);
    }
}
