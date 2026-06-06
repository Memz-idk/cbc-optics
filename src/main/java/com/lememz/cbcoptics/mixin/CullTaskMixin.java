package com.lememz.cbcoptics.mixin;

import com.lememz.cbcoptics.attachment.CameraState;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import com.logisticscraft.occlusionculling.util.Vec3d;
import dev.tr7zw.entityculling.CullTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;

@Mixin(CullTask.class)
public class CullTaskMixin {

    @SuppressWarnings("SpellCheckingInspection")
    @Final
    @Shadow
    private Set<EntityType<?>> entityWhistelist;

    @Inject(method = "cullEntities", at = @At("HEAD"))
    public void cbcoptics$cullEntities(Vec3 cameraMC, Vec3d camera, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }
        Optional<CameraState> cameraState = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        if(cameraState.isEmpty()) {
            return;
        }
        entityWhistelist.add(cameraState.get().getContraptionEntity().getType());
    }
}
