package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TrajectoryHitResult extends HitResult {

    public static final TrajectoryHitResult MISS = new TrajectoryHitResult(Vec3.ZERO, Type.MISS, null, null);

    final Type type;
    final BlockPos blockPos;
    final Entity entity;

    protected TrajectoryHitResult(Vec3 pLocation, Type type, @Nullable BlockPos hitBlock, @Nullable Entity hitEntity) {
        super(pLocation);
        this.type = type;
        this.blockPos = hitBlock;
        this.entity = hitEntity;
    }

    public TrajectoryHitResult(Vec3 pLocation, BlockPos blockPos) {
        this(pLocation, Type.BLOCK, blockPos, null);
    }

    public TrajectoryHitResult(Vec3 pLocation, Entity entity) {
        this(pLocation, Type.ENTITY, null, entity);
    }

    public boolean missed() {
        return this.type == Type.MISS;
    }

    @Override
    public Type getType() {
        return type;
    }

}
