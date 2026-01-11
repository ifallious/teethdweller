package TeethDweller.teethdweller;

import TeethDweller.teethdweller.network.ClientNetworking;
import TeethDweller.teethdweller.renderer.DentistEntityRenderer;
import TeethDweller.teethdweller.renderer.ReceptionistEntityRenderer;
import TeethDweller.teethdweller.renderer.TeethDwellerEntityRenderer;
import TeethDweller.teethdweller.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TeethDwellerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register networking
		ClientNetworking.registerClientPackets();

		// Register entity renderers
		EntityRendererRegistry.register(ModEntities.TEETH_DWELLER, TeethDwellerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.DENTIST, DentistEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.RECEPTIONIST, ReceptionistEntityRenderer::new);
	}
}