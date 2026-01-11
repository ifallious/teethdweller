package TeethDweller.teethdweller.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialogue box screen that displays NPC dialogue on screen instead of in chat.
 */
@Environment(EnvType.CLIENT)
public class DialogueScreen extends Screen {
    
    private final String speakerName;
    private final List<Text> messages;
    private int currentMessageIndex = 0;
    private ButtonWidget nextButton;
    private ButtonWidget closeButton;
    
    public DialogueScreen(String speakerName, List<Text> messages) {
        super(Text.literal("Dialogue"));
        this.speakerName = speakerName != null ? speakerName : "Unknown";
        this.messages = new ArrayList<>();
        
        // Ensure we have valid messages
        if (messages != null && !messages.isEmpty()) {
            for (Text msg : messages) {
                if (msg != null) {
                    // Verify the text has content
                    String textContent = msg.getString();
                    if (textContent != null && !textContent.trim().isEmpty()) {
                        this.messages.add(msg);
                    } else {
                        // If getString() returns empty, create a new Text from the content
                        this.messages.add(Text.literal(msg.getContent().toString()));
                    }
                }
            }
        }
        
        // Debug: if still empty, add a fallback
        if (this.messages.isEmpty()) {
            this.messages.add(Text.literal("No messages received"));
        }
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Calculate dialogue box dimensions
        int boxWidth = Math.min(330, width - 40);
        int boxHeight = 90;
        int boxX = (width - boxWidth) / 2;
        int boxY = height - boxHeight - 40;
        
        // Create next button (if there are more messages)
        if (currentMessageIndex < messages.size() - 1) {
            nextButton = ButtonWidget.builder(
                Text.literal("Next"),
                button -> {
                    currentMessageIndex++;
                    updateButtons();
                }
            ).dimensions(boxX + boxWidth - 100, boxY + boxHeight - 25, 80, 20).build();
            addDrawableChild(nextButton);
        }
        
        // Create close button
        closeButton = ButtonWidget.builder(
            Text.literal("Close"),
            button -> close()
        ).dimensions(boxX + boxWidth - 100, boxY + boxHeight - 25, 80, 20).build();
        addDrawableChild(closeButton);
        
        updateButtons();
    }
    
    private void updateButtons() {
        if (nextButton != null) {
            nextButton.visible = currentMessageIndex < messages.size() - 1;
        }
        if (closeButton != null) {
            closeButton.visible = currentMessageIndex >= messages.size() - 1 || messages.size() == 1;
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw semi-transparent background
        context.fill(0, 0, width, height, 0x80000000);
        
        // Calculate dialogue box dimensions
        int boxWidth = Math.min(330, width - 40);
        int boxHeight = 90;
        int boxX = (width - boxWidth) / 2;
        int boxY = height - boxHeight - 40;
        
        // Draw dialogue box background
        context.fill(boxX - 2, boxY - 2, boxX + boxWidth + 2, boxY + boxHeight + 2, 0xFF000000);
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xFF2C2C2C);
        context.fill(boxX, boxY, boxX + boxWidth, boxY + 20, 0xFF1A1A1A);
        
        // Draw speaker name
        Text speakerText = Text.literal(speakerName).formatted(Formatting.BOLD, Formatting.WHITE);
        context.drawTextWithShadow(textRenderer, speakerText, boxX + 10, boxY + 6, 0xFFFFFFFF);
        
        // Draw current message
        if (currentMessageIndex < messages.size() && !messages.isEmpty()) {
            Text message = messages.get(currentMessageIndex);
            if (message != null) {
                int lineHeight = textRenderer.fontHeight + 2;
                int startY = boxY + 30;
                
                // Try to render the message - use wrapLines which should work with Text objects
                try {
                    var wrappedLines = textRenderer.wrapLines(message, boxWidth - 20);
                    if (!wrappedLines.isEmpty()) {
                        // Render wrapped lines
                        for (int i = 0; i < wrappedLines.size() && i < 3; i++) {
                            context.drawTextWithShadow(textRenderer, wrappedLines.get(i), boxX + 10, startY + i * lineHeight, 0xFFFFFFFF);
                        }
                    } else {
                        // If wrapping produced no lines, render the message directly
                        // This handles cases where the text might be empty or the Text object is malformed
                        String messageText = message.getString();
                        if (messageText != null && !messageText.trim().isEmpty()) {
                            // Render as literal text
                            context.drawTextWithShadow(textRenderer, Text.literal(messageText), boxX + 10, startY, 0xFFFFFFFF);
                        } else {
                            // Last resort: try rendering the Text object directly
                            context.drawTextWithShadow(textRenderer, message, boxX + 10, startY, 0xFFFFFFFF);
                        }
                    }
                } catch (Exception e) {
                    // If anything fails, try to render as plain text
                    String messageText = message.getString();
                    if (messageText != null && !messageText.isEmpty()) {
                        context.drawTextWithShadow(textRenderer, Text.literal(messageText), boxX + 10, startY, 0xFFFFFFFF);
                    }
                }
            }
        } else {
            // Debug: show if we have no messages
            context.drawTextWithShadow(textRenderer, Text.literal("No messages to display"), boxX + 10, boxY + 30, 0xFFFF0000);
        }
        
        // Draw page indicator if multiple messages
        if (messages.size() > 1) {
            String pageText = (currentMessageIndex + 1) + "/" + messages.size();
            int pageTextWidth = textRenderer.getWidth(pageText);
            context.drawTextWithShadow(textRenderer, pageText, boxX + boxWidth - pageTextWidth - 10, boxY + 6, 0xFFAAAAAA);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    
}
