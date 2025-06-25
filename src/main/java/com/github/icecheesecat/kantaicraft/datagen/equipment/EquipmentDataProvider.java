//package com.github.icecheesecat.kantaicraft.datagen.equipment;
//
//import com.github.icecheesecat.kantaicraft.KantaiCraft;
//import com.github.icecheesecat.kantaicraft.datagen.EquipmentFile;
//import com.google.common.base.Preconditions;
//import net.minecraft.data.CachedOutput;
//import net.minecraft.data.DataProvider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.packs.PackType;
//import net.minecraftforge.common.data.ExistingFileHelper;
//
//import java.nio.file.Path;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Function;
//
//public abstract class EquipmentDataProvider implements DataProvider {
//
//    protected static final ExistingFileHelper.ResourceType EQUIPMENT = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", "equipments");
//
//    private static final Map<ResourceLocation, EquipmentFile> generatedFiles = new HashMap<>();
//    private PackOutput packOutput;
//    private String modid;
//    private String folder;
//    private ExistingFileHelper existingFileHelper;
//    private final Function<ResourceLocation, EquipmentFile> factory;
//    public static final String SMALL_CANNON_FOLDER = "small_cannon";
//
//    public EquipmentDataProvider(PackOutput output, String modid, String folder, ExistingFileHelper existingFileHelper, Function<ResourceLocation, EquipmentFile> factory) {
//        this.packOutput = output;
//        this.modid = modid;
//        this.folder = folder;
//        this.existingFileHelper = existingFileHelper;
//        this.factory = factory;
//    }
//
//
//    @Override
//    public CompletableFuture<?> run(CachedOutput cache) {
//        generatedFiles.clear();
//        registerEquipments();
//        return generateAll(cache);
//    }
//
//    protected CompletableFuture<?> generateAll(CachedOutput cache) {
//        CompletableFuture<?>[] futures = new CompletableFuture<?>[this.generatedFiles.size()];
//        int i = 0;
//
//        for (var equipmentFile : this.generatedFiles.values()) {
//            Path target = getPath(equipmentFile);
//            futures[i++] = DataProvider.saveStable(cache, equipmentFile.toJson(), target);
//        }
//
//        return CompletableFuture.allOf(futures);
//    }
//
//    protected Path getPath(EquipmentFile file) {
//        ResourceLocation loc = file.getLoc();
//        return this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(loc.getNamespace()).resolve("equipments").resolve(loc.getPath() + ".json");
//    }
//
//    public abstract void registerEquipments();
//
//    public void create(ResourceLocation resourceLocation) {
//        getBuilder(resourceLocation.toString());
//    }
//
//    @Override
//    public String getName() {
//        return "Equipment File: " + this.modid;
//    }
//
//    private ResourceLocation extendWithFolder(ResourceLocation rl) {
//        if (rl.getPath().contains("/")) {
//            System.out.println(rl);
//            return rl;
//        }
//
//        var retLoc = new ResourceLocation(rl.getNamespace(), folder + "/" + rl.getPath());
//        System.out.println(retLoc);
//        return retLoc;
//    }
//
//    public ResourceLocation modLoc(String name) {
//        return new ResourceLocation(modid, name);
//    }
//
//    public ResourceLocation mcLoc(String name) {
//        return new ResourceLocation(name);
//    }
//
//    public EquipmentFile getBuilder(String path) {
//        Preconditions.checkNotNull(path, "Path must not be null");
//        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path));
//        this.existingFileHelper.trackGenerated(outputLoc, EQUIPMENT);
//        return generatedFiles.computeIfAbsent(outputLoc, factory);
//    }
//}
