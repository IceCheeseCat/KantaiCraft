package com.github.icecheesecat.kantaicraft.equipment;

import java.util.ArrayList;
import java.util.List;

public class EquipmentTreeNode {
    Equipment equipment;
    List<EquipmentTreeNode> children = new ArrayList<>();

    public EquipmentTreeNode(Equipment equipment) {
        this.equipment = equipment;
    }

    public EquipmentTreeNode(Equipment equipment, List<EquipmentTreeNode> children) {
        this(equipment);
        this.children = children;
    }

//    public static EquipmentTreeNode create(Equipment equipment, List<EquipmentTreeNode> children) {
//        EquipmentTreeNode parent = new EquipmentTreeNode(equipment);
//        parent.children = children;
//
//        return parent;
//    }

}
