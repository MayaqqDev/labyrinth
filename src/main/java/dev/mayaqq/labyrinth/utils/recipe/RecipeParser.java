/*
 * This class is a modified version of the RecipeParser class from the Incubus Core library.
 * The library is licensed under the MIT license.
 */

package dev.mayaqq.labyrinth.utils.recipe;

import com.google.gson.*;
import com.google.gson.stream.*;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;

import java.io.*;
import java.nio.charset.*;

@SuppressWarnings({"unused", "deprecation"})
public class RecipeParser {

    private static final JsonParser PARSER = new JsonParser();

    public static JsonObject fromInputStream(InputStream in) {
        return PARSER.parse(new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))).getAsJsonObject();
    }

    public static ItemStack stackFromJson(JsonObject json, String elementName) {
        Item item = Registries.ITEM.get(Identifier.tryParse(json.get(elementName).getAsString()));
        int count = json.has("count") ? json.get("count").getAsInt() : 1;
        return item != Items.AIR ? new ItemStack(item, count) : ItemStack.EMPTY;
    }

    public static ItemStack stackFromJson(JsonObject json) {
        return stackFromJson(json, "item");
    }

    public static IngredientStack ingredientStackFromJson(JsonObject json) {
        Ingredient ingredient = json.has("ingredient") ? Ingredient.fromJson(json.getAsJsonObject("ingredient")) : Ingredient.fromJson(json);
        NbtCompound recipeViewNbt = null;
        int count = json.has("count") ? json.get("count").getAsInt() : 1;

        if (json.has("recipeViewNbt")) {
            try {
                recipeViewNbt = NbtHelper.fromNbtProviderString(json.get("recipeViewNbt").getAsString());
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        return IngredientStack.of(ingredient, recipeViewNbt, count);
    }

    public static DefaultedList<IngredientStack> ingredientStacksFromJson(JsonArray array, int size) {
        DefaultedList<IngredientStack> ingredients = DefaultedList.ofSize(size);
        int dif = size - array.size();
        for (int i = 0; i < array.size() && i < size; i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            ingredients.add(ingredientStackFromJson(object));
        }
        if(dif > 0) {
            for (int i = 0; i < dif; i++) {
                ingredients.add(IngredientStack.EMPTY);
            }
        }
        return ingredients;
    }


    /**
     * Parses an ItemStack json object with optional NBT data
     * Can be used in RecipeSerializers to get ItemStacks with NBT as output
     * @param json The JsonObject to parse
     * @return An ItemStack with nbt data, like specified in the json
     */
    public static ItemStack getItemStackWithNbtFromJson(JsonObject json) {
        Item item = ShapedRecipe.getItem(json);
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        }

        int count = JsonHelper.getInt(json, "count", 1);
        if (count < 1) {
            throw new JsonSyntaxException("Invalid output count: " + count);
        }

        ItemStack stack = new ItemStack(item, count);
        String nbt = JsonHelper.getString(json, "nbt", "");
        if(nbt.isEmpty()) {
            return stack;
        }

        try {
            NbtCompound compound = NbtHelper.fromNbtProviderString(nbt);
            compound.remove("palette");
            stack.setNbt(compound);
        } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid output nbt: " + nbt);
        }

        return stack;
    }

}
