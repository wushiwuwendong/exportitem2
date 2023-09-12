package org.xiaodong.exportitem2.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class ObsidianBlock extends Block {
    public ObsidianBlock() {
        super(Properties.of(Material.SAND, MaterialColor.CLAY).strength(60f, 120f).harvestTool(ToolType.PICKAXE).harvestLevel(0).sound(SoundType.STONE));
    }
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ObsidianHelloTileEntity();
    }

}

