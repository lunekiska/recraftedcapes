package dev.kisca.recraftedcapes.mixins.client;

import dev.kisca.recraftedcapes.CapeItem;
import dev.kisca.recraftedcapes.compat.curios.ClientCapeIntegration;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ElytraLayer.class, remap = false)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M>
{
    public ElytraLayerMixin(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Inject(at = @At(value =  "HEAD"), method = "getElytraTexture", cancellable = true)

    private void getElytraTextureInject(ItemStack stack, T entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ItemStack chestStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestStack.is(CapeItem.ITEM.get())) {
            cir.setReturnValue(new ResourceLocation(FMLPaths.GAMEDIR.get() + "/capes/" + CapeItem.getId(stack.getItem()) + ".png"));
        }
        if (entity instanceof Player player && ModList.get().isLoaded("curios")) {
            ClientCapeIntegration.curiosElytraInject(stack, player, cir);
        }
    }

}