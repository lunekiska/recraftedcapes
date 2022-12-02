package dev.kisca.recraftedcapes.recipe;

import com.google.gson.JsonObject;
import dev.kisca.recraftedcapes.RecraftedCapes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ShapelessCapeRecipe extends ShapelessRecipe {
    public ShapelessCapeRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients)
    {
        super(id, group, result, ingredients);
    }

    private static ShapelessCapeRecipe wrap(ShapelessRecipe recipe, @Nullable JsonObject json)
    {
        ItemStack stack = recipe.getResultItem();
        ShapedCapeRecipe.applyCapeType(stack, json);
        return new ShapelessCapeRecipe(recipe.getId(), recipe.getGroup(), stack, recipe.getIngredients());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer()
    {
        return Serializer.SERIALIZER.get();
    }

    public static class Serializer extends ShapelessRecipe.Serializer
    {
        public static final RegistryObject<RecipeSerializer<ShapelessCapeRecipe>> SERIALIZER = RegistryObject.create(RecraftedCapes.id("crafting_cape_shapeless"), ForgeRegistries.RECIPE_SERIALIZERS);

        public Serializer()
        {
            setRegistryName(SERIALIZER.getId());
        }

        @Override
        public @NotNull ShapelessCapeRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json)
        {
            return wrap(super.fromJson(id, json), json);
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public ShapelessCapeRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer)
        {
            return wrap(super.fromNetwork(id, buffer), null);
        }
    }
}
