package TeethDweller.teethdweller.network;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.item.ManualToothbrushItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModNetworking {
    
    // Packet IDs
    public static final Identifier OPEN_MINIGAME_ID = Identifier.of(TeethDweller.MOD_ID, "open_minigame");
    public static final Identifier MINIGAME_COMPLETE_ID = Identifier.of(TeethDweller.MOD_ID, "minigame_complete");
    public static final Identifier OPEN_DIALOGUE_ID = Identifier.of(TeethDweller.MOD_ID, "open_dialogue");
    
    // Payload for opening minigame (server -> client)
    public record OpenMinigamePayload() implements CustomPayload {
        public static final CustomPayload.Id<OpenMinigamePayload> ID = new CustomPayload.Id<>(OPEN_MINIGAME_ID);
        public static final PacketCodec<RegistryByteBuf, OpenMinigamePayload> CODEC = PacketCodec.unit(new OpenMinigamePayload());
        
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    // Payload for minigame completion (client -> server)
    public record MinigameCompletePayload(boolean success) implements CustomPayload {
        public static final CustomPayload.Id<MinigameCompletePayload> ID = new CustomPayload.Id<>(MINIGAME_COMPLETE_ID);
        public static final PacketCodec<RegistryByteBuf, MinigameCompletePayload> CODEC = PacketCodec.of(
            (payload, buf) -> buf.writeBoolean(payload.success),
            buf -> new MinigameCompletePayload(buf.readBoolean())
        );
        
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    
    // Payload for opening dialogue (server -> client)
    // We store messages as strings with formatting codes to avoid Text serialization complexity
    public record OpenDialoguePayload(String speakerName, List<String> messageStrings) implements CustomPayload {
        public static final CustomPayload.Id<OpenDialoguePayload> ID = new CustomPayload.Id<>(OPEN_DIALOGUE_ID);
        public static final PacketCodec<RegistryByteBuf, OpenDialoguePayload> CODEC = PacketCodec.of(
            (payload, buf) -> {
                buf.writeString(payload.speakerName);
                buf.writeCollection(payload.messageStrings, PacketCodecs.STRING);
            },
            buf -> {
                String speakerName = buf.readString();
                List<String> messageStrings = buf.readCollection(ArrayList::new, PacketCodecs.STRING);
                return new OpenDialoguePayload(speakerName, messageStrings);
            }
        );
        
        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
        
        // Helper method to create payload from Text list
        // We extract the string content using getString() which should return the text content
        public static OpenDialoguePayload fromTexts(String speakerName, List<Text> messages) {
            List<String> strings = new ArrayList<>();
            for (Text message : messages) {
                // getString() should return the plain text content without formatting codes
                String textContent = message.getString();
                // Ensure we have valid content
                if (textContent == null) {
                    textContent = "";
                }
                strings.add(textContent);
            }
            return new OpenDialoguePayload(speakerName, strings);
        }
        
        // Helper method to convert to Text list
        // Create new Text.literal from the string content
        public List<Text> toTexts() {
            List<Text> texts = new ArrayList<>();
            for (String messageString : messageStrings) {
                if (messageString != null && !messageString.isEmpty()) {
                    texts.add(Text.literal(messageString));
                } else {
                    // Fallback to prevent empty messages
                    texts.add(Text.literal("(Empty message)"));
                }
            }
            return texts;
        }
    }
    
    public static void registerServerPackets() {
        // Register payload types
        PayloadTypeRegistry.playS2C().register(OpenMinigamePayload.ID, OpenMinigamePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OpenDialoguePayload.ID, OpenDialoguePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(MinigameCompletePayload.ID, MinigameCompletePayload.CODEC);
        
        // Handle minigame completion from client
        ServerPlayNetworking.registerGlobalReceiver(MinigameCompletePayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            if (payload.success()) {
                context.server().execute(() -> {
                    ManualToothbrushItem.onBrushingComplete(player);
                });
            }
        });
    }
    
    public static void sendOpenMinigamePacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new OpenMinigamePayload());
    }
    
    public static void sendOpenDialoguePacket(ServerPlayerEntity player, String speakerName, List<Text> messages) {
        ServerPlayNetworking.send(player, OpenDialoguePayload.fromTexts(speakerName, messages));
    }
}

