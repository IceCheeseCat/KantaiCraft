package com.github.icecheesecat.kantaicraft.entityship.entity;

import java.util.*;

public class EquippableSlots {

    Set<String> boneNames = new HashSet<>();

    public EquippableSlots() {
    }

    public EquippableSlots(String... boneNames) {
        this.boneNames = new HashSet<>(Arrays.stream(boneNames).toList());
    }

}
