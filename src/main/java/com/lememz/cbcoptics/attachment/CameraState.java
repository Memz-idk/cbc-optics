package com.lememz.cbcoptics.attachment;

import com.lememz.cbcoptics.block.CannonSightBlock;
import com.lememz.cbcoptics.compat.SableCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class CameraState {

    private final PitchOrientedContraptionEntity contraptionEntity;
    private final BlockPos sightPos;
    private @Nullable Vec3 prevSightPosition;
    private @Nullable Vector3f prevRotation;
    private Vec3 calculatedSightPosition;
    private Vector3f calculatedRotation;

    public CameraState(PitchOrientedContraptionEntity contraptionEntity, BlockPos sightPos, @Nullable Vec3 prevSightPosition, @Nullable Vector3f prevRotation, @Nullable Vec3 sightPosition, @Nullable Vector3f rotation) {
        this.contraptionEntity = contraptionEntity;
        this.sightPos = sightPos;
        this.prevSightPosition = prevSightPosition;
        this.prevRotation = prevRotation;
        this.calculatedSightPosition = sightPosition == null ? calculateSightPosition() : sightPosition;
        this.calculatedRotation = rotation == null ? calculateRotation() : rotation;
    }

    public CameraState(PitchOrientedContraptionEntity contraptionEntity, BlockPos sightPos) {
        this(contraptionEntity, sightPos, null, null, null, null);
    }

    public void update() {
        this.calculatedSightPosition = calculateSightPosition();
        this.calculatedRotation = calculateRotation();
    }

    public void savePrevState() {
        this.prevSightPosition = this.calculatedSightPosition;
        this.prevRotation = this.calculatedRotation;
    }

    private Vec3 calculateSightPosition() {
        return this.contraptionEntity.toGlobalVector(this.sightPos.getCenter(), 0);
    }

    public Vector3f calculateRotation() {
        Optional<StructureBlockInfo> block = this.contraptionEntity.getContraption().getBlocks().entrySet().stream()
                .filter(p -> p.getKey().equals(this.sightPos))
                .map(Map.Entry::getValue)
                .findFirst();
        if (block.isEmpty()) {
            throw new IllegalStateException("Could not find cannon sight on cannon");
        }
        Direction facing = block.get().state().getValue(CannonSightBlock.LOOKING);
        Vec3i forwardInt = facing.getNormal();
        Vector3f forward = new Vector3f(forwardInt.getX(), forwardInt.getY(), forwardInt.getZ());
        AbstractMountedCannonContraption cannon = (AbstractMountedCannonContraption)this.contraptionEntity.getContraption();
        Vector3f cannonUnrotatedForward = cannon.initialOrientation().step();
        Vector3f cannonForward = this.contraptionEntity
                .toGlobalVector(Vec3.atCenterOf(cannon.getStartPos().relative(cannon.initialOrientation())), 0)
                .subtract(this.contraptionEntity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 0))
                .normalize().toVector3f();
        Quaternionf cannonOrientation = new Quaternionf().rotateTo(cannonUnrotatedForward, cannonForward);
        cannonOrientation.transform(forward);
        float roll = 0;
        if(ModList.get().isLoaded(SableCompat.SABLE_ID)) {
            Vector3f up = new Vector3f(0, 1, 0);
            Quaternionf orientation = SableCompat.getCannonOrientation(this.contraptionEntity);
            orientation.transform(forward);
            Vector3f right = forward.cross(up, new Vector3f());
            orientation.transform(up);
            Vector3f refUp = right.cross(forward, new Vector3f());
            roll = (float)Math.toDegrees(Math.atan2(
                    up.dot(right),
                    up.dot(refUp)
            ));
        }
        float yaw = (float)Math.toDegrees(Math.atan2(-forward.get(0), forward.z));
        float pitch = (float)Math.toDegrees(Math.safeAsin(-forward.y));
        return new Vector3f(pitch, yaw, roll);
    }

    public Vec3 getSightInterpolatedPosition(float partialTick) {
        if(this.prevSightPosition == null) {
            return this.calculatedSightPosition;
        }
        return this.prevSightPosition.lerp(this.calculatedSightPosition, partialTick);
    }

    public Vector3f getInterpolatedRotation(float partialTick) {
        if(this.prevRotation == null) {
            return this.calculatedRotation;
        }
        return new Vector3f(
                Mth.lerp(partialTick, this.prevRotation.x, this.calculatedRotation.x),
                Mth.rotLerp(partialTick, this.prevRotation.y, this.calculatedRotation.y),
                Mth.lerp(partialTick, this.prevRotation.z, this.calculatedRotation.z)
        );
    }

    public PitchOrientedContraptionEntity getContraptionEntity() {
        return contraptionEntity;
    }

    public static class SyncHandler implements AttachmentSyncHandler<Optional<CameraState>> {

        @Override
        public void write(RegistryFriendlyByteBuf buf, Optional<CameraState> attachment, boolean initialSync) {
            attachment.ifPresent(cameraState -> {
                buf.writeInt(cameraState.contraptionEntity.getId());
                buf.writeBlockPos(cameraState.sightPos);
                buf.writeNullable(cameraState.prevSightPosition, FriendlyByteBuf::writeVec3);
                buf.writeNullable(cameraState.prevRotation, (b, v) -> b.writeVector3f(v));
                buf.writeNullable(cameraState.calculatedSightPosition, FriendlyByteBuf::writeVec3);
                buf.writeNullable(cameraState.calculatedRotation, (b, v) -> b.writeVector3f(v));
            });
        }

        @Override
        public Optional<CameraState> read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable Optional<CameraState> previousValue) {
            if (!(holder instanceof Player player)) {
                throw new IllegalStateException("CameraState attachment can only be attached to Player, instead got "+holder.getClass().getName());
            }
            if(!buf.isReadable()) {
                return Optional.empty();
            }
            Level level = player.level();
            PitchOrientedContraptionEntity contraptionEntity = (PitchOrientedContraptionEntity)level.getEntity(buf.readInt());
            if(contraptionEntity == null) {
                return Optional.empty();
            }
            BlockPos sightPos = buf.readBlockPos();
            Vec3 prevSightPos = buf.readNullable(FriendlyByteBuf::readVec3);
            Vector3f prevRotation = buf.readNullable(b -> b.readVector3f());
            Vec3 calculatedSightPosition = buf.readNullable(FriendlyByteBuf::readVec3);
            Vector3f calculatedRotation = buf.readNullable(b -> b.readVector3f());
            return Optional.of(new CameraState(
                    contraptionEntity, sightPos, prevSightPos, prevRotation, calculatedSightPosition, calculatedRotation
            ));
        }
    }
}
