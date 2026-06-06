package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.block.CannonSightBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;

import java.util.List;

@Mixin(MountedBigCannonContraption.class)
public class MountedBigCannonContraptionMixin {

    @Inject(method = "collectCannonBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;registryAccess()Lnet/minecraft/core/RegistryAccess;"))
    private void cbcoptics$collectCannonBlocks(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local List<StructureBlockInfo> cannonBlocks) {
        outer:
        for(int i = 0; i < cannonBlocks.size(); ++i) {
            for(var direction : Direction.values()) {
                BlockPos relPos = cannonBlocks.get(i).pos().relative(direction);
                BlockState block = level.getBlockState(relPos);
                if(block.getBlock() instanceof CannonSightBlock) {
                    cannonBlocks.add(new StructureBlockInfo(relPos, block, null));
                    break outer;
                }
            }
        }
    }
}
