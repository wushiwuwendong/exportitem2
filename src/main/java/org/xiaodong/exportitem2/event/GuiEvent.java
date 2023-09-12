package org.xiaodong.exportitem2.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiEvent {

    @SubscribeEvent
    public static void onGuiScreenMouseMoved(GuiScreenEvent.DrawScreenEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>) event.getGui();
            Slot slot = containerScreen.getSlotUnderMouse();
            if (slot != null && slot.hasItem()) {
                ItemStack itemStack = slot.getItem();
                String itemName = itemStack.getDisplayName().getString();

                // 发送消息给玩家，替换为你自己的发送消息代码
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("你移动到了物品：" + itemName), Minecraft.getInstance().player.getUUID());
            }else{
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("没有触发物品" ), Minecraft.getInstance().player.getUUID());
            }
        }else{
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("非玩家gui" ), Minecraft.getInstance().player.getUUID());
        }
    }
}
