package net.larsmans.infinitybuttons.block.custom.emergencybutton;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.advancement.InfinityButtonsTriggers;
import net.larsmans.infinitybuttons.block.custom.button.AbstractButton;
import net.larsmans.infinitybuttons.config.AlarmEnum;
import net.larsmans.infinitybuttons.config.InfinityButtonsConfig;
import net.larsmans.infinitybuttons.network.AlarmPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmergencyButton extends AbstractButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(EmergencyButton::new);
    }
    private static final VoxelShape STONE_DOWN = Block.box(4, 0, 4, 12, 1, 12);
    private static final VoxelShape STONE_UP = Block.box(4, 15, 4, 12, 16, 12);
    private static final VoxelShape STONE_NORTH = Block.box(4, 4, 15, 12, 12, 16);
    private static final VoxelShape STONE_EAST = Block.box(0, 4, 4, 1, 12, 12);
    private static final VoxelShape STONE_SOUTH = Block.box(4, 4, 0, 12, 12, 1);
    private static final VoxelShape STONE_WEST = Block.box(15, 4, 4, 16, 12, 12);

    private static final VoxelShape FLOOR_SHAPE = Shapes.or(Block.box(5, 1, 5, 11, 5, 11), STONE_DOWN);
    private static final VoxelShape FLOOR_PRESSED_SHAPE = Shapes.or(Block.box(5, 1, 5, 11, 3, 11), STONE_DOWN);
    private static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(5, 5, 11, 11, 11, 15), STONE_NORTH);
    private static final VoxelShape NORTH_PRESSED_SHAPE = Shapes.or(Block.box(5, 5, 13, 11, 11, 15), STONE_NORTH);
    private static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(1, 5, 5, 5, 11, 11), STONE_EAST);
    private static final VoxelShape EAST_PRESSED_SHAPE = Shapes.or(Block.box(1, 5, 5, 3, 11, 11), STONE_EAST);
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(5, 5, 1, 11, 11, 5), STONE_SOUTH);
    private static final VoxelShape SOUTH_PRESSED_SHAPE = Shapes.or(Block.box(5, 5, 1, 11, 11, 3), STONE_SOUTH);
    private static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(11, 5, 5, 15, 11, 11), STONE_WEST);
    private static final VoxelShape WEST_PRESSED_SHAPE = Shapes.or(Block.box(13, 5, 5, 15, 11, 11), STONE_WEST);
    private static final VoxelShape CEILING_SHAPE = Shapes.or(Block.box(5, 11, 5, 11, 15, 11), STONE_UP);
    private static final VoxelShape CEILING_PRESSED_SHAPE = Shapes.or(Block.box(5, 13, 5, 11, 15, 11), STONE_UP);

    public EmergencyButton(Properties properties) {
        super(false, properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean pressed = state.getValue(PRESSED);
        return switch (state.getValue(FACE)) {
            case FLOOR -> pressed ? FLOOR_PRESSED_SHAPE : FLOOR_SHAPE;
            case WALL -> switch (direction) {
                case EAST -> pressed ? EAST_PRESSED_SHAPE : EAST_SHAPE;
                case WEST -> pressed ? WEST_PRESSED_SHAPE : WEST_SHAPE;
                case SOUTH -> pressed ? SOUTH_PRESSED_SHAPE : SOUTH_SHAPE;
                default -> pressed ? NORTH_PRESSED_SHAPE : NORTH_SHAPE;
            };
            case CEILING -> pressed ? CEILING_PRESSED_SHAPE : CEILING_SHAPE;
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(PRESSED)) {
            return InteractionResult.CONSUME;
        }
        this.powerOn(state, level, pos);
        this.playClickSound(player, level, pos, true);
        emergencySound(level, pos);
        if (player instanceof ServerPlayer serverPlayer) {
            InfinityButtonsTriggers.EMERGENCY_TRIGGER.get().trigger(serverPlayer);
        }
        panicVillagers(level, pos);
        level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void panicVillagers(Level level, BlockPos pos) {
        if (level.isClientSide || !InfinityButtonsConfig.ALARM_VILLAGER_PANIC.get()) {
            return;
        }
        List<LivingEntity> villagers = new ArrayList<>();
        if (InfinityButtonsConfig.ALARM_SOUND_TYPE.get() == AlarmEnum.GLOBAL) {
            List<LivingEntity> dup = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(512), e -> e.getType() == EntityType.VILLAGER);
            for (Player p : level.players()) {
                dup.addAll(level.getEntitiesOfClass(LivingEntity.class, new AABB(p.blockPosition()).inflate(512), e -> e.getType() == EntityType.VILLAGER));
            }
            for (LivingEntity villager : dup) {
                if (!villagers.contains(villager)) {
                    villagers.add(villager);
                }
            }
        } else {
            villagers = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(InfinityButtonsConfig.ALARM_SOUND_RANGE.get()), e -> e.getType() == EntityType.VILLAGER);
        }
        for (LivingEntity villager : villagers) {
            if (villager instanceof Villager villagerEntity) {
                villagerEntity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, level.getGameTime());
            }
        }
    }

    @Override
    public int getPressTicks() {
        return 10;
    }

    @Override
    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, this.getClickSound(pressed), SoundSource.BLOCKS, 1, pressed ? 0.6f : 0.5f);
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return SoundEvents.BONE_BLOCK_BREAK;
    }

    public static void emergencySound(Level level, BlockPos pos) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.players()) {
                PacketDistributor.sendToPlayer(player, new AlarmPayload(pos, InfinityButtonsConfig.ALARM_SOUND_TYPE.get()));
            }
        }
    }
}
