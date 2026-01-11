package TeethDweller.teethdweller.renderer;

import TeethDweller.teethdweller.TeethDweller;
import TeethDweller.teethdweller.entity.TeethDwellerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SpiderEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Teeth Dweller entity.
 * Uses a spider model for creepy appearance.
 */
@Environment(EnvType.CLIENT)
public class TeethDwellerEntityRenderer extends MobEntityRenderer<TeethDwellerEntity, LivingEntityRenderState, SpiderEntityModel> {

    private static final Identifier TEXTURE = Identifier.of(TeethDweller.MOD_ID, "textures/entity/teeth_dweller.png");

    public TeethDwellerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SpiderEntityModel(context.getPart(EntityModelLayers.SPIDER)), 0.8f);
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

