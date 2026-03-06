package org.lightning323.RailOptimization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.redstone.Orientation;

import java.util.HashMap;

import static net.minecraft.world.level.block.Block.*;
import static net.minecraft.world.level.block.PoweredRailBlock.POWERED;
import static net.minecraft.world.level.block.PoweredRailBlock.SHAPE;

public class RailLogic {

    private static final Direction[] EAST_WEST_DIR = new Direction[]{Direction.WEST, Direction.EAST};
    private static final Direction[] NORTH_SOUTH_DIR = new Direction[]{Direction.SOUTH, Direction.NORTH};

    // 1.21.2 specific flags
    private static final int UPDATE_FORCE_PLACE = UPDATE_MOVE_BY_PISTON | UPDATE_KNOWN_SHAPE | UPDATE_CLIENTS;

    public static int RAIL_POWER_LIMIT = 8;

    /**
     * Ported update logic for 1.21.2
     */
    public static void giveShapeUpdate(Level level, BlockState state, BlockPos pos, BlockPos fromPos, Direction direction) {
        BlockState oldState = level.getBlockState(pos);
        // In 1.21.2, ensure the flags are passed correctly to updateOrDestroy

        BlockState newState = oldState.updateShape(
                level,            // LevelReader
                level,            // ScheduledTickAccess
                pos,              // Current position
                direction,        // Direction towards fromPos
                fromPos,          // Neighbor position
                level.getBlockState(fromPos), // Neighbor state
                level.random      // RandomSource
        );

        Block.updateOrDestroy(
                oldState,newState,
                level,
                pos,
                UPDATE_CLIENTS & -34
        );
    }

    public static void setRailPowerLimit(int powerLimit) {
        RAIL_POWER_LIMIT = powerLimit;
    }

    public static void customUpdateState(PoweredRailBlock self, BlockState state, Level level, BlockPos pos) {
        boolean shouldBePowered = level.hasNeighborSignal(pos) ||
                ((PoweredRailBlockInvoker)self).invokeFindPoweredRailSignal(level, pos, state, true, 0) ||
                ((PoweredRailBlockInvoker)self).invokeFindPoweredRailSignal(level, pos, state, false, 0);

        Orientation downOrientation = getOrientationFromDir(Direction.DOWN);
        Orientation upOrientation = getOrientationFromDir(Direction.UP);

        if (shouldBePowered != state.getValue(POWERED)) {
            RailShape railShape = state.getValue(SHAPE);
            if (isAscending(railShape)) {
                level.setBlock(pos, state.setValue(POWERED, shouldBePowered), 3);
                // neighborChanged signatures remain largely similar but check mappings for 'neighborUpdate'
                level.updateNeighborsAtExceptFromFacing(pos.below(), self, Direction.UP, upOrientation);
                level.updateNeighborsAtExceptFromFacing(pos.above(), self, Direction.DOWN,downOrientation);
            } else if (shouldBePowered) {
                powerLane(self, level, pos, state, railShape);
            } else {
                dePowerLane(self, level, pos, state, railShape);
            }
        }
    }

    public static boolean isAscending(RailShape shape) {
        return shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_SOUTH ||
                shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST;
    }

    public static boolean findPoweredRailSignalFaster(PoweredRailBlock self, Level world, BlockPos pos,
                                                      boolean bl, int distance, RailShape shape,
                                                      HashMap<BlockPos,Boolean> checkedPos) {
        BlockState blockState = world.getBlockState(pos);
        if (checkedPos.containsKey(pos) && checkedPos.get(pos)) {
            return world.hasNeighborSignal(pos) ||
                    findPoweredRailSignalFaster(self, world, pos, blockState, bl, distance + 1, checkedPos);
        } else {
            if (blockState.is(self)) {
                RailShape railShape = blockState.getValue(SHAPE);
                if ((shape == RailShape.EAST_WEST && (railShape == RailShape.NORTH_SOUTH || railShape == RailShape.ASCENDING_NORTH || railShape == RailShape.ASCENDING_SOUTH)) ||
                        (shape == RailShape.NORTH_SOUTH && (railShape == RailShape.EAST_WEST || railShape == RailShape.ASCENDING_EAST || railShape == RailShape.ASCENDING_WEST))) {
                    return false;
                } else if (blockState.getValue(POWERED)) {
                    return world.hasNeighborSignal(pos) ||
                            findPoweredRailSignalFaster(self, world, pos, blockState, bl, distance + 1, checkedPos);
                }
            }
            return false;
        }
    }

    // Overload for recursive calls
    public static boolean findPoweredRailSignalFaster(PoweredRailBlock self, Level level,
                                                      BlockPos pos, BlockState state, boolean bl, int distance,
                                                      HashMap<BlockPos,Boolean> checkedPos) {
        if (distance >= RAIL_POWER_LIMIT - 1) return false;

        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean bl2 = true;
        RailShape railShape = state.getValue(SHAPE);

        // 1.21.2 ordinal-based switch is still valid, though using the enum directly is safer
        switch (railShape) {
            case NORTH_SOUTH -> { if (bl) ++k; else --k; }
            case EAST_WEST -> { if (bl) --i; else ++i; }
            case ASCENDING_EAST -> {
                if (bl) --i; else { ++i; ++j; bl2 = false; }
                railShape = RailShape.EAST_WEST;
            }
            case ASCENDING_WEST -> {
                if (bl) { --i; ++j; bl2 = false; } else ++i;
                railShape = RailShape.EAST_WEST;
            }
            case ASCENDING_NORTH -> {
                if (bl) ++k; else { --k; ++j; bl2 = false; }
                railShape = RailShape.NORTH_SOUTH;
            }
            case ASCENDING_SOUTH -> {
                if (bl) { ++k; ++j; bl2 = false; } else --k;
                railShape = RailShape.NORTH_SOUTH;
            }
        }

        return findPoweredRailSignalFaster(self, level, new BlockPos(i, j, k), bl, distance, railShape, checkedPos) ||
                (bl2 && findPoweredRailSignalFaster(self, level, new BlockPos(i, j - 1, k), bl, distance, railShape, checkedPos));
    }

    public static void powerLane(PoweredRailBlock self, Level world, BlockPos pos,
                                 BlockState mainState, RailShape railShape) {
        world.setBlock(pos, mainState.setValue(POWERED, true), UPDATE_FORCE_PLACE);
        HashMap<BlockPos,Boolean> checkedPos = new HashMap<>();
        checkedPos.put(pos, true);
        int[] count = new int[2];

        Direction[] dirs = (railShape == RailShape.NORTH_SOUTH) ? NORTH_SOUTH_DIR : EAST_WEST_DIR;
        for (int i = 0; i < dirs.length; i++) {
            setRailPositionsPower(self, world, pos, checkedPos, count, i, dirs[i]);
        }
        updateRails(self, railShape == RailShape.EAST_WEST, world, pos, mainState, count);
    }

    public static void dePowerLane(PoweredRailBlock self, Level world, BlockPos pos,
                                   BlockState mainState, RailShape railShape) {
        world.setBlock(pos, mainState.setValue(POWERED, false), UPDATE_FORCE_PLACE);
        int[] count = new int[2];

        Direction[] dirs = (railShape == RailShape.NORTH_SOUTH) ? NORTH_SOUTH_DIR : EAST_WEST_DIR;
        for (int i = 0; i < dirs.length; i++) {
            setRailPositionsDePower(self, world, pos, count, i, dirs[i]);
        }
        updateRails(self, railShape == RailShape.EAST_WEST, world, pos, mainState, count);
    }

    private static void setRailPositionsPower(PoweredRailBlock self, Level world, BlockPos pos,
                                              HashMap<BlockPos, Boolean> checkedPos, int[] count, int i, Direction dir) {
        for (int z = 1; z < RAIL_POWER_LIMIT; z++) {
            BlockPos newPos = pos.relative(dir, z);
            BlockState state = world.getBlockState(newPos);
            if (checkedPos.containsKey(newPos)) {
                if (!checkedPos.get(newPos)) break;
                count[i]++;
            } else if (!state.is(self) || state.getValue(POWERED) || !(
                    world.hasNeighborSignal(newPos) ||
                            findPoweredRailSignalFaster(self, world, newPos, state, true, 0, checkedPos) ||
                            findPoweredRailSignalFaster(self, world, newPos, state, false, 0, checkedPos)
            )) {
                checkedPos.put(newPos, false);
                break;
            } else {
                checkedPos.put(newPos, true);
                world.setBlock(newPos, state.setValue(POWERED, true), UPDATE_FORCE_PLACE);
                count[i]++;
            }
        }
    }

    private static void setRailPositionsDePower(PoweredRailBlock self, Level world, BlockPos pos,
                                                int[] count, int i, Direction dir) {
        for (int z = 1; z < RAIL_POWER_LIMIT; z++) {
            BlockPos newPos = pos.relative(dir, z);
            BlockState state = world.getBlockState(newPos);
            if (!state.is(self) || !state.getValue(POWERED) || world.hasNeighborSignal(newPos) ||
                    ((PoweredRailBlockInvoker)self).invokeFindPoweredRailSignal(world, newPos, state, true, 0) ||
                    ((PoweredRailBlockInvoker)self).invokeFindPoweredRailSignal(world, newPos, state, false, 0)) break;
            world.setBlock(newPos, state.setValue(POWERED, false), UPDATE_FORCE_PLACE);
            count[i]++;
        }
    }

    private static void shapeUpdateEnd(PoweredRailBlock self, Level world, BlockPos pos, BlockState mainState,
                                       int endPos, Direction direction, int currentPos, BlockPos blockPos) {
        if (currentPos == endPos) {
            BlockPos newPos = pos.relative(direction, currentPos + 1);
            giveShapeUpdate(world, mainState, newPos, pos, direction);
            BlockState state = world.getBlockState(blockPos);
            if (state.is(self) && isAscending(state.getValue(SHAPE)))
                giveShapeUpdate(world, mainState, newPos.above(), pos, direction);
        }
    }

    private static void neighborUpdateEnd(PoweredRailBlock self, Level world, BlockPos pos, int endPos,
                                          Direction direction, Block block, int currentPos, BlockPos blockPos, Orientation orientation) {
        if (currentPos == endPos) {
            BlockPos newPos = pos.relative(direction, currentPos + 1);
            world.neighborChanged(newPos, block, orientation);
            BlockState state = world.getBlockState(blockPos);
            if (state.is(self) && isAscending(state.getValue(SHAPE)))
                world.neighborChanged(newPos.above(), block, orientation);
        }
    }

    /**
     * Converts a Direction into a valid 1.21.2 Orientation.
     * Maps the 'front' to your direction and picks a valid 'top' axis.
     */
    private static Orientation getOrientationFromDir(Direction front) {
        //An orientation class is direction + an orientation of up, down, left or right in said direction you are facing
        Direction up = (front.getAxis().isVertical()) ? Direction.NORTH : Direction.UP;
        return Orientation.of(up, front, Orientation.SideBias.LEFT);
    }

    // Logic in updateRails remains logically identical, assuming Direction arrays are used properly.
    private static void updateRails(PoweredRailBlock self, boolean eastWest, Level world,
                                    BlockPos pos, BlockState mainState, int[] count) {
        Direction[] currentDirs = eastWest ? EAST_WEST_DIR : NORTH_SOUTH_DIR;
        Block block = mainState.getBlock();

        for (int i = 0; i < currentDirs.length; ++i) {
            int countAmt = count[i];
            if (i == 1 && countAmt == 0) continue;
            Direction dir = currentDirs[i];
            Orientation orientation = getOrientationFromDir(dir);

            for (int c = countAmt; c >= i; c--) {
                BlockPos p = pos.relative(dir, c);

                // Neighboring block updates
                if (c == 0 && count[1] == 0) world.neighborChanged(p.relative(dir.getOpposite()), block, orientation);
                neighborUpdateEnd(self, world, pos, countAmt, dir, block, c, p, orientation);

                // standard 1.21.2 block updates
                world.neighborChanged(p.below(), block, orientation);
                world.neighborChanged(p.above(), block, orientation);

                if (eastWest) {
                    world.neighborChanged(p.north(), block, orientation);
                    world.neighborChanged(p.south(), block, orientation);
                } else {
                    world.neighborChanged(p.west(), block, orientation);
                    world.neighborChanged(p.east(), block, orientation);
                }

                BlockPos posBelow = p.below();
                world.neighborChanged(posBelow.below(), block, orientation);
                if (eastWest) {
                    world.neighborChanged(posBelow.north(), block, orientation);
                    world.neighborChanged(posBelow.south(), block, orientation);
                } else {
                    world.neighborChanged(posBelow.west(), block, orientation);
                    world.neighborChanged(posBelow.east(), block, orientation);
                }

                if (c == countAmt) world.neighborChanged(pos.relative(dir, c + 1).below(), block, orientation);
                if (c == 0 && count[1] == 0) world.neighborChanged(p.relative(dir.getOpposite()).below(), block, orientation);
            }

            // Final shape pass
            for (int c = countAmt; c >= i; c--) {
                BlockPos pos1 = pos.relative(dir, c);
                if (eastWest) {
                    if (c == 0 && count[1] == 0) giveShapeUpdate(world, mainState, pos1.relative(dir.getOpposite()), pos, dir.getOpposite());
                    shapeUpdateEnd(self, world, pos, mainState, countAmt, dir, c, pos1);
                    giveShapeUpdate(world, mainState, pos1.below(), pos, Direction.DOWN);
                    giveShapeUpdate(world, mainState, pos1.above(), pos, Direction.UP);
                    giveShapeUpdate(world, mainState, pos1.north(), pos, Direction.NORTH);
                    giveShapeUpdate(world, mainState, pos1.south(), pos, Direction.SOUTH);
                } else {
                    giveShapeUpdate(world, mainState, pos1.west(), pos, Direction.WEST);
                    giveShapeUpdate(world, mainState, pos1.east(), pos, Direction.EAST);
                    giveShapeUpdate(world, mainState, pos1.below(), pos, Direction.DOWN);
                    giveShapeUpdate(world, mainState, pos1.above(), pos, Direction.UP);
                    shapeUpdateEnd(self, world, pos, mainState, countAmt, dir, c, pos1);
                    if (c == 0 && count[1] == 0) giveShapeUpdate(world, mainState, pos1.relative(dir.getOpposite()), pos, dir.getOpposite());
                }
            }
        }
    }
}