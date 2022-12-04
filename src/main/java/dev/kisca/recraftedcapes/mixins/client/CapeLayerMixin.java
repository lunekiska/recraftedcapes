package dev.kisca.recraftedcapes.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.kisca.recraftedcapes.CapeItem;
import dev.kisca.recraftedcapes.compat.curios.ClientCapeIntegration;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class CapeLayerMixin extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public CapeLayerMixin(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
    }

    @Inject(at = @At(value =  "HEAD"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", cancellable = true)
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci)
    {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack.is(CapeItem.ITEM.get()) && pLivingEntity.isModelPartShown(PlayerModelPart.CAPE))
        {
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0D, 0.0D, 0.125D);
            double d0 = Mth.lerp((double)pPartialTicks, pLivingEntity.xCloakO, pLivingEntity.xCloak) - Mth.lerp((double)pPartialTicks, pLivingEntity.xo, pLivingEntity.getX());
            double d1 = Mth.lerp((double)pPartialTicks, pLivingEntity.yCloakO, pLivingEntity.yCloak) - Mth.lerp((double)pPartialTicks, pLivingEntity.yo, pLivingEntity.getY());
            double d2 = Mth.lerp((double)pPartialTicks, pLivingEntity.zCloakO, pLivingEntity.zCloak) - Mth.lerp((double)pPartialTicks, pLivingEntity.zo, pLivingEntity.getZ());
            float f = pLivingEntity.yBodyRotO + (pLivingEntity.yBodyRot - pLivingEntity.yBodyRotO);
            double d3 = (double)Mth.sin(f * ((float)Math.PI / 180F));
            double d4 = (double)(-Mth.cos(f * ((float)Math.PI / 180F)));
            float f1 = (float)d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
            f3 = Mth.clamp(f3, -20.0F, 20.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            float f4 = Mth.lerp(pPartialTicks, pLivingEntity.oBob, pLivingEntity.bob);
            f1 += Mth.sin(Mth.lerp(pPartialTicks, pLivingEntity.walkDistO, pLivingEntity.walkDist) * 6.0F) * 32.0F * f4;
            if (pLivingEntity.isCrouching()) {
                f1 += 25.0F;
            }

            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entitySolid(new ResourceLocation(FMLPaths.GAMEDIR.get() + "/capes/" + CapeItem.getId(itemstack.getItem()) + ".png")));
            this.getParentModel().renderCloak(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pMatrixStack.popPose();
            ci.cancel();
        }

        if (ModList.get().isLoaded("curios")) {
            ClientCapeIntegration.curiosCapeInject(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pPackedLight, ci, this.getParentModel());
        }
    }
}
