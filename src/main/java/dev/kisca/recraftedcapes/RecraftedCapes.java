package dev.kisca.recraftedcapes;

import com.mojang.logging.LogUtils;
import dev.kisca.recraftedcapes.compat.curios.CapeIntegration;
import dev.kisca.recraftedcapes.recipe.ShapedCapeRecipe;
import dev.kisca.recraftedcapes.recipe.ShapelessCapeRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.nio.file.Path;

@Mod("recraftedcapes")
public class RecraftedCapes
{
    public static final String MODID = "recraftedcapes";
    public static final String CURIOS_MODID = "curios";
	
    private static final Logger LOG = LogUtils.getLogger();

    public RecraftedCapes()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::enqueueIMC);

        bus.addGenericListener(Item.class, (RegistryEvent.Register<Item> e) -> e.getRegistry().register(new CapeItem()));
        bus.addGenericListener(RecipeSerializer.class, (RegistryEvent.Register<RecipeSerializer<?>> e) -> e.getRegistry().register(new ShapelessCapeRecipe.Serializer()));
        bus.addGenericListener(RecipeSerializer.class, (RegistryEvent.Register<RecipeSerializer<?>> e) -> e.getRegistry().register(new ShapedCapeRecipe.Serializer()));
        bus.addListener((InterModEnqueueEvent e) -> InterModComms.sendTo(CURIOS_MODID, SlotTypeMessage.REGISTER_TYPE, () ->
                new SlotTypeMessage.Builder("cape").build()));
        bus.addListener(RecraftedCapes::registerCapeTextures);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios")) {
            CapeIntegration.InqueueIMC();
        }
    }
    public static void registerCapeTextures(FMLClientSetupEvent evt)
    {
        try
        {
            new ResourceLocation(FMLPaths.GAMEDIR.get().toString(), "/capes/" + CapeItem.CAPE_TYPE_NBT + ".png");
        }
        catch (Throwable e)
        {
            LOG.error("Failed to load cape textures for RecraftedCapes. Report immediately to author with logs.", e);
        }
    }

    private static void registerTexturesIn(Path dir){
            new ResourceLocation(FMLPaths.GAMEDIR.get().toString(), "/capes/" + CapeItem.CAPE_TYPE_NBT + ".png");
    }

    public static ResourceLocation id(String path)
    {
        return new ResourceLocation(MODID, path);
    }
    public static class Config
    {
        public static final Config INSTANCE;
        public static final ForgeConfigSpec SPEC;

        public final ForgeConfigSpec.BooleanValue showOnElytras;

        static
        {
            Pair<Config, ForgeConfigSpec> configure = new ForgeConfigSpec.Builder().configure(Config::new);
            INSTANCE = configure.getLeft();
            SPEC = configure.getRight();
        }

        public Config(ForgeConfigSpec.Builder builder)
        {
            showOnElytras = builder.comment("Whether or not to allow the cape design to also reflect on elytras.")
                    .define("showOnElytras", true);
        }
    }
}
