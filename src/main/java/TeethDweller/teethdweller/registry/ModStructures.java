package TeethDweller.teethdweller.registry;

import TeethDweller.teethdweller.TeethDweller;

public class ModStructures {

    // For JSON-only structure in Fabric, we don't need to register anything in code.
    // The structure is defined entirely in JSON files:
    // - worldgen/structure/dentist_office.json (structure definition)
    // - worldgen/structure_set/dentist_office.json (placement rules)
    // - worldgen/template_pool/dentist_office/start.json (structure pieces)
    // - structure/dentist_office.nbt (the actual structure NBT file)
    // - tags/worldgen/biome/has_structure/dentist_office.json (biome tag)

    public static void initialize() {
        TeethDweller.LOGGER.info("Structures for " + TeethDweller.MOD_ID + " are loaded via JSON datapacks");
    }
}

