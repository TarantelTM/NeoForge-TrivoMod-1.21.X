package net.trivo.trivocustommod.block.entity;

import cpw.mods.util.Lazy;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trivo.trivocustommod.block.custom.InfuseStationBlock;
import net.trivo.trivocustommod.screen.InfuseStationMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxUI;

public class InfuseStationBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);

    private static final int INPUT_SLOT = 0;

    protected final ContainerData data;
    private static int progress = 0;
    private static int maxprogress = 78;

    public InfuseStationBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.INFUSE_STATION_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> InfuseStationBlockEntity.this.progress;
                    case 1 -> InfuseStationBlockEntity.this.maxprogress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> InfuseStationBlockEntity.this.progress = value;
                    case 1 -> InfuseStationBlockEntity.this.maxprogress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
    @Override
    public void onLoad() {
        super.onLoad();
        setChanged();
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.trivoworks.infuse_station_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new InfuseStationMenu(id, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("infuse_station_block.progress", progress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag);
        progress = tag.getInt("infuse_station_block.progress");
    }
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if(side == null)
            return itemHandler;

        return itemHandler;
    }
    public static void tick(Level level1, BlockPos pos, BlockState state1, InfuseStationBlockEntity pEntity) {
        if(level1.isClientSide()) {
            return;
        }
        if (hasInfuseItem(pEntity)) {
            increaseInfuseProgress();
            setChanged(level1, pos, state1);

            if (hasProgressFinished()) {
                giveAbilities();
                resetProgress();
            }
        } else{
            resetProgress();
        }
    }

    private static void resetProgress() {
        progress = 0;
    }

    private static void giveAbilities() {
        Player player = Minecraft.getInstance().player;

        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 2));
    }

    private static boolean hasInfuseItem(InfuseStationBlockEntity pEntity) {
        boolean hasDragonItem = pEntity.itemHandler.getStackInSlot(0).getItem() == Items.DRAGON_EGG;

        return hasDragonItem;
    }

    private static boolean hasProgressFinished() {
        return progress >= maxprogress;
    }

    private static void increaseInfuseProgress() {
        progress++;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider prov) {
        return saveWithFullMetadata(prov);
    }
}
