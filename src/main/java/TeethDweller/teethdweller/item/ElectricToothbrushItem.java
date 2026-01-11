package TeethDweller.teethdweller.item;

import TeethDweller.teethdweller.data.TeethDwellerDataManager;
import TeethDweller.teethdweller.data.TeethDwellerPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Electric toothbrush that skips the minigame.
 */
public class ElectricToothbrushItem extends Item {
    
    public ElectricToothbrushItem(Settings settings) {
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
            int remainingMinutes = (int)(remainingTicks / 1200);
            user.sendMessage(Text.literal("You need to wait " + remainingMinutes + " more minutes before brushing again.")
                .formatted(Formatting.YELLOW), true);
            return ActionResult.FAIL;
        }
        
        // Electric toothbrush skips the minigame - instant brushing!
        performBrushing(user, data, currentTime);
        
        // Play electric buzzing sound
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BEEHIVE_WORK, 
            SoundCategory.PLAYERS, 1.0f, 2.0f);
        
        return ActionResult.SUCCESS;
    }
    
    private void performBrushing(PlayerEntity player, TeethDwellerPlayerData data, long currentTime) {
        // Update brushing data
        data.setLastBrushTime(currentTime);
        data.incrementBrushesToday();
        
        // If infected, extend hatch time
        if (data.hasDweller()) {
            data.extendHatchTime(currentTime);
            data.incrementBrushesWhileInfected();
            player.sendMessage(Text.literal("The electric brush cleans efficiently... but something still feels wrong.")
                .formatted(Formatting.DARK_PURPLE), true);
        } else {
            player.sendMessage(Text.literal("Your teeth are sparkling clean! The electric brush works wonders!")
                .formatted(Formatting.GREEN), true);
        }
        
        TeethDwellerDataManager.savePlayerData(player);
    }
}

