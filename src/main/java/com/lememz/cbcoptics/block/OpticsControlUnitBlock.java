package com.lememz.cbcoptics.block;

import com.lememz.cbcoptics.attachment.AttachedOpticsControlUnit;
import com.lememz.cbcoptics.block.entity.OpticsControlUnitBlockEntity;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import com.lememz.cbcoptics.init.CBCOpticsItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OpticsControlUnitBlock extends Block implements EntityBlock {

    public OpticsControlUnitBlock() {
        super(BlockBehaviour.Properties.of().strength(1, 20));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OpticsControlUnitBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!stack.is(CBCOpticsItems.SIGHT_VIEWER)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        Optional<AttachedOpticsControlUnit> data = player.getData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT);
        if(data.isEmpty()) {
            player.displayClientMessage(Component.translatable("message.cbcoptics.optics_control_unit_attach"), true);
            player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.of(new AttachedOpticsControlUnit(pos)));
        }else {
            player.displayClientMessage(Component.translatable("message.cbcoptics.optics_control_unit_unattach"), true);
            player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.empty());
        }
        return ItemInteractionResult.SUCCESS;
    }
}
