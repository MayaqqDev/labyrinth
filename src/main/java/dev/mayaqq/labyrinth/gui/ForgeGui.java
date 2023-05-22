package dev.mayaqq.labyrinth.gui;

import dev.mayaqq.labyrinth.recipes.ForgeRecipe;
import dev.mayaqq.labyrinth.registry.RecipeRegistry;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;

public class ForgeGui {
    public static void gui(ServerPlayerEntity player, BlockPos pos) {
        try {
            SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, player, false) {};
            gui.setTitle(Text.translatable("gui.labyrinth.forge.title"));
            RecipeManager recipeManager = player.getServer().getRecipeManager();
            Collection<Recipe<?>> recipes = recipeManager.values();
            ArrayList<ForgeRecipe> forgeRecipes = new ArrayList<>();
            for (int i = 0; i < recipes.size(); i++) {
                Recipe<?> recipe = recipes.toArray(new Recipe<?>[0])[i];
                if (recipe.getType() == RecipeRegistry.FORGING) {
                    forgeRecipes.add((ForgeRecipe) recipe);
                }
            }
            for (int i = 0; i < forgeRecipes.size(); i++) {
                ForgeRecipe recipe = forgeRecipes.get(i);
                DefaultedList<Ingredient> ingredients = recipe.getIngredients();
                GuiElementBuilder builder = new GuiElementBuilder();
                builder.setItem(recipe.getOutput(DynamicRegistryManager.EMPTY).getItem());
                builder.addLoreLine(Text.of(" "));
                for (Ingredient ingredient : ingredients) {
                    String color = null;
                    for (int j = 0; j < player.getInventory().size(); j++) {
                        if (ingredient.test(player.getInventory().getStack(j))) {
                            color = "§a✔ ";
                            break;
                        } else {
                            color = "§c✗ ";
                        }
                    }
                    builder.addLoreLine(Text.of(color + ingredient.getMatchingStacks()[0].toString()));
                }
                builder.setCallback((index, clickType, actionType) -> {
                    int hasAll = 0;
                    for (Ingredient ingredient : ingredients) {
                        for (int j = 0; j < player.getInventory().size(); j++) {
                            if (ingredient.test(player.getInventory().getStack(j))) {
                                hasAll++;
                                break;
                            }
                        }
                    }
                    if (hasAll == ingredients.size()) {
                        ingredients.forEach(ingredient -> {
                            for (int j = 0; j < player.getInventory().size(); j++) {
                                if (ingredient.test(player.getInventory().getStack(j))) {
                                    player.getInventory().removeStack(j, ingredient.getMatchingStacks()[0].getCount());
                                    break;
                                }
                            }
                        });
                        ServerWorld world = player.getWorld();
                        world.spawnEntity(new ItemEntity(player.getWorld(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, recipe.getOutput(DynamicRegistryManager.EMPTY).copy()));
                        gui.close();
                        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.spawnParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 4, 0, 0, 0, 0);

                    } else {
                        player.sendMessage(Text.of("§cYou don't have all the ingredients!"), true);
                        gui.close();
                    }
                });
                gui.setSlot(i, builder.build());
            }

            gui.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
