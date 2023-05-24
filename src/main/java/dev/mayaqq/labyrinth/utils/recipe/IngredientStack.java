/*
 * This class is a modified version of the IngredientStack class from the Incubus Core library.
 * The library is licensed under the MIT license.
 */

package dev.mayaqq.labyrinth.utils.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
public class IngredientStack {

    public static final IngredientStack EMPTY = new IngredientStack(Ingredient.EMPTY, Optional.empty(), 0);
    private final Ingredient ingredient;
    private final Optional<NbtCompound> recipeViewNbt;
    private final int count;

    private IngredientStack(@NotNull Ingredient ingredient, Optional<NbtCompound> recipeViewNbt, int count) {
        this.ingredient = ingredient;
        this.recipeViewNbt = recipeViewNbt;
        this.count = count;
    }

    public static IngredientStack of(@NotNull Ingredient ingredient, @Nullable NbtCompound recipeViewNbt, int count) {
        if(ingredient.isEmpty()) {
            return EMPTY;
        }
        return new IngredientStack(ingredient, Optional.ofNullable(recipeViewNbt), count);
    }

    public static IngredientStack of(Ingredient ingredient) {
        return of(ingredient, null, 1);
    }

    public static IngredientStack ofItems(ItemConvertible... items) {
        return of(Ingredient.ofItems(items), null, 1);
    }

    public static IngredientStack ofItems(int count, ItemConvertible... items) {
        return of(Ingredient.ofItems(items), null, count);
    }

    public static IngredientStack ofStacks(ItemStack... stacks) {
        return of(Ingredient.ofStacks(stacks), null, 1);
    }

    public static IngredientStack ofStacks(int count, ItemStack... stacks) {
        return of(Ingredient.ofStacks(stacks), null, count);
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    public boolean testStrict(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() == count;
    }

    public boolean testCountless(ItemStack stack) {
        return ingredient.test(stack);
    }

    public void write(PacketByteBuf buf) {
        ingredient.write(buf);
        buf.writeBoolean(recipeViewNbt.isPresent());
        recipeViewNbt.ifPresent(buf::writeNbt);
        buf.writeInt(count);
    }

    public static IngredientStack fromByteBuf(PacketByteBuf buf) {
        return new IngredientStack(Ingredient.fromPacket(buf), buf.readBoolean() ? Optional.ofNullable(buf.readNbt()) : Optional.empty(), buf.readInt());
    }

    public static DefaultedList<IngredientStack> decodeByteBuf(PacketByteBuf buf, int size) {
        DefaultedList<IngredientStack> ingredients = DefaultedList.ofSize(size);
        for (int i = 0; i < size; i++) {
            ingredients.add(fromByteBuf(buf));
        }
        return ingredients;
    }

    @SuppressWarnings("all")
    public List<ItemStack> getStacks() {
        return Arrays.stream(((MatchingStackAccessor) (Object) ingredient)
                        .getMatchingStacks())
                .peek(stack -> stack.setCount(count))
                .peek(stack -> recipeViewNbt.ifPresent(nbt -> stack.setNbt(nbt)))
                .collect(Collectors.toList());
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }

    public boolean isEmpty() {
        return this == EMPTY || ingredient.isEmpty();
    }

    public static DefaultedList<Ingredient> listIngredients(List<IngredientStack> ingredients) {
        DefaultedList<Ingredient> preview = DefaultedList.ofSize(ingredients.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            preview.set(i, ingredients.get(i).getIngredient());
        }
        return preview;
    }


    public static boolean matchInvExclusively(Inventory inv, List<IngredientStack> ingredients, int size, int offset) {
        List<ItemStack> invStacks = new ArrayList<>(size);
        for (int i = offset; i < size + offset; i++) {
            invStacks.add(inv.getStack(i));
        }
        AtomicInteger matches = new AtomicInteger();
        ingredients.forEach(ingredient -> {
            for (int i = 0; i < invStacks.size(); i++) {
                if(ingredient.isEmpty()) {
                    matches.getAndIncrement();
                    break;
                }
                ItemStack stack = invStacks.get(i);
                if(ingredient.test(stack)) {
                    matches.getAndIncrement();
                    invStacks.remove(i);
                    break;
                }
            }
        });
        return matches.get() == size;
    }

    public static void decrementExclusively(Inventory inv, List<IngredientStack> ingredients, int size, int offset) {
        List<ItemStack> invStacks = new ArrayList<>(size);
        for (int i = offset; i < size + offset; i++) {
            invStacks.add(inv.getStack(i));
        }
        ingredients.forEach(ingredient -> {
            for (int i = 0; i < invStacks.size(); i++) {
                if(ingredient.isEmpty()) {
                    break;
                }
                ItemStack stack = invStacks.get(i);
                if(ingredient.test(stack)) {
                    stack.decrement(ingredient.count);
                    invStacks.remove(i);
                    break;
                }
            }
        });
    }
}
