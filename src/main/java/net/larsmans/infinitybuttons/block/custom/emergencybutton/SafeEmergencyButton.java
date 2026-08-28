package net.larsmans.infinitybuttons.block.custom.emergencybutton;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.larsmans.infinitybuttons.advancement.InfinityButtonsTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SafeEmergencyButton extends FaceAttachedHorizontalDirectionalBlock {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(SafeEmergencyButton::new);
    }
    public static final EnumProperty<SEBStateEnum> STATE = EnumProperty.create("state", SEBStateEnum.class);

    private static final VoxelShape STONE_DOWN = Block.box(3, 0, 3, 13, 1, 13);
    private static final VoxelShape STONE_UP = Block.box(3, 15, 3, 13, 16, 13);
    private static final VoxelShape STONE_NORTH = Block.box(3, 3, 15, 13, 13, 16);
    private static final VoxelShape STONE_EAST = Block.box(0, 3, 3, 1, 13, 13);
    private static final VoxelShape STONE_SOUTH = Block.box(3, 3, 0, 13, 13, 1);
    private static final VoxelShape STONE_WEST = Block.box(15, 3, 3, 16, 13, 13);

    private static final VoxelShape FLOOR_CLOSED_SHAPE = Shapes.or(Block.box(4, 1, 4, 12, 8, 12), STONE_DOWN);
    private static final VoxelShape FLOOR_OPEN_SHAPE = Shapes.or(Block.box(5, 1, 5, 11, 5, 11), STONE_DOWN);
    private static final VoxelShape FLOOR_PRESSED_SHAPE = Shapes.or(Block.box(5, 1, 5, 11, 3, 11), STONE_DOWN);
    private static final VoxelShape CEILING_CLOSED_SHAPE = Shapes.or(Block.box(4, 8, 4, 12, 15, 12), STONE_UP);
    private static final VoxelShape CEILING_OPEN_SHAPE = Shapes.or(Block.box(5, 11, 5, 11, 15, 11), STONE_UP);
    private static final VoxelShape CEILING_PRESSED_SHAPE = Shapes.or(Block.box(5, 13, 5, 11, 15, 11), STONE_UP);
    private static final VoxelShape NORTH_CLOSED_SHAPE = Shapes.or(Block.box(4, 4, 8, 12, 12, 15), STONE_NORTH);
    private static final VoxelShape NORTH_OPEN_SHAPE = Shapes.or(Block.box(5, 5, 11, 11, 11, 15), STONE_NORTH);
    private static final VoxelShape NORTH_PRESSED_SHAPE = Shapes.or(Block.box(5, 5, 13, 11, 11, 15), STONE_NORTH);
    private static final VoxelShape EAST_CLOSED_SHAPE = Shapes.or(Block.box(1, 4, 4, 8, 12, 12), STONE_EAST);
    private static final VoxelShape EAST_OPEN_SHAPE = Shapes.or(Block.box(1, 5, 5, 5, 11, 11), STONE_EAST);
    private static final VoxelShape EAST_PRESSED_SHAPE = Shapes.or(Block.box(1, 5, 5, 3, 11, 11), STONE_EAST);
    private static final VoxelShape SOUTH_CLOSED_SHAPE = Shapes.or(Block.box(4, 4, 1, 12, 12, 8), STONE_SOUTH);
    private static final VoxelShape SOUTH_OPEN_SHAPE = Shapes.or(Block.box(5, 5, 1, 11, 11, 5), STONE_SOUTH);
    private static final VoxelShape SOUTH_PRESSED_SHAPE = Shapes.or(Block.box(5, 5, 1, 11, 11, 3), STONE_SOUTH);
    private static final VoxelShape WEST_CLOSED_SHAPE = Shapes.or(Block.box(8, 4, 4, 15, 12, 12), STONE_WEST);
    private static final VoxelShape WEST_OPEN_SHAPE = Shapes.or(Block.box(11, 5, 5, 15, 11, 11), STONE_WEST);
    private static final VoxelShape WEST_PRESSED_SHAPE = Shapes.or(Block.box(13, 5, 5, 15, 11, 11), STONE_WEST);

    public SafeEmergencyButton(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STATE, SEBStateEnum.CLOSED).setValue(FACE, AttachFace.FLOOR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE, FACING, FACE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        SEBStateEnum seb = state.getValue(STATE);
        return switch (state.getValue(FACE)) {
            case FLOOR -> switch (seb) {
                case CLOSED -> FLOOR_CLOSED_SHAPE;
                case OPEN -> FLOOR_OPEN_SHAPE;
                case PRESSED -> FLOOR_PRESSED_SHAPE;
            };
            case CEILING -> switch (seb) {
                case CLOSED -> CEILING_CLOSED_SHAPE;
                case OPEN -> CEILING_OPEN_SHAPE;
                case PRESSED -> CEILING_PRESSED_SHAPE;
            };
            case WALL -> switch (direction) {
                case EAST -> switch (seb) {
                    case CLOSED -> EAST_CLOSED_SHAPE;
                    case OPEN -> EAST_OPEN_SHAPE;
                    case PRESSED -> EAST_PRESSED_SHAPE;
                };
                case SOUTH -> switch (seb) {
                    case CLOSED -> SOUTH_CLOSED_SHAPE;
                    case OPEN -> SOUTH_OPEN_SHAPE;
                    case PRESSED -> SOUTH_PRESSED_SHAPE;
                };
                case WEST -> switch (seb) {
                    case CLOSED -> WEST_CLOSED_SHAPE;
                    case OPEN -> WEST_OPEN_SHAPE;
                    case PRESSED -> WEST_PRESSED_SHAPE;
                };
                default -> switch (seb) {
                    case CLOSED -> NORTH_CLOSED_SHAPE;
                    case OPEN -> NORTH_OPEN_SHAPE;
                    case PRESSED -> NORTH_PRESSED_SHAPE;
                };
            };
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isSpectator()) {
            return InteractionResult.FAIL;
        }
        boolean adventure = player instanceof ServerPlayer serverPlayer
                && serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE;
        return switch (state.getValue(STATE)) {
            case PRESSED -> InteractionResult.CONSUME;
            case OPEN -> {
                if (player.isShiftKeyDown() && !adventure) {
                    this.closeCase(state, level, pos);
                    this.playToggleSound(player, level, pos, false);
                } else {
                    this.powerOn(state, level, pos);
                    this.playClickSound(player, level, pos, true);
                    EmergencyButton.emergencySound(level, pos);
                    if (player instanceof ServerPlayer serverPlayer) {
                        InfinityButtonsTriggers.EMERGENCY_TRIGGER.get().trigger(serverPlayer);
                    }
                    EmergencyButton.panicVillagers(level, pos);
                    level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
                }
                yield InteractionResult.sidedSuccess(level.isClientSide);
            }
            case CLOSED -> {
                if (player.isShiftKeyDown() && !adventure) {
                    this.openCase(state, level, pos);
                    this.playToggleSound(player, level, pos, true);
                    yield InteractionResult.sidedSuccess(level.isClientSide);
                }
                player.displayClientMessage(InfinityButtonsUtil.SAFE_EMERGENCY_BUTTON_ACTIONBAR_TEXT, true);
                yield InteractionResult.CONSUME;
            }
        };
    }

    public void openCase(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(STATE, SEBStateEnum.OPEN), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }

    public void closeCase(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(STATE, SEBStateEnum.CLOSED), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }

    public void powerOn(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(STATE, SEBStateEnum.PRESSED), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
        level.scheduleTick(pos, this, 10);
    }

    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, SoundEvents.BONE_BLOCK_BREAK, SoundSource.BLOCKS, 1, pressed ? 0.6f : 0.5f);
    }

    protected void playToggleSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, pressed ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1f, 1);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (movedByPiston || state.is(newState.getBlock())) {
            return;
        }
        if (state.getValue(STATE) == SEBStateEnum.PRESSED) {
            this.updateNeighbors(state, level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(STATE) == SEBStateEnum.PRESSED ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (state.getValue(STATE) == SEBStateEnum.PRESSED && getConnectedDirection(state) == direction) {
            return 15;
        }
        return 0;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STATE) == SEBStateEnum.PRESSED) {
            level.setBlock(pos, state.setValue(STATE, SEBStateEnum.OPEN), Block.UPDATE_ALL);
            this.updateNeighbors(state, level, pos);
            this.playClickSound(null, level, pos, false);
            level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
        }
    }

    private void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        InfinityButtonsUtil.tooltip(tooltip, "safe_emergency_button");
    }
}
