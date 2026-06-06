package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.attachment.CameraState;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void cbcoptics$bobView(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }
        Optional<CameraState> state = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        if(state.isPresent()) {
            ci.cancel();
        }
    }
}
