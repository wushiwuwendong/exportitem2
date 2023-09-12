package org.xiaodong.exportitem2.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class CommandET {
    public static CommandET instance = new CommandET();
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> mbequoteCommand
                = Commands.literal("et")
                .requires((commandSource) -> commandSource.hasPermission(0))
                .then(Commands.literal("item")
                        .executes(commandContext -> sendMessage(commandContext, "物品数据已导出到item_data.csv文件"))
                ).then(Commands.literal("cb")
                        .executes(commandContext ->cbxs(commandContext)))
                ;
        dispatcher.register(mbequoteCommand);

    }
    static int cbxs(CommandContext<CommandSource> commandContext){
        ClientPlayerEntity player = Minecraft.getInstance().player;
        ItemStack itemStack= player.getMainHandItem();
         String displayername=itemStack.getDisplayName().getString();
        long sl=0;
         try{
             sl= ShippingRegistry.getValue(itemStack);
         }catch (Exception e){
             System.out.println(e);
         }

        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                commandContext.getSource().getDisplayName(), new StringTextComponent("物品价值:"+displayername+",待出售数量:"+sl));



        player.sendMessage(finalText,player.getUUID());
        return 1;
    }
    static void exportitem(){
        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter("item_data.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 写入CSV文件的标题行
        try {
            csvWriter.append("ID,中文名\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 获取所有注册的物品
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            // 获取物品ID和中文名
            String itemId = item.getRegistryName().toString();
            String itemName = item.getDescription().getString();

            // 写入物品ID和中文名到CSV文件
            try {
                csvWriter.append(itemId).append(",").append(itemName).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 关闭CSV文件
        try {
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("物品数据已导出到item_data.csv文件");
    }
    static int sendMessage(CommandContext<CommandSource> commandContext, String message) throws CommandSyntaxException {
        exportitem();
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
    enum QuoteSource {
        MONTY_PYTHON(new String [] {"Nobody expects the Spanish Inquisition!",
                "What sad times are these when passing ruffians can say 'Ni' at will to old ladies.",
                "That's the machine that goes 'ping'.",
                "Have you got anything without spam?",
                "We interrupt this program to annoy you and make things generally more irritating.",
                "My brain hurts!"}),
        YOGI_BERRA(new String [] {"When you come to a fork in the road... take it.",
                "It ain't over till it's over.",
                "The future ain't what it used to be.",
                "If the world was perfect, it wouldn't be."}),
        YOGI_BEAR(new String[] {"I'm smarter than the average bear."}),
        BLUES_BROTHERS(new String [] {"Four fried chickens and a Coke, please.",
                "It's 106 miles to Chicago, we've got a full tank, half pack of cigarettes, it's dark out, and we're wearing sunglasses. Hit it.",
                "Are you the police?  No ma'am, we're musicians."});

        public String getQuote() {
            return quotes[new Random().nextInt(quotes.length)];
        }

        QuoteSource(String [] quotes) {
            this.quotes = quotes;
        }

        private String [] quotes;
    }
}

