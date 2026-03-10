package org.lightning323.performancetweaks.optimizations.server.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface PoweredRailBlockInvoker {

    public boolean invokeFindPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean bl, int distance);
}
