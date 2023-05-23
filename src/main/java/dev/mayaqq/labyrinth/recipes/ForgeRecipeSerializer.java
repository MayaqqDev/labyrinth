package dev.mayaqq.labyrinth.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ForgeRecipeSerializer implements RecipeSerializer<ForgeRecipe> {

    private ForgeRecipeSerializer() {
    }

    public static final ForgeRecipeSerializer INSTANCE = new ForgeRecipeSerializer();

    public static final Identifier ID = new Identifier("labyrinth", "forging");
    @Override
    public ForgeRecipe read(Identifier id, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        Block material = Registries.BLOCK.get(new Identifier(recipeJson.material));
        for (JsonElement je : recipeJson.input) {
            ingredients.add(Ingredient.fromJson(je));
        }
        Item outputItem = Registries.ITEM.get(new Identifier(recipeJson.result));
        ItemStack output = new ItemStack(outputItem, 1);
        return new ForgeRecipe(ingredients, output, id, material);
    }

    @Override
    public ForgeRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<Ingredient> ingredients = buf.readCollection(DefaultedList::ofSize, Ingredient::fromPacket);

        ItemStack output = buf.readItemStack();
        Block material = Registries.BLOCK.get(new Identifier(buf.readString()));
        return new ForgeRecipe(ingredients, output, id, material);
    }

    @Override
    public void write(PacketByteBuf buf, ForgeRecipe recipe) {
        buf.writeCollection(recipe.getIngredients(), (buf2, ingredient) -> ingredient.write(buf2));
        buf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
        buf.writeString(Registries.BLOCK.getId(recipe.getMaterial()).toString());
    }



    static class ExampleRecipeJsonFormat {
        String type;
        JsonArray input;
        String result;
        String material;
    }
}
