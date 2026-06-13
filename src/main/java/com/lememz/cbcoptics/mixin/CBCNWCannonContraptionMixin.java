package com.lememz.cbcoptics.mixin;

import com.dsvv.cbcat.cannon.medium_rocketpod.contraption.MountedMediumRocketRailContraption;
import com.lememz.cbcoptics.init.CBCOpticsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMediumcannonContraption;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedMunitionsLauncherContraption;
import riftyboi.cbcmodernwarfare.cannon_control.contraption.MountedRotarycannonContraption;

import java.util.HashMap;
import java.util.Map;

@Mixin({
        MountedMediumcannonContraption.class, MountedMunitionsLauncherContraption.class,
        MountedMediumRocketRailContraption.class, MountedRotarycannonContraption.class
})
public class CBCNWCannonContraptionMixin {

    @Inject(method = "collectCannonBlocks", at = @At("TAIL"))
    private void cbcoptics$collectCannonBlocks(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        AbstractMountedCannonContraption self = (AbstractMountedCannonContraption)(Object)this;
        Map<BlockPos, StructureTemplate.StructureBlockInfo> toAdd = new HashMap<>();
        outer:
        for(var entry : self.getBlocks().entrySet()) {
            for(var direction : Direction.values()) {
                BlockPos relPos = entry.getKey().relative(direction);
                BlockState block = level.getBlockState(relPos.offset(self.anchor));
                if(block.is(CBCOpticsBlocks.AUTOCANNON_SIGHT)) {
                    toAdd.put(relPos, new StructureTemplate.StructureBlockInfo(relPos, block, null));
                    break outer;
                }
            }
        }
        self.getBlocks().putAll(toAdd);
    }
}
