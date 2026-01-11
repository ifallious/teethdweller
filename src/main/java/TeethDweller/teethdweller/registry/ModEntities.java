package TeethDweller.teethdweller.registry;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.entity.DentistEntity;
import TeethDweller.teethdweller.entity.ReceptionistEntity;
import TeethDweller.teethdweller.entity.TeethDwellerEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    
    public static final EntityType<TeethDwellerEntity> TEETH_DWELLER = Registry.register(
        Registries.ENTITY_TYPE,
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TeethDweller.MOD_ID, "teeth_dweller")),
        EntityType.Builder.create(TeethDwellerEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 0.8f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TeethDweller.MOD_ID, "teeth_dweller")))
    );
    
    public static final EntityType<DentistEntity> DENTIST = Registry.register(
        Registries.ENTITY_TYPE,
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TeethDweller.MOD_ID, "dentist")),
        EntityType.Builder.create(DentistEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.6f, 1.95f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TeethDweller.MOD_ID, "dentist")))
    );
    
    public static final EntityType<ReceptionistEntity> RECEPTIONIST = Registry.register(
        Registries.ENTITY_TYPE,
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TeethDweller.MOD_ID, "receptionist")),
        EntityType.Builder.create(ReceptionistEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.6f, 1.95f)
            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TeethDweller.MOD_ID, "receptionist")))
    );
    
    public static void initialize() {
        TeethDweller.LOGGER.info("Registering entities for " + TeethDweller.MOD_ID);
        
        FabricDefaultAttributeRegistry.register(TEETH_DWELLER, TeethDwellerEntity.createTeethDwellerAttributes());
        FabricDefaultAttributeRegistry.register(DENTIST, DentistEntity.createDentistAttributes());
        FabricDefaultAttributeRegistry.register(RECEPTIONIST, ReceptionistEntity.createReceptionistAttributes());
    }
}

