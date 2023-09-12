package org.xiaodong.exportitem2.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

public class ObsidianHelloTileEntity extends TileEntity implements ITickableTileEntity {
    private static final int MAX_TIME = 5 * 20;
    private int timer = 0;

    public ObsidianHelloTileEntity() {
        super(TileEntityTypeRegistry.obsidianCounterTileEntity.get());
    }

    @Override
    public void tick() {

        if (level != null && !level.isClientSide) {
            if (timer == MAX_TIME) {
                PlayerEntity player = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 1, false);

            if(player!=null){
                NonNullList<ItemStack> backpack=player.inventory.items;
                TranslationTextComponent finalText=null;
                StringBuilder builder = new StringBuilder();

                builder.append("\n§a**************************§f\n");
                builder.append("§a      背包价值查询     §f\n");
                long zjz=0;

                for (ItemStack item : backpack) {
                    if (!item.isEmpty()) {
                        long sl=0;


                        try{
                            sl= ShippingRegistry.getValue(item);
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        if(sl!=0){
                            String itemName = item.getDisplayName().getString();
                            builder.append("§f"+itemName+"§f 数量:§b"+item.getCount()+"§f 价值:"+sl+"/个x"+item.getCount()+"=§e§e"+sl*item.getCount()+"§r§f\n");
                            zjz=zjz+sl*item.getCount();
                        }


                    }
                }
                builder.append("总价值:§e§n"+zjz+"§r§f\n");
                builder.append("§f注意：该显示内容起提示作用，你需要将物品放入出货方块才行§f\n");
                builder.append("§a**************************§f\n");
                finalText=new TranslationTextComponent("chat.type.announcement",
                        "显示出售内容", builder);
                player.sendMessage(finalText, player.getUUID());
            }





                timer = 0;
            }
            timer++;
        }
    }
}


