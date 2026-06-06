package com.lememz.cbcoptics.init;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.attachment.CameraState;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

public final class CBCOpticsAttachments {

    private CBCOpticsAttachments() {}

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CBCOptics.MOD_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<CameraState>>> CAMERA_STATE =
            ATTACHMENT_TYPES.register("camera_state", () ->
                    AttachmentType.<Optional<CameraState>>builder(Optional::empty)
                            .sync(new CameraState.SyncHandler())
                            .build()
            );
}
