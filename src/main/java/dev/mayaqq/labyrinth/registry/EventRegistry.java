package dev.mayaqq.labyrinth.registry;

import dev.mayaqq.labyrinth.utils.mutliblock.Multiblock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class EventRegistry {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (Multiblock.attachments.get(pos) != null && Multiblock.elements.get(pos) != null) {
                ElementHolder holder = new ElementHolder() {
                    @Override
                    public Vec3d getPos() {
                        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                    }
                };
                BlockDisplayElement element = new BlockDisplayElement();
                HolderAttachment attachment = ChunkAttachment.of(holder, (ServerWorld) world, pos);
                element.setBlockState(Multiblock.elements.get(pos).getBlockState());
                element.setGlowing(false);
                element.setScale(new Vector3f(0.5F, 0.5F, 0.5F));
                element.setOffset(new Vec3d(0.25F, 0.25F, 0.25F));
                holder.addElement(element);
                Multiblock.attachments.get(pos).destroy();
                Multiblock.attachments.put(pos, attachment);
                Multiblock.elements.put(pos, element);
            }
        });
    }
}
