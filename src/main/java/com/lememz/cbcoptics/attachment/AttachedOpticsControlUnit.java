package com.lememz.cbcoptics.attachment;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public record AttachedOpticsControlUnit(BlockPos pos) {

    public static class SyncHandler implements AttachmentSyncHandler<Optional<AttachedOpticsControlUnit>> {

        @Override
        public void write(RegistryFriendlyByteBuf buf, Optional<AttachedOpticsControlUnit> attachment, boolean initialSync) {
            attachment.ifPresent(state -> buf.writeBlockPos(state.pos));
        }

        @Override
        public @Nullable Optional<AttachedOpticsControlUnit> read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable Optional<AttachedOpticsControlUnit> previousValue) {
            if(!buf.isReadable()) {
                return Optional.empty();
            }
            return Optional.of(new AttachedOpticsControlUnit(buf.readBlockPos()));
        }
    }
}
