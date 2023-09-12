package org.xiaodong.exportitem2.block;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "exportitem2");
    public static final RegistryObject<TileEntityType<ObsidianHelloTileEntity>> obsidianCounterTileEntity = TILE_ENTITIES.register("obsidian_block_tile", () -> TileEntityType.Builder.of(ObsidianHelloTileEntity::new, BlockRegistry.obsidianBlock.get()).build(null));
    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }

}

