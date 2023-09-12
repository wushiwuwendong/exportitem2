package org.xiaodong.exportitem2;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xiaodong.exportitem2.block.BlockRegistry;
import org.xiaodong.exportitem2.block.TileEntityTypeRegistry;
import org.xiaodong.exportitem2.command.CommandET;
import org.xiaodong.exportitem2.command.TestCommand;
import org.xiaodong.exportitem2.event.GuiEvent;
import org.xiaodong.exportitem2.item.ItemRegistry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("BetterShow")
public class Exportitem2 {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private GuiEvent guievent=new GuiEvent();
    public Exportitem2() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
       // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::CommandEvent);
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntityTypeRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
      //  MinecraftForge.EVENT_BUS.register(new RegisterCommandEvent());

        LOGGER.info("注册完成", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("exportitem2", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    private void CommandEvent(final RegisterCommandsEvent event){

    }
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {


    }
    @SubscribeEvent
    public static void init(GuiScreenEvent.DrawScreenEvent event) {
        LOGGER.info("注册gui事件");
        if (event.getGui() instanceof InventoryScreen) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>) event.getGui();
            Slot slot = containerScreen.getSlotUnderMouse();
            LOGGER.info("玩家gui");
            if (slot != null && slot.hasItem()) {
                ItemStack itemStack = slot.getItem();
                String itemName = itemStack.getDisplayName().getString();

                // 发送消息给玩家，替换为你自己的发送消息代码
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("你移动到了物品：" + itemName), Minecraft.getInstance().player.getUUID());
            }else{
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("没有触发物品" ), Minecraft.getInstance().player.getUUID());
            }
        }else{
            LOGGER.info("非玩家gui");
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("非玩家gui" ), Minecraft.getInstance().player.getUUID());
        }

    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("注册指令事件");
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        CommandET.register(dispatcher);
        MinecraftForge.EVENT_BUS.register(guievent);
        LOGGER.info("注册指令事件完毕");
    }
    @SubscribeEvent
    public void onRegisterPlayerEvent(PlayerEvent.ItemPickupEvent event){
        PlayerEntity player = event.getPlayer();
        NonNullList<ItemStack> armorInventory = player.inventory.armor;
        NonNullList<ItemStack> offhandInventory = player.inventory.offhand;
        NonNullList<ItemStack> backpack=player.inventory.items;
        for (ItemStack item : backpack) {
            if (!item.isEmpty()) {

                String itemName = item.getDisplayName().getString();
                TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                        "xdItemshows", new StringTextComponent("您身上的物品:"+itemName));

                player.sendMessage(finalText, player.getUUID());
            }
        }

    }

    static int sendMessage(CommandContext<CommandSource> commandContext, String message) throws CommandSyntaxException {

        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                commandContext.getSource().getDisplayName(), new StringTextComponent(message));

        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            commandContext.getSource().getServer().getPlayerList().getPlayer(entity.getUUID()).sendMessage(finalText, ChatType.CHAT, entity.getUUID());
            //func_232641_a_ is sendMessage()
        } else {
            commandContext.getSource().getServer().getPlayerList().broadcastMessage(finalText, ChatType.SYSTEM, Util.NIL_UUID);
        }
        return 1;
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");

        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
        // 创建CSV文件


    }
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        LOGGER.info("HELLO from server And registercommands");
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSource> cmd = dispatcher.register(
                Commands.literal("xiaodong").then(
                        Commands.literal("test")
                                .requires((commandSource) -> commandSource.hasPermission(0))
                                .executes(TestCommand.instance)
                )
        );
        dispatcher.register(Commands.literal("bs").redirect(cmd));
    }
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
