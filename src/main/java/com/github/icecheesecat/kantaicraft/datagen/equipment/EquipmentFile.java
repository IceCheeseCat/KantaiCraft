//package com.github.icecheesecat.kantaicraft.datagen;
//
//import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
//import com.google.gson.JsonObject;
//import net.minecraft.resources.ResourceLocation;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class EquipmentFile {
//    String name;
//    int id;
//    Map<EquipmentStatType, Number> stats = new HashMap<>();
//    ResourceLocation loc;
//
//    public EquipmentFile(String name, ResourceLocation loc) {
//        this.name = name;
//        this.loc = loc;
//    }
//
//    public EquipmentFile setId(int id) {
//        this.id = id;
//        return this;
//    }
//
//    public ResourceLocation getLoc() {
//        return loc;
//    }
//
//    public JsonObject toJson() {
//        JsonObject root = new JsonObject();
//
//        root.addProperty("name:", this.name);
//        root.addProperty("id:", this.id);
//
//        JsonObject statObject = new JsonObject();
//        for (var stat: this.stats.entrySet()) {
//            statObject.addProperty(stat.getKey().name(), stat.getValue());
//        }
//
//        root.add("stats", statObject);
//
//        return root;
//    }
//
//    public EquipmentFile addStat(EquipmentStatType statType, Number value) {
//        this.stats.put(statType, value);
//        return this;
//    }
//}
