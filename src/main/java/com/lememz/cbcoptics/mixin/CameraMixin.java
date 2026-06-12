package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.attachment.CameraState;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    protected void setPosition(double x, double y, double z) {}

    @Shadow
    protected void setRotation(float yRot, float xRot, float roll) {}

    @Inject(method = "setup", at = @At("TAIL"))
    public void cbcoptics$setup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if(!(entity instanceof Player player)) {
            return;
        }
        Optional<CameraState> state = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        if(state.isEmpty()) {
            return;
        }
        CameraState camera = state.get();
        Vec3 pos = camera.getSightInterpolatedPosition(partialTick);
        Vector3f rotation = camera.getInterpolatedRotation(partialTick);
        this.setPosition(pos.x, pos.y, pos.z);
        this.setRotation(rotation.y, rotation.x, rotation.z);
    }

    @Inject(method = "isDetached", at = @At("HEAD"), cancellable = true)
    public void cbcoptics$isDetached(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }
        Optional<CameraState> state = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        if(state.isPresent()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
