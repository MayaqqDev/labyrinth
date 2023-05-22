package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.Labyrinth;
import dev.mayaqq.labyrinth.recipes.ForgeRecipe;
import dev.mayaqq.labyrinth.recipes.ForgeRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class RecipeRegistry {
    public static final String FORGING_ID = "forging";
    public static RecipeType<ForgeRecipe> FORGING;
    static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String id) {
        return Registry.register(Registries.RECIPE_TYPE, Labyrinth.id(id), new RecipeType<T>() {
            @Override
            public String toString() {
                return "labyrinth:" + id;
            }
        });
    }
    public static void register() {
        FORGING = registerRecipeType(FORGING_ID);
        Registry.register(Registries.RECIPE_SERIALIZER, ForgeRecipeSerializer.ID, ForgeRecipeSerializer.INSTANCE);
    }
}
