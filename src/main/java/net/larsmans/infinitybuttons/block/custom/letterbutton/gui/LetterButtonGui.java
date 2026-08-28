package net.larsmans.infinitybuttons.block.custom.letterbutton.gui;

import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButton;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButtonEnum;
import net.larsmans.infinitybuttons.network.LetterButtonSelectPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class LetterButtonGui extends Screen {
    private static final ResourceLocation LETTER_TEXTURE = ResourceLocation.fromNamespaceAndPath("infinitybuttons", "textures/block/letter_button/characters.png");
    private static final int BUTTON_WIDTH = 24;
    private static final int BUTTON_HEIGHT = 24;
    private static final int BUTTON_MARGIN = 4;
    private static final int BUTTONS_PER_ROW = 7;
    private static final int NUM_BUTTONS = 49;

    private final LetterButton letterButton;
    private final BlockState state;
    private final Level level;
    private final BlockPos pos;
    private static int selectedButton;

    public LetterButtonGui(LetterButton letterButton, BlockState state, Level level, BlockPos pos) {
        super(Component.translatable("block.infinitybuttons.letter_button"));
        this.letterButton = letterButton;
        this.state = state;
        this.level = level;
        this.pos = pos;
    }

    @Override
    protected void init() {
        selectedButton = letterButton.getEnumId(state);
        super.init();
        int startX = (width - (BUTTONS_PER_ROW * (BUTTON_WIDTH + BUTTON_MARGIN))) / 2;
        int startY = (height - (((NUM_BUTTONS - 1) / BUTTONS_PER_ROW + 1) * (BUTTON_HEIGHT + BUTTON_MARGIN))) / 2;

        for (int i = 0; i < NUM_BUTTONS; i++) {
            int row = i / BUTTONS_PER_ROW;
            int col = i % BUTTONS_PER_ROW;
            int x = startX + col * (BUTTON_WIDTH + BUTTON_MARGIN);
            int y = startY + row * (BUTTON_HEIGHT + BUTTON_MARGIN);
            int button = i;
            addRenderableWidget(new ImageLetterButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, col * 20, row * 20, button, b -> onClick(button)));
        }

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> onClose())
                .bounds((width - 80) / 2, startY + (((NUM_BUTTONS - 1) / BUTTONS_PER_ROW) + 1) * (BUTTON_HEIGHT + BUTTON_MARGIN), 80, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        for (LetterButtonEnum buttonEnum : LetterButtonEnum.values()) {
            if (selectedButton == buttonEnum.ordinal() && level.getBlockState(pos).getBlock() instanceof LetterButton) {
                letterButton.setState(state, level, pos, buttonEnum);
                PacketDistributor.sendToServer(new LetterButtonSelectPayload(pos, buttonEnum));
            }
        }
        super.onClose();
    }

    public static int getSelectedButton() {
        return selectedButton;
    }

    protected void onClick(int button) {
        selectedButton = button;
        onClose();
    }

    public static class ImageLetterButton extends AbstractButton {
        private final int texX;
        private final int texY;
        private final int buttonId;

        public ImageLetterButton(int x, int y, int width, int height, int texX, int texY, int buttonId, OnPress onPress) {
            super(x, y, width, height, Component.empty());
            this.texX = texX;
            this.texY = texY;
            this.buttonId = buttonId;
            this.onPress = onPress;
        }

        private final OnPress onPress;

        @Override
        public void onPress() {
            this.onPress.onPress(this);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(LETTER_TEXTURE, getX(), getY(), texX, texY, getWidth(), getHeight());
            if (isHovered || LetterButtonGui.getSelectedButton() == buttonId) {
                int color = isHovered ? 0xFF808080 : 0xFFC0C0C0;
                int t = 2;
                graphics.fill(getX() - t, getY() - t, getX() + getWidth() + t, getY(), color);
                graphics.fill(getX() - t, getY(), getX(), getY() + getHeight(), color);
                graphics.fill(getX() - t, getY() + getHeight(), getX() + getWidth() + t, getY() + getHeight() + t, color);
                graphics.fill(getX() + getWidth(), getY(), getX() + getWidth() + t, getY() + getHeight(), color);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
        }

        public interface OnPress {
            void onPress(ImageLetterButton button);
        }
    }
}
