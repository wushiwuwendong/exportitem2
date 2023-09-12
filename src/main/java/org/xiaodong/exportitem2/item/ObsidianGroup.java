package org.xiaodong.exportitem2.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ObsidianGroup extends ItemGroup {
    public ObsidianGroup() {
        super("obsidian_group");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegistry.obsidianIngot.get());
    }
}

