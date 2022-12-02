package dev.kisca.recraftedcapes.mixins.client;

import dev.kisca.recraftedcapes.CapeItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T>
{
    @Shadow @Final
    private ModelPart cloak;

    public PlayerModelMixin(ModelPart pRoot) {
        super(pRoot);
    }

    @Inject(at = @At(value =  "TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", cancellable = true)

    private void renderInject(T p_103395_, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_, CallbackInfo ci) {
        if (p_103395_.getItemBySlot(EquipmentSlot.CHEST).isEmpty() || p_103395_.getItemBySlot(EquipmentSlot.CHEST).is(CapeItem.ITEM.get())) {
            if (p_103395_.isCrouching()) {
                this.cloak.z = 2.0F;
                this.cloak.y = 1.85F;
            } else {
                this.cloak.z = 0.0F;
                this.cloak.y = 0.0F;
            }
        } else if (p_103395_.isCrouching()) {
            this.cloak.z = 0.3F;
            this.cloak.y = 0.8F;
        } else {
            this.cloak.z = -1.1F;
            this.cloak.y = -0.85F;
        }
    }
}
