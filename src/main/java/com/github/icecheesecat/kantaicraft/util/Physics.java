package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

public class Physics implements INBTSerializable<CompoundTag> {

    Vec3 pos;
    Vec3 vel;
    Vec3 acc;
    Vec3 prevPos;

    static final float dt = 0.01f;

    public Physics() {
    }

    public Physics(Vec3 pos, Vec3 vel, Vec3 acc) {
        this.pos = this.prevPos = pos;
        this.vel = vel;
        this.acc = acc;
    }

    public static Physics create(CompoundTag nbt) {
        Physics physics = new Physics();
        physics.deserializeNBT(nbt);
        return physics;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("pos", CompoundTagHelper.putVec3(this.pos));
        nbt.put("vel", CompoundTagHelper.putVec3(this.vel));
        nbt.put("acc", CompoundTagHelper.putVec3(this.acc));
        nbt.put("prevpos", CompoundTagHelper.putVec3(this.prevPos));

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.pos = CompoundTagHelper.getVec3(nbt.getCompound("pos"));
        this.vel = CompoundTagHelper.getVec3(nbt.getCompound("vel"));
        this.acc = CompoundTagHelper.getVec3(nbt.getCompound("acc"));
        this.prevPos = CompoundTagHelper.getVec3(nbt.getCompound("prevpos"));

    }

    // midpoint method
    protected void update() {
        prevPos = new Vec3(pos.x, pos.y, pos.z);
        float half_dt = dt/2.0f;
        Vec3 vel_at_half = vel.add(acc.scale(half_dt));
        pos = pos.add(vel_at_half.scale(dt));
        vel = vel.add(acc.scale(dt));
    }

    public void setNextPosition(Vec3 newPos) {
        this.prevPos = this.pos;
        this.pos = newPos;
    }

    public Vec3 displacement() {
        return this.pos.subtract(this.prevPos);
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getVel() {
        return vel;
    }

    public Vec3 getAcc() {
        return acc;
    }

    public Vec3 getPrevPos() {
        return prevPos;
    }
}
