package com.lememz.cbcoptics.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber
@ParametersAreNonnullByDefault
public final class CBCOpticsKeyMappings {

    private CBCOpticsKeyMappings() {}

    public static final List<Lazy<KeyMapping>> KEY_MAPPINGS = new ArrayList<>();
    public static final Lazy<KeyMapping> EXIT_SIGHT = addKeyMap(() -> new KeyMapping(
            "key.cbcoptics.exit_sight", KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN), "key.categories.cbcoptics"
    ));
    public static final Lazy<KeyMapping> ENTER_SIGHT = addKeyMap(() -> new KeyMapping(
            "key.cbcoptics.enter_sight", KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN), "key.categories.cbcoptics"
    ));
    public static final Lazy<KeyMapping> TOGGLE_SIGHT = addKeyMap(() -> new KeyMapping(
            "key.cbcoptics.toggle_sight", KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN), "key.categories.cbcoptics"
    ));
    public static final Lazy<KeyMapping> NEXT_SIGHT = addKeyMap(() -> new KeyMapping(
            "key.cbcoptics.next_sight", KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN), "key.categories.cbcoptics"
    ));
    public static final Lazy<KeyMapping> PREVIOUS_SIGHT = addKeyMap(() -> new KeyMapping(
            "key.cbcoptics.previous_sight", KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN), "key.categories.cbcoptics"
    ));

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        KEY_MAPPINGS.forEach(mapping -> event.register(mapping.get()));
    }

    private static Lazy<KeyMapping> addKeyMap(Supplier<KeyMapping> supplier) {
        Lazy<KeyMapping> mapping = Lazy.of(supplier);
        KEY_MAPPINGS.add(mapping);
        return mapping;
    }
}
