package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.attachment.CameraState;
import com.lememz.cbcoptics.block.SightBlock;
import com.lememz.cbcoptics.data.SightLinkerData;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import com.lememz.cbcoptics.init.CBCOpticsDataComponentTypes;
import com.lememz.cbcoptics.init.CBCOpticsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import java.util.Optional;

@Mixin(PitchOrientedContraptionEntity.class)
public class PitchOrientedContraptionEntityMixin {

    @Shadow
    private BlockPos controllerPos;

    @Inject(method = "handlePlayerInteraction", at = @At("HEAD"), cancellable = true)
    public void cbcoptics$handlePlayerInteraction(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand, CallbackInfoReturnable<Boolean> cir) {
        if(player.getData(CBCOpticsAttachments.CAMERA_STATE).isPresent()) {
            player.getItemInHand(InteractionHand.MAIN_HAND).use(player.level(), player, InteractionHand.MAIN_HAND);
        }
        PitchOrientedContraptionEntity self = (PitchOrientedContraptionEntity)(Object)this;
        if(!(self.getContraption() instanceof AbstractMountedCannonContraption cannon)) {
            return;
        }
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        StructureTemplate.StructureBlockInfo sight = cannon.getBlocks().values().stream().filter(
                block -> block.state().getBlock() instanceof SightBlock
        ).findFirst().orElse(null);
        if(sight == null) {
            return;
        }
        if(item.is(CBCOpticsItems.SIGHT_LINKER)) {
            item.set(CBCOpticsDataComponentTypes.SIGHT_LINKER, new SightLinkerData(controllerPos));
            player.displayClientMessage(Component.translatable("message.cbcoptics.sight_linker_got"), true);
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }
        if (interactionHand != InteractionHand.MAIN_HAND || !item.is(CBCOpticsItems.SIGHT_VIEWER.get())) {
            return;
        }
        if(!player.level().isClientSide()) {
            player.setData(CBCOpticsAttachments.CAMERA_STATE, Optional.of(new CameraState(self, sight.pos())));
        }
        cir.setReturnValue(true);
        cir.cancel();
    }
}
