package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.attachment.CameraState;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import com.lememz.cbcoptics.init.CBCOpticsBlocks;
import com.lememz.cbcoptics.init.CBCOpticsItems;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import java.util.Optional;

@Mixin(PitchOrientedContraptionEntity.class)
public class PitchOrientedContraptionEntityMixin {

    @Inject(method = "handlePlayerInteraction", at = @At("HEAD"), cancellable = true)
    public void cbcoptics$handlePlayerInteraction(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand, CallbackInfoReturnable<Boolean> cir) {
        if(player.getData(CBCOpticsAttachments.CAMERA_STATE).isPresent()) {
            player.getItemInHand(InteractionHand.MAIN_HAND).use(player.level(), player, InteractionHand.MAIN_HAND);
        }
        PitchOrientedContraptionEntity self = (PitchOrientedContraptionEntity)(Object)this;
        boolean isUsingViewer = player.getItemInHand(InteractionHand.MAIN_HAND).is(CBCOpticsItems.SIGHT_VIEWER.get());
        if (interactionHand != InteractionHand.MAIN_HAND || !isUsingViewer || !(self.getContraption() instanceof MountedBigCannonContraption cannon)) {
            return;
        }
        StructureTemplate.StructureBlockInfo sight = cannon.getBlocks().values().stream().filter(
                block -> block.state().is(CBCOpticsBlocks.CANNON_SIGHT.get())
        ).findFirst().orElse(null);
        if(sight == null) {
            return;
        }
        if(!player.level().isClientSide()) {
            player.setData(CBCOpticsAttachments.CAMERA_STATE, Optional.of(new CameraState(self, sight.pos())));
        }
        cir.setReturnValue(true);
        cir.cancel();
    }
}
