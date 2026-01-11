package TeethDweller.teethdweller.renderer;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.TeethDwellerClient;
import TeethDweller.teethdweller.entity.TeethDwellerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Teeth Dweller entity.
 * Uses a custom model for unique appearance.
 */
@Environment(EnvType.CLIENT)
public class TeethDwellerEntityRenderer extends MobEntityRenderer<TeethDwellerEntity, LivingEntityRenderState, TeethDwellerModel> {

    private static final Identifier TEXTURE = Identifier.of(TeethDweller.MOD_ID, "textures/entity/teeth_dweller.png");

    public TeethDwellerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TeethDwellerModel(context.getPart(TeethDwellerClient.TEETH_DWELLER_LAYER)), 0.8f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return TEXTURE;
    }
}

