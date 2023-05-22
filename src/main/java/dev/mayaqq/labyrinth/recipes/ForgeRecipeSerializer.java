package dev.mayaqq.labyrinth.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.Collections;

public class ForgeRecipeSerializer implements RecipeSerializer<ForgeRecipe> {

    private ForgeRecipeSerializer() {
    }

    public static final ForgeRecipeSerializer INSTANCE = new ForgeRecipeSerializer();

    public static final Identifier ID = new Identifier("labyrinth", "forging");
    @Override
    public ForgeRecipe read(Identifier id, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        for (JsonElement je : recipeJson.input) {
            ingredients.add(Ingredient.fromJson(je));
        }
        Item outputItem = Registries.ITEM.get(new Identifier(recipeJson.result));
        ItemStack output = new ItemStack(outputItem, 1);
        return new ForgeRecipe(ingredients, output, id);
    }

    @Override
    public ForgeRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.replaceAll(ignored -> Ingredient.fromPacket(buf));
        ItemStack output = buf.readItemStack();
        return new ForgeRecipe(ingredients, output, id);
    }

    @Override
    public void write(PacketByteBuf buf, ForgeRecipe recipe) {
        recipe.getIngredients().forEach(ingredient -> ingredient.write(buf));
        buf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
    }



    static class ExampleRecipeJsonFormat {
        String type;
        JsonArray input;
        String result;
    }
}
