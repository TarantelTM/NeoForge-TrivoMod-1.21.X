package net.trivo.trivocustommod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trivo.trivocustommod.TrivoCustomMod;
import net.trivo.trivocustommod.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrivoCustomMod.MODID);

    public static final Supplier<CreativeModeTab> TRIVO_WORKS_TAB = CREATIVE_MODE_TABS.register("trivo_works_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.INFUSE_STATION_BLOCK.get()))
                    .title(Component.translatable("creativetab.trivo_works_tab"))
                    .displayItems((parameters, output) -> {

                        output.accept(ModBlocks.INFUSE_STATION_BLOCK.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
