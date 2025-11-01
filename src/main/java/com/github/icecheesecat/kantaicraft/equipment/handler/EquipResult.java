package com.github.icecheesecat.kantaicraft.equipment.handler;

public enum EquipResult {

    FAIL(false),
    SUCCESS(true),
    BODY_PART_HAS_USED(false),
    HANDLER_IS_FULL(false),
    CANNOT_EQUIP_THIS_TYPE(false),
    OUT_OF_INDEX(false);

    private final boolean canEquip;

    EquipResult(boolean canEquip) {
        this.canEquip = canEquip;
    }
}
