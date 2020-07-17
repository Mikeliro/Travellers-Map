package net.dark_roleplay.travellers_map.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MapperUtil {

    public static BlockState getFirstMappableBlock(World world, BlockPos.Mutable pos, int startHeight, int minHeight){
        pos.setY(startHeight);

        MaterialColor color = null;
        while(pos.getY() >= minHeight && (color == null || color == MaterialColor.AIR)){
            if(world.isAirBlock(pos)) {
                pos.move(0, -1, 0);
                continue;
            }

            BlockState state = world.getBlockState(pos);
            color = state.getMaterialColor(world, pos);
            if(color == null || color == MaterialColor.AIR) {
                pos.move(0, -1, 0);
                continue;
            }

            return state;
        }

        return null;
    }

    public static int getFluidDepth(World world, BlockPos.Mutable pos){
        int depth = 0;

        while(!world.getFluidState(pos).isEmpty()){
            depth++;
            pos.move(0, -1, 0);
        }

        return depth;
    }
}
