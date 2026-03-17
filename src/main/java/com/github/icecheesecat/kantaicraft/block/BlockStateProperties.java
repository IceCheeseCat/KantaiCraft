package com.github.icecheesecat.kantaicraft.block;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BlockStateProperties {
    public static final BooleanProperty IS_FACILITY_CORE = BooleanProperty.create("is_facility_core");
    public static final BooleanProperty WORKING_FACILITY = BooleanProperty.create("working_facility");
    public static final DirectionProperty FACILITY_FACING = DirectionProperty.create("facility_facing");

}
