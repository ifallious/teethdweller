package TeethDweller.teethdweller;

import TeethDweller.teethdweller.handler.TeethDwellerSpawnHandler;
import TeethDweller.teethdweller.network.ModNetworking;
import TeethDweller.teethdweller.registry.ModDataComponents;
import TeethDweller.teethdweller.registry.ModEntities;
import TeethDweller.teethdweller.registry.ModItems;
import TeethDweller.teethdweller.registry.ModStructures;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeethDweller implements ModInitializer {
	public static final String MOD_ID = "teethdweller";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Teeth Dweller mod...");

		// Register all mod content
		ModDataComponents.initialize();
		ModItems.initialize();
		ModEntities.initialize();
		ModStructures.initialize();

		// Register networking
		ModNetworking.registerServerPackets();

		// Register spawn handler
		TeethDwellerSpawnHandler.register();

		LOGGER.info("Teeth Dweller mod initialized! Remember to brush your teeth!");
	}
}