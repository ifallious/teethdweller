package TeethDweller.teethdweller.entity;

import TeethDweller.teethdweller.data.TeethDwellerDataManager;
import TeethDweller.teethdweller.data.TeethDwellerPlayerData;
import TeethDweller.teethdweller.network.ModNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The Receptionist NPC - can check if player has a teeth dweller.
 */
public class ReceptionistEntity extends PathAwareEntity {
    
    public ReceptionistEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        // Set default name if not already set (e.g., when spawned via command)
        // NBT loading will override this if the entity has a saved custom name
        if (this.getCustomName() == null) {
            this.setCustomName(Text.literal("Dental Receptionist").formatted(Formatting.GREEN));
        }
        this.setCustomNameVisible(true);
    }
    
    @Override
    protected void initGoals() {
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(4, new LookAroundGoal(this));
    }
    
    public static DefaultAttributeContainer.Builder createReceptionistAttributes() {
        return PathAwareEntity.createMobAttributes()
            .add(EntityAttributes.MAX_HEALTH, 20.0)
            .add(EntityAttributes.MOVEMENT_SPEED, 0.5);
    }
    
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (getEntityWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.PASS;
        }

        TeethDwellerPlayerData data = TeethDwellerDataManager.getPlayerData(player);
        String speakerName = this.getCustomName() != null ? this.getCustomName().getString() : "Dental Receptionist";
        List<Text> messages = new ArrayList<>();

        messages.add(Text.literal("Welcome to the Dental Office! Let me check your records...")
            .formatted(Formatting.WHITE));

        // Check teeth status
        if (!data.hasTeeth()) {
            messages.add(Text.literal("Oh my! You don't have any teeth! Please see the dentist immediately!")
                .formatted(Formatting.RED));
            ModNetworking.sendOpenDialoguePacket(serverPlayer, speakerName, messages);
            return ActionResult.SUCCESS;
        }

        // Check for dweller
        if (data.hasDweller()) {
            long currentTime = player.getEntityWorld().getTime();
            long ticksUntilHatch = data.getDwellerHatchTime() - currentTime;
            int hoursUntilHatch = (int)(ticksUntilHatch / 1000); // 1000 ticks = 1 hour
            
            messages.add(Text.literal("I'm detecting something unusual in your dental scan...")
                .formatted(Formatting.YELLOW));
            messages.add(Text.literal("You have a Teeth Dweller! It will emerge in about " + hoursUntilHatch + " hours!")
                .formatted(Formatting.RED));
            messages.add(Text.literal("Please see the dentist to have it removed!")
                .formatted(Formatting.GOLD));
        } else {
            messages.add(Text.literal("Your dental records look clean! No issues detected.")
                .formatted(Formatting.GREEN));
            messages.add(Text.literal("Remember to brush " + (3 - data.getBrushesToday()) + " more times today!")
                .formatted(Formatting.AQUA));
        }
        
        ModNetworking.sendOpenDialoguePacket(serverPlayer, speakerName, messages);
        return ActionResult.SUCCESS;
    }
    
    @Override
    public boolean cannotDespawn() {
        return true;
    }
    
    @Override
    protected boolean canStartRiding(net.minecraft.entity.Entity entity) {
        return false;
    }
}

