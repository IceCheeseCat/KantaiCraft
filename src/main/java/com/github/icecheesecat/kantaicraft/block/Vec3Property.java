package com.github.icecheesecat.kantaicraft.block;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class Vec3Property extends Property<Vec3i> {
    public Vec3Property(String pName) {
        super(pName, Vec3i.class);
    }

    @Override
    public Collection<Vec3i> getPossibleValues() {
        return Collections.emptyList();
    }

    @Override
    public String getName(Vec3i vec) {
        return vec.toShortString();
    }

    @Override
    public Optional<Vec3i> getValue(String pValue) {
        return Optional.empty();
    }
}
