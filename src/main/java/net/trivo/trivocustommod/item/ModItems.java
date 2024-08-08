package net.trivo.trivocustommod.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trivo.trivocustommod.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("trivoworks");

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
