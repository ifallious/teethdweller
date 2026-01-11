package TeethDweller.teethdweller.screen;

import TeethDweller.teethdweller.network.ClientNetworking;
import TeethDweller.teethdweller.screen.widget.TeethWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple 2D minigame for brushing teeth.
 * Player must move the brush back and forth across the teeth.
 */
@Environment(EnvType.CLIENT)
public class BrushingMinigameScreen extends Screen {

    private static final int TEETH_COUNT = 8;
    private static final int TOOTH_WIDTH = 30;
    private static final int TOOTH_HEIGHT = 50;
    private static final int TOOTH_GAP = 5;

    private List<TeethWidget> teethWidgets = new ArrayList<>();

    public BrushingMinigameScreen() {
        super(Text.literal("Brush Your Teeth!"));
    }

    @Override
    protected void init() {
        super.init();

        teethWidgets.clear();

        // Calculate starting position to center teeth
        int totalWidth = TEETH_COUNT * (TOOTH_WIDTH + TOOTH_GAP) - TOOTH_GAP;
        int teethStartX = (width - totalWidth) / 2;
        int teethStartY = (height - TOOTH_HEIGHT) / 2;

        // Create a widget for each tooth
        for (int i = 0; i < TEETH_COUNT; i++) {
            int toothX = teethStartX + i * (TOOTH_WIDTH + TOOTH_GAP);
            int toothY = teethStartY;

            TeethWidget toothWidget = new TeethWidget(toothX, toothY, TOOTH_WIDTH, TOOTH_HEIGHT);
            toothWidget.setOnBrushCallback(this::checkCompletion);
            teethWidgets.add(toothWidget);
            addDrawableChild(toothWidget);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw dark background
        context.fill(0, 0, width, height, 0xCC000000);

        // Draw title
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFFFF);

        // Draw instructions
        context.drawCenteredTextWithShadow(textRenderer,
            Text.literal("Click and drag across the teeth to brush them!"),
            width / 2, 40, 0xFFAAAAAA);

        // Render widgets (including teeth widgets)
        super.render(context, mouseX, mouseY, delta);

        // Draw progress
        int totalBrushes = 0;
        int requiredTotal = TEETH_COUNT * 3; // 3 brushes per tooth
        for (TeethWidget tooth : teethWidgets) {
            totalBrushes += tooth.getBrushCount();
        }

        String progress = "Progress: " + totalBrushes + "/" + requiredTotal;
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(progress),
            width / 2, height - 40, 0xFF00FF00);
    }

    private void checkCompletion() {
        // Check if all teeth are fully brushed
        boolean allBrushed = true;
        for (TeethWidget tooth : teethWidgets) {
            if (!tooth.isFullyBrushed()) {
                allBrushed = false;
                break;
            }
        }

        if (allBrushed) {
            onComplete();
        }
    }

    private void onComplete() {
        ClientNetworking.sendMinigameComplete(true);
        close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        super.close();
    }
}

