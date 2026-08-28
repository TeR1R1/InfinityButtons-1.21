package net.larsmans.infinitybuttons.block;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.larsmans.infinitybuttons.block.custom.Doorbell;
import net.larsmans.infinitybuttons.block.custom.DoorbellButton;
import net.larsmans.infinitybuttons.block.custom.LampButton;
import net.larsmans.infinitybuttons.block.custom.LanternButton;
import net.larsmans.infinitybuttons.block.custom.button.ArrowButton;
import net.larsmans.infinitybuttons.block.custom.button.CopperButton;
import net.larsmans.infinitybuttons.block.custom.button.DiamondButton;
import net.larsmans.infinitybuttons.block.custom.button.EmeraldButton;
import net.larsmans.infinitybuttons.block.custom.button.FallingButton;
import net.larsmans.infinitybuttons.block.custom.button.PrismarineButton;
import net.larsmans.infinitybuttons.block.custom.button.StickyCopperButton;
import net.larsmans.infinitybuttons.block.custom.button.StoneButton;
import net.larsmans.infinitybuttons.block.custom.button.WaxedCopperButton;
import net.larsmans.infinitybuttons.block.custom.button.WoodenButton;
import net.larsmans.infinitybuttons.block.custom.consolebutton.ConsoleButton;
import net.larsmans.infinitybuttons.block.custom.consolebutton.LargeConsoleButton;
import net.larsmans.infinitybuttons.block.custom.consolebutton.SmallConsoleButton;
import net.larsmans.infinitybuttons.block.custom.emergencybutton.EmergencyButton;
import net.larsmans.infinitybuttons.block.custom.emergencybutton.SafeEmergencyButton;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.BigBrickSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.BookshelfSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.ChiseledNetherBrickSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.ChiseledStoneBrickSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.DeepslateTileSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.FullBlockBrickSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.MudBrickSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.PlankSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.TileSecretButton;
import net.larsmans.infinitybuttons.block.custom.torch.RedstoneTorchButton;
import net.larsmans.infinitybuttons.block.custom.torch.RedstoneTorchLever;
import net.larsmans.infinitybuttons.block.custom.torch.RedstoneWallTorchButton;
import net.larsmans.infinitybuttons.block.custom.torch.RedstoneWallTorchLever;
import net.larsmans.infinitybuttons.block.custom.torch.TorchButton;
import net.larsmans.infinitybuttons.block.custom.torch.TorchLever;
import net.larsmans.infinitybuttons.block.custom.torch.WallTorchButton;
import net.larsmans.infinitybuttons.block.custom.torch.WallTorchLever;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class InfinityButtonsBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(InfinityButtons.MOD_ID);

    /**
     * Buttons
     */

    public static final DeferredBlock<Block> DEEPSLATE_BUTTON = registerStoneButton("deepslate");
    public static final DeferredBlock<Block> GRANITE_BUTTON = registerStoneButton("granite");
    public static final DeferredBlock<Block> DIORITE_BUTTON = registerStoneButton("diorite");
    public static final DeferredBlock<Block> ANDESITE_BUTTON = registerStoneButton("andesite");
    public static final DeferredBlock<Block> CALCITE_BUTTON = registerStoneButton("calcite");
    public static final DeferredBlock<Block> TUFF_BUTTON = registerStoneButton("tuff");
    public static final DeferredBlock<Block> DRIPSTONE_BUTTON = registerStoneButton("dripstone");

    public static final DeferredBlock<Block> COPPER_BUTTON = registerCopperButton("copper", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final DeferredBlock<Block> EXPOSED_COPPER_BUTTON = registerCopperButton("exposed_copper", WeatheringCopper.WeatherState.EXPOSED);
    public static final DeferredBlock<Block> WEATHERED_COPPER_BUTTON = registerCopperButton("weathered_copper", WeatheringCopper.WeatherState.WEATHERED);
    public static final DeferredBlock<Block> OXIDIZED_COPPER_BUTTON = registerCopperButton("oxidized_copper", WeatheringCopper.WeatherState.OXIDIZED);

    public static final DeferredBlock<Block> WAXED_COPPER_BUTTON = registerWaxedCopperButton("copper");
    public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_BUTTON = registerWaxedCopperButton("exposed_copper");
    public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_BUTTON = registerWaxedCopperButton("weathered_copper");
    public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_BUTTON = registerWaxedCopperButton("oxidized_copper");

    public static final DeferredBlock<Block> STICKY_COPPER_BUTTON = registerStickyCopperButton("copper");
    public static final DeferredBlock<Block> STICKY_EXPOSED_COPPER_BUTTON = registerStickyCopperButton("exposed_copper");
    public static final DeferredBlock<Block> STICKY_WEATHERED_COPPER_BUTTON = registerStickyCopperButton("weathered_copper");
    public static final DeferredBlock<Block> STICKY_OXIDIZED_COPPER_BUTTON = registerStickyCopperButton("oxidized_copper");

    public static final DeferredBlock<Block> IRON_BUTTON = registerArrowButton("iron");
    public static final DeferredBlock<Block> GOLD_BUTTON = registerArrowButton("gold");

    public static final DeferredBlock<Block> EMERALD_BUTTON = registerBlock("emerald_button", () -> new EmeraldButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), false));

    public static final DeferredBlock<Block> DIAMOND_BUTTON = registerBlock("diamond_button", () -> new DiamondButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), false));

    public static final DeferredBlock<Block> PRISMARINE_BUTTON = registerPrismarineButton("prismarine");
    public static final DeferredBlock<Block> PRISMARINE_BRICK_BUTTON = registerPrismarineButton("prismarine_brick");
    public static final DeferredBlock<Block> DARK_PRISMARINE_BUTTON = registerPrismarineButton("dark_prismarine");

    public static final DeferredBlock<Block> SAND_BUTTON = registerSandButton("sand", false);
    public static final DeferredBlock<Block> RED_SAND_BUTTON = registerSandButton("red_sand", false);
    public static final DeferredBlock<Block> GRAVEL_BUTTON = registerSandButton("gravel", true);

    public static final DeferredBlock<Block> WHITE_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("white");
    public static final DeferredBlock<Block> LIGHT_GRAY_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("light_gray");
    public static final DeferredBlock<Block> GRAY_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("gray");
    public static final DeferredBlock<Block> BLACK_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("black");
    public static final DeferredBlock<Block> BROWN_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("brown");
    public static final DeferredBlock<Block> RED_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("red");
    public static final DeferredBlock<Block> ORANGE_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("orange");
    public static final DeferredBlock<Block> YELLOW_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("yellow");
    public static final DeferredBlock<Block> LIME_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("lime");
    public static final DeferredBlock<Block> GREEN_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("green");
    public static final DeferredBlock<Block> CYAN_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("cyan");
    public static final DeferredBlock<Block> LIGHT_BLUE_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("light_blue");
    public static final DeferredBlock<Block> BLUE_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("blue");
    public static final DeferredBlock<Block> PURPLE_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("purple");
    public static final DeferredBlock<Block> MAGENTA_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("magenta");
    public static final DeferredBlock<Block> PINK_CONCRETE_POWDER_BUTTON = registerConcretePowderButton("pink");

    public static DeferredBlock<Block> registerStoneButton(String name) {
        return registerBlock(name + "_button", () -> new StoneButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.STONE), false));
    }

    public static DeferredBlock<Block> registerCopperButton(String name, WeatheringCopper.WeatherState level) {
        return registerBlock(name + "_button", () -> new CopperButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), false, level));
    }

    public static DeferredBlock<Block> registerWaxedCopperButton(String name) {
        return registerBlock("waxed_" + name + "_button", () -> new WaxedCopperButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), false));
    }

    public static DeferredBlock<Block> registerStickyCopperButton(String name) {
        return registerBlock("sticky_" + name + "_button", () -> new StickyCopperButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), false));
    }

    public static DeferredBlock<Block> registerArrowButton(String name) {
        return registerBlock(name + "_button", () -> new ArrowButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), false));
    }

    public static DeferredBlock<Block> registerPrismarineButton(String name) {
        return registerBlock(name + "_button", () -> new PrismarineButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.STONE), false));
    }

    public static DeferredBlock<Block> registerConcretePowderButton(String name) {
        return registerSandButton(name + "_concrete_powder", false);
    }

    public static DeferredBlock<Block> registerSandButton(String name, boolean gravel) {
        return registerBlock(name + "_button", () -> new FallingButton(gravel, BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(gravel ? SoundType.GRAVEL : SoundType.SAND), false));
    }

    /**
     * Large Buttons
     */

    public static final DeferredBlock<Block> OAK_LARGE_BUTTON = registerWoodenLargeButton("oak");
    public static final DeferredBlock<Block> SPRUCE_LARGE_BUTTON = registerWoodenLargeButton("spruce");
    public static final DeferredBlock<Block> BIRCH_LARGE_BUTTON = registerWoodenLargeButton("birch");
    public static final DeferredBlock<Block> JUNGLE_LARGE_BUTTON = registerWoodenLargeButton("jungle");
    public static final DeferredBlock<Block> ACACIA_LARGE_BUTTON = registerWoodenLargeButton("acacia");
    public static final DeferredBlock<Block> DARK_OAK_LARGE_BUTTON = registerWoodenLargeButton("dark_oak");
    public static final DeferredBlock<Block> MANGROVE_LARGE_BUTTON = registerWoodenLargeButton("mangrove");
    public static final DeferredBlock<Block> CHERRY_LARGE_BUTTON = registerWoodenLargeButton("cherry");
    public static final DeferredBlock<Block> BAMBOO_LARGE_BUTTON = registerWoodenLargeButton("bamboo");
    public static final DeferredBlock<Block> WARPED_LARGE_BUTTON = registerWoodenLargeButton("warped");
    public static final DeferredBlock<Block> CRIMSON_LARGE_BUTTON = registerWoodenLargeButton("crimson");

    public static final DeferredBlock<Block> STONE_LARGE_BUTTON = registerStoneLargeButton("stone");
    public static final DeferredBlock<Block> DEEPSLATE_LARGE_BUTTON = registerStoneLargeButton("deepslate");
    public static final DeferredBlock<Block> GRANITE_LARGE_BUTTON = registerStoneLargeButton("granite");
    public static final DeferredBlock<Block> DIORITE_LARGE_BUTTON = registerStoneLargeButton("diorite");
    public static final DeferredBlock<Block> ANDESITE_LARGE_BUTTON = registerStoneLargeButton("andesite");
    public static final DeferredBlock<Block> CALCITE_LARGE_BUTTON = registerStoneLargeButton("calcite");
    public static final DeferredBlock<Block> TUFF_LARGE_BUTTON = registerStoneLargeButton("tuff");
    public static final DeferredBlock<Block> DRIPSTONE_LARGE_BUTTON = registerStoneLargeButton("dripstone");
    public static final DeferredBlock<Block> POLISHED_BLACKSTONE_LARGE_BUTTON = registerStoneLargeButton("polished_blackstone");

    public static final DeferredBlock<Block> COPPER_LARGE_BUTTON = registerCopperLargeButton("copper", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final DeferredBlock<Block> EXPOSED_COPPER_LARGE_BUTTON = registerCopperLargeButton("exposed_copper", WeatheringCopper.WeatherState.EXPOSED);
    public static final DeferredBlock<Block> WEATHERED_COPPER_LARGE_BUTTON = registerCopperLargeButton("weathered_copper", WeatheringCopper.WeatherState.WEATHERED);
    public static final DeferredBlock<Block> OXIDIZED_COPPER_LARGE_BUTTON = registerCopperLargeButton("oxidized_copper", WeatheringCopper.WeatherState.OXIDIZED);

    public static final DeferredBlock<Block> WAXED_COPPER_LARGE_BUTTON = registerWaxedCopperLargeButton("copper");
    public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_LARGE_BUTTON = registerWaxedCopperLargeButton("exposed_copper");
    public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_LARGE_BUTTON = registerWaxedCopperLargeButton("weathered_copper");
    public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_LARGE_BUTTON = registerWaxedCopperLargeButton("oxidized_copper");

    public static final DeferredBlock<Block> STICKY_COPPER_LARGE_BUTTON = registerStickyCopperLargeButton("copper");
    public static final DeferredBlock<Block> STICKY_EXPOSED_COPPER_LARGE_BUTTON = registerStickyCopperLargeButton("exposed_copper");
    public static final DeferredBlock<Block> STICKY_WEATHERED_COPPER_LARGE_BUTTON = registerStickyCopperLargeButton("weathered_copper");
    public static final DeferredBlock<Block> STICKY_OXIDIZED_COPPER_LARGE_BUTTON = registerStickyCopperLargeButton("oxidized_copper");

    public static final DeferredBlock<Block> IRON_LARGE_BUTTON = registerArrowLargeButton("iron");
    public static final DeferredBlock<Block> GOLD_LARGE_BUTTON = registerArrowLargeButton("gold");

    public static final DeferredBlock<Block> EMERALD_LARGE_BUTTON = registerBlock("emerald_large_button", () -> new EmeraldButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), true));

    public static final DeferredBlock<Block> DIAMOND_LARGE_BUTTON = registerBlock("diamond_large_button", () -> new DiamondButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), true));

    public static final DeferredBlock<Block> PRISMARINE_LARGE_BUTTON = registerPrismarineLargeButton("prismarine");
    public static final DeferredBlock<Block> PRISMARINE_BRICK_LARGE_BUTTON = registerPrismarineLargeButton("prismarine_brick");
    public static final DeferredBlock<Block> DARK_PRISMARINE_LARGE_BUTTON = registerPrismarineLargeButton("dark_prismarine");

    public static final DeferredBlock<Block> SAND_LARGE_BUTTON = registerSandLargeButton("sand", false);
    public static final DeferredBlock<Block> RED_SAND_LARGE_BUTTON = registerSandLargeButton("red_sand", false);
    public static final DeferredBlock<Block> GRAVEL_LARGE_BUTTON = registerSandLargeButton("gravel", true);

    public static final DeferredBlock<Block> WHITE_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("white");
    public static final DeferredBlock<Block> LIGHT_GRAY_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("light_gray");
    public static final DeferredBlock<Block> GRAY_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("gray");
    public static final DeferredBlock<Block> BLACK_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("black");
    public static final DeferredBlock<Block> BROWN_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("brown");
    public static final DeferredBlock<Block> RED_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("red");
    public static final DeferredBlock<Block> ORANGE_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("orange");
    public static final DeferredBlock<Block> YELLOW_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("yellow");
    public static final DeferredBlock<Block> LIME_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("lime");
    public static final DeferredBlock<Block> GREEN_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("green");
    public static final DeferredBlock<Block> CYAN_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("cyan");
    public static final DeferredBlock<Block> LIGHT_BLUE_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("light_blue");
    public static final DeferredBlock<Block> BLUE_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("blue");
    public static final DeferredBlock<Block> PURPLE_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("purple");
    public static final DeferredBlock<Block> MAGENTA_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("magenta");
    public static final DeferredBlock<Block> PINK_CONCRETE_POWDER_LARGE_BUTTON = registerConcretePowderLargeButton("pink");

    public static DeferredBlock<Block> registerWoodenLargeButton(String name) {
        return registerBlock(name + "_large_button", () -> new WoodenButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.WOOD), true));
    }

    public static DeferredBlock<Block> registerStoneLargeButton(String name) {
        return registerBlock(name + "_large_button", () -> new StoneButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.STONE), true));
    }

    public static DeferredBlock<Block> registerCopperLargeButton(String name, WeatheringCopper.WeatherState level) {
        return registerBlock(name + "_large_button", () -> new CopperButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), true, level));
    }

    public static DeferredBlock<Block> registerWaxedCopperLargeButton(String name) {
        return registerBlock("waxed_" + name + "_large_button", () -> new WaxedCopperButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), true));
    }

    public static DeferredBlock<Block> registerStickyCopperLargeButton(String name) {
        return registerBlock("sticky_" + name + "_large_button", () -> new StickyCopperButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), true));
    }

    public static DeferredBlock<Block> registerArrowLargeButton(String name) {
        return registerBlock(name + "_large_button", () -> new ArrowButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.METAL).requiresCorrectToolForDrops(), true));
    }

    public static DeferredBlock<Block> registerPrismarineLargeButton(String name) {
        return registerBlock(name + "_large_button", () -> new PrismarineButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(SoundType.STONE), true));
    }

    public static DeferredBlock<Block> registerConcretePowderLargeButton(String name) {
        return registerSandLargeButton(name + "_concrete_powder", false);
    }

    public static DeferredBlock<Block> registerSandLargeButton(String name, boolean gravel) {
        return registerBlock(name + "_large_button", () -> new FallingButton(gravel, BlockBehaviour.Properties.of().strength(0.5f).noCollission().noOcclusion().sound(gravel ? SoundType.GRAVEL : SoundType.SAND), true));
    }

    /**
     * Emergency Buttons
     */

    public static final DeferredBlock<Block> WHITE_EMERGENCY_BUTTON = registerEmergencyButton("white");
    public static final DeferredBlock<Block> LIGHT_GRAY_EMERGENCY_BUTTON = registerEmergencyButton("light_gray");
    public static final DeferredBlock<Block> GRAY_EMERGENCY_BUTTON = registerEmergencyButton("gray");
    public static final DeferredBlock<Block> BLACK_EMERGENCY_BUTTON = registerEmergencyButton("black");
    public static final DeferredBlock<Block> BROWN_EMERGENCY_BUTTON = registerEmergencyButton("brown");
    public static final DeferredBlock<Block> RED_EMERGENCY_BUTTON = registerEmergencyButton("red");
    public static final DeferredBlock<Block> ORANGE_EMERGENCY_BUTTON = registerEmergencyButton("orange");
    public static final DeferredBlock<Block> YELLOW_EMERGENCY_BUTTON = registerEmergencyButton("yellow");
    public static final DeferredBlock<Block> LIME_EMERGENCY_BUTTON = registerEmergencyButton("lime");
    public static final DeferredBlock<Block> GREEN_EMERGENCY_BUTTON = registerEmergencyButton("green");
    public static final DeferredBlock<Block> CYAN_EMERGENCY_BUTTON = registerEmergencyButton("cyan");
    public static final DeferredBlock<Block> LIGHT_BLUE_EMERGENCY_BUTTON = registerEmergencyButton("light_blue");
    public static final DeferredBlock<Block> BLUE_EMERGENCY_BUTTON = registerEmergencyButton("blue");
    public static final DeferredBlock<Block> PURPLE_EMERGENCY_BUTTON = registerEmergencyButton("purple");
    public static final DeferredBlock<Block> MAGENTA_EMERGENCY_BUTTON = registerEmergencyButton("magenta");
    public static final DeferredBlock<Block> PINK_EMERGENCY_BUTTON = registerEmergencyButton("pink");
    public static final DeferredBlock<Block> FANCY_EMERGENCY_BUTTON = registerEmergencyButton("fancy");

    public static final DeferredBlock<Block> WHITE_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("white");
    public static final DeferredBlock<Block> LIGHT_GRAY_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("light_gray");
    public static final DeferredBlock<Block> GRAY_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("gray");
    public static final DeferredBlock<Block> BLACK_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("black");
    public static final DeferredBlock<Block> BROWN_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("brown");
    public static final DeferredBlock<Block> RED_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("red");
    public static final DeferredBlock<Block> ORANGE_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("orange");
    public static final DeferredBlock<Block> YELLOW_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("yellow");
    public static final DeferredBlock<Block> LIME_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("lime");
    public static final DeferredBlock<Block> GREEN_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("green");
    public static final DeferredBlock<Block> CYAN_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("cyan");
    public static final DeferredBlock<Block> LIGHT_BLUE_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("light_blue");
    public static final DeferredBlock<Block> BLUE_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("blue");
    public static final DeferredBlock<Block> PURPLE_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("purple");
    public static final DeferredBlock<Block> MAGENTA_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("magenta");
    public static final DeferredBlock<Block> PINK_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("pink");
    public static final DeferredBlock<Block> FANCY_SAFE_EMERGENCY_BUTTON = registerSafeEmergencyButton("fancy");

    public static DeferredBlock<Block> registerEmergencyButton(String name) {
        return registerBlock(name + "_emergency_button", () -> new EmergencyButton(BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().sound(SoundType.METAL)));
    }

    public static DeferredBlock<Block> registerSafeEmergencyButton(String name) {
        return registerBlock(name + "_safe_emergency_button", () -> new SafeEmergencyButton(BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().sound(SoundType.METAL)));
    }

    /**
     * Secret Buttons
     */

    public static final DeferredBlock<Block> BOOKSHELF_SECRET_BUTTON = registerBlock("bookshelf_secret_button", () -> new BookshelfSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noOcclusion().sound(SoundType.WOOD).strength(1.5f), byName("bookshelf")));

    public static final DeferredBlock<Block> BRICK_SECRET_BUTTON = registerBlock("brick_secret_button", () -> new FullBlockBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("bricks")));

    public static final DeferredBlock<Block> STONE_BRICK_SECRET_BUTTON = registerBlock("stone_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6.0f), byName("stone_bricks")));

    public static final DeferredBlock<Block> MOSSY_STONE_BRICK_SECRET_BUTTON = registerBlock("mossy_stone_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6.0f), byName("mossy_stone_bricks")));

    public static final DeferredBlock<Block> CRACKED_STONE_BRICK_SECRET_BUTTON = registerBlock("cracked_stone_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6.0f), byName("cracked_stone_bricks")));

    public static final DeferredBlock<Block> CHISELED_STONE_BRICK_SECRET_BUTTON = registerBlock("chiseled_stone_brick_secret_button", () -> new ChiseledStoneBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6.0f), byName("chiseled_stone_bricks")));

    public static final DeferredBlock<Block> DEEPSLATE_BRICK_SECRET_BUTTON = registerBlock("deepslate_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).noOcclusion().sound(SoundType.DEEPSLATE_BRICKS).requiresCorrectToolForDrops().strength(3.5f, 6.0f), byName("deepslate_bricks")));

    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_BRICK_SECRET_BUTTON = registerBlock("cracked_deepslate_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).noOcclusion().sound(SoundType.DEEPSLATE_BRICKS).requiresCorrectToolForDrops().strength(3.5f, 6.0f), byName("cracked_deepslate_bricks")));

    public static final DeferredBlock<Block> DEEPSLATE_TILE_SECRET_BUTTON = registerBlock("deepslate_tile_secret_button", () -> new DeepslateTileSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).noOcclusion().sound(SoundType.DEEPSLATE_TILES).requiresCorrectToolForDrops().strength(3.5f, 6.0f), byName("deepslate_tiles")));

    public static final DeferredBlock<Block> CRACKED_DEEPSLATE_TILE_SECRET_BUTTON = registerBlock("cracked_deepslate_tile_secret_button", () -> new DeepslateTileSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).noOcclusion().sound(SoundType.DEEPSLATE_TILES).requiresCorrectToolForDrops().strength(3.5f, 6.0f), byName("cracked_deepslate_tiles")));

    public static final DeferredBlock<Block> OAK_PLANK_SECRET_BUTTON = registerPlankSecretButton("oak", MapColor.WOOD);
    public static final DeferredBlock<Block> SPRUCE_PLANK_SECRET_BUTTON = registerPlankSecretButton("spruce", MapColor.WOOD);
    public static final DeferredBlock<Block> BIRCH_PLANK_SECRET_BUTTON = registerPlankSecretButton("birch", MapColor.WOOD);
    public static final DeferredBlock<Block> JUNGLE_PLANK_SECRET_BUTTON = registerPlankSecretButton("jungle", MapColor.WOOD);
    public static final DeferredBlock<Block> ACACIA_PLANK_SECRET_BUTTON = registerPlankSecretButton("acacia", MapColor.COLOR_ORANGE);
    public static final DeferredBlock<Block> DARK_OAK_PLANK_SECRET_BUTTON = registerPlankSecretButton("dark_oak", MapColor.WOOD);
    public static final DeferredBlock<Block> MANGROVE_PLANK_SECRET_BUTTON = registerPlankSecretButton("mangrove", MapColor.WOOD);
    public static final DeferredBlock<Block> CHERRY_PLANK_SECRET_BUTTON = registerPlankSecretButton("cherry", MapColor.TERRACOTTA_PINK);
    public static final DeferredBlock<Block> CRIMSON_PLANK_SECRET_BUTTON = registerNetherPlankSecretButton("crimson", MapColor.CRIMSON_STEM);
    public static final DeferredBlock<Block> WARPED_PLANK_SECRET_BUTTON = registerNetherPlankSecretButton("warped", MapColor.WARPED_STEM);

    public static final DeferredBlock<Block> MUD_BRICK_SECRET_BUTTON = registerBlock("mud_brick_secret_button", () -> new MudBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).noOcclusion().sound(SoundType.MUD_BRICKS).requiresCorrectToolForDrops().strength(1.5f, 3.0f), byName("mud_bricks")));

    public static final DeferredBlock<Block> END_STONE_BRICK_SECRET_BUTTON = registerBlock("end_stone_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(3.0f, 9.0f), byName("end_stone_bricks")));

    public static final DeferredBlock<Block> PURPUR_BLOCK_SECRET_BUTTON = registerBlock("purpur_block_secret_button", () -> new TileSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(1.5f, 6.0f).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops(), byName("purpur_block")));

    public static final DeferredBlock<Block> QUARTZ_BRICK_SECRET_BUTTON = registerBlock("quartz_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(0.8f), byName("quartz_bricks")));

    public static final DeferredBlock<Block> DARK_PRISMARINE_SECRET_BUTTON = registerBlock("dark_prismarine_secret_button", () -> new FullBlockBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6.0f), byName("dark_prismarine")));

    public static final DeferredBlock<Block> POLISHED_BLACKSTONE_BRICK_SECRET_BUTTON = registerBlock("polished_blackstone_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("polished_blackstone_bricks")));

    public static final DeferredBlock<Block> CRACKED_POLISHED_BLACKSTONE_BRICK_SECRET_BUTTON = registerBlock("cracked_polished_blackstone_brick_secret_button", () -> new BigBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("cracked_polished_blackstone_bricks")));

    public static final DeferredBlock<Block> CHISELED_POLISHED_BLACKSTONE_SECRET_BUTTON = registerBlock("chiseled_polished_blackstone_secret_button", () -> new ChiseledStoneBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noOcclusion().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("chiseled_polished_blackstone")));

    public static final DeferredBlock<Block> NETHER_BRICK_SECRET_BUTTON = registerBlock("nether_brick_secret_button", () -> new FullBlockBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).noOcclusion().sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("nether_bricks")));

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_SECRET_BUTTON = registerBlock("cracked_nether_brick_secret_button", () -> new FullBlockBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).noOcclusion().sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("cracked_nether_bricks")));

    public static final DeferredBlock<Block> CHISELED_NETHER_BRICK_SECRET_BUTTON = registerBlock("chiseled_nether_brick_secret_button", () -> new ChiseledNetherBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).noOcclusion().sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("chiseled_nether_bricks")));

    public static final DeferredBlock<Block> RED_NETHER_BRICK_SECRET_BUTTON = registerBlock("red_nether_brick_secret_button", () -> new FullBlockBrickSecretButton(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).noOcclusion().sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops().strength(2.0f, 6.0f), byName("red_nether_bricks")));

    public static DeferredBlock<Block> registerPlankSecretButton(String name, MapColor color) {
        return registerBlock(name + "_plank_secret_button", () -> new PlankSecretButton(BlockBehaviour.Properties.of().mapColor(color).noOcclusion().sound(SoundType.WOOD).strength(2.0f, 3.0f), byName(name + "_planks")));
    }

    public static DeferredBlock<Block> registerNetherPlankSecretButton(String name, MapColor color) {
        return registerBlock(name + "_plank_secret_button", () -> new PlankSecretButton(BlockBehaviour.Properties.of().mapColor(color).noOcclusion().sound(SoundType.WOOD).strength(2.0f, 3.0f), byName(name + "_planks")));
    }

    /**
     * Misc
     */

    public static final DeferredBlock<Block> DOORBELL = registerBlock("doorbell", () -> new Doorbell(doorbellSettings()));
    public static final DeferredBlock<Block> DOORBELL_BUTTON = registerBlock("doorbell_button", () -> new DoorbellButton(doorbellSettings()));
    public static final DeferredBlock<Block> LAMP_BUTTON = registerBlock("lamp_button", () -> new LampButton(lampSettings(), false));
    public static final DeferredBlock<Block> LAMP_LEVER = registerBlock("lamp_lever", () -> new LampButton(lampSettings(), true));
    public static final DeferredBlock<Block> LETTER_BUTTON = registerBlock("letter_button", () -> new LetterButton(BlockBehaviour.Properties.of().strength(0.5f).noCollission().sound(SoundType.WOOD), false));
    public static final DeferredBlock<Block> LETTER_LEVER = registerBlock("letter_lever", () -> new LetterButton(BlockBehaviour.Properties.ofFullCopy(LETTER_BUTTON.get()), true));
    public static final DeferredBlock<Block> LANTERN_BUTTON = registerBlock("lantern_button", () -> new LanternButton(lanternSettings().lightLevel(state -> 15), false, byName("lantern")));
    public static final DeferredBlock<Block> LANTERN_LEVER = registerBlock("lantern_lever", () -> new LanternButton(lanternSettings().lightLevel(state -> 15), true, byName("lantern")));
    public static final DeferredBlock<Block> SOUL_LANTERN_BUTTON = registerBlock("soul_lantern_button", () -> new LanternButton(lanternSettings().lightLevel(state -> 10), false, byName("soul_lantern")));
    public static final DeferredBlock<Block> SOUL_LANTERN_LEVER = registerBlock("soul_lantern_lever", () -> new LanternButton(lanternSettings().lightLevel(state -> 10), true, byName("soul_lantern")));

    public static BlockBehaviour.Properties doorbellSettings() {
        return BlockBehaviour.Properties.of().noOcclusion().noCollission().strength(0.5f).sound(SoundType.WOOD);
    }

    public static BlockBehaviour.Properties lampSettings() {
        return BlockBehaviour.Properties.of().noOcclusion().strength(0.3f).sound(SoundType.GLASS).lightLevel(getLampButtonLight());
    }

    public static BlockBehaviour.Properties lanternSettings() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion().requiresCorrectToolForDrops().strength(3.5f).sound(SoundType.LANTERN);
    }

    /**
     * Console Buttons
     */

    public static final DeferredBlock<Block> SMALL_CONSOLE_BUTTON = registerBlock("small_console_button", () -> new SmallConsoleButton(consoleButtonSettings(), false));
    public static final DeferredBlock<Block> SMALL_CONSOLE_LEVER = registerBlock("small_console_lever", () -> new SmallConsoleButton(consoleButtonSettings(), true));
    public static final DeferredBlock<Block> CONSOLE_BUTTON = registerBlock("console_button", () -> new ConsoleButton(consoleButtonSettings(), false));
    public static final DeferredBlock<Block> CONSOLE_LEVER = registerBlock("console_lever", () -> new ConsoleButton(consoleButtonSettings(), true));
    public static final DeferredBlock<Block> LARGE_CONSOLE_BUTTON = registerBlock("large_console_button", () -> new LargeConsoleButton(consoleButtonSettings(), false));
    public static final DeferredBlock<Block> LARGE_CONSOLE_LEVER = registerBlock("large_console_lever", () -> new LargeConsoleButton(consoleButtonSettings(), true));
    public static final DeferredBlock<Block> BIG_CONSOLE_BUTTON = registerBlock("big_console_button", () -> new LargeConsoleButton(consoleButtonSettings(), false));
    public static final DeferredBlock<Block> BIG_CONSOLE_LEVER = registerBlock("big_console_lever", () -> new LargeConsoleButton(consoleButtonSettings(), true));

    public static BlockBehaviour.Properties consoleButtonSettings() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noOcclusion().strength(0.5f).sound(SoundType.METAL);
    }

    /**
     * Torches
     */

    public static final DeferredBlock<Block> TORCH_BUTTON = registerBlock("torch_button", () -> new TorchButton(torchSettings(14), ParticleTypes.FLAME, byName("torch")));
    public static final DeferredBlock<Block> WALL_TORCH_BUTTON = registerBlock("wall_torch_button", () -> new WallTorchButton(torchSettings(14).dropsLike(TORCH_BUTTON.get()), ParticleTypes.FLAME, byName("torch")));
    public static final DeferredBlock<Block> TORCH_LEVER = registerBlock("torch_lever", () -> new TorchLever(torchSettings(14), ParticleTypes.FLAME, byName("torch")));
    public static final DeferredBlock<Block> WALL_TORCH_LEVER = registerBlock("wall_torch_lever", () -> new WallTorchLever(torchSettings(14).dropsLike(TORCH_LEVER.get()), ParticleTypes.FLAME, byName("torch")));
    public static final DeferredBlock<Block> SOUL_TORCH_BUTTON = registerBlock("soul_torch_button", () -> new TorchButton(torchSettings(10), ParticleTypes.SOUL_FIRE_FLAME, byName("soul_torch")));
    public static final DeferredBlock<Block> SOUL_WALL_TORCH_BUTTON = registerBlock("soul_wall_torch_button", () -> new WallTorchButton(torchSettings(10).dropsLike(SOUL_TORCH_BUTTON.get()), ParticleTypes.SOUL_FIRE_FLAME, byName("soul_torch")));
    public static final DeferredBlock<Block> SOUL_TORCH_LEVER = registerBlock("soul_torch_lever", () -> new TorchLever(torchSettings(10), ParticleTypes.SOUL_FIRE_FLAME, byName("soul_torch")));
    public static final DeferredBlock<Block> SOUL_WALL_TORCH_LEVER = registerBlock("soul_wall_torch_lever", () -> new WallTorchLever(torchSettings(10).dropsLike(SOUL_TORCH_LEVER.get()), ParticleTypes.SOUL_FIRE_FLAME, byName("soul_torch")));
    public static final DeferredBlock<Block> REDSTONE_TORCH_BUTTON = registerBlock("redstone_torch_button", () -> new RedstoneTorchButton(torchSettings(7), byName("redstone_torch")));
    public static final DeferredBlock<Block> REDSTONE_WALL_TORCH_BUTTON = registerBlock("redstone_wall_torch_button", () -> new RedstoneWallTorchButton(torchSettings(7).dropsLike(REDSTONE_TORCH_BUTTON.get()), byName("redstone_torch")));
    public static final DeferredBlock<Block> REDSTONE_TORCH_LEVER = registerBlock("redstone_torch_lever", () -> new RedstoneTorchLever(torchSettings(7), byName("redstone_torch")));
    public static final DeferredBlock<Block> REDSTONE_WALL_TORCH_LEVER = registerBlock("redstone_wall_torch_lever", () -> new RedstoneWallTorchLever(torchSettings(7).dropsLike(REDSTONE_TORCH_LEVER.get()), byName("redstone_torch")));

    public static BlockBehaviour.Properties torchSettings(int light) {
        return BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().lightLevel(state -> light).sound(SoundType.WOOD);
    }

    /**
     * Methods
     */

    private static Block byName(String block) {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(block));
    }

    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    private static ToIntFunction<BlockState> getLampButtonLight() {
        return state -> state.getValue(LampButton.PRESSED) ? 15 : 0;
    }
}
