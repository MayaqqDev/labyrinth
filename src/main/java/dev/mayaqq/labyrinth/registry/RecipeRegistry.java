package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.recipes.ForgeRecipe;
import dev.mayaqq.labyrinth.recipes.ForgeRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static dev.mayaqq.labyrinth.Labyrinth.id;

public class RecipeRegistry {
    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, ForgeRecipeSerializer.ID, ForgeRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, id(ForgeRecipe.Type.ID), ForgeRecipe.Type.INSTANCE);
    }
}
