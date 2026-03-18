package com.github.icecheesecat.kantaicraft.model.block;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class BlockLight {

    public static int getPackedLight(Level level, BlockPos blockPos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, blockPos);
        int skyLight = level.getBrightness(LightLayer.SKY, blockPos);

        return LightTexture.pack(blockLight, skyLight);
    }

}
