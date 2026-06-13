package com.lememz.cbcoptics.event;

import com.lememz.cbcoptics.CBCOptics;
import com.lememz.cbcoptics.attachment.AttachedOpticsControlUnit;
import com.lememz.cbcoptics.attachment.CameraState;
import com.lememz.cbcoptics.block.SightBlock;
import com.lememz.cbcoptics.block.entity.OpticsControlUnitBlockEntity;
import com.lememz.cbcoptics.init.CBCOpticsAttachments;
import com.lememz.cbcoptics.init.CBCOpticsKeyMappings;
import com.lememz.cbcoptics.mixin.ControllerPosAccessor;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.joml.Matrix4f;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }
        Optional<CameraState> state = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        state.ifPresent(cameraState -> {
            cameraState.savePrevState();
            cameraState.update();
        });
        if(CBCOpticsKeyMappings.TOGGLE_SIGHT.get().consumeClick()) {
            if(state.isPresent()) player.setData(CBCOpticsAttachments.CAMERA_STATE, Optional.empty());
            else enterSight(player);
            return;
        }
        if(state.isPresent() && CBCOpticsKeyMappings.EXIT_SIGHT.get().isDown()) {
            player.setData(CBCOpticsAttachments.CAMERA_STATE, Optional.empty());
            return;
        }
        if(state.isEmpty() && CBCOpticsKeyMappings.ENTER_SIGHT.get().isDown()) {
            CBCOpticsKeyMappings.ENTER_SIGHT.get().consumeClick();
            enterSight(player);
            return;
        }
        if(state.isPresent() && CBCOpticsKeyMappings.ENTER_SIGHT.get().consumeClick()) {
            enterSight(player);
            return;
        }
        if(state.isPresent() && CBCOpticsKeyMappings.NEXT_SIGHT.get().consumeClick()) {
            enterSight(player);
            return;
        }
        if(state.isPresent() && CBCOpticsKeyMappings.PREVIOUS_SIGHT.get().consumeClick()) {
            enterSight(player, -1);
        }
    }

    private static @Nullable PitchOrientedContraptionEntity getCannonForEnterSight(LocalPlayer player, int multiSightScrollOffset) {
        Optional<AttachedOpticsControlUnit> attach = player.getData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT);
        if(attach.isPresent()) {
            Optional<CameraState> cameraState = player.getData(CBCOpticsAttachments.CAMERA_STATE);
            BlockEntity be = player.level().getBlockEntity(attach.get().pos());
            if(!(be instanceof OpticsControlUnitBlockEntity unit)) {
                player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.empty());
                return getCannonForEnterSight(player, multiSightScrollOffset);
            }
            List<BlockPos> entries = unit.getEntries();
            if(entries.isEmpty()) {
                player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.empty());
                return getCannonForEnterSight(player, multiSightScrollOffset);
            }
            int idx = 0;
            if(cameraState.isPresent()) {
                CameraState state = cameraState.get();
                BlockPos stateMountPos = ((ControllerPosAccessor)state.getContraptionEntity()).getControllerPos();
                for(int i = 0; i < entries.size(); ++i) {
                    if(entries.get(i).equals(stateMountPos)) {
                        idx = (((i+multiSightScrollOffset)%entries.size())+entries.size())%entries.size();
                        break;
                    }
                }
            }
            BlockPos finalPos = entries.get(idx);
            BlockEntity beMount = player.level().getBlockEntity(finalPos);
            if(beMount == null) {
                player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.empty());
                return getCannonForEnterSight(player, multiSightScrollOffset);
            }
            try {
                Field f = beMount.getClass().getDeclaredField("mountedContraption");
                f.setAccessible(true);
                PitchOrientedContraptionEntity entity = (PitchOrientedContraptionEntity)f.get(beMount);
                if(entity == null || entity.isRemoved()) {
                    player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.empty());
                    return getCannonForEnterSight(player, multiSightScrollOffset);
                }
                return entity;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                player.setData(CBCOpticsAttachments.ATTACHED_OPTICS_CONTROL_UNIT, Optional.empty());
                return getCannonForEnterSight(player, multiSightScrollOffset);
            }
        }
        List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(5));
        Optional<Entity> possibleCannon = entities.stream()
                .filter(entity -> entity instanceof PitchOrientedContraptionEntity)
                .min(Comparator.comparing(player::distanceToSqr));
        return (PitchOrientedContraptionEntity)possibleCannon.orElse(null);
    }

    private static void enterSight(LocalPlayer player) {
        enterSight(player, 1);
    }

    private static void enterSight(LocalPlayer player, int multiSightScrollOffset) {
        PitchOrientedContraptionEntity cannon = getCannonForEnterSight(player, multiSightScrollOffset);
        if(cannon == null) {
            return;
        }
        StructureTemplate.StructureBlockInfo sight = cannon.getContraption().getBlocks().values().stream().filter(
                block -> block.state().getBlock() instanceof SightBlock
        ).findFirst().orElse(null);
        if(sight == null) {
            return;
        }
        player.setData(CBCOpticsAttachments.CAMERA_STATE, Optional.of(new CameraState(cannon, sight.pos())));
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }
        Optional<CameraState> state = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        if(state.isPresent()) {
            event.setCanceled(true);
        }
    }

    private static final ResourceLocation SIGHT_CROSSHAIR = ResourceLocation.fromNamespaceAndPath(CBCOptics.MOD_ID, "textures/gui/sight_crosshair.png");
    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        if(!event.getName().equals(VanillaGuiLayers.CROSSHAIR)) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            return;
        }
        Optional<CameraState> state = player.getData(CBCOpticsAttachments.CAMERA_STATE);
        if(state.isEmpty()) {
            return;
        }
        event.setCanceled(true);
        Window window = Minecraft.getInstance().getWindow();
        int size = window.getGuiScaledWidth();
        int x = window.getGuiScaledWidth()/2 - size/2;
        int y = window.getGuiScaledHeight()/2 - size/2;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SIGHT_CROSSHAIR);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Matrix4f pose = event.getGuiGraphics().pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance()
                .begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(pose, x, y + size, 0).setUv(0, 1);
        buffer.addVertex(pose, x + size, y + size, 0).setUv(1, 1);
        buffer.addVertex(pose, x + size, y, 0).setUv(1, 0);
        buffer.addVertex(pose, x, y, 0).setUv(0, 0);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }
}
