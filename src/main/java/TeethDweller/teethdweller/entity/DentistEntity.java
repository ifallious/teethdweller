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
 * The Dentist NPC - can check for and remove teeth dwellers, and restore teeth.
 */
public class DentistEntity extends PathAwareEntity {
    
    public DentistEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        // Set default name if not already set (e.g., when spawned via command)
        // NBT loading will override this if the entity has a saved custom name
        if (this.getCustomName() == null) {
            this.setCustomName(Text.literal("Dr. Molar").formatted(Formatting.AQUA));
        }
        this.setCustomNameVisible(true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(4, new LookAroundGoal(this));
    }
    
    public static DefaultAttributeContainer.Builder createDentistAttributes() {
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
        
        // Check if player needs teeth restored
        if (!data.hasTeeth()) {
            restoreTeeth(serverPlayer, data);
            return ActionResult.SUCCESS;
        }
        
        // Check for dweller
        if (data.hasDweller()) {
            removeDweller(serverPlayer, data);
            return ActionResult.SUCCESS;
        }
        
        // Player is healthy
        String speakerName = this.getCustomName() != null ? this.getCustomName().getString() : "Dr. Molar";
        List<Text> messages = new ArrayList<>();
        messages.add(Text.literal("The dentist examines your teeth...")
            .formatted(Formatting.WHITE));
        messages.add(Text.literal("Your teeth look perfectly healthy! Keep up the good brushing!")
            .formatted(Formatting.GREEN));
        ModNetworking.sendOpenDialoguePacket(serverPlayer, speakerName, messages);
        
        return ActionResult.SUCCESS;
    }
    
    private void removeDweller(ServerPlayerEntity player, TeethDwellerPlayerData data) {
        String speakerName = this.getCustomName() != null ? this.getCustomName().getString() : "Dr. Molar";
        List<Text> messages = new ArrayList<>();
        messages.add(Text.literal("The dentist examines your teeth closely...")
            .formatted(Formatting.WHITE));
        messages.add(Text.literal("Oh my! There's something living in there! Hold still...")
            .formatted(Formatting.RED));
        
        // Remove the dweller
        data.clearDweller();
        TeethDwellerDataManager.savePlayerData(player);
        
        messages.add(Text.literal("There we go! I've removed the creature. You're safe now!")
            .formatted(Formatting.GREEN));
        messages.add(Text.literal("Remember to brush your teeth regularly to prevent this!")
            .formatted(Formatting.YELLOW));
        ModNetworking.sendOpenDialoguePacket(player, speakerName, messages);
    }
    
    private void restoreTeeth(ServerPlayerEntity player, TeethDwellerPlayerData data) {
        String speakerName = this.getCustomName() != null ? this.getCustomName().getString() : "Dr. Molar";
        List<Text> messages = new ArrayList<>();
        messages.add(Text.literal("The dentist looks at your mouth...")
            .formatted(Formatting.WHITE));
        messages.add(Text.literal("Oh dear! You've lost all your teeth! Let me fix that...")
            .formatted(Formatting.RED));
        
        // Restore teeth
        data.setHasTeeth(true);
        data.clearDweller(); // Make sure dweller state is clean
        TeethDwellerDataManager.savePlayerData(player);
        
        messages.add(Text.literal("There! Brand new teeth! Take better care of them this time!")
            .formatted(Formatting.GREEN));
        ModNetworking.sendOpenDialoguePacket(player, speakerName, messages);
    }
    
    @Override
    public boolean cannotDespawn() {
        return true; // Dentists don't despawn
    }
    
    @Override
    protected boolean canStartRiding(net.minecraft.entity.Entity entity) {
        return false;
    }
}

