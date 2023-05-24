package dev.mayaqq.labyrinth.utils.mutliblock;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.function.Predicate;

import static dev.mayaqq.labyrinth.Labyrinth.LOGGER;

public class Multiblock {
    private final char[][][] pattern;
    private final HashMap<Character, Predicate<BlockState>> predicates;
    private final int width;
    private final int height;
    private final int length;

    public static HashMap<BlockPos, HolderAttachment> attachments = new HashMap<>();
    public static HashMap<BlockPos, BlockDisplayElement> elements = new HashMap<>();

    public Multiblock(char[][][] pattern, HashMap<Character, Predicate<BlockState>> predicates) {
        this.pattern = pattern;
        this.predicates = predicates;
        this.width = pattern[0].length;
        this.height = pattern.length;
        this.length = pattern[0][0].length;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLength() {
        return this.length;
    }

    public boolean check(BlockPos pos, World world) {
        HashMap<BlockPos, HolderAttachment> tempAttachments = new HashMap<>();
        //find the $ in the pattern
        BlockPos corner = findOffset(pos);
        if (corner == null) {
            LOGGER.error("Multiblock pattern does not contain $");
            return false;
        }
        boolean result = true;
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                for (int k = 0; k < pattern[i][j].length; k++) {
                    BlockPos blockPos = corner.add(j, i, k);
                    BlockDisplayElement element = new BlockDisplayElement();
                    if (attachments.containsKey(blockPos)) {
                        attachments.get(blockPos).destroy();
                        attachments.remove(blockPos);
                        elements.remove(blockPos);
                    } else {
                        ElementHolder holder = new ElementHolder() {
                            @Override
                            public Vec3d getPos() {
                                return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            }
                        };
                        Predicate<BlockState> predicate = predicates.get(pattern[i][j][k]);
                        // get the blockstate from the predicate
                        for (Block state : Registries.BLOCK) {
                            BlockState blockState = state.getDefaultState();
                            if (predicate.test(blockState)) {
                                element.setBlockState(blockState);
                                break;
                            }
                        }
                        if (!world.getBlockState(blockPos).isAir() && !predicates.get(pattern[i][j][k]).test(world.getBlockState(blockPos))) {
                            element.setGlowing(true);
                        }
                        element.setOffset(new Vec3d(0.25F, 0.25F, 0.25F));
                        if (!predicates.get(pattern[i][j][k]).test(world.getBlockState(blockPos))) {
                            element.setScale(new Vector3f(0.5F, 0.5F, 0.5F));
                        } else {
                            element.setScale(new Vector3f(0.0F, 0.0F, 0.0F));
                        }
                        holder.addElement(element);
                        HolderAttachment attachment = ChunkAttachment.of(holder, (ServerWorld) world, blockPos);
                        attachments.put(blockPos, attachment);
                        tempAttachments.put(blockPos, attachment);
                        elements.put(blockPos, element);
                    }
                    if (!predicates.get(pattern[i][j][k]).test(world.getBlockState(blockPos))) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    public BlockPos findOffset(BlockPos pos) {
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                for (int k = 0; k < this.getLength(); k++) {
                    if (pattern[i][j][k] == '$') {
                        return pos.add(-j, -i, -k);
                    }
                }
            }
        }
        return null;
    }
}
