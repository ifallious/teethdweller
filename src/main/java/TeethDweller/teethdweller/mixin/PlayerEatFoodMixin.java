package TeethDweller.teethdweller.mixin;

import TeethDweller.teethdweller.data.TeethDwellerDataManager;
import TeethDweller.teethdweller.data.TeethDwellerPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to prevent players from eating food when they have no teeth.
 */
@Mixin(PlayerEntity.class)
public class PlayerEatFoodMixin {

    @Inject(at = @At("HEAD"), method = "canConsume", cancellable = true)
    private void onCanConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity self = (PlayerEntity)(Object)this;

        if (!self.getEntityWorld().isClient()) {
            TeethDwellerPlayerData data = TeethDwellerDataManager.getPlayerData(self);

            if (!data.hasTeeth()) {
                self.sendMessage(Text.literal("You can't eat without teeth! Visit a dentist!")
                    .formatted(Formatting.RED), true);
                cir.setReturnValue(false);
            }
        }
    }
}

