package dev.mayaqq.labyrinth.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.mayaqq.labyrinth.utils.recipe.IngredientStack;
import dev.mayaqq.labyrinth.utils.recipe.RecipeParser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ForgeRecipeSerializer implements RecipeSerializer<ForgeRecipe> {

    private ForgeRecipeSerializer() {
    }

    public static final ForgeRecipeSerializer INSTANCE = new ForgeRecipeSerializer();

    public static final Identifier ID = new Identifier("labyrinth", "forging");
    @Override
    public ForgeRecipe read(Identifier id, JsonObject json) {
        JsonArray input = JsonHelper.getArray(json, "input");
        DefaultedList<IngredientStack> craftingInputs = RecipeParser.ingredientStacksFromJson(input, input.size());
        Block material = Registries.BLOCK.get(Identifier.tryParse(JsonHelper.getString(json, "material")));
        Item outputItem = Registries.ITEM.get(Identifier.tryParse(JsonHelper.getString(json, "result")));
        ItemStack output = new ItemStack(outputItem, 1);
        return new ForgeRecipe(craftingInputs, output, id, material);
    }

    @Override
    public ForgeRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<IngredientStack> ingredients = IngredientStack.decodeByteBuf(buf, buf.readShort());

        ItemStack output = buf.readItemStack();
        Block material = Registries.BLOCK.get(new Identifier(buf.readString()));
        return new ForgeRecipe(ingredients, output, id, material);
    }

    @Override
    public void write(PacketByteBuf buf, ForgeRecipe recipe) {
        buf.writeShort(recipe.input.size());
        for (IngredientStack ingredientStack : recipe.input) {
            ingredientStack.write(buf);
        }
        buf.writeItemStack(recipe.getOutput(DynamicRegistryManager.EMPTY));
        buf.writeString(Registries.BLOCK.getId(recipe.getMaterial()).toString());
    }
}
