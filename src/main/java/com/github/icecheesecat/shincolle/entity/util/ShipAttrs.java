package com.github.icecheesecat.shincolle.entity.util;

import com.github.icecheesecat.shincolle.Shincolle;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.INBTSerializable;

public class ShipAttrs implements INBTSerializable<CompoundTag> {

    private float firePower; // 0
    private float torpedo; // 1
    private float antiAir; // 2
    private float asw; //anti-submarine weaponry // 3
    private float los; //line of sight // 4
    private float luck; // 5
    private float health; // 6
    private float armor; // 7
    private float evasion; // 8
    private int speed; // 9
    private int airCraft; // 10
    private int fuel; // 11
    private int ammo; //12

    public ShipAttrs(float firePower) {
        this.firePower = firePower;
    }

    public ShipAttrs(float firePower, float torpedo, float antiAir, float asw, float los, float luck, float health, float armor, float evasion, int speed, int airCraft, int fuel, int ammo) {
        this.firePower = firePower;
        this.torpedo = torpedo;
        this.antiAir = antiAir;
        this.asw = asw;
        this.los = los;
        this.luck = luck;
        this.health = health;
        this.armor = armor;
        this.evasion = evasion;
        this.speed = speed;
        this.airCraft = airCraft;
        this.fuel = fuel;
        this.ammo = ammo;
    }

    public ShipAttrs(ShipAttrs attrs) {
        this.firePower = attrs.firePower;
        this.torpedo = attrs.torpedo;
        this.antiAir = attrs.antiAir;
        this.asw = attrs.asw;
        this.los = attrs.los;
        this.luck = attrs.luck;
        this.health = attrs.health;
        this.armor = attrs.armor;
        this.evasion = attrs.evasion;
        this.speed = attrs.speed;
        this.airCraft = attrs.airCraft;
        this.fuel = attrs.fuel;
        this.ammo = attrs.ammo;
    }

    private ShipAttrs(Builder builder) {
        this.firePower = builder.firePower;
        this.torpedo = builder.torpedo;
        this.antiAir = builder.antiAir;
        this.asw = builder.asw;
        this.los = builder.los;
        this.luck = builder.luck;
        this.health = builder.health;
        this.armor = builder.armor;
        this.evasion = builder.evasion;
        this.speed = builder.speed;
        this.airCraft = builder.airCraft;
        this.fuel = builder.fuel;
        this.ammo = builder.ammo;
    }

    public float getFirePower() {
        return firePower;
    }

    public void setFirePower(float firePower) {
        this.firePower = firePower;
    }

    public float getTorpedo() {
        return torpedo;
    }

    public void setTorpedo(float torpedo) {
        this.torpedo = torpedo;
    }

    public float getAntiAir() {
        return antiAir;
    }

    public void setAntiAir(float antiAir) {
        this.antiAir = antiAir;
    }

    public float getAsw() {
        return asw;
    }

    public void setAsw(float asw) {
        this.asw = asw;
    }

    public float getLos() {
        return los;
    }

    public void setLos(float los) {
        this.los = los;
    }

    public float getLuck() {
        return luck;
    }

    public void setLuck(float luck) {
        this.luck = luck;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public float getEvasion() {
        return evasion;
    }

    public void setEvasion(float evasion) {
        this.evasion = evasion;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAirCraft() {
        return airCraft;
    }

    public void setAirCraft(int airCraft) {
        this.airCraft = airCraft;
    }

    public int getMaxFuelConsumption() {
        return fuel;
    }

    public void setMaxFuelConsumption(int fuel) {
        this.fuel = fuel;
    }

    public int getMaxAmmoConsumption() {
        return ammo;
    }

    public void setMaxAmmoConsumption(int ammo) {
        this.ammo = ammo;
    }

    @Override
    public CompoundTag serializeNBT() {
//        CompoundTag nbt = new CompoundTag();
        ListTag float_list = new ListTag();
        ListTag int_list = new ListTag();

        float_list.add(FloatTag.valueOf(this.firePower));
        float_list.add(FloatTag.valueOf(this.torpedo));
        float_list.add(FloatTag.valueOf(this.antiAir));
        float_list.add(FloatTag.valueOf(this.asw));
        float_list.add(FloatTag.valueOf(this.los));
        float_list.add(FloatTag.valueOf(this.luck));
        float_list.add(FloatTag.valueOf(this.health));
        float_list.add(FloatTag.valueOf(this.armor));
        float_list.add(FloatTag.valueOf(this.evasion));
        int_list.add(IntTag.valueOf(this.speed));
        int_list.add(IntTag.valueOf(this.airCraft));
        int_list.add(IntTag.valueOf(this.fuel));
        int_list.add(IntTag.valueOf(this.ammo));

        CompoundTag t = new CompoundTag();
          t.put(Shincolle.MODID + ".shipattrs.floats", float_list);
          t.put(Shincolle.MODID + ".shipattrs.ints", int_list);
        return t;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag float_list = new ListTag();
            float_list = nbt.getList(Shincolle.MODID + ".shipattrs.floats", Tag.TAG_FLOAT);
        ListTag int_list = new ListTag();
            int_list = nbt.getList(Shincolle.MODID + ".shipattrs.floats", Tag.TAG_INT);
        this.firePower = float_list.getFloat(0);
        this.torpedo = float_list.getFloat(1);
        this.antiAir = float_list.getFloat(2);
        this.asw = float_list.getFloat(3);
        this.los = float_list.getFloat(4);
        this.luck = float_list.getFloat(5);
        this.health = float_list.getFloat(6);
        this.armor = float_list.getFloat(7);
        this.evasion = float_list.getFloat(8);
        this.speed = int_list.getInt(0);
        this.airCraft = int_list.getInt(1);
        this.fuel = int_list.getInt(2);
        this.ammo = int_list.getInt(3);
    }

    public static final class Builder {
        private float firePower;
        private float torpedo;
        private float antiAir;
        private float asw;
        private float los;
        private float luck;
        private float health;
        private float armor;
        private float evasion;
        private int speed;
        private int airCraft;
        private int fuel;
        private int ammo;

        public Builder() {

        }

        public static Builder aShipStatus() {
            return new Builder();
        }

        public Builder withFirePower(float firePower) {
            this.firePower = firePower;
            return this;
        }

        public Builder withTorpedo(float torpedo) {
            this.torpedo = torpedo;
            return this;
        }

        public Builder withAntiAir(float antiAir) {
            this.antiAir = antiAir;
            return this;
        }

        public Builder withAsw(float asw) {
            this.asw = asw;
            return this;
        }

        public Builder withLos(float los) {
            this.los = los;
            return this;
        }

        public Builder withLuck(float luck) {
            this.luck = luck;
            return this;
        }

        public Builder withHealth(float health) {
            this.health = health;
            return this;
        }

        public Builder withArmor(float armor) {
            this.armor = armor;
            return this;
        }

        public Builder withEvasion(float evasion) {
            this.evasion = evasion;
            return this;
        }

        public Builder withSpeed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder withAirCraft(int airCraft) {
            this.airCraft = airCraft;
            return this;
        }

        public Builder withFuel(int fuel) {
            this.fuel = fuel;
            return this;
        }

        public Builder withAmmo(int ammo) {
            this.ammo = ammo;
            return this;
        }

        public ShipAttrs build() {
            return new ShipAttrs(firePower, torpedo, antiAir, asw, los, luck, health, armor, evasion, speed, airCraft, fuel, ammo);
        }
    }
}
