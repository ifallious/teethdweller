package TeethDweller.teethdweller.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class TeethDwellerModel extends EntityModel<LivingEntityRenderState> {
    public TeethDwellerModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData createBodyLayer() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();
        partdefinition.addChild("bb_main",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-8.0F, -6.0F, -8.0F, 16.0F, 6.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 22)
                .cuboid(-8.0F, -25.0F, -8.0F, 16.0F, 6.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 44)
                .cuboid(-8.0F, -19.0F, 6.0F, 16.0F, 13.0F, 2.0F, new Dilation(0.0F))
                .uv(36, 44)
                .cuboid(-8.0F, -19.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 44)
                .cuboid(-4.0F, -19.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(36, 52)
                .cuboid(0.0F, -19.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(44, 52)
                .cuboid(4.0F, -19.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 44)
                .cuboid(5.0F, -12.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 52)
                .cuboid(1.0F, -12.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 59)
                .cuboid(-3.0F, -12.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 59)
                .cuboid(-7.0F, -12.0F, -8.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );
        return TexturedModelData.of(meshdefinition, 128, 128);
    }
}
