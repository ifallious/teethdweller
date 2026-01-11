package TeethDweller.teethdweller.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

/**
 * A widget that represents a single tooth and handles brushing interactions.
 */
@Environment(EnvType.CLIENT)
public class TeethWidget extends ClickableWidget {
    
    private static final int REQUIRED_BRUSHES = 3;
    
    private int brushCount = 0;
    private boolean isBrushing = false;
    private Runnable onBrushCallback;
    
    public TeethWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.literal("Tooth"));
    }
    
    public void setOnBrushCallback(Runnable callback) {
        this.onBrushCallback = callback;
    }
    
    public void brush() {
        if (brushCount < REQUIRED_BRUSHES) {
            brushCount++;
            if (onBrushCallback != null) {
                onBrushCallback.run();
            }
        }
    }
    
    public boolean isFullyBrushed() {
        return brushCount >= REQUIRED_BRUSHES;
    }
    
    public int getBrushCount() {
        return brushCount;
    }
    
    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Color based on brush count
        int color;
        if (brushCount >= REQUIRED_BRUSHES) {
            color = 0xFFFFFFFF; // White - fully brushed
        } else if (brushCount > 0) {
            color = 0xFFFFFF99; // Yellow - partially brushed
        } else {
            color = 0xFFCCCC99; // Dirty - not brushed
        }
        
        // Draw tooth
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color);
        
        // Draw border
        context.fill(getX(), getY(), getX() + getWidth(), getY() + 1, 0xFF333333); // top
        context.fill(getX(), getY() + getHeight() - 1, getX() + getWidth(), getY() + getHeight(), 0xFF333333); // bottom
        context.fill(getX(), getY(), getX() + 1, getY() + getHeight(), 0xFF333333); // left
        context.fill(getX() + getWidth() - 1, getY(), getX() + getWidth(), getY() + getHeight(), 0xFF333333); // right
    }
    
    @Override
    public void onClick(Click click, boolean doubled) {
        isBrushing = true;
        brush();
    }
    
    @Override
    public void onRelease(Click click) {
        isBrushing = false;
    }
    
    @Override
    protected void onDrag(Click click, double offsetX, double offsetY) {
        // Brush when dragging over this tooth
        if (!isBrushing) {
            isBrushing = true;
            brush();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        // Add narration for accessibility
        this.appendDefaultNarrations(builder);
    }
}

