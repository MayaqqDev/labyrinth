package dev.mayaqq.labyrinth.gui;

import dev.mayaqq.labyrinth.recipes.ForgeRecipe;
import dev.mayaqq.labyrinth.registry.RecipeRegistry;
import dev.mayaqq.labyrinth.utils.recipe.IngredientStack;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import static dev.mayaqq.labyrinth.Labyrinth.LOGGER;

public class ForgeGui {
    public static void gui(ServerPlayerEntity player, BlockPos pos) {
        ServerWorld world = player.getWorld();
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, player, false) {};
        // sets the title of the gui
        gui.setTitle(Text.translatable("gui.labyrinth.forge.title"));
        // makes the gui look nicer
        GuiElementBuilder background = new GuiElementBuilder();
        background.setItem(Items.BLACK_STAINED_GLASS_PANE);
        background.setName(Text.of(" "));
        for (int i = 0; i < 9; ++i) {
            gui.setSlot(i, background.build());
        }
        for (int i = 45; i < 54; ++i) {
            gui.setSlot(i, background.build());
        }
        for (int i = 9; i < 45; i += 9) {
            gui.setSlot(i, background.build());
            gui.setSlot(i + 8, background.build());
        }
        gui.setSlot(49, new GuiElementBuilder()
                .setItem(Items.BARRIER)
                .setName(Text.of("§c§lClose"))
                .setCallback((index, clickType, actionType) -> {
                    gui.close();
                })
        );
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
        // creates a gui element for every recipe
        for (int i = 0; i < forgeRecipes.size(); i++) {
            int guiAddition = 10;
            for (int j = 6; j <= 34; j += 7) {
                if (i > j) {
                    guiAddition += 2;
                } else {
                    break;
                }
            }
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
                guiElement.addLoreLine(Text.of(color + ingredient.getCount() + "× " + ingredient.getIngredient().getMatchingStacks()[0].getName().getString()));
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
            gui.setSlot(i + guiAddition, guiElement.build());
        }
        gui.open();
    }
}
