package TeethDweller.teethdweller.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * The Teeth Dweller - a horrifying creature that emerges from the player's mouth.
 * Very strong and fast, difficult to defeat.
 */
public class TeethDwellerEntity extends HostileEntity {
    
    private UUID targetPlayerUuid;
    
    public TeethDwellerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Override
    protected void initGoals() {
        // High priority: attack the player it spawned from
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.5, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
        
        // Target the player it spawned from first, then any player
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }
    
    public static DefaultAttributeContainer.Builder createTeethDwellerAttributes() {
        return HostileEntity.createHostileAttributes()
            .add(EntityAttributes.MAX_HEALTH, 100.0)  // Very tanky
            .add(EntityAttributes.ATTACK_DAMAGE, 8.0)  // High damage
            .add(EntityAttributes.MOVEMENT_SPEED, 0.6)  // Very fast
            .add(EntityAttributes.FOLLOW_RANGE, 500.0)  // Long tracking range
            .add(EntityAttributes.ARMOR, 0.0)  // Some armor
            .add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.5);  // Hard to knock back
    }
    
    public void setTargetPlayer(UUID playerUuid) {
        this.targetPlayerUuid = playerUuid;
    }
    
    public UUID getTargetPlayerUuid() {
        return targetPlayerUuid;
    }
    
    @Override
    public void tick() {
        super.tick();

        // If we have a specific target player, prioritize them
        if (targetPlayerUuid != null && getTarget() == null) {
            PlayerEntity targetPlayer = getEntityWorld().getPlayerByUuid(targetPlayerUuid);
            if (targetPlayer != null && targetPlayer.isAlive()) {
                setTarget(targetPlayer);
            }
        }
    }

    @Override
    public boolean cannotDespawn() {
        // Don't despawn - player must deal with it
        return true;
    }
}

