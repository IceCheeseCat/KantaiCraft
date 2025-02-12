package com.github.icecheesecat.kantaicraft.faction;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class FactionEvent extends Event {

    protected FactionTag factionTag;
    protected LivingEntity entity;
    protected Level level;

    public FactionEvent(FactionTag factionTag, LivingEntity entity, Level level) {
        this.factionTag = factionTag;
        this.entity = entity;
        this.level = level;
    }

    public FactionTag getFactionTag() {
        return factionTag;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Level getLevel() {
        return level;
    }



    public static class Join extends FactionEvent {

        public Join(FactionTag factionTag, LivingEntity entity, Level level) {
            super(factionTag, entity, level);
        }
    }

    public static class Change extends FactionEvent {
        protected FactionTag originFactionTag;
        public Change(FactionTag originFactionTag, FactionTag newFactionTag, LivingEntity entity, Level level) {
            super(newFactionTag, entity, level);
            this.originFactionTag = originFactionTag;
        }

        public FactionTag getOriginFactionTag() {
            return originFactionTag;
        }
    }

    public static class Create extends FactionEvent {
        public Create(FactionTag factionTag, LivingEntity creator, Level level) {
            super(factionTag, creator, level);
        }
    }

}
