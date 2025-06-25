//package com.github.icecheesecat.kantaicraft.datagen.equipment;
//
//import com.github.icecheesecat.kantaicraft.KantaiCraft;
//import com.github.icecheesecat.kantaicraft.datagen.EquipmentFile;
//import com.github.icecheesecat.kantaicraft.equipment.EquipmentStatType;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.common.data.ExistingFileHelper;
//
//import java.util.function.Function;
//
//public class CannonDataProvider extends EquipmentDataProvider {
//    private static final Function<ResourceLocation, EquipmentFile> factory
//            = rl -> new EquipmentFile(rl.getPath(), rl);
//    public CannonDataProvider(PackOutput output, String modid, String folder, ExistingFileHelper existingFileHelper) {
//        super(output, modid, folder, existingFileHelper, factory);
//    }
//
//    @Override
//    public void registerEquipments() {
//        simpleCannon(new ResourceLocation(KantaiCraft.MODID, "12cm_single_gun_mount"), 101, 0, 20.0d, 25.0d, 100)
//                .addStat(EquipmentStatType.FIREPOWER, 1.0d);
//    }
//
//    public EquipmentFile simpleCannon(ResourceLocation rl, int id, int size, double missile_velocity, double range, long cooldown) {
//        return getBuilder(rl.toString()).setId(id)
//                .addStat(EquipmentStatType.CANNON_SIZE, size)
//                .addStat(EquipmentStatType.CANNON_MISSILE_VELOCITY, missile_velocity)
//                .addStat(EquipmentStatType.CANNON_RANGE, range)
//                .addStat(EquipmentStatType.CANNON_COOLDOWN, cooldown);
//    }
//}
