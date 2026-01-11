package TeethDweller.teethdweller.item;

import TeethDweller.teethdweller.data.TeethDwellerDataManager;
import TeethDweller.teethdweller.data.TeethDwellerPlayerData;
import TeethDweller.teethdweller.network.ModNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Manual toothbrush that requires a minigame to use.
 */
public class ManualToothbrushItem extends Item {
    
    public ManualToothbrushItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        
        TeethDwellerPlayerData data = TeethDwellerDataManager.getPlayerData(user);
        long currentTime = world.getTime();
        
        // Check if player has teeth
        if (!data.hasTeeth()) {
            user.sendMessage(Text.literal("You don't have any teeth to brush!")
                .formatted(Formatting.RED), true);
            return ActionResult.FAIL;
        }
        
        // Check cooldown
        if (!data.canBrush(currentTime)) {
            long remainingTicks = data.getRemainingCooldown(currentTime);
            int remainingMinutes = (int)(remainingTicks / 1200); // 1200 ticks = 1 minute
            user.sendMessage(Text.literal("You need to wait " + remainingMinutes + " more minutes before brushing again.")
                .formatted(Formatting.YELLOW), true);
            return ActionResult.FAIL;
        }
        
        // Open the brushing minigame on client
        if (user instanceof ServerPlayerEntity serverPlayer) {
            ModNetworking.sendOpenMinigamePacket(serverPlayer);
        }
        
        return ActionResult.SUCCESS;
    }
    
    /**
     * Called when the minigame is completed successfully.
     */
    public static void onBrushingComplete(ServerPlayerEntity player) {
        TeethDwellerPlayerData data = TeethDwellerDataManager.getPlayerData(player);
        long currentTime = player.getEntityWorld().getTime();
        
        // Update brushing data
        data.setLastBrushTime(currentTime);
        data.incrementBrushesToday();
        
        // If infected, extend hatch time
        if (data.hasDweller()) {
            data.extendHatchTime(currentTime);
            data.incrementBrushesWhileInfected();
            player.sendMessage(Text.literal("Your teeth feel cleaner... but something still feels wrong.")
                .formatted(Formatting.DARK_PURPLE), true);
        } else {
            player.sendMessage(Text.literal("Your teeth are sparkling clean!")
                .formatted(Formatting.GREEN), true);
        }
        
        TeethDwellerDataManager.savePlayerData(player);
    }
}

