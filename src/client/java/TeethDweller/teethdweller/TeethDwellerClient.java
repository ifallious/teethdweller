package TeethDweller.teethdweller;

import TeethDweller.teethdweller.network.ClientNetworking;
import TeethDweller.teethdweller.renderer.DentistEntityRenderer;
import TeethDweller.teethdweller.renderer.ReceptionistEntityRenderer;
import TeethDweller.teethdweller.renderer.TeethDwellerEntityRenderer;
import TeethDweller.teethdweller.renderer.TeethDwellerModel;
import TeethDweller.teethdweller.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class TeethDwellerClient implements ClientModInitializer {
	public static final EntityModelLayer TEETH_DWELLER_LAYER = new EntityModelLayer(Identifier.of(TeethDweller.MOD_ID, "teeth_dweller"), "main");

	@Override
	public void onInitializeClient() {
		// Register networking
		ClientNetworking.registerClientPackets();

		// Register model layers
		EntityModelLayerRegistry.registerModelLayer(TEETH_DWELLER_LAYER, TeethDwellerModel::createBodyLayer);

		// Register entity renderers
		EntityRendererRegistry.register(ModEntities.TEETH_DWELLER, TeethDwellerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.DENTIST, DentistEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.RECEPTIONIST, ReceptionistEntityRenderer::new);
	}
}