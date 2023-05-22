package dev.mayaqq.labyrinth.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ForgeRecipe implements Recipe<Inventory> {

    protected final DefaultedList<Ingredient> input;
    private final ItemStack result;
    private final Identifier id;

    public ForgeRecipe(DefaultedList<Ingredient> input, ItemStack result, Identifier id) {
        this.input = input;
        this.result = result;
        this.id = id;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return input;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        input.forEach(ingredient -> {
            if (!ingredient.test(inventory.getStack(0))) {
                return;
            }
        });
        return false;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ForgeRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ForgeRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "forging";
    }
}
