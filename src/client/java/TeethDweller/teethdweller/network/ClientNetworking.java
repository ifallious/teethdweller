package TeethDweller.teethdweller.network;

import TeethDweller.teethdweller.screen.BrushingMinigameScreen;
import TeethDweller.teethdweller.screen.DialogueScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ClientNetworking {
    
    public static void registerClientPackets() {
        // Handle open minigame packet from server
        ClientPlayNetworking.registerGlobalReceiver(
            ModNetworking.OpenMinigamePayload.ID,
            (payload, context) -> {
                context.client().execute(() -> {
                    MinecraftClient.getInstance().setScreen(new BrushingMinigameScreen());
                });
            }
        );
        
        // Handle open dialogue packet from server
        ClientPlayNetworking.registerGlobalReceiver(
            ModNetworking.OpenDialoguePayload.ID,
            (payload, context) -> {
                context.client().execute(() -> {
                    MinecraftClient.getInstance().setScreen(new DialogueScreen(payload.speakerName(), payload.toTexts()));
                });
            }
        );
    }
    
    public static void sendMinigameComplete(boolean success) {
        ClientPlayNetworking.send(new ModNetworking.MinigameCompletePayload(success));
    }
}

