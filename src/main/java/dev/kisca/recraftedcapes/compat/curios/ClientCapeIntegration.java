package dev.kisca.recraftedcapes.compat.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import dev.kisca.recraftedcapes.CapeItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

import static dev.kisca.recraftedcapes.RecraftedCapes.dynamicCapeTexture;
import static dev.kisca.recraftedcapes.RecraftedCapes.s;

public class ClientCapeIntegration
{
    public static void curiosCapeInject(PoseStack pMatrixStack, MultiBufferSource pBuffer, int p_116617_, AbstractClientPlayer pLivingEntity, float p_116621_, CallbackInfo ci, PlayerModel model) {
        ItemStack itemstack = getItemAtBack(pLivingEntity);
        ItemStack chest = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack != null && (chest.getItem() instanceof ArmorItem || chest.isEmpty())) {
            if (itemstack.is(CapeItem.ITEM.get()) && pLivingEntity.isModelPartShown(PlayerModelPart.CAPE) && (CapeItem.getId(itemstack) != null)) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(0.0D, 0.0D, 0.125D);
                double d0 = Mth.lerp((double)p_116621_, pLivingEntity.xCloakO, pLivingEntity.xCloak) - Mth.lerp((double)p_116621_, pLivingEntity.xo, pLivingEntity.getX());
                double d1 = Mth.lerp((double)p_116621_, pLivingEntity.yCloakO, pLivingEntity.yCloak) - Mth.lerp((double)p_116621_, pLivingEntity.yo, pLivingEntity.getY());
                double d2 = Mth.lerp((double)p_116621_, pLivingEntity.zCloakO, pLivingEntity.zCloak) - Mth.lerp((double)p_116621_, pLivingEntity.zo, pLivingEntity.getZ());
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

                float f4 = Mth.lerp(p_116621_, pLivingEntity.oBob, pLivingEntity.bob);
                f1 += Mth.sin(Mth.lerp(p_116621_, pLivingEntity.walkDistO, pLivingEntity.walkDist) * 6.0F) * 32.0F * f4;
                if (pLivingEntity.isCrouching()) {
                    f1 += 25.0F;
                }

                pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
                pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(dynamicCapeTexture(s))));
                model.renderCloak(pMatrixStack, vertexconsumer, p_116617_, OverlayTexture.NO_OVERLAY);
                pMatrixStack.popPose();
                ci.cancel();
            }
        }

    }

    public static void curiosElytraInject(ItemStack stack, Player entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ItemStack chestStack = getItemAtBack(entity);
        if (chestStack != null) {
            if (chestStack.is(CapeItem.ITEM.get()) && (CapeItem.getId(chestStack) != null)) {
                ResourceLocation capeId = CapeItem.getId(chestStack);
                cir.setReturnValue(new ResourceLocation(dynamicCapeTexture(s)));
            }
        }

    }


    public static ItemStack getItemAtBack(Player player) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> triplet = CuriosApi.getCuriosHelper().findEquippedCurio(CapeItem.ITEM.get(), player);
        if (!triplet.isEmpty()) {
            return triplet.get().getRight();
        }
        return null;
    }
}