package TeethDweller.teethdweller.handler;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.data.TeethDwellerDataManager;
import TeethDweller.teethdweller.data.TeethDwellerPlayerData;
import TeethDweller.teethdweller.entity.TeethDwellerEntity;
import TeethDweller.teethdweller.registry.ModEntities;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Random;

/**
 * Handles the spawning logic for teeth dwellers.
 */
public class TeethDwellerSpawnHandler {
    
    private static final Random random = new Random();
    private static final long CHECK_INTERVAL = 1200; // Check every minute (1200 ticks)
    
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long time = server.getOverworld().getTime();
            
            // Only check periodically
            if (time % CHECK_INTERVAL != 0) return;
            
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                processPlayer(player, time);
            }
        });
    }
    
    private static void processPlayer(ServerPlayerEntity player, long currentTime) {
        TeethDwellerPlayerData data = TeethDwellerDataManager.getPlayerData(player);
        ServerWorld world = (ServerWorld) player.getEntityWorld();
        
        // Check for new day
        long currentDay = currentTime / 24000;
        TeethDwellerDataManager.onNewDay(player, currentDay);
        
        // Check if dweller should hatch
        if (data.shouldDwellerHatch(currentTime)) {
            spawnTeethDweller(player, data);
            return;
        }
        
        // Check for new dweller infection (only at night, if no current dweller)
        if (!data.hasDweller() && data.hasTeeth() && isNight(world)) {
            tryInfectPlayer(player, data, currentTime);
        }
    }
    
    private static boolean isNight(ServerWorld world) {
        long timeOfDay = world.getTimeOfDay() % 24000;
        return timeOfDay >= 13000 && timeOfDay <= 23000;
    }
    
    private static void tryInfectPlayer(ServerPlayerEntity player, TeethDwellerPlayerData data, long currentTime) {
        double spawnChance = TeethDwellerDataManager.getDwellerSpawnChance(player);
        
        // Roll for infection (adjusted for check interval)
        // We check every minute, so divide chance appropriately
        double adjustedChance = spawnChance / 10.0; // 10 checks per night roughly
        
        if (random.nextDouble() < adjustedChance) {
            data.infectWithDweller(currentTime);
            TeethDwellerDataManager.savePlayerData(player);
            
            // Silent infection - player doesn't know!
            TeethDweller.LOGGER.info("Player {} has been infected with a Teeth Dweller!", player.getName().getString());
        }
    }
    
    private static void spawnTeethDweller(ServerPlayerEntity player, TeethDwellerPlayerData data) {
        ServerWorld world = (ServerWorld) player.getEntityWorld();
        
        // Create the teeth dweller
        TeethDwellerEntity dweller = new TeethDwellerEntity(ModEntities.TEETH_DWELLER, world);
        dweller.setPosition(player.getX(), player.getY(), player.getZ());
        dweller.setTargetPlayer(player.getUuid());
        
        // Spawn it
        world.spawnEntity(dweller);
        
        // Remove player's teeth
        data.setHasTeeth(false);
        data.clearDweller();
        TeethDwellerDataManager.savePlayerData(player);
        
        // Notify the player
        player.sendMessage(Text.literal("AAAARGH! Something horrible bursts from your mouth!")
            .formatted(Formatting.DARK_RED, Formatting.BOLD), false);
        player.sendMessage(Text.literal("The Teeth Dweller has emerged! You've lost all your teeth!")
            .formatted(Formatting.RED), false);
        player.sendMessage(Text.literal("Visit a dentist to get new teeth...")
            .formatted(Formatting.YELLOW), false);
        
        TeethDweller.LOGGER.info("Teeth Dweller spawned from player {}!", player.getName().getString());
    }
}

