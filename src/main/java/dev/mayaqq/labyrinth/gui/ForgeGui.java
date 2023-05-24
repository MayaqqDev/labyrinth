package dev.mayaqq.labyrinth.gui;

import dev.mayaqq.labyrinth.recipes.ForgeRecipe;
import dev.mayaqq.labyrinth.registry.RecipeRegistry;
import dev.mayaqq.labyrinth.utils.recipe.IngredientStack;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;

public class ForgeGui {
    public static void gui(ServerPlayerEntity player, BlockPos pos) {
        ServerWorld world = player.getWorld();
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, player, false) {};
        // sets the title of the gui
        gui.setTitle(Text.translatable("gui.labyrinth.forge.title"));
        // creates an array of every recipe that is a forge recipe
        RecipeManager recipeManager = player.getServer().getRecipeManager();
        Collection<Recipe<?>> recipes = recipeManager.values();
        ArrayList<ForgeRecipe> forgeRecipes = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            Recipe<?> recipe = recipes.toArray(new Recipe<?>[0])[i];
            if (recipe.getType() == RecipeRegistry.FORGING && ((ForgeRecipe) recipe).getMaterial() == player.world.getBlockState(pos.down()).getBlock()) {
                forgeRecipes.add((ForgeRecipe) recipe);
            }
        }
        // creates a gui element for recipes in the array
        for (int i = 0; i < forgeRecipes.size(); i++) {
            ForgeRecipe recipe = forgeRecipes.get(i);
            DefaultedList<IngredientStack> ingredients = recipe.getIngredientStacks();
            GuiElementBuilder guiElement = new GuiElementBuilder();
            ItemStack stack = recipe.getOutput(DynamicRegistryManager.EMPTY).copy();
            stack.onCraft(world, player, stack.getCount());
            if (!stack.getEnchantments().isEmpty()) {
                for (int y = 0; y < stack.getEnchantments().size(); y++) {
                    NbtCompound enchantment = stack.getEnchantments().getCompound(y);
                    Identifier id = new Identifier(enchantment.get("id").asString());
                    String levelString = enchantment.get("lvl").asString();
                    int level = Integer.parseInt(levelString.substring(0, levelString.length() - 1));
                    guiElement.enchant(Registries.ENCHANTMENT.get(id), level);
                }
            }
            guiElement.setItem(stack.getItem());
            guiElement.addLoreLine(Text.of(" "));
            // goes through every ingredient in the recipe and checks if the player has it
            for (IngredientStack ingredient : ingredients) {
                String color = "§c✗ ";
                for (int j = 0; j < player.getInventory().size(); j++) {
                    if (ingredient.test(player.getInventory().getStack(j))) {
                        color = "§a✔ ";
                        break;
                    }
                }
                // creates a lore line for the ingredient
                StringBuilder loreLine = new StringBuilder();
                int count = ingredient.getCount();
                loreLine.append(color);
                loreLine.append(count);
                loreLine.append(" ");
                loreLine.append(ingredient.getIngredient().getMatchingStacks()[0].getName().getString());
                if (count > 1) {
                    loreLine.append("s");
                }
                guiElement.addLoreLine(Text.of(loreLine.toString()));
            }
            // creates a callback for when you click on the recipe, checks if the player has all the ingredients and if they do, removes them and gives them the output
            guiElement.setCallback((index, clickType, actionType) -> {
                int hasAll = 0;
                // checks if the player has all the ingredients
                for (IngredientStack ingredient : ingredients) {
                    for (int j = 0; j < player.getInventory().size(); j++) {
                        if (ingredient.test(player.getInventory().getStack(j))) {
                            hasAll++;
                            break;
                        }
                    }
                }
                // if the player has all the ingredients, removes them and gives them the output
                if (hasAll == ingredients.size()) {
                    ingredients.forEach(ingredient -> {
                        for (int j = 0; j < player.getInventory().size(); j++) {
                            if (ingredient.test(player.getInventory().getStack(j))) {
                                player.getInventory().removeStack(j, ingredient.getCount());
                                break;
                            }
                        }
                    });
                    // spawns the output item entity
                    world.spawnEntity(new ItemEntity(player.getWorld(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack));
                    gui.close();
                    // visual effects on when the player crafts the item
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.spawnParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 4, 0, 0, 0, 0);
                } else {
                    // if the player doesn't have all the ingredients, sends a message and closes the gui
                    player.sendMessage(Text.translatable("gui.labyrinth.forge.message.negative.ingredient"), true);
                    gui.close();
                }
            });
            gui.setSlot(i, guiElement.build());
        }
        gui.open();
    }
}
