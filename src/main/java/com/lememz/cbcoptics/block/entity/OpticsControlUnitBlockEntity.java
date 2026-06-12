package com.lememz.cbcoptics.block.entity;

import com.lememz.cbcoptics.init.CBCOpticsBlockEntityTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OpticsControlUnitBlockEntity extends BlockEntity {

    private final List<BlockPos> entries = new ArrayList<>();

    public OpticsControlUnitBlockEntity(BlockPos pos, BlockState blockState) {
        super(CBCOpticsBlockEntityTypes.OPTICS_CONTROL_UNIT.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.entries.clear();
        ListTag entries = tag.getList("entries", Tag.TAG_COMPOUND);
        for(int i = 0; i < entries.size(); ++i) {
            CompoundTag entryTag = entries.getCompound(i);
            Optional<BlockPos> mountPos = NbtUtils.readBlockPos(entryTag, "mount_pos");
            if(mountPos.isEmpty()) {
                continue;
            }
            this.entries.add(mountPos.get());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag entries = new ListTag();
        for(var entry : this.entries) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.put("mount_pos", NbtUtils.writeBlockPos(entry));
            entries.add(entryTag);
        }
        tag.put("entries", entries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    private void sync() {
        if(this.level != null && !this.level.isClientSide) {
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void addEntry(BlockPos entry) {
        this.entries.add(entry);
        this.setChanged();
        this.sync();
    }

    public void clearEntries() {
        this.entries.clear();
        this.setChanged();
        this.sync();
    }

    public List<BlockPos> getEntries() {
        return this.entries;
    }
}
