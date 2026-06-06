package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.mixinhelpers.camera.camera_rotation.EntitySubLevelRotationHelper;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaterniond;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(EntitySubLevelRotationHelper.class)
public class EntitySubLevelRotationHelperMixin {

    @Inject(method = "getEntityOrientation", at = @At("HEAD"), cancellable = true)
    private static void cbcoptics$getEntityOrientation(Entity cameraEntity, Function<SubLevel, Pose3dc> poseProvider, float partialTicks, EntitySubLevelRotationHelper.Type type, CallbackInfoReturnable<Quaterniond> cir) {
        if(cameraEntity instanceof Player player && player.getData(CBCOpticsAttachments.CAMERA_STATE).isPresent()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
