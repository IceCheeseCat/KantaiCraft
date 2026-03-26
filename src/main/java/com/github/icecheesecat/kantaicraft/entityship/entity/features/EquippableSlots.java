package com.github.icecheesecat.kantaicraft.entityship.entity.features;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EquippableSlots {

    Set<String> boneNames = new HashSet<>();

    public EquippableSlots() {
    }

    public EquippableSlots(String... boneNames) {
        this.boneNames = new HashSet<>(Arrays.stream(boneNames).toList());
    }

}
