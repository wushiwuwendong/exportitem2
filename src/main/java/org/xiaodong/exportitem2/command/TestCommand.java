package org.xiaodong.exportitem2.command;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TestCommand implements Command<CommandSource> {
    public static TestCommand instance = new TestCommand();

    @Override
    public int run(CommandContext<CommandSource> context) {
        Entity entity = context.getSource().getEntity();
        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                context.getSource().getDisplayName(), new StringTextComponent("test"));
        context.getSource().getServer().getPlayerList().getPlayer(entity.getUUID()).sendMessage(finalText, ChatType.CHAT, entity.getUUID());
        return 0;
    }
}
