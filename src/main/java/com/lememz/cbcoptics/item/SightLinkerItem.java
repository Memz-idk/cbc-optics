package com.lememz.cbcoptics.item;

import com.lememz.cbcoptics.block.entity.OpticsControlUnitBlockEntity;
import com.lememz.cbcoptics.data.SightLinkerData;
import com.lememz.cbcoptics.init.CBCOpticsDataComponentTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SightLinkerItem extends Item {

    public SightLinkerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if(!(blockEntity instanceof OpticsControlUnitBlockEntity unit) || context.getPlayer() == null) {
            return super.useOn(context);
        }
        if(context.getPlayer().isCrouching()) {
            unit.clearEntries();
            context.getPlayer().displayClientMessage(Component.translatable("message.cbcoptics.sight_linker_clear"), true);
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        SightLinkerData data = context.getItemInHand().get(CBCOpticsDataComponentTypes.SIGHT_LINKER);
        if(data == null) {
            return super.useOn(context);
        }
        if(unit.getEntries().contains(data.mountPos())) {
            return super.useOn(context);
        }
        unit.addEntry(data.mountPos());
        context.getPlayer().displayClientMessage(Component.translatable("message.cbcoptics.sight_linker_add"), true);
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }
}
