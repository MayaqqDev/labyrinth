/*
 * This class is a modified version of the IngredientStack class from the Incubus Core library.
 * The library is licensed under the MIT license.
 */

package dev.mayaqq.labyrinth.utils.recipe;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.predicate.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.dynamic.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public record IngredientStack(Ingredient ingredient, int count, ComponentPredicate components) {

    public static final Codec<IngredientStack> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item").forGetter(IngredientStack::ingredient),
            Codecs.POSITIVE_INT.fieldOf("count").orElse(1).forGetter(IngredientStack::count),
            ComponentPredicate.CODEC.optionalFieldOf("components", ComponentPredicate.EMPTY).forGetter(IngredientStack::components)
    ).apply(instance, IngredientStack::new));

    public static final PacketCodec<RegistryByteBuf, IngredientStack> PACKET_CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC, IngredientStack::ingredient,
            PacketCodecs.VAR_INT, IngredientStack::count,
            ComponentPredicate.PACKET_CODEC, IngredientStack::components,
            IngredientStack::new);

    public static final PacketCodec<RegistryByteBuf, Optional<IngredientStack>> OPTIONAL_PACKET_CODEC = PACKET_CODEC.collect(PacketCodecs::optional);
    public static final IngredientStack EMPTY = new IngredientStack(Ingredient.EMPTY);

    public IngredientStack(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public IngredientStack(Ingredient ingredient, int count) {
        this(ingredient, count, ComponentPredicate.EMPTY);
    }

    public IngredientStack(Ingredient ingredient, int count, ComponentPredicate components) {
        this.ingredient = ingredient;
        this.count = count;
        this.components = components;
    }

    public IngredientStack withComponents(UnaryOperator<ComponentPredicate.Builder> builderCallback) {
        return new IngredientStack(this.ingredient, this.count);
    }

    private static ItemStack createDisplayStack(RegistryEntry<Item> item, int count, ComponentPredicate components) {
        return new ItemStack(item, count, components.toChanges());
    }

    public boolean matches(ItemStack stack) {
        return this.ingredient.test(stack) && stack.getCount() >= this.count && this.components.test(stack);
    }

    public Ingredient ingredient() {
        return this.ingredient;
    }

    public int count() {
        return this.count;
    }

    public ComponentPredicate components() {
        return this.components;
    }

    public Collection<ItemStack> getStacks() {
        ItemStack[] stacks = ingredient.getMatchingStacks();

        if (stacks == null)
            return new ArrayList<>();

        return Arrays.stream(stacks)
                .peek(stack -> stack.setCount(count))
                .collect(Collectors.toList());
    }

}
