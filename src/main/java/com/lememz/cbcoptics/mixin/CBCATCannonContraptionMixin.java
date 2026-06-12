package com.lememz.cbcoptics.mixin;

import com.dsvv.cbcat.cannon.heavy_autocannon.contraption.MountedHeavyAutocannonContraption;
import com.dsvv.cbcat.cannon.medium_rocketpod.contraption.MountedMediumRocketRailContraption;
import com.dsvv.cbcat.cannon.rocketpod.contraption.MountedRocketPodContraption;
import com.dsvv.cbcat.cannon.twin_autocannon.contraption.MountedTwinAutocannonContraption;
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

import java.util.HashMap;
import java.util.Map;

@Mixin({
        MountedHeavyAutocannonContraption.class, MountedMediumRocketRailContraption.class,
        MountedRocketPodContraption.class, MountedTwinAutocannonContraption.class
})
public class CBCATCannonContraptionMixin {

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
