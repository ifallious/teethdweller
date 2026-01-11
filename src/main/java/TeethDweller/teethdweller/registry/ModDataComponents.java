package TeethDweller.teethdweller.registry;

import TeethDweller.teethdweller.TeethDweller;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    
    // Last time the toothbrush was used (in game ticks)
    public static final ComponentType<Long> LAST_USE_TIME = register("last_use_time",
        builder -> builder.codec(Codec.LONG).packetCodec(PacketCodecs.VAR_LONG));
    
    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TeethDweller.MOD_ID, name),
            builderOperator.apply(ComponentType.builder()).build()
        );
    }
    
    public static void initialize() {
        TeethDweller.LOGGER.info("Registering data components for " + TeethDweller.MOD_ID);
    }
}

