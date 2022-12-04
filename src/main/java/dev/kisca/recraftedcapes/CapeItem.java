package dev.kisca.recraftedcapes;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CapeItem extends Item implements ICurioItem {
    public static final RegistryObject<Item> ITEM = RegistryObject.create(RecraftedCapes.id("cape"), ForgeRegistries.ITEMS);

    public static final String CAPE_TYPE_NBT = "CapeType"; // Key for ResourceLocation

    public CapeItem()
    {
        super(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
        setRegistryName(ITEM.getId());
    }

    public static ResourceLocation getId(ItemStack itemstack) {
        CompoundTag compoundTag = itemstack.getTag();
        if (compoundTag != null)
        {
            String capeTag = compoundTag.getString(CAPE_TYPE_NBT);
            return new ResourceLocation(capeTag);
        }
        return null;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack)
    {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1f, 1f);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack)
    {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> desc, @NotNull TooltipFlag flags)
    {
        String type = "red";
        CompoundTag tag = stack.getTag();
        if (tag != null)
        {
            String string = tag.getString(CAPE_TYPE_NBT);
            if (!string.isEmpty()) type = string;
        }
        desc.add(new TextComponent("Cape Design").withStyle(ChatFormatting.GOLD)
                .append(new TextComponent(": ").withStyle(ChatFormatting.AQUA))
                .append(new TextComponent(type.replace('_', ' ')).withStyle(ChatFormatting.YELLOW)));
    }
}
