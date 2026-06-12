package com.lememz.cbcoptics.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
            case "com.lememz.cbcoptics.mixin.EntitySubLevelRotationHelperMixin" ->
                    isClassPresent("dev.ryanhcode.sable.Sable");
            case "com.lememz.cbcoptics.mixin.CullTaskMixin" ->
                    isClassPresent("dev.tr7zw.entityculling.EntityCullingMod");
            case "com.lememz.cbcoptics.mixin.CBCATCannonContraptionMixin" ->
                    isClassPresent("com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology");
            case "com.lememz.cbcoptics.mixin.CBCNWCannonContraptionMixin" ->
                    isClassPresent("riftyboi.cbcmodernwarfare.CBCModernWarfare");
            default -> true;
        };
    }

    private boolean isClassPresent(String className) {
        String resource = className.replace('.', '/') + ".class";
        return getClass().getClassLoader().getResource(resource) != null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
