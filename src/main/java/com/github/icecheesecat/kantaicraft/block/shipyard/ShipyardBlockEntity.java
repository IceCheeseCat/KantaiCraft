package com.github.icecheesecat.kantaicraft.block.shipyard;

import com.github.icecheesecat.kantaicraft.block.facilities.FacilityCoreBlockEntity;
import com.github.icecheesecat.kantaicraft.blueprint.Blueprint;
import com.github.icecheesecat.kantaicraft.menu.shipyard.ShipyardMenu;
import com.github.icecheesecat.kantaicraft.network.ModPacketHandler;
import com.github.icecheesecat.kantaicraft.network.packet.shipyard.ShipyardPacket;
import com.github.icecheesecat.kantaicraft.registries.ModBlock;
import com.github.icecheesecat.kantaicraft.registries.ModItem;
import com.github.icecheesecat.kantaicraft.util.Constant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class ShipyardBlockEntity extends FacilityCoreBlockEntity implements Container, MenuProvider, GeoBlockEntity {

    public final int processShipSize;
    protected int[] processTime;
    protected int[] maxProcessTime;
    protected NonNullList<ItemStack> blueprintItems;
    protected NonNullList<ItemStack> prevBlueprintItems;
    protected NonNullList<UUID> owners;
    LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> new ItemStackHandler(blueprintItems));

    public ShipyardBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlock.SHIPYARD_BETYPE.get(), pPos, pBlockState);
        this.processShipSize = 1; // constant size
        this.processTime = new int[this.processShipSize];
        this.maxProcessTime = new int[this.processShipSize];
        for (int i = 0; i < processShipSize; i++) {
            this.processTime[i] = this.maxProcessTime[i] = -1;
        }
        blueprintItems = NonNullList.withSize(this.processShipSize, ItemStack.EMPTY);
        prevBlueprintItems = NonNullList.withSize(this.processShipSize, ItemStack.EMPTY);
        owners = NonNullList.withSize(this.processShipSize, Constant.uuidEmpty);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ShipyardBlockEntity shipyardBlockEntity) {
        if (level.isClientSide) return;
        if (!shipyardBlockEntity.isTickable()) return;

        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
            ItemStack prev = shipyardBlockEntity.prevBlueprintItems.get(i);
            ItemStack curr = shipyardBlockEntity.blueprintItems.get(i);
            if (!ItemStack.isSameItem(prev, curr)) {
                if (curr.isEmpty()) {
                    shipyardBlockEntity.resetProcess(i);
                }
                else {
                    Blueprint blueprint = Blueprint.createFromTag(curr.getTag());
                    shipyardBlockEntity.startProcess(i, blueprint.getProcessTime());
                }
            }
        }

        // update items to tick processes
        shipyardBlockEntity.tickAllProcesses();

//        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
//            System.out.print(shipyardBlockEntity.blueprintItems.get(i) + ": ");
//            System.out.print(shipyardBlockEntity.processTime[i] + ", ");
//            System.out.print(shipyardBlockEntity.maxProcessTime[i]);
//            System.out.println();
//        }
//        System.out.println();

        // copy all items to previous
        for (int i = 0; i < shipyardBlockEntity.processShipSize; i++) {
            shipyardBlockEntity.prevBlueprintItems.set(i, shipyardBlockEntity.blueprintItems.get(i).copy());
        }

        // sync processes
        ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ShipyardPacket(shipyardBlockEntity));

    }

    private void tickAllProcesses() {
        for (int i = 0; i < processShipSize; i++) {
            if (this.blueprintItems.get(i).isEmpty()) {
                resetProcess(i);
            }
            else if (hasProcess(i)) {
                if (!hasCompletedBuilding(i)) {
                    this.processTime[i]++;
                }
            }
        }
    }

    private void resetProcess(int i) {
        this.processTime[i] = this.maxProcessTime[i] = -1;
        setChanged();
    }

    public void removeProcessedItem(int i) {
        this.removeItem(i, this.blueprintItems.get(i).getCount());
        setChanged();
    }

    public NonNullList<UUID> getOwners() {
        return owners;
    }

    public void setOwners(NonNullList<UUID> owners) {
        this.owners = owners;
    }

    public boolean setOwnerAt(int index, UUID owner) {
        if (!this.hasProcess(index)) {
            this.owners.set(index, owner);
            return true;
        }
        else {
            return false;
        }
    }

    public void setProcessTime(int index, int processTime) {
        this.processTime[index] = processTime;
        setChanged();
    }

    public void setMaxProcessTime(int index, int totalProcessTime) {
        this.maxProcessTime[index] = totalProcessTime;
        setChanged();
    }

    public int getRemainTime(int i) {
        return this.maxProcessTime[i] - this.processTime[i];
    }

    public boolean hasProcess(int i) {
        return this.maxProcessTime[i] != -1;
    }

    public int getProcessShipSize() {
        return this.processShipSize;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("shipyard_menu");
    }

    /**
     * Sync both processes and built data before client open this block entity's menu
     */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShipyardMenu(pContainerId, pPlayerInventory, this, ContainerLevelAccess.create(pPlayer.level(), this.getBlockPos()));
    }

    private CompoundTag saveAllItems() {
        CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, this.blueprintItems);
        return nbt;
    }

    private CompoundTag savePreviousAllItems() {
        CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, this.prevBlueprintItems);
        return nbt;
    }

    private CompoundTag saveOwners() {
        CompoundTag nbt = new CompoundTag();
        for (int i = 0; i < this.processShipSize; i++) {
            nbt.putUUID("owner" + i, owners.get(i));
        }

        return nbt;
    }

    @Override
    public @NotNull CompoundTag saveExtraData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putIntArray("process_time", this.processTime);
        nbt.putIntArray("total_process_time", this.maxProcessTime);
        nbt.put("blueprint_items", this.saveAllItems());
        nbt.put("previous_blueprint_items", this.savePreviousAllItems());
        nbt.put("owners", saveOwners());
        return nbt;
    }

    @Override
    public void loadExtraData(@NotNull CompoundTag nbt) {
        this.processTime = nbt.getIntArray("process_time");
        this.maxProcessTime = nbt.getIntArray("total_process_time");
        ContainerHelper.loadAllItems(nbt.getCompound("blueprint_items"), this.blueprintItems);
        ContainerHelper.loadAllItems(nbt.getCompound("previous_blueprint_items"), this.prevBlueprintItems);
        loadOwners(nbt.getCompound("owners"));
    }

    private void loadOwners(CompoundTag nbt) {
        for (int i = 0; i < this.processShipSize; i++) {
            owners.set(i, nbt.getUUID("owner" + i));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.saveAdditional(nbt);

        return nbt;
    }

    @Override
    public int getContainerSize() {
        return this.blueprintItems.size();
    }

    @Override
    public boolean isEmpty() {
        return this.blueprintItems.isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.blueprintItems.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.blueprintItems, pSlot, pAmount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.blueprintItems, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (!pStack.is(ModItem.SHIP_BLUEPRINT.get()) && !pStack.isEmpty()) {
            return;
        }
        this.blueprintItems.set(pSlot, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    private void startProcess(int index, int maxProcessTime) {
        this.maxProcessTime[index] = maxProcessTime;
        this.processTime[index] = 0;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return this.blueprintItems.get(pIndex).isEmpty() && pStack.is(ModItem.SHIP_BLUEPRINT.get());
    }

    public float getProcessPercentage(int i) {
        if (!this.hasProcess(i)) return -1.0f;
        return (float) this.processTime[i] / this.maxProcessTime[i];
    }

    public boolean isProcessDone(int i) {
        return this.getProcessAt(i) == this.getMaxProcessAt(i);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.blueprintItems.clear();
        setChanged();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            this.lazyItemHandler.cast();
        }
        return LazyOptional.empty();
    }

    public int getProcessAt(int index) {
        return this.processTime[index];
    }

    public int getMaxProcessAt(int index) {
        return this.maxProcessTime[index];
    }

    public boolean hasCompletedBuilding(int i) {
        return hasProcess(i) && this.processTime[i] >= this.maxProcessTime[i];
    }

    public Blueprint getBlueprintAt(int i) {
        if (this.getItem(i).isEmpty()) {
            return Blueprint.createEmpty();
        }
        return Blueprint.createFromTag(this.getItem(i).getTag());
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final RawAnimation working0 = RawAnimation.begin().thenLoop("working_crane_0");
    private final RawAnimation working1 = RawAnimation.begin().thenLoop("working_crane_1");
    private final RawAnimation idle = RawAnimation.begin().thenPlayAndHold("idle");

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "idle", this::hideResourceAnimation));
        controllers.add(new AnimationController<GeoAnimatable>(this, "working0", this::working0Animation));
        controllers.add(new AnimationController<GeoAnimatable>(this, "working1", this::working1Animation));
    }

    private PlayState working0Animation(AnimationState<GeoAnimatable> animationState) {
        if (this.hasProcess(0)) {
            return animationState.setAndContinue(working0);
        }

        return PlayState.STOP;
    }

    private PlayState working1Animation(AnimationState<GeoAnimatable> animationState) {
        if (this.hasProcess(0)) {
            return animationState.setAndContinue(working1);
        }

        return PlayState.STOP;
    }

    private PlayState hideResourceAnimation(AnimationState<GeoAnimatable> animationState) {
        if (!this.hasProcess(0)) {
            return animationState.setAndContinue(idle);
        }

        return PlayState.STOP;
    }

}
