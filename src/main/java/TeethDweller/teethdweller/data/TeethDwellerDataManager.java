package TeethDweller.teethdweller.data;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages teeth dweller data for all players.
 * Uses a simple in-memory cache that persists for the session.
 */
public class TeethDwellerDataManager {
    private static final Map<UUID, TeethDwellerPlayerData> playerDataCache = new HashMap<>();

    public static TeethDwellerPlayerData getPlayerData(PlayerEntity player) {
        return getPlayerData(player.getUuid());
    }

    public static TeethDwellerPlayerData getPlayerData(UUID uuid) {
        return playerDataCache.computeIfAbsent(uuid, k -> new TeethDwellerPlayerData());
    }

    public static void savePlayerData(PlayerEntity player) {
        // Data is already in cache, nothing to do for now
        // In a full implementation, this would persist to disk
    }

    public static void clearCache(PlayerEntity player) {
        playerDataCache.remove(player.getUuid());
    }

    /**
     * Called when a new day starts to reset brush counts.
     */
    public static void onNewDay(PlayerEntity player, long currentDay) {
        TeethDwellerPlayerData data = getPlayerData(player);
        if (data.getLastDayChecked() < currentDay) {
            data.setBrushesToday(0);
            data.setBrushesWhileInfected(0);
            data.setLastDayChecked(currentDay);
        }
    }

    /**
     * Gets the spawn chance for a teeth dweller based on brushing today.
     * 3 brushes: 0.1%, 2 brushes: 1%, 1 brush: 10%, 0 brushes: 50%
     */
    public static double getDwellerSpawnChance(PlayerEntity player) {
        TeethDwellerPlayerData data = getPlayerData(player);

        // Can't spawn if already has dweller or no teeth
        if (data.hasDweller() || !data.hasTeeth()) {
            return 0.0;
        }

        return switch (data.getBrushesToday()) {
            case 0 -> 0.50;  // 50%
            case 1 -> 0.10;  // 10%
            case 2 -> 0.01;  // 1%
            default -> 0.001; // 0.1% for 3+
        };
    }
}

