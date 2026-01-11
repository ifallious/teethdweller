package TeethDweller.teethdweller.renderer;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.entity.ReceptionistEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Receptionist entity.
 * Uses a biped model for humanoid appearance.
 */
@Environment(EnvType.CLIENT)
public class ReceptionistEntityRenderer extends MobEntityRenderer<ReceptionistEntity, BipedEntityRenderState, BipedEntityModel<BipedEntityRenderState>> {

    private static final Identifier TEXTURE = Identifier.of(TeethDweller.MOD_ID, "textures/entity/receptionist.png");

    public ReceptionistEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public BipedEntityRenderState createRenderState() {
        return new BipedEntityRenderState();
    }

    @Override
    public Identifier getTexture(BipedEntityRenderState state) {
        return TEXTURE;
    }
}

