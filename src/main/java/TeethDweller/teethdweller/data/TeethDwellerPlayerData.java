package TeethDweller.teethdweller.data;

import net.minecraft.nbt.NbtCompound;

/**
 * Stores all teeth-related data for a player.
 */
public class TeethDwellerPlayerData {
    // Brushing tracking
    private int brushesToday = 0;
    private long lastBrushTime = 0; // Game time when last brushed
    private long lastDayChecked = 0; // The in-game day when we last reset brush count
    
    // Dweller state
    private boolean hasDweller = false;
    private long dwellerSpawnTime = 0; // When the dweller entered teeth
    private long dwellerHatchTime = 0; // When the dweller will hatch (absolute game time)
    private int brushesWhileInfected = 0; // How many times brushed today while infected
    
    // Teeth state
    private boolean hasTeeth = true;
    
    // Cooldown tracking (6 in-game hours = 6000 ticks)
    public static final long BRUSH_COOLDOWN_TICKS = 6000;
    
    // Dweller timing constants
    public static final long BASE_HATCH_TIME_TICKS = 24000; // 1 day = 24000 ticks
    public static final long MAX_EXTENSION_TICKS = 15 * 24000; // 15 days max
    
    public TeethDwellerPlayerData() {}
    
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("brushesToday", brushesToday);
        nbt.putLong("lastBrushTime", lastBrushTime);
        nbt.putLong("lastDayChecked", lastDayChecked);
        nbt.putBoolean("hasDweller", hasDweller);
        nbt.putLong("dwellerSpawnTime", dwellerSpawnTime);
        nbt.putLong("dwellerHatchTime", dwellerHatchTime);
        nbt.putInt("brushesWhileInfected", brushesWhileInfected);
        nbt.putBoolean("hasTeeth", hasTeeth);
    }
    
    public void readFromNbt(NbtCompound nbt) {
        brushesToday = nbt.getInt("brushesToday", 0);
        lastBrushTime = nbt.getLong("lastBrushTime", 0);
        lastDayChecked = nbt.getLong("lastDayChecked", 0);
        hasDweller = nbt.getBoolean("hasDweller", false);
        dwellerSpawnTime = nbt.getLong("dwellerSpawnTime", 0);
        dwellerHatchTime = nbt.getLong("dwellerHatchTime", 0);
        brushesWhileInfected = nbt.getInt("brushesWhileInfected", 0);
        hasTeeth = nbt.getBoolean("hasTeeth", true);
    }
    
    // Getters and setters
    public int getBrushesToday() { return brushesToday; }
    public void setBrushesToday(int count) { this.brushesToday = count; }
    public void incrementBrushesToday() { this.brushesToday++; }
    
    public long getLastBrushTime() { return lastBrushTime; }
    public void setLastBrushTime(long time) { this.lastBrushTime = time; }
    
    public long getLastDayChecked() { return lastDayChecked; }
    public void setLastDayChecked(long day) { this.lastDayChecked = day; }
    
    public boolean hasDweller() { return hasDweller; }
    public void setHasDweller(boolean has) { this.hasDweller = has; }
    
    public long getDwellerSpawnTime() { return dwellerSpawnTime; }
    public void setDwellerSpawnTime(long time) { this.dwellerSpawnTime = time; }
    
    public long getDwellerHatchTime() { return dwellerHatchTime; }
    public void setDwellerHatchTime(long time) { this.dwellerHatchTime = time; }
    
    public int getBrushesWhileInfected() { return brushesWhileInfected; }
    public void setBrushesWhileInfected(int count) { this.brushesWhileInfected = count; }
    public void incrementBrushesWhileInfected() { this.brushesWhileInfected++; }
    
    public boolean hasTeeth() { return hasTeeth; }
    public void setHasTeeth(boolean has) { this.hasTeeth = has; }
    
    public boolean canBrush(long currentTime) {
        // If never brushed before (lastBrushTime == 0), allow brushing
        if (lastBrushTime == 0) {
            return true;
        }
        return currentTime - lastBrushTime >= BRUSH_COOLDOWN_TICKS;
    }
    
    public long getRemainingCooldown(long currentTime) {
        long elapsed = currentTime - lastBrushTime;
        return Math.max(0, BRUSH_COOLDOWN_TICKS - elapsed);
    }
    
    /**
     * Extends the hatch time based on how many times brushed while infected.
     * 1st brush: +0.75 days, 2nd: +0.5 days, 3rd: +0.25 days
     */
    public void extendHatchTime(long currentTime) {
        if (!hasDweller) return;
        
        long extension = switch (brushesWhileInfected) {
            case 0 -> (long)(0.75 * 24000); // 0.75 days
            case 1 -> (long)(0.5 * 24000);  // 0.5 days
            case 2 -> (long)(0.25 * 24000); // 0.25 days
            default -> 0;
        };
        
        // Cap at 15 days from initial spawn
        long maxHatchTime = dwellerSpawnTime + MAX_EXTENSION_TICKS;
        dwellerHatchTime = Math.min(dwellerHatchTime + extension, maxHatchTime);
    }
    
    /**
     * Infects the player with a teeth dweller.
     */
    public void infectWithDweller(long currentTime) {
        hasDweller = true;
        dwellerSpawnTime = currentTime;
        dwellerHatchTime = currentTime + BASE_HATCH_TIME_TICKS;
        brushesWhileInfected = 0;
    }
    
    /**
     * Clears the dweller infection (after hatching or dentist removal).
     */
    public void clearDweller() {
        hasDweller = false;
        dwellerSpawnTime = 0;
        dwellerHatchTime = 0;
        brushesWhileInfected = 0;
    }
    
    /**
     * Checks if the dweller should hatch.
     */
    public boolean shouldDwellerHatch(long currentTime) {
        return hasDweller && currentTime >= dwellerHatchTime;
    }
}

