package dev.mayaqq.labyrinth.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.mayaqq.labyrinth.registry.LabyrinthItems;
import dev.mayaqq.labyrinth.registry.LabyrinthRecipes;
import dev.mayaqq.labyrinth.utils.recipe.ForgeRecipeInput;
import dev.mayaqq.labyrinth.utils.recipe.IngredientStack;
import eu.pb4.polymer.core.api.item.PolymerRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ForgeRecipe implements Recipe<ForgeRecipeInput>, PolymerRecipe {

    protected final String group;
    protected final DefaultedList<IngredientStack> input;
    protected final ItemStack result;
    protected final Block material;

    public ForgeRecipe(String group, DefaultedList<IngredientStack> input, ItemStack result, Block material) {
        this.group = group;
        this.input = input;
        this.result = result;
        this.material = material;
    }


    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        for (IngredientStack stack : input) {
            ingredients.add(stack.ingredient());
        }
        return ingredients;
    }

    public DefaultedList<IngredientStack> getIngredientStacks() {
        return this.input;
    }

    @Override
    public boolean matches(ForgeRecipeInput input, World world) {
        return true;
    }

    @Override
    public ItemStack craft(ForgeRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return getResult(lookup);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return result;
    }

    public ItemStack getResult() {
        return result;
    }

    public Block getMaterial() {
        return material;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(LabyrinthItems.TEST_SWORD);
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return new ForgeRecipeSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return LabyrinthRecipes.FORGING;
    }

    public static class ForgeRecipeSerializer implements RecipeSerializer<ForgeRecipe> {

        private static final MapCodec<ForgeRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter((recipe) -> recipe.group),
                IngredientStack.CODEC.listOf().fieldOf("input").flatXmap(ingredientStacks -> {
                    IngredientStack[] results = ingredientStacks.stream().filter((ingredient) -> !ingredient.ingredient().isEmpty()).toArray(IngredientStack[]::new);
                    if (results.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    } else {
                        return DataResult.success(DefaultedList.copyOf(IngredientStack.EMPTY, results));
                    }
                }, DataResult::success).forGetter((recipe) -> recipe.input),
                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                Registries.BLOCK.getCodec().fieldOf("material").forGetter((recipe) -> recipe.material)
        ).apply(instance, ForgeRecipe::new));

        public static final PacketCodec<RegistryByteBuf, ForgeRecipe> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, ForgeRecipe::getGroup,
                IngredientStack.PACKET_CODEC.collect(PacketCodecs.toCollection(DefaultedList::ofSize)), ForgeRecipe::getIngredientStacks,
                ItemStack.PACKET_CODEC, ForgeRecipe::getResult,
                PacketCodecs.registryValue(RegistryKeys.BLOCK), ForgeRecipe::getMaterial,
                ForgeRecipe::new
        );

        @Override
        public MapCodec<ForgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ForgeRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
