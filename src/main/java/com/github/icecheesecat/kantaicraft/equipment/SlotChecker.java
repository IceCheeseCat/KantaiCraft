package com.github.icecheesecat.kantaicraft.equipment;

import java.util.Set;

public class SlotChecker {

    Set<EquipmentClass> set;

    public boolean contains(EquipmentClass type) {
        return set.contains(type);
    }

    public static SlotChecker create(Set<EquipmentClass> types) {
        SlotChecker sc = new SlotChecker();
        sc.set = types;
        return sc;
    }

}
