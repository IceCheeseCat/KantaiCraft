package com.github.icecheesecat.kantaicraft.block.expedition;

import com.github.icecheesecat.kantaicraft.item.BlockItem;
import com.github.icecheesecat.kantaicraft.model.block.model.ExpeditionBlockModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Consumer;

public class ExpeditionBlockItem extends BlockItem implements GeoItem {

    private AnimatableInstanceCache m_animatableInstanceCache = new SingletonAnimatableInstanceCache(this);

    public ExpeditionBlockItem(Block p_block, Properties p_properties) {
        super(p_block, p_properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar p_controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return m_animatableInstanceCache;
    }

    public void setAnimatableInstanceCache(AnimatableInstanceCache p_animatableInstanceCache) {
        m_animatableInstanceCache = p_animatableInstanceCache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> p_consumer) {
        p_consumer.accept(new ExpeditionItemExtensions());
    }

    private static class ExpeditionItemExtensions implements IClientItemExtensions {

        private GeoItemRenderer<ExpeditionBlockItem> m_renderer;

        public GeoItemRenderer<ExpeditionBlockItem> getRenderer() {
            return m_renderer;
        }

        public void setRenderer(GeoItemRenderer<ExpeditionBlockItem> p_renderer) {
            m_renderer = p_renderer;
        }

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (getRenderer() == null) {
                setRenderer(new GeoItemRenderer<>(new ExpeditionBlockModel<>()));
            }

            return getRenderer();
        }
    }
}
