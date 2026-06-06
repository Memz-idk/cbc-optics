package com.lememz.cbcoptics.compat;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import org.joml.Quaternionf;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public final class SableCompat {

    private SableCompat() {}

    public static final String SABLE_ID = "sable";

    public static Quaternionf getCannonOrientation(PitchOrientedContraptionEntity entity) {
        SubLevelAccess sub = SableCompanion.INSTANCE.getContaining(entity);
        if(sub == null) {
            return new Quaternionf().identity();
        }
        return new Quaternionf(sub.logicalPose().orientation());
    }
}
