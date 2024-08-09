package net.trivo.trivocustommod.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.trivo.trivocustommod.TrivoCustomMod;
import net.trivo.trivocustommod.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TrivoCustomMod.MODID);

    public static final Supplier<BlockEntityType<InfuseStationBlockEntity>> INFUSE_STATION_BE =
            REGISTER.register("infuse_station_be", () ->
                    BlockEntityType.Builder.of(InfuseStationBlockEntity::new,
                            ModBlocks.INFUSE_STATION_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
