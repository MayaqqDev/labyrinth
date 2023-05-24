package dev.mayaqq.labyrinth.recipes;

import dev.mayaqq.labyrinth.registry.ItemRegistry;
import dev.mayaqq.labyrinth.registry.RecipeRegistry;
import dev.mayaqq.labyrinth.utils.recipe.IngredientStack;
import eu.pb4.polymer.core.api.item.PolymerRecipe;
import net.minecraft.block.Block;
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

public class ForgeRecipe implements Recipe<Inventory>, PolymerRecipe {

    protected final DefaultedList<IngredientStack> input;
    private final ItemStack result;
    private final Identifier id;
    private final Block material;

    public ForgeRecipe(DefaultedList<IngredientStack> input, ItemStack result, Identifier id, Block material) {
        this.input = input;
        this.result = result;
        this.id = id;
        this.material = material;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return IngredientStack.listIngredients(this.input);
    }

    public DefaultedList<IngredientStack> getIngredientStacks() {
        return this.input;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        for (IngredientStack ingredient : input) {
            boolean foundMatch = false;
            for (int slot = 0; slot < inventory.size(); slot++) {
                ItemStack stack = inventory.getStack(slot);
                if (ingredient.test(stack)) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                return false;
            }
        }
        return true;
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

    public Block getMaterial() {
        return this.material;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.TEST_SWORD);
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return ForgeRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.FORGING;
    }
}
