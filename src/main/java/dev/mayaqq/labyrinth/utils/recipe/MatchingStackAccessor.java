/*
 * This class is a modified version of the MatchingStackAccessor class from the Incubus Core library.
 * The library is licensed under the MIT license.
 */

package dev.mayaqq.labyrinth.utils.recipe;

import net.minecraft.item.ItemStack;

public interface MatchingStackAccessor {

    ItemStack[] getMatchingStacks();
}
