package net.dark_roleplay.travellers_map.api.util;

import net.minecraft.util.math.BlockPos;

public class MapRenderUtil {

    public static Long[][] getVisibleSegments(int winX, int winZ, double scale, BlockPos center) {

        int dX = (int) Math.ceil(((winX / 2F) + 1 * scale));
        int dZ = (int) Math.ceil(((winZ / 2F) + 1 * scale));

        int minSegX = (center.getX() - dX) >> 9;
        int minSegZ = (center.getZ() - dZ) >> 9;
        int maxSegX = (center.getX() + dX) >> 9;
        int maxSegZ = (center.getZ() + dZ) >> 9;

        Long[][] result = new Long[maxSegX - minSegX + 1][maxSegZ - minSegZ + 1];

        for (int x = minSegX; x <= maxSegX; x++) {
            for (int z = minSegZ; z <= maxSegZ; z++) {
                result[x][z] = ((long) x & 0xFFFFFFFFL) << 32 | ((long) z & 0xFFFFFFFFL);
                System.out.println("X: " + x + " Z: " + z);
            }
        }
        return result;
    }
}
